package dev.glabay.jvm;

import dev.glabay.jvm.test.JudgementEngine;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-25
 */
public class InMemoryCompiler {
    private static final String CLASS_NAME = "Challenge";
    private static final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

    public static void compileAndRun() throws Exception {
        var compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("Must run on JDK");
        }
        var standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        var fileManager = new MemoryFileManager(standardFileManager);
        var file = new File("sandbox/Challenge.java");
        var sourceObject = new StringJavaFileObject(CLASS_NAME, Files.readString(file.toPath()));
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
}
