package dev.glabay.jvm;

import dev.glabay.jvm.test.JudgementEngine;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-25
 */
public class InMemoryCompiler {


    public static String compileAndRun(String className, String source, String challengeId) throws Exception {
        var compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("Must run on JDK");
        }

        var diagnostics = new DiagnosticCollector<JavaFileObject>();
        var standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        var fileManager = new MemoryFileManager(standardFileManager);
        var sourceObject = new StringJavaFileObject(className, source);
        var classpath = System.getProperty("java.class.path");
        var task = compiler.getTask(
            null,
            fileManager,
            diagnostics,
            List.of("-classpath", classpath),
            null,
            List.of(sourceObject)
        );

        boolean success = task.call();

        if (!success) {
            var errors = new StringBuilder();
            for (Diagnostic<?> d : diagnostics.getDiagnostics()) {
                errors.append(d.getMessage(null)).append("\n");
            }
            throw new RuntimeException(errors.toString());
        }

        var classLoader = new MemoryClassLoader(fileManager.getAllClassBytes());
        var clazz = classLoader.loadClass(className);
        var result = JudgementEngine.runUnitTest(clazz, challengeId);
        return result.toString();
    }
}
