package dev.misieur.fast;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FastBufferedImageTest {

    private static BufferedImage image;
    private static BufferedImage emptyImage;

    @Order(1)
    @DisplayName("Load BufferedImage")
    @Test
    void loadImage() throws IOException {
        image = FastBufferedImage.getImageFromBytes(pngBytes(50, 100, false));
        System.out.println(image.getWidth() + "x" + image.getHeight());
        emptyImage = FastBufferedImage.getImageFromBytes(pngBytes(100, 50, true));
        System.out.println(emptyImage.getWidth() + "x" + emptyImage.getHeight());
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

    @Order(2)
    @Test
    void testIsImageEmpty() {
        assertFalse(FastBufferedImage.isImageEmpty(image));
        System.out.println(image.getWidth() + "x" + image.getHeight());
        assertTrue(FastBufferedImage.isImageEmpty(emptyImage));
        System.out.println(emptyImage.getWidth() + "x" + emptyImage.getHeight());
    }

    @Order(3)
    @Test
    void testResizeImage() {
        image = FastBufferedImage.resizeImage(image, 64, 32);
        System.out.println(image.getWidth() + "x" + image.getHeight());
        assertEquals(64, image.getWidth());
        assertEquals(32, image.getHeight());
    }

    private static byte @NotNull [] pngBytes(int width, int height, boolean empty) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Color[] colors;
        if (empty) {
            colors = new Color[]{new Color(0, 0, 0, 0)};
        } else {
            // 16 random colors + 1 transparent
            colors = new Color[17];
            for (int i = 0; i < 16; i++) {
                colors[i] = new Color(
                        (int) (Math.random() * 256),
                        (int) (Math.random() * 256),
                        (int) (Math.random() * 256),
                        255
                );
            }
            colors[16] = new Color(0, 0, 0, 0);
        }


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = colors[(int) (Math.random() * colors.length)];
                img.setRGB(x, y, color.getRGB());
            }
        }

        return FastBufferedImage.getImageBytes(img);
    }

}
