package net.myscloud.pandora.core.resource;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import net.myscloud.pandora.core.boot.DefaultConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Set;

/**
 * Created by user on 2015/8/7.
 */
public interface ClassReader {

    static final Logger LOGGER = LogManager.getLogger();

    default Set<Class<?>> getClass(String packageName, boolean recursive) {
        return this.getClassByAnnotation(packageName, null, null, recursive);
    }

    /**
     * 默认实现以文件形式的读取
     */
    default Set<Class<?>> getClass(String packageName, Class<?> parent, boolean recursive) {
        return this.getClassByAnnotation(packageName, parent, null, recursive);
    }

    /**
     * 根据条件获取class
     *
     * @param packageName
     * @param packagePath
     * @param parent
     * @param annotation
     * @param recursive
     * @return
     */
    default Set<Class<?>> findClassByPackage(final String packageName, final String packagePath, final Class<?> parent, final Class<? extends Annotation> annotation, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if ((!dir.exists()) || (!dir.isDirectory())) {
            LOGGER.warn("Package " + packageName + " non-existent!");
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = accept(dir, recursive);
        // 循环所有文件
        if (null != dirfiles && dirfiles.length > 0) {
            for (File file : dirfiles) {
                // 如果是目录 则继续扫描
                if (file.isDirectory()) {
                    findClassByPackage(packageName + "." + file.getName(), file.getAbsolutePath(), parent, annotation, recursive, classes);
                } else {
                    // 如果是java类文件 去掉后面的.class 只留下类名
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> clazz = Class.forName(packageName + '.' + className);
                        if (null != parent && null != annotation) {
                            if (null != clazz.getSuperclass() && clazz.getSuperclass().equals(parent) &&
                                    null != clazz.getAnnotation(annotation)) {
                                classes.add(clazz);
                            }
                            continue;
                        }
                        if (null != parent) {
                            if (null != clazz.getSuperclass() && clazz.getSuperclass().equals(parent)) {
                                classes.add(clazz);
                            } else {
                                if (null != clazz.getInterfaces() && clazz.getInterfaces().length > 0 && clazz.getInterfaces()[0].equals(parent)) {
                                    classes.add(clazz);
                                }
                            }
                            continue;
                        }
                        if (null != annotation) {
                            if (null != clazz.getAnnotation(annotation)) {
                                classes.add(clazz);
                            }
                            continue;
                        }
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        LOGGER.error("Cannot find *.class files in jar file：" + e.getMessage());
                    }
                }
            }
        }
        return classes;
    }

    /**
     * 过滤文件规则
     *
     * @param file
     * @param recursive
     * @return
     */
    default File[] accept(File file, final boolean recursive) {
        return file.listFiles(temp -> (recursive && file.isDirectory()) || (file.getName().endsWith(".class")));
    }

    default Set<Class<?>> getClassByAnnotation(String packageName, Class<? extends Annotation> annotation, boolean recursive) {
        return this.getClassByAnnotation(packageName, null, annotation, recursive);
    }

    default Set<Class<?>> getClassByAnnotation(String packageName, Class<?> parent, Class<? extends Annotation> annotation, boolean recursive) {
        Preconditions.checkNotNull(packageName, "PackageName cannot be null");
        Set<Class<?>> classes = Sets.newHashSet();
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的URL
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), DefaultConfig.PROJECT_ENCODING);
                Set<Class<?>> subClasses = findClassByPackage(packageName, filePath, parent, annotation, recursive, classes);
                if (subClasses.size() > 0) {
                    classes.addAll(subClasses);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return classes;
    }
}
