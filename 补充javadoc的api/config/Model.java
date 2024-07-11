package zju.cst.aces.api.config;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Model枚举用于表示不同LLM模型
 * 内含三种基础模型
 * 包括模型名称、URL、上下文长度、温度、频率惩罚和存在惩罚等参数
 * @author dfa
 * @date 2024/07/11
 */
public enum Model {
    GPT_3_5_TURBO("gpt-3.5-turbo", new ModelConfig.Builder()
            .withModelName("gpt-3.5-turbo")
            .withUrl("https://api.openai.com/v1/chat/completions")
            .withContextLength(4096)
            .withTemperature(0.5)
            .withFrequencyPenalty(0)
            .withPresencePenalty(0)
            .build()),
    GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106", new ModelConfig.Builder()
            .withModelName("gpt-3.5-turbo-1106")
            .withUrl("https://api.openai.com/v1/chat/completions")
            .withContextLength(16385)
            .withTemperature(0.5)
            .withFrequencyPenalty(0)
            .withPresencePenalty(0)
            .build()),
    CODE_LLAMA("code-llama", new ModelConfig.Builder()
            .withModelName("code-llama")
            .withUrl(null)
            .withContextLength(16385)
            .withTemperature(0.5)
            .withFrequencyPenalty(0)
            .withPresencePenalty(0)
            .build());
    // 添加更多模型

    private final String modelName;
    private final ModelConfig defaultConfig;

    /**
     * 构造函数，用于初始化Model对象
     * @param modelName
     * @param defaultConfig
     */
    Model(String modelName, ModelConfig defaultConfig) {
        this.modelName = modelName;
        this.defaultConfig = defaultConfig;
    }

    /**
     * 获取模型名称
     * @return {@link String }
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * 获取默认配置
     * @return {@link ModelConfig }
     */
    public ModelConfig getDefaultConfig() {
        return defaultConfig;
    }

    /**
     * 根据模型名称获取Model对象
     * @param modelName
     * @return {@link Model }
     */
    public static Model fromString(String modelName) {
        for (Model model : Model.values()) {
            if (model.getModelName().equalsIgnoreCase(modelName)) {
                return model;
            }
        }
        throw new IllegalArgumentException("No Model with name " + modelName +
                "\nSupport models: " + Arrays.stream(Model.values()).map(Model::getModelName).collect(Collectors.joining(", ")));
    }
}
