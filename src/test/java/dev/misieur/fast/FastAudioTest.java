package dev.misieur.fast;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FastAudioTest {

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

    @DisplayName("OGG AudioProperties test")
    @Test
    void testSmallPngCompression() throws Exception {
        byte[] bytes = FastFiles.extractFileBytes("/Allegro_Non_Molto_Winter_Vivaldi.ogg");
        FastAudio.AudioProperties props = FastAudio.getAudioProperties(bytes, "ogg");
        assertNotNull(props);
        System.out.println(props);
    }

    @DisplayName("MP3 AudioProperties test")
    @Test
    void testBigPngCompression() throws Exception {
        byte[] bytes = FastFiles.extractFileBytes("/Allegro_Non_Molto_Winter_Vivaldi.mp3");
        FastAudio.AudioProperties props = FastAudio.getAudioProperties(bytes, "mp3");
        assertNotNull(props);
        System.out.println(props);
    }
}