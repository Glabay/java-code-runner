package dev.glabay;

import dev.glabay.jvm.MemoryClassLoader;
import dev.glabay.jvm.MemoryFileManager;
import dev.glabay.jvm.StringJavaFileObject;
import dev.glabay.jvm.test.JudgementEngine;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project java-code-runner
 * @social Discord: Glabay
 * @since 2026-02-27
 */
public class Main {
    private static final String CLASS_NAME = "Challenge";
    private static final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

    void main() {
        try {
            var compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                throw new IllegalStateException("Must run on JDK");
            }
            var standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
            var fileManager = new MemoryFileManager(standardFileManager);
            var file = Path.of("/sandbox", "Challenge.java");
            var sourceObject = new StringJavaFileObject(CLASS_NAME, Files.readString(file));
            var task = compiler.getTask(
                null,
                fileManager,
                diagnostics,
                null,
                null,
                List.of(sourceObject)
            );
            var success = task.call();

            if (!success) {
                var errors = new StringBuilder();
                for (var d : diagnostics.getDiagnostics()) {
                    errors.append(d.getMessage(null)).append("\n");
                }
                throw new RuntimeException(errors.toString());
            }

            var classLoader = new MemoryClassLoader(fileManager.getAllClassBytes());
            var clazz = classLoader.loadClass(CLASS_NAME);
            JudgementEngine.runUnitTest(clazz);
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
