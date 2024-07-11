package zju.cst.aces.api.impl;

import zju.cst.aces.api.Runner;
import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.ClassInfo;
import zju.cst.aces.dto.MethodInfo;
import zju.cst.aces.dto.PromptInfo;
import zju.cst.aces.dto.RoundRecord;
import zju.cst.aces.runner.AbstractRunner;
import zju.cst.aces.runner.ClassRunner;
import zju.cst.aces.runner.MethodRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 实现Runner接口
 * 用于运行类和方法测试
 * @author dfa
 * @date 2024/07/11
 */
public class RunnerImpl implements Runner {
    Config config;

    /**
     * 创建实例
     * @param config
     */
    public RunnerImpl(Config config) {
        this.config = config;
    }

    /**
     * 运行类测试
     * @param fullClassName
     */
    public void runClass(String fullClassName) {
        try {
            new ClassRunner(config, fullClassName).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 运行方法测试
     * @param fullClassName
     * @param methodInfo
     */
    public void runMethod(String fullClassName, MethodInfo methodInfo) {
        try {
            new MethodRunner(config, fullClassName, methodInfo).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
