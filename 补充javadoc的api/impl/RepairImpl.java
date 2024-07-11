package zju.cst.aces.api.impl;

import lombok.Data;
import okhttp3.Response;
import zju.cst.aces.api.Repair;
import zju.cst.aces.api.Validator;
import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.ChatResponse;
import zju.cst.aces.dto.PromptInfo;
import zju.cst.aces.runner.MethodRunner;

import java.io.IOException;

import static zju.cst.aces.runner.AbstractRunner.*;
import static zju.cst.aces.api.impl.ChatGenerator.*;

/**
 * 实现Repair接口
 * 用于自动修复代码
 * 两种方法修复：
 * 1.基于规则的方法
 * 2.基于LLM的方法
 * @author dfa
 * @date 2024/07/11
 */
@Data
public class RepairImpl implements Repair {

    Config config;

    PromptConstructorImpl promptConstructorImpl;

    boolean success = false;

    /**
     * 创建实例
     * @param config
     * @param promptConstructorImpl
     */
    public RepairImpl(Config config, PromptConstructorImpl promptConstructorImpl) {
        this.config = config;
        this.promptConstructorImpl = promptConstructorImpl;
    }

    /**
     * 基于规则的方法修复代码
     * @param code
     * @return {@link String }
     */
    @Override
    public String ruleBasedRepair(String code) {
        code = changeTestName(code, promptConstructorImpl.getTestName());
        code = repairPackage(code, promptConstructorImpl.getPromptInfo().getClassInfo().getPackageName());
        code = repairImports(code, promptConstructorImpl.getPromptInfo().getClassInfo().getImports());
        return code;
    }

    /**
     * 基于LLM的方法修复代码
     * @param code
     * @param rounds
     * @return {@link String }
     */
    @Override
    public String LLMBasedRepair(String code, int rounds) {
        PromptInfo promptInfo = promptConstructorImpl.getPromptInfo();
        promptInfo.setUnitTest(code);
        String fullClassName = promptInfo.getClassInfo().getPackageName() + "." + promptInfo.getClassInfo().getClassName();
        if (MethodRunner.runTest(config, promptConstructorImpl.getFullTestName(), promptInfo, rounds)) {
            this.success = true;
            return code;
        }

        promptConstructorImpl.generate();
        if (promptConstructorImpl.isExceedMaxTokens()) {
            config.getLog().error("Exceed max prompt tokens: " + promptInfo.methodInfo.methodName + " Skipped.");
            return code;
        }
        ChatResponse response = chat(config, promptConstructorImpl.getMessages());
        String newcode = extractCodeByResponse(response);
        if (newcode.isEmpty()) {
            config.getLog().warn("Test for method < " + promptInfo.methodInfo.methodName + " > extract code failed");
            return code;
        } else {
            return newcode;
        }
    }

    /**
     * 获取是否成功修复
     *
     * @param code
     * @return {@link String }
     */
    @Override
    public String LLMBasedRepair(String code) {
        PromptInfo promptInfo = promptConstructorImpl.getPromptInfo();
        promptInfo.setUnitTest(code);
        String fullClassName = promptInfo.getClassInfo().getPackageName() + "." + promptInfo.getClassInfo().getClassName();
        if (MethodRunner.runTest(config, promptConstructorImpl.getFullTestName(), promptInfo, 0)) {
            config.getLog().info("Test for method < " + promptInfo.methodInfo.methodName + " > doesn't need repair");
            return code;
        }

        promptConstructorImpl.generate();

        if (promptConstructorImpl.isExceedMaxTokens()) {
            config.getLog().error("Exceed max prompt tokens: " + promptInfo.methodInfo.methodName + " Skipped.");
            return code;
        }
        ChatResponse response = chat(config, promptConstructorImpl.getMessages());
        String newcode = extractCodeByResponse(response);
        if (newcode.isEmpty()) {
            config.getLog().warn("Test for method < " + promptInfo.methodInfo.methodName + " > extract code failed");
            return code;
        } else {
            return newcode;
        }
    }
}
