package zju.cst.aces.api.impl;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import lombok.Data;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import zju.cst.aces.api.Validator;
import zju.cst.aces.api.config.Config;
import zju.cst.aces.dto.PromptInfo;
import zju.cst.aces.util.TestCompiler;

import java.nio.file.Path;
import java.util.List;

import zju.cst.aces.api.Validator;

/**
 * 实现Validator接口
 * 验证器实现，验证生成的java test的代码
 * @author dfa
 * @date 2024/07/11
 */
@Data
public class ValidatorImpl implements Validator {

    TestCompiler compiler;

    /**
     * 创建实例
     * @param testOutputPath
     * @param compileOutputPath
     * @param targetPath
     * @param classpathElements
     */
    public ValidatorImpl(Path testOutputPath, Path compileOutputPath, Path targetPath, List<String> classpathElements) {
        this.compiler = new TestCompiler(testOutputPath, compileOutputPath, targetPath, classpathElements);
    }

    /**
     * 语法验证
     * @param code
     * @return boolean
     */
    @Override
    public boolean syntacticValidate(String code) {
        try {
            StaticJavaParser.parse(code);
            return true;
        } catch (ParseProblemException e) {
            return false;
        }
    }

    /**
     * 语义验证
     * @param code
     * @param className
     * @param outputPath
     * @param promptInfo
     * @return boolean
     */
    @Override
    public boolean semanticValidate(String code, String className, Path outputPath, PromptInfo promptInfo) {
        compiler.setCode(code);
        return compiler.compileTest(className, outputPath, promptInfo);
    }

    /**
     * 运行时验证
     * @param fullTestName
     * @return boolean
     */
    @Override
    public boolean runtimeValidate(String fullTestName) {
        return compiler.executeTest(fullTestName).getTestsFailedCount() == 0;
    }

    /**
     * 编译
     * @param className
     * @param outputPath
     * @param promptInfo
     * @return boolean
     */
    @Override
    public boolean compile(String className, Path outputPath, PromptInfo promptInfo) {
        return compiler.compileTest(className, outputPath, promptInfo);
    }

    /**
     * 执行
     * @param fullTestName
     * @return {@link TestExecutionSummary }
     */
    @Override
    public TestExecutionSummary execute(String fullTestName) {
        return compiler.executeTest(fullTestName);
    }
}
