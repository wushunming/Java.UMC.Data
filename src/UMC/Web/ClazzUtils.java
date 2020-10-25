package UMC.Web;

import UMC.Data.Utility;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


class ClazzUtils {
    private static final String CLASS_SUFFIX = ".class";
    private static final String PACKAGE_SEPARATOR = ".";

    public static List<String> getClazzName(URL url) {
        List<String> result = new ArrayList<>();
        int pathLength = url.getPath().length();
        if (url != null) {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {

                String path = url.getPath();
                getAllClassNameByFile(new File(path), true, pathLength, result);
            } else if ("jar".equals(protocol)) {
                JarFile jarFile = null;
                try {
                    jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                } catch (Exception e) {
//                    e.printStackTrace();
                }
                if (jarFile != null) {
                    getAllClassNameByJar(jarFile, true, result);
                }
            }
        }
        return result;
    }

    private static void getAllClassNameByJar(JarFile jarFile, boolean flag, List<String> result) {

//        List<String> result = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 判断是不是class文件
            if (name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, "").replace("/", ".");
                if (flag) {
                    result.add(name);
                } else {
                    // 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类
                    if (-1 == name.indexOf("$")) {
                        result.add(name);
                    }
                }
            }
        }
    }


    /**
     * 查找包下的所有类的名字
     *
     * @param packageName
     * @param showChildPackageFlag 是否需要显示子包内容
     * @return List集合，内容为类的全名
     */
    public static List<String> getClazzName(String packageName, boolean showChildPackageFlag) {
        int pathLength = Utility.class.getProtectionDomain().getCodeSource().getLocation().getPath().length();
        List<String> result = new ArrayList<>();
        String suffixPath = packageName.replaceAll("\\.", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources(suffixPath);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String path = url.getPath();
                        System.out.println(path);
                        getAllClassNameByFile(new File(path), showChildPackageFlag, pathLength, result);
                    } else if ("jar".equals(protocol)) {
                        JarFile jarFile = null;
                        try {
                            jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (jarFile != null) {
                            getAllClassNameByJar(jarFile, packageName, showChildPackageFlag, result);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 递归获取所有class文件的名字
     *
     * @param file
     * @param flag 是否需要迭代遍历
     * @return List
     */
    private static void getAllClassNameByFile(File file, boolean flag, int pathIndex, List<String> result) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            String path = file.getPath();
            // 注意：这里替换文件分割符要用replace。因为replaceAll里面的参数是正则表达式,而windows环境中File.separator="\\"的,因此会有问题
            if (path.endsWith(CLASS_SUFFIX)) {

                String clazzName = path.substring(pathIndex, path.length() - CLASS_SUFFIX.length())
                        .replace(File.separator, PACKAGE_SEPARATOR);

                if (-1 == clazzName.indexOf("$")) {
                    result.add(clazzName);
                }
            }
            return;

        } else {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File f : listFiles) {
                    if (flag) {
                        getAllClassNameByFile(f, flag, pathIndex, result);
                    } else {
                        if (f.isFile()) {
                            String path = f.getPath();
                            if (path.endsWith(CLASS_SUFFIX)) {
                                String clazzName = path.substring(pathIndex, path.length() - CLASS_SUFFIX.length())
                                        .replace(File.separator, PACKAGE_SEPARATOR);
                                if (-1 == clazzName.indexOf("$")) {
                                    result.add(clazzName);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 递归获取jar所有class文件的名字
     *
     * @param jarFile
     * @param packageName 包名
     * @param flag        是否需要迭代遍历
     * @return List
     */
    private static void getAllClassNameByJar(JarFile jarFile, String packageName, boolean flag, List<String> result) {
//        List<String> result = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 判断是不是class文件
            if (name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, "").replace("/", ".");
                if (flag) {
                    // 如果要子包的文件,那么就只要开头相同且不是内部类就ok
                    if (name.startsWith(packageName) && -1 == name.indexOf("$")) {
                        result.add(name);
                    }
                } else {
                    // 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类
                    if (packageName.equals(name.substring(0, name.lastIndexOf("."))) && -1 == name.indexOf("$")) {
                        result.add(name);
                    }
                }
            }
        }
//        return result;
    }

}
