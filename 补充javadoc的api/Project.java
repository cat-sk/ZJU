package zju.cst.aces.api;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * 项目接口
 *
 * @author dfa
 * @date 2024/07/11
 */
public interface Project {
    /**
     * 获取父项目
     * @return {@link Project }
     */
    Project getParent();

    /**
     * 获取基础目录
     * @return {@link File }
     */
    File getBasedir();
    /**
     * 获取打包类型
     * Get the project packaging type.
     */
    String getPackaging();

    /**
     * 获取组Id
     * @return {@link String }
     */
    String getGroupId();

    /**
     * 获取工件Id
     * @return {@link String }
     */
    String getArtifactId();

    /**
     * 获取编译原根目录
     * @return {@link List }<{@link String }>
     */
    List<String> getCompileSourceRoots();

    /**
     * 获取工件路径
     * @return {@link Path }
     */
    Path getArtifactPath();

    /**
     * 获取构建路径的方法
     * @return {@link Path }
     */
    Path getBuildPath();

}
