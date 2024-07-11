package zju.cst.aces.api;

import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.Message;

import java.util.List;

/**
 * 生成器接口
 * @author dfa
 * @date 2024/07/11
 */
public interface Generator {

    /**
     * 初始化
     * @param messages
     * @return {@link String }
     */
    String generate(List<Message> messages);

}
