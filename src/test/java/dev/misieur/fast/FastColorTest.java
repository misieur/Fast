package dev.misieur.fast;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FastColorTest {

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
    public void testParseColorHex() {
        String hex = "#ff0000";
        Color expected = new Color(255, 0, 0);
        Color actual = FastColor.parseColor(hex);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseColorHSV() {
        String hsv = "hsv(120, 100%, 50%)";
        Color expected = Color.getHSBColor(120f / 360f, 1f, 0.5f);
        Color actual = FastColor.parseColor(hsv);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseColorHexWithAlpha() {
        String hex = "#00ff0080";
        Color expected = new Color(0, 255, 0, 128);
        Color actual = FastColor.parseColor(hex);
        assertEquals(expected, actual);
    }
}