package dev.glabay.jvm;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-25
 */
public class StringJavaFileObject extends SimpleJavaFileObject {
    private final String source;

    protected StringJavaFileObject(String className, String source) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.source = source;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return source;
    }
}
