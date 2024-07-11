package zju.cst.aces.api.impl;

import zju.cst.aces.util.LogFormatter;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @description: 日志实现类
 * @author dfa
 * @date 2024/07/11
 */
public class LoggerImpl implements zju.cst.aces.api.Logger {

    java.util.logging.Logger log;

    /**
     * 初始化日志记录器
     * 配置控制台处理器来记录所有级别的日志消息
     * 自定义的日志格式化器
     */
    public LoggerImpl() {
        this.log = java.util.logging.Logger.getLogger("ChatUniTest");
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new LogFormatter());
        this.log.addHandler(consoleHandler);
        this.log.setUseParentHandlers(false);
    }

    /**
     * @param msg
     */
    @Override
    public void info(String msg) {
        log.info(msg);
    }

    /**
     * @param msg
     */
    @Override
    public void warn(String msg) {
        log.warning(msg);
    }

    /**
     * @param msg
     */
    @Override
    public void error(String msg) {
        log.severe(msg);
    }

    /**
     * @param msg
     */
    @Override
    public void debug(String msg) {
        log.config(msg);
    }
}
