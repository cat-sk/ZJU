package zju.cst.aces.api;

import zju.cst.aces.dto.Message;

import java.util.List;

/**
 * Prompt构造器接口
 * 生成提示消息列表
 * 便于自动化测试或代码生成
 * @author dfa
 * @date 2024/07/11
 */
public interface PromptConstructor {

    List<Message> generate();

}
