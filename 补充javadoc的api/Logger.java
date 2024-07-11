package zju.cst.aces.api;

/**
 * 日志接口
 * @author dfa
 * @date 2024/07/11
 */
public interface Logger {

    void info(String msg);
    void warn(String msg);
    void error(String msg);
    void debug(String msg);
}
