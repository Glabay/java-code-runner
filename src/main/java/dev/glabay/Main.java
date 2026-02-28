package dev.glabay;

import dev.glabay.jvm.InMemoryCompiler;

/**
 * @author Glabay | Glabay-Studios
 * @project java-code-runner
 * @social Discord: Glabay
 * @since 2026-02-27
 */
public class Main {

    void main() {
        try {
            InMemoryCompiler.compileAndRun();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
