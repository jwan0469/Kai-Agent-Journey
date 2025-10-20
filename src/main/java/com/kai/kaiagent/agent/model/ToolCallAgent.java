package com.kai.kaiagent.agent.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工具调用代理，实现think和act方法处理工具调用
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    private final ToolCallback[] availableTools; //可用工具列表
    private ChatResponse toolCallChatResponse; // 暂存上一次模型的工具调用指令
    private final ToolCallingManager toolCallingManager; //专门用来解析大模型返回的工具调用信息,并执行实际结果
    private final ChatOptions chatOptions; // 禁用 Spring AI 默认的“自动工具调用


    /*
        当我们 new 一个 ToolCallAgent 时，构造函数会执行下面的功能.
     */
    public ToolCallAgent(ToolCallback[] availableTools) {
        super(); // 调用父类 ReActAgent 的构造函数
        this.availableTools = availableTools; // 加载可用工具
        this.toolCallingManager = ToolCallingManager.builder().build(); // 初始化工具执行器
        this.chatOptions = DashScopeChatOptions.builder()
                .withProxyToolCalls(true) // 禁用默认代理
                .build(); // 自定义模型调用配置
    }


    /**
     * 思考阶段：分析当前状态并确定要调用的工具
     */
    @Override
    public boolean think() {
        // 添加下一步提示词
        if (StrUtil.isNotBlank(getNextStepPrompt())) {
            getMessageList().add(new UserMessage(getNextStepPrompt()));
        }

        // 调用AI获取工具选择
        try {
            //组装Prompt:历史上下文（用户要约会计划+PDF），
            //          系统设定（你是执行型Agent，可调工具），
            //          工具目录（地图、图片、PDF、终止）。
            Prompt prompt = new Prompt(getMessageList(), this.chatOptions);
            //调用大语言模型得到回复。模型给出工具调用计划（结构化，有顺序的）
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call()
                    .chatResponse();

            //回复就被装进 toolCallChatResponse，供 act() 使用
            this.toolCallChatResponse = chatResponse;

            // 解析是否有工具调用
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();

            // 记录选择
            String result = assistantMessage.getText();
            log.info("{}的思考: {}", getName(), result);
            log.info("{}选择了{}个工具", getName(), toolCallList.size());

            if (toolCallList.size() > 0) {
                String toolCallInfo = toolCallList.stream()
                        .map(tc -> String.format("工具: %s, 参数: %s", tc.name(), tc.arguments()))
                        .collect(Collectors.joining("\n"));
                log.info(toolCallInfo);
            }

            // 没有工具调用时记录助手消息,是否进去act.
            if (toolCallList.isEmpty()) {
                getMessageList().add(assistantMessage);
                return false;
            }

            return true; //有工具，本轮进入act
        } catch (Exception e) {
            log.error("{}思考过程错误: {}", getName(), e.getMessage());
            getMessageList().add(new AssistantMessage("处理错误: " + e.getMessage()));
            return false;
        }
    }

    /**
     * 行动阶段：执行选定的工具
     */
    @Override
    public String act() {
        if (!toolCallChatResponse.hasToolCalls()) {
            return "没有工具需要调用";
        }

        // 执行工具调用
        Prompt prompt = new Prompt(getMessageList(), this.chatOptions);
        ToolExecutionResult result = toolCallingManager.executeToolCalls(prompt, toolCallChatResponse);

        // 写回上下文：让下一轮 think() 能“看见”刚刚的结果
        setMessageList(result.conversationHistory());
        ToolResponseMessage response = (ToolResponseMessage) CollUtil.getLast(result.conversationHistory());

        // 检查是否触发“终止工具”，用于显式结束循环
        boolean terminated = response.getResponses().stream()
                .anyMatch(r -> r.name().equals("doTerminate"));
        if (terminated) {
            setState(AgentState.FINISHED);
        }

        // 格式化结果输出
        String results = response.getResponses().stream()
                .map(r -> "工具[" + r.name() + "]结果: " + r.responseData())
                .collect(Collectors.joining("\n"));
        log.info(results);

        return results;
    }
}

