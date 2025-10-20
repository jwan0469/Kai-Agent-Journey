# Kai AI Agent - AI智能体系统

基于 Spring Boot 3.4.5 + Java 21 + Spring AI + LangChain4j + DashScope 的智能AI交互系统实战项目，适用于 AI 应用开发、智能体构建等场景。项目从基础模型调用到 RAG 知识库问答 📚 再到工具规划与 MCP 服务集成，完整覆盖了 AI Agent 🧠 的核心技术，包括多模型接入、上下文记忆、ReAct 智能规划、多模态支持 🖼️、自定义工具开发等解决方案。

---

## 项目简介

Kai AI Agent 是一个以 AI 开发为核心的智能体项目，旨在提供强大的 AI 交互能力，包括：

- 多轮对话与上下文记忆
- 知识库问答 (RAG)
- 工具调用与智能规划
- MCP 服务集成
- 多模态应用支持

系统集成了主流 AI 模型，如 阿里云 DashScope（通义千问）等，同时支持多种 AI 框架，使开发者能够快速构建智能应用。

---

## 技术栈

- 基础框架：Java 21 + Spring Boot 3.4.5
- AI 框架：Spring AI + LangChain4j
- AI 模型接入：
  - 阿里云 DashScope（通义千问）
- 工具与库：
  - Knife4j (API 文档)
  - Hutool (工具类库)
  - Lombok (代码简化)
- 服务配置：支持多环境配置 (local, dev, prod)

---

## 核心功能

### 1. 多种方式调用 AI 大模型

- Spring AI 框架集成
- LangChain4j 链式调用
- 自定义 HTTP / SDK 调用

### 2. 知识库问答 (RAG)

- 支持接入本地知识库
- 支持向量数据库检索
- 提供上下文增强查询

### 3. 智能工具调用

- 文件操作
- 联网搜索
- 网页抓取
- 资源下载
- PDF 生成

### 4. MCP 服务能力

- 自定义 MCP 服务开发
- 图片搜索服务
- 支持多种调用方式

### 5. 智能体规划能力

- 基于 ReAct 模式的自主规划
- 多步骤任务执行
- 任务状态追踪

---

## 项目结构

```
kai-ai-agent/
 ├── src/
 │   ├── main/
 │   │   ├── java/
 │   │   │   └── com/
 │   │   │       └── kai/
 │   │   │           └── agent/
 │   │   │               ├── controller/    # 接口控制器
 │   │   │               ├── demo/          # 示例代码
 │   │   │               │   └── invoke/    # 不同调用方式示例
 │   │   │               ├── config/        # 配置类
 │   │   │               ├── service/       # 服务实现
 │   │   │               ├── model/         # 数据模型
 │   │   │               └── KaiAiAgentApplication.java  # 启动类
 │   │   └── resources/
 │   │       ├── application.yml            # 通用配置
 │   │       └── application-local.yml      # 本地环境配置
 │   └── test/                              # 测试代码
 └── pom.xml                                # 项目依赖
```

---

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- IDE: IntelliJ IDEA (推荐)

### 本地运行

1. 克隆项目
   
   ```bash
   git clone https://github.com/yourusername/kai-ai-agent.git
   cd kai-ai-agent
   ```
2. 配置 API 密钥
   修改 `src/main/resources/application-local.yml` 文件中的 API 密钥：
   
   ```
   spring:
      ai:
       dashscope:
   	api-key: 你的DashScope API密钥
   ```
3. 启动应用
   
   ```
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```

4.访问 API 文档
打开浏览器访问：
http://localhost:8123/api/swagger-ui.html

## 扩展功能

* 对话记忆持久化
* 结构化输出
* 多模态支持（图像 / 文本）
* 知识库扩展
* 自定义工具开发
* 智能体增强

## 贡献指南

欢迎提交 Issue 或 Pull Request！

1. Fork 本仓库
2. 创建特性分支
   <pre class="overflow-visible!" data-start="3115" data-end="3169"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-bash"><span><span>git checkout -b feature/your-feature
   </span></span></code></div></div></pre>
3. 提交更改
   <pre class="overflow-visible!" data-start="3183" data-end="3234"><div class="contain-inline-size rounded-2xl relative bg-token-sidebar-surface-primary"><div class="sticky top-9"><div class="absolute end-0 bottom-0 flex h-9 items-center pe-2"><div class="bg-token-bg-elevated-secondary text-token-text-secondary flex items-center gap-4 rounded-sm px-2 font-sans text-xs"></div></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="whitespace-pre! language-bash"><span><span>git commit -am </span><span>&#34;Add some feature&#34;</span><span>
   </span></span></code></div></div></pre>
4. 推送分支并发起 Pull Request

---

## 联系方式

* 项目维护者: Kai
* 项目主页: [https://github.com/yourusername/kai-ai-agent](https://github.com/yourusername/kai-ai-agent)
* 项目系列: “Kai AI 智能体”系列，致力于帮助开发者快速掌握 AI Agent 架构与智能应用开发

