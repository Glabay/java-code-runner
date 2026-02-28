package dev.glabay.jvm.test;

import dev.glabay.challenges.ChallengeManager;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Glabay | Glabay-Studios
 * @project JavaDuel
 * @social Discord: Glabay
 * @since 2026-02-26
 */
public class JudgementEngine {

    public static TestResult runUnitTest(Class<?> clazz, String challengeId) {
        var method = validateSolveMethod(clazz);
        var cachedChallenge = ChallengeManager.getChallengeById(challengeId);
        var testCases = cachedChallenge.visibleTestCases();
            testCases.addAll(cachedChallenge.hiddenTestCases());
        var passed = new AtomicInteger(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var test : testCases) {
                Future<?> future = executor.submit(() -> {
                    var instance = clazz.getDeclaredConstructor().newInstance();
                    return (String) method.invoke(instance, test.input());
                });
                try {
                    var result = future.get(2, TimeUnit.SECONDS);

                    if (test.expected().equals(result)) {
                        passed.getAndIncrement();
                    }
                }
                catch (TimeoutException e) {
                    future.cancel(true);
                }
                catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
        return new TestResult(passed.get(), testCases.size());
    }

    private static Method validateSolveMethod(Class<?> clazz) {
        try {
            Method method = clazz.getDeclaredMethod("solve", String.class);
            if (!method.getReturnType().equals(String.class)) {
                throw new IllegalArgumentException("Method solve(String) must return String");
            }
            if (!Modifier.isPublic(method.getModifiers())) {
                throw new IllegalArgumentException("Method solve(String) must be public");
            }
            return method;
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class must declare: public String solve(String input)");
        }
    }
}
