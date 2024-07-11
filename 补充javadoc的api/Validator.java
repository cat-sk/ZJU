package zju.cst.aces.api;

import org.junit.platform.launcher.listeners.TestExecutionSummary;
import zju.cst.aces.dto.PromptInfo;

import java.nio.file.Path;

/**
 * 验证器接口
 * @author dfa
 * @date 2024/07/11
 */
public interface Validator {

    /**
     * 语法验证
     * @param code
     * @return boolean
     */
    boolean syntacticValidate(String code);

    /**
     * 语义验证
     * @param code
     * @param className
     * @param outputPath
     * @param promptInfo
     * @return boolean
     */
    boolean semanticValidate(String code, String className, Path outputPath, PromptInfo promptInfo);

    /**
     * 运行时验证
     * @param fullTestName
     * @return boolean
     */
    boolean runtimeValidate(String fullTestName);

    /**
     * 编译
     * @param className
     * @param outputPath
     * @param promptInfo
     * @return boolean
     */
    public boolean compile(String className, Path outputPath, PromptInfo promptInfo);

    /**
     * 执行测试验证
     * @param fullTestName
     * @return {@link TestExecutionSummary }
     */
    public TestExecutionSummary execute(String fullTestName);
}
