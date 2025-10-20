package com.kai.kaiagent.tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 网页抓取工具类
 */
public class WebScrapingTool {

    /**
     * 抓取指定 URL 的网页内容（HTML）
     */
    @Tool(description = "Scrape the HTML content of a web page by URL")
    public String scrapeWebPage(
            @ToolParam(description = "URL of the web page to scrape") String url) {
        try {
            // 建议加 UA 和超时，防止部分网站拒绝请求
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (compatible; LoveAppBot/1.0)")
                    .timeout(10_000)
                    .get();

            // 返回完整 HTML 内容
            return doc.outerHtml();
        } catch (Exception e) {
            return "Error scraping web page: " + e.getMessage();
        }
    }
}
