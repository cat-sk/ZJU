package zju.cst.aces.api.config;

import lombok.Data;

/**
 * model的配置类
 * 设置LLM的配置参数
 * 使用建造者模式
 * @author dfa
 * @date 2024/07/11
 */
@Data
public class ModelConfig {
    public String modelName;
    public String url;
    public int contextLength;
    public double temperature;
    public int frequencyPenalty;
    public int presencePenalty;

    /**
     * 构造参数
     * @param builder
     */
    private ModelConfig(Builder builder) {
        this.modelName = builder.modelName;
        this.url = builder.url;
        this.contextLength = builder.contextLength;
        this.temperature = builder.temperature;
        this.frequencyPenalty = builder.frequencyPenalty;
        this.presencePenalty = builder.presencePenalty;
    }

    /**
     * 建造者设计模式
     * 逐步构建复杂的对象
     * @author dfa
     * @date 2024/07/11
     */
    public static class Builder {
        private String modelName = "gpt-3.5-turbo";
        private String url = "https://api.openai.com/v1/chat/completions";
        private int contextLength = 4096;
        private double temperature = 0.5;
        private int frequencyPenalty = 0;
        private int presencePenalty = 0;

        /**
         * 设置模型名称
         * @param modelName
         * @return {@link Builder }
         */
        public Builder withModelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        /**
         * 设置url
         * @param url
         * @return {@link Builder }
         */
        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置上下文长度
         * @param contextLength
         * @return {@link Builder }
         */
        public Builder withContextLength(int contextLength) {
            this.contextLength = contextLength;
            return this;
        }

        /**
         * 设置存在惩罚参数
         * @param penalty
         * @return {@link Builder }
         */
        public Builder withPresencePenalty(int penalty) {
            this.presencePenalty = penalty;
            return this;
        }

        /**
         * 设置频率惩罚参数
         * @param penalty
         * @return {@link Builder }
         */
        public Builder withFrequencyPenalty(int penalty) {
            this.frequencyPenalty = penalty;
            return this;
        }

        /**
         * 设置温度
         * @param temperature
         * @return {@link Builder }
         */
        public Builder withTemperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        /**
         * 构建
         * @return {@link ModelConfig }
         */
        public ModelConfig build() {
            return new ModelConfig(this);
        }
    }
}
