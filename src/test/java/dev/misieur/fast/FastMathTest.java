package dev.misieur.fast;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FastMathTest {

    @BeforeEach
    void logTestStart(@NonNull TestInfo testInfo) {
        System.out.println("----------------------------------------");
        System.out.println("Executing test: " + testInfo.getDisplayName());
        System.out.println("----------------------------------------");
    }

    @AfterAll
    static void logTestComplete() {
        System.out.println("----------------------------------------");
        System.out.println("All tests completed.");
        System.out.println("----------------------------------------");
    }

    @Test
    void testSin() {
        assertEquals(0.0f, FastMath.sin(0), "sin(0) should be 0");
        assertEquals(1.0f, FastMath.sin(FastMath.PI_2), "sin(π/2) should be 1");
        assertEquals(0.0f, FastMath.sin(FastMath.PI), 1E-15, "sin(π) should be 0");
        assertEquals(-1.0f, FastMath.sin((float) (Math.PI * 1.5)), "sin(3π/2) should be -1");
        assertEquals(0.0f, FastMath.sin(FastMath.TAU), "sin(2π) should be 0");

        for (int i = 0; i < 1000; i++) {
            float angle = (float) (Math.random() * Math.PI * 2); // Generate random angle
            assertEquals((float) Math.sin(angle), FastMath.sin(angle), 1E-4, "FastMath.sin should approximate Math.sin");
            assertEquals(FastMath.sin(angle), FastMath.sin(angle + FastMath.TAU), 1E-4, "sin(x) should equal sin(x + 2π)");
            assertEquals(FastMath.sin(angle), FastMath.sin(angle - FastMath.TAU), 1E-4, "sin(x) should equal sin(x - 2π)");
        }
    }

    @Test
    void testCos() {
        assertEquals(1.0f, FastMath.cos(0), "cos(0) should be 1");
        assertEquals(0.0f, FastMath.cos(FastMath.PI_2), 1.5E-16, "cos(π/2) should be 0");
        assertEquals(-1.0f, FastMath.cos(FastMath.PI), "cos(π) should be -1");
        assertEquals(0.0f, FastMath.cos((float) (Math.PI * 1.5)), "cos(3π/2) should be 0");
        assertEquals(1.0f, FastMath.cos(FastMath.TAU), "cos(2π) should be 1");

        for (int i = 0; i < 1000; i++) {
            float angle = (float) (Math.random() * Math.PI * 2); // Generate random angle
            assertEquals((float) Math.cos(angle), FastMath.cos(angle), 1E-4, "FastMath.cos should approximate Math.cos");
            assertEquals(FastMath.cos(angle), FastMath.cos(angle + FastMath.TAU), 1E-4, "cos(x) should equal cos(x + 2π)");
            assertEquals(FastMath.cos(angle), FastMath.cos(angle - FastMath.TAU), 1E-4, "cos(x) should equal cos(x - 2π)");
        }
    }

    @Test
    void testAtan2() {
        assertEquals(0.0f, FastMath.atan2(0, 1), "atan2(0, 1) should be 0");
        assertEquals(FastMath.PI, FastMath.atan2(0, -1), "atan2(0, -1) should be π");
        assertEquals(FastMath.PI_2, FastMath.atan2(1, 0), "atan2(1, 0) should be π/2");
        assertEquals(-FastMath.PI_2, FastMath.atan2(-1, 0), "atan2(-1, 0) should be -π/2");

        assertEquals(3 * FastMath.PI / 4, FastMath.atan2(1, -1), 1E-7, "atan2(1, -1) should be 3π/4");
        assertEquals(-3 * FastMath.PI / 4, FastMath.atan2(-1, -1), 1E-7, "atan2(-1, -1) should be -3π/4");
        assertEquals(-FastMath.PI / 4, FastMath.atan2(-1, 1), 1E-7, "atan2(-1, 1) should be -π/4");
        assertEquals(FastMath.PI / 4, FastMath.atan2(1, 1), 1E-7, "atan2(1, 1) should be π/4");

        for (int i = 0; i < 1000; i++) {
            float x = (float) ((Math.random() - 0.5) * 1000); // [-500, 500]
            float y = (float) ((Math.random() - 0.5) * 1000); // [-500, 500]
            assertEquals((float) Math.atan2(y, x), FastMath.atan2(y, x), 1E-3, "FastMath.atan2 should approximate Math.atan2");
        }
    }


}
