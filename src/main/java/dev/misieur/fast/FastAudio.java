package dev.misieur.fast;

import org.jetbrains.annotations.Nullable;

public class FastAudio {

    /**
     * Retrieves the audio properties from the supplied raw audio data.
     * Supports MP3, OGG
     * (uses Native) (<a href="https://github.com/pdeljanov/Symphonia">See Symphonia</a>)
     *
     * @param audioBytes the raw audio data as a byte array
     * @return an {@link AudioProperties} instance
     */
    public static @Nullable AudioProperties getAudioProperties(byte[] audioBytes) {
        return getAudioProperties(audioBytes, null);
    }

    /**
     * Retrieves the audio properties from the supplied raw audio data.
     * Supports MP3, OGG
     * (uses Native) (<a href="https://github.com/pdeljanov/Symphonia">See Symphonia</a>)
     *
     * @param audioBytes the raw audio data as a byte array
     * @return an {@link AudioProperties} instance
     */
    public static @Nullable AudioProperties getAudioProperties(byte[] audioBytes, String ext) {
        if (Native.enabled()) return Native.getAudioProperties(audioBytes, ext);
        System.err.println("Trying to use getAudioProperties while Native isn't loaded");
        return null;
    }

    public record AudioProperties(int channels, int sampleRate, float durationSeconds) {
    }

}
