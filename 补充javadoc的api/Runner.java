package zju.cst.aces.api;

import zju.cst.aces.dto.MethodInfo;

/**
 * 运行器接口
 * 运行类和方法测试
 * 用于封装和提供运行测试的功能
 * @author dfa
 * @date 2024/07/11
 */
public interface Runner {

    /**
     * 运行类测试
     * @param fullClassName
     */
    public void runClass(String fullClassName);

    /**
     * 运行方法测试
     * @param fullClassName
     * @param methodInfo
     */
    public void runMethod(String fullClassName, MethodInfo methodInfo);
}