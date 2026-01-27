package dev.misieur.fast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class Native {

    private Native() {
    }

    private static volatile boolean enabled = false;

    static native byte[] optimizePng(byte[] input);

    static native @Nullable FastAudio.AudioProperties getAudioProperties(byte[] input, @Nullable String ext);

    public static void load() throws IOException {
        String libName = switch (OS.forName(System.getProperty("os.name"))) {
            case WINDOWS -> "rust.dll";
            case LINUX -> "librust.so";
            case MAC_OS -> "librust.dylib";
        };
        String resourcePath = "/natives/" + libName;
        Path tempDir = Files.createTempDirectory("fast-native-");
        Path extracted = FastFiles.extractFile(resourcePath, tempDir);
        extracted.toFile().setReadable(true);
        extracted.toFile().setExecutable(true);

        System.load(extracted.toAbsolutePath().toString());
        registerCleanup(tempDir);
        enabled = true;
    }

    public static boolean enabled() {
        return enabled;
    }

    enum OS {
        WINDOWS, LINUX, MAC_OS;

        public static OS forName(@NotNull String os) {
            String osName = os.toLowerCase(Locale.ROOT);
            if (osName.contains("windows")) return WINDOWS;
            if (osName.contains("linux")) return LINUX;
            if (osName.contains("mac") || osName.contains("darwin") || osName.contains("osx")) return MAC_OS;
            throw new IllegalStateException("Unsupported OS: " + os);
        }
    }

    private static void registerCleanup(@NotNull Path tempDir) {
        Runtime.getRuntime().addShutdownHook(Thread.ofPlatform().name("fast-native-lib-cleanup").unstarted(() -> {
            try {
                FastFiles.deleteFolderSync(tempDir);
            } catch (IOException ignored) {
            }
        }));
    }

}
