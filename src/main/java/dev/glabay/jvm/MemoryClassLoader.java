package dev.glabay.jvm;

import java.util.Map;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-25
 */
public class MemoryClassLoader extends ClassLoader {

    private final Map<String, byte[]> classes;

    public MemoryClassLoader(Map<String, byte[]> classes) {
        super(ClassLoader.getSystemClassLoader());
        this.classes = classes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = classes.get(name);
        if (bytes == null) {
            return super.findClass(name);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }
}
