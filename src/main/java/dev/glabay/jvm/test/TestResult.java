package dev.glabay.jvm.test;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-26
 */
public record TestResult(
    int passed,
    int total
) {
    public boolean allPassed() {
        return passed == total;
    }
}
