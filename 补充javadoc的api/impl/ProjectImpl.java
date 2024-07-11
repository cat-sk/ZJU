package zju.cst.aces.api.impl;

import org.apache.maven.project.MavenProject;
import zju.cst.aces.api.Project;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 实现project的接口
 * 封装和提供对Maven项目的访问
 * @author dfa
 * @date 2024/07/11
 */
public class ProjectImpl implements Project {

    MavenProject project;

    /**
     * 构造函数，传入MavenProject对象
     * @param project
     */
    public ProjectImpl(MavenProject project) {
        this.project = project;
    }

    /**
     * 获取父项目
     * @return {@link Project }
     */
    @Override
    public Project getParent() {
        if (project.getParent() == null) {
            return null;
        }
        return new ProjectImpl(project.getParent());
    }

    /**
     * 获取项目的基目录
     * @return {@link File }
     */
    @Override
    public File getBasedir() {
        return project.getBasedir();
    }

    /**
     * 获取项目的打包方式
     * @return {@link String }
     */
    @Override
    public String getPackaging() {
        return project.getPackaging();
    }

    /**
     * 获取GroupId
     * @return {@link String }
     */
    @Override
    public String getGroupId() {
        return project.getGroupId();
    }

    /**
     * 获取ArtifactId
     * @return {@link String }
     */
    @Override
    public String getArtifactId() {
        return project.getArtifactId();
    }

    /**
     * 获取项目的编译原根目录列表
     * @return {@link List }<{@link String }>
     */
    @Override
    public List<String> getCompileSourceRoots() {
        return project.getCompileSourceRoots();
    }

    /**
     * 获取项目的工件路径
     * @return {@link Path }
     */
    @Override
    public Path getArtifactPath() {
        return Paths.get(project.getBuild().getDirectory()).resolve(project.getBuild().getFinalName() + ".jar");
    }

    /**
     * 获取项目的构建路径
     * @return {@link Path }
     */
    @Override
    public Path getBuildPath() {
        return Paths.get(project.getBuild().getOutputDirectory());
    }

}
