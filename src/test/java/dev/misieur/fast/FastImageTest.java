package dev.misieur.fast;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FastImageTest {

    @BeforeAll
    static void loadNative() throws IOException {
        System.out.println("Loading native library...");
        Native.load();
        System.out.println("Native library loaded. Starting tests...");
    }

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

    @DisplayName("Small PNG compression")
    @Test
    void testSmallPngCompression() {
        byte[] png = pngBytes(32, 32);
        byte[] optimized = FastImage.optimizePng(png);
        System.out.println("Original PNG size: " + png.length + ", optimized size: " + optimized.length);
        assertTrue(optimized.length < png.length,
                () -> "Optimized size (" + optimized.length + ") should be less than original size (" + png.length + ")");
    }

    @DisplayName("Big PNG compression")
    @Test
    void testBigPngCompression() {
        byte[] png = pngBytes(2048, 2048);
        byte[] optimized = FastImage.optimizePng(png);
        System.out.println("Original PNG size: " + png.length + ", optimized size: " + optimized.length);
        assertTrue(optimized.length < png.length,
                () -> "Optimized size (" + optimized.length + ") should be less than original size (" + png.length + ")");
    }

    private static byte @NotNull [] pngBytes(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 16 random colors + 1 transparent
        Color[] colors = new Color[17];
        for (int i = 0; i < 16; i++) {
            colors[i] = new Color(
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    255
            );
        }
        colors[16] = new Color(0, 0, 0, 0);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = colors[(int) (Math.random() * colors.length)];
                img.setRGB(x, y, color.getRGB());
            }
        }

        return FastBufferedImage.getImageBytes(img);
    }

}
