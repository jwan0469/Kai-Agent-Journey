package com.kai.kaiagent.advisor;

import com.kai.kaiagent.app.LoveApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

/**
 * 自定义日志 Advisor，打印用户输入和 AI 输出
 */
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    // ✅ 手动定义日志对象
    private static final Logger log = LoggerFactory.getLogger(LoveApp.class);

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0; // 执行顺序
    }

    // 请求前打印用户输入
    private AdvisedRequest before(AdvisedRequest request) {
        log.info("AI Request: {}", request.userText());
        return request;
    }

    // 响应后打印 AI 输出
    private void observeAfter(AdvisedResponse response) {
        log.info("AI Response: {}", response.response().getResult().getOutput().getText());
    }

    // 同步调用处理
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest req, CallAroundAdvisorChain chain) {
        req = before(req);
        AdvisedResponse res = chain.nextAroundCall(req);
        observeAfter(res);
        return res;
    }

    // 流式调用处理
    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest req, StreamAroundAdvisorChain chain) {
        req = before(req);
        Flux<AdvisedResponse> res = chain.nextAroundStream(req);
        return new MessageAggregator().aggregateAdvisedResponse(res, this::observeAfter);
    }
}
