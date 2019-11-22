package org.fhi360.lamis.resource;

import com.sun.jna.Platform;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LibraryManager {
    private static final String WIN32_X86 = "Win32_x86";
    private static final String WIN64_X64 = "Win64_x64";
    private static final String LINUX_X86 = "Linux_x86";
    private static final String LINUX_X86_64 = "Linux_x86_64";
    
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String PATH_SEPARATOR = System.getProperty("path.separator");
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private static final Logger LOG = LoggerFactory.getLogger(LibraryManager.class);

    public LibraryManager() {
    }

    public static void initLibraryPath() {
        String libraryPath = getLibraryPath();
        String jnaLibraryPath = System.getProperty("jna.library.path");
        if (StringUtils.isEmpty(jnaLibraryPath)) {
            System.setProperty("jna.library.path", libraryPath);
        } else {
            System.setProperty("jna.library.path", String.format("%s%s%s", jnaLibraryPath, PATH_SEPARATOR, libraryPath));
        }

        System.setProperty("java.library.path", String.format("%s%s%s", System.getProperty("java.library.path"), PATH_SEPARATOR, libraryPath));
        if (Platform.isMac()) {
            String jnaPlatformLibraryPath = System.getProperty("jna.platform.library.path");
            if (StringUtils.isEmpty(jnaPlatformLibraryPath)) {
                System.setProperty("jna.platform.library.path", libraryPath);
            } else {
                System.setProperty("jna.platform.library.path", String.format("%s%s%s", jnaPlatformLibraryPath, PATH_SEPARATOR, libraryPath));
            }

            System.setProperty("java.platform.library.path", String.format("%s%s%s", System.getProperty("java.platform.library.path"), PATH_SEPARATOR, libraryPath));
        }

        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {
        }

    }

    public static String getLibraryPath() {
        StringBuilder path = new StringBuilder();
        int index = getWorkingDirectory().lastIndexOf(FILE_SEPARATOR);
        if (index == -1) {
            return null;
        } else {
            //String part = getWorkingDirectory().substring(0, index);
            String part = "";
            if (Platform.isWindows()) {
                if (part.endsWith("Bin")) {
                    path.append(part);
                    path.append(FILE_SEPARATOR);
                    path.append(Platform.is64Bit() ? "Win64_x64" : "Win32_x86");
                }
            } else if (Platform.isLinux()) {
                index = part.lastIndexOf(FILE_SEPARATOR);
                if (index == -1) {
                    return null;
                }

                part = part.substring(0, index);
                path.append(part);
                path.append(FILE_SEPARATOR);
                path.append("Lib");
                path.append(FILE_SEPARATOR);
                path.append(Platform.is64Bit() ? "Linux_x86_64" : "Linux_x86");
            } else if (Platform.isMac()) {
                index = part.lastIndexOf(FILE_SEPARATOR);
                if (index == -1) {
                    return null;
                }

                part = part.substring(0, index);
                path.append(part);
                path.append(FILE_SEPARATOR);
                path.append("Frameworks");
                path.append(FILE_SEPARATOR);
                path.append("MacOSX");
            }
            path.append("C:\\LAMIS2\\neurotec\\Bin").append(FILE_SEPARATOR);
            path.append(Platform.is64Bit() ? "Win64_x64" : "Win32_x86");
            return path.toString();
        }
    }
    
    private static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }
}

