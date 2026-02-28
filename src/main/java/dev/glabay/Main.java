package dev.glabay;

import dev.glabay.jvm.InMemoryCompiler;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Glabay | Glabay-Studios
 * @project java-code-runner
 * @social Discord: Glabay
 * @since 2026-02-27
 */
public class Main {

    void main(String... args) {
        var challengeId = args[0];
        var userId = args[1];
        try {
            var executionRoot = Paths.get("/executions");
            var executionDir = Files.createTempDirectory(executionRoot, "exec-".concat(userId));
            var result = InMemoryCompiler.compileAndRun(userId, challengeId);
            // TODO: save this to a temporary file
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
