package dev.misieur.fast;

import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public class FastBytes {

    public static byte @NotNull [] getBytesFromBase64(@NotNull String base64String) {
        try {
            int commaIndex = base64String.indexOf(",");
            String base64Image = (commaIndex != -1)
                    ? base64String.substring(commaIndex + 1)
                    : base64String;

            return Base64.getMimeDecoder().decode(base64Image.trim());
        } catch (IllegalArgumentException e) {
            return new byte[0];
        }
    }

}
