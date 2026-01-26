package dev.misieur.fast;

public class FastImage {

    /**
     * Optimizes a PNG image using oxipng through Rust.
     * (uses Native)
     *
     * @param pngImageBytes the PNG image data to be optimized
     * @return a byte array containing the optimized PNG image
     */
    public static byte[] optimizePng(byte[] pngImageBytes) {
        if (Native.enabled()) return Native.optimizePng(pngImageBytes);
        System.err.println("Trying to use optimizePng while Native isn't loaded");
        return pngImageBytes;
    }

}
