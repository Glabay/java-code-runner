package dev.glabay.jvm.test;

import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
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

    private static JsonMapper getMapper() {
        return JsonMapper.builder()
            .build();
    }

    public static void runUnitTest(Class<?> clazz) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var challenge = new File("sandbox/challenge.json");
            var cachedChallenge = getMapper().readValue(challenge, Challenge.class);
            var testCases = cachedChallenge.visibleTestCases();
                testCases.addAll(cachedChallenge.hiddenTestCases());
            var passed = new AtomicInteger(0);

            for (var test : testCases) {
                Future<?> future = executor.submit(() -> {
                    var instance = clazz.getDeclaredConstructor().newInstance();
                    var method = validateSolveMethod(clazz);
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
            var result = new TestResult(passed.get(), testCases.size());
            var resultFile = new File("sandbox/result.json");
            getMapper().writerWithDefaultPrettyPrinter().writeValue(resultFile, result);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
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
