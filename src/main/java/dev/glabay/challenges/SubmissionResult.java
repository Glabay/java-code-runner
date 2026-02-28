package dev.glabay.challenges;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-26
 */
public record SubmissionResult(
    boolean compiled,
    boolean passed,
    int testsPassed,
    int totalTests,
    String error
) {}
