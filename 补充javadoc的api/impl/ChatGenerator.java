package zju.cst.aces.api.impl;

import okhttp3.Response;
import zju.cst.aces.api.Generator;
import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.ChatResponse;
import zju.cst.aces.dto.Message;
import zju.cst.aces.runner.AbstractRunner;
import zju.cst.aces.util.AskGPT;
import zju.cst.aces.util.CodeExtractor;

import java.util.List;
import zju.cst.aces.api.Generator;

/**
 * chat生成器
 * 实现了Generator的接口
 * 根据消息列表生成代码片段
 * @author dfa
 * @date 2024/07/11
 */
public class ChatGenerator implements Generator {

    Config config;

    /**
     * 创建实例
     * @param config
     */
    public ChatGenerator(Config config) {
        this.config = config;
    }

    /**
     * 根据消息列表生成代码片段
     * @param messages
     * @return {@link String }
     */
    @Override
    public String generate(List<Message> messages) {
        return extractCodeByResponse(chat(config, messages));
    }

    /**
     * 用于与chatAPI进行交互
     * @param config
     * @param messages
     * @return {@link ChatResponse }
     */
    public static ChatResponse chat(Config config, List<Message> messages) {
        ChatResponse response = new AskGPT(config).askChatGPT(messages);
        if (response == null) {
            throw new RuntimeException("Response is null, failed to get response.");
        }
        return response;
    }

    /**
     * 从响应中提取代码
     * @param response
     * @return {@link String }
     */
    public static String extractCodeByResponse(ChatResponse response) {
        return new CodeExtractor(getContentByResponse(response)).getExtractedCode();
    }

    /**
     * 从响应中获取内容
     * @param response
     * @return {@link String }
     */
    public static String getContentByResponse(ChatResponse response) {
        return AbstractRunner.parseResponse(response);
    }

    /**
     * 从内容中提取代码
     * @param content
     * @return {@link String }
     */
    public static String extractCodeByContent(String content) {
        return new CodeExtractor(content).getExtractedCode();
    }
}
