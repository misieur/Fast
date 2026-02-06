package dev.misieur.fast;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FastFilesTest {

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
    @Order(1)
    void testExtractFile(@TempDir Path tempDir) throws IOException {
        Path extracted = FastFiles.extractFile("/Allegro_Non_Molto_Winter_Vivaldi.mp3", tempDir);
        assertTrue(Files.exists(extracted));
        assertTrue(Files.size(extracted) > 0);
    }

    @Test
    @Order(2)
    void testExtractFileBytes() throws IOException {
        byte[] bytes = FastFiles.extractFileBytes("/Allegro_Non_Molto_Winter_Vivaldi.ogg");
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);

        try (InputStream in = FastFilesTest.class.getResourceAsStream("/Allegro_Non_Molto_Winter_Vivaldi.ogg")) {
            assertNotNull(in);
            byte[] expected = in.readAllBytes();
            assertArrayEquals(expected, bytes);
        }
    }

    @Test
    @Order(3)
    void testDeleteFolderSync(@NotNull @TempDir Path tempDir) throws IOException {
        Path subFolder = tempDir.resolve("folder");
        Files.createDirectories(subFolder);
        Files.writeString(subFolder.resolve("file.txt"), "Hello, World!");

        FastFiles.deleteFolderSync(tempDir);
        assertFalse(Files.exists(tempDir));
    }

    @Test
    @Order(4)
    void testExtractFolderFromJar(@NotNull @TempDir Path tempDir) throws Exception {
        Path folder = tempDir.resolve("folder");
        FastFiles.extractFolderFromJar("folder", folder);
        assertTrue(Files.exists(folder));
        try (Stream<Path> stream = Files.list(folder)) {
            assertEquals(1, stream.count());
        }
    }

}
