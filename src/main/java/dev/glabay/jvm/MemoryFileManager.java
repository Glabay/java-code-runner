package dev.glabay.jvm;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-25
 */
public class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private final Map<String, ByteArrayJavaFileObject> compiledClasses = new HashMap<>();

    public MemoryFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
                                               String className,
                                               JavaFileObject.Kind kind,
                                               FileObject sibling) {

        ByteArrayJavaFileObject file = new ByteArrayJavaFileObject(className, kind);
        compiledClasses.put(className, file);
        return file;
    }

    public Map<String, byte[]> getAllClassBytes() {
        Map<String, byte[]> result = new HashMap<>();
        compiledClasses.forEach((k, v) -> result.put(k, v.getBytes()));
        return result;
    }
}
