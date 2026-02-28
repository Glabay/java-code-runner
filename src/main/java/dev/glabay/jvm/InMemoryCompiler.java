package dev.glabay.jvm;

import dev.glabay.jvm.test.JudgementEngine;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-25
 */
public class InMemoryCompiler {
    private static final String CLASS_NAME = "Challenge";
    private static final Path executionRoot = Paths.get("/executions");
    private static final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

    public static String compileAndRun(String userId, String challengeId) throws Exception {
        var compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("Must run on JDK");
        }
        var standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        var fileManager = new MemoryFileManager(standardFileManager);
        var sourceObject = new StringJavaFileObject(CLASS_NAME, getChallengeClassForUserId(userId));
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
        var result = JudgementEngine.runUnitTest(clazz, challengeId);
        return result.toString();
    }

    private static String getChallengeClassForUserId(String userId) throws Exception {
        var executionDir = Files.createTempDirectory(executionRoot, "exec-".concat(userId));
        var javaClazz = executionDir.resolve("Challenge.java");
        return Files.readString(javaClazz);
    }
}
