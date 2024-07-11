package zju.cst.aces.api.impl.obfuscator.util;

import okio.BufferedSource;
import okio.Okio;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import zju.cst.aces.api.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * 解析java类文件和JAR文件
 * 使用ASM库
 *
 * @author dfa
 * @date 2024/07/11
 */
public class ASMParser {

    private final Config config;

    /**
     * 创建实例
     * @param config
     */
    public ASMParser(Config config) {
        this.config = config;
    }

    /**
     * 读取类文件
     * @param classNodes
     * @param methodSigs
     * @return {@link Set }<{@link String }>
     */
    Set<String> getEntries(Set<ClassNode> classNodes, Collection<String> methodSigs) {
        Set<String> entries = new HashSet<>();
        return entries;
    }

    /**
     * 读取类文件
     * @param classFile
     * @return {@link Set }<{@link ClassNode }>
     * @throws IOException
     */
    public Set<ClassNode> loadClasses(File classFile) throws IOException {
        Set<ClassNode> classes = new HashSet<>();
        InputStream is = new FileInputStream(classFile);
        return readClass(classFile.getName(), is, classes);
    }


    /**
     * 读取JAR文件
     * @param jarFile
     * @return {@link Set }<{@link ClassNode }>
     * @throws IOException
     */
    public Set<ClassNode> loadClasses(JarFile jarFile) throws IOException {
        Set<ClassNode> targetClasses = new HashSet<>();
        Stream<JarEntry> str = jarFile.stream();
        str.forEach(z -> readJar(jarFile, z, targetClasses));
        jarFile.close();
        return targetClasses;
    }


    /**
     * 读取JAR文件
     * @param className
     * @param is
     * @param targetClasses
     * @return {@link Set }<{@link ClassNode }>
     */
    private Set<ClassNode> readClass(String className, InputStream is, Set<ClassNode> targetClasses) {
        try {
            BufferedSource source = Okio.buffer(Okio.source(is));
            byte[] bytes = source.readByteArray();
            String cafebabe = String.format("%02X%02X%02X%02X", bytes[0], bytes[1], bytes[2], bytes[3]);
            if (!cafebabe.toLowerCase().equals("cafebabe")) {
                // This class doesn't have a valid magic
                return targetClasses;
            }
            ClassNode cn = getNode(bytes);
            targetClasses.add(cn);
        } catch (Exception e) {
//            config.getLog().warn("Fail to read class {}" + className + e);
            throw new RuntimeException("Fail to read class {}" + className + ": " + e);
        }
        return targetClasses;
    }


    /**
     * 获取类节点
     * @param jar
     * @param entry
     * @param targetClasses
     * @return {@link Set }<{@link ClassNode }>
     */
    private Set<ClassNode> readJar(JarFile jar, JarEntry entry, Set<ClassNode> targetClasses) {
        String name = entry.getName();
        if (name.endsWith(".class")) {
            String className = name.replace(".class", "").replace("/", ".");
            // if relevant options are not specified, classNames will be empty
            try (InputStream jis = jar.getInputStream(entry)) {
                return readClass(className, jis, targetClasses);
            } catch (IOException e) {
                config.getLog().warn("Fail to read class {} in jar {}" + entry + jar.getName() + e);
            }
        } else if (name.endsWith("jar") || name.endsWith("war")) {

        }
        return targetClasses;
    }


    /**
     * 获取类节点
     * @param bytes
     * @return {@link ClassNode }
     */
    private ClassNode getNode(byte[] bytes) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode cn = new ClassNode();
        try {
            cr.accept(cn, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // garbage collection friendly
        cr = null;
        return cn;
    }
}