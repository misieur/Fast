package dev.misieur.fast;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MiscTest {

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

    @DisplayName("FastBytes String")
    @Test
    void testFastBytesStr() {
        byte[] bytes = FastBytes.getBytesFromBase64("SGVsbG8sIFdvcmxkIQ==");
        String s = new String(bytes);
        System.out.println(s);
        assertEquals("Hello, World!", s);
    }

    @DisplayName("FastBytes Image")
    @Test
    void testFastBytesImg() throws IOException {
        byte[] bytes = FastBytes.getBytesFromBase64("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAAAAADhZOFXAAAAAnRSTlMAAHaTzTgAAAA9SURBVHjaY2DwUmIAAjkgVlORl5YBcRwV5dQMGUBAQ1MGTNsqKMmBGdKi4qIgWlZCREwQpFNQSICXT4QBAKjnBFxYqkD9AAAAAElFTkSuQmCC");
        String s = new String(bytes);
        System.out.println(s);
        BufferedImage img = FastBufferedImage.getImageFromBytes(bytes);
        assertEquals(8, img.getWidth());
        assertEquals(8, img.getHeight());
    }

    @DisplayName("FastEnum")
    @Test
    void testFastEnum() {
        long start = System.nanoTime();
        assertEquals(TestEnum.ONE, FastEnum.get("ONE", TestEnum.class));
        assertNull(FastEnum.get("FIVE", TestEnum.class));
        assertTrue(FastEnum.contains("ONE", TestEnum.class));
        assertFalse(FastEnum.contains("FIVE", TestEnum.class));
        assertEquals(TestEnum.TWO, FastEnum.getOrElseGet("FIVE", TestEnum.class, () -> TestEnum.TWO));
        assertEquals(TestEnum.TWO, FastEnum.getOrElse("FIVE", TestEnum.class, TestEnum.TWO));
        assertEquals(Optional.of(TestEnum.THREE), FastEnum.getOptional("THREE", TestEnum.class));
        assertEquals(Optional.empty(), FastEnum.getOptional("FIVE", TestEnum.class));
        System.out.println("Time: " + (System.nanoTime() - start) / 1_000_000D + "ms");
    }

    enum TestEnum {
        ONE, TWO, THREE, FOUR
    }

}
