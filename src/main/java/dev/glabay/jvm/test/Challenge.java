package dev.glabay.jvm.test;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-26
 */
public record Challenge(
    String id,
    String title,
    String description,
    String methodSignature,
    List<TestCase> visibleTestCases,
    List<TestCase> hiddenTestCases
) {}
