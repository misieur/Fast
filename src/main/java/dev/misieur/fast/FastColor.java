package dev.misieur.fast;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FastColor {

    /**
     * Creates a new {@code Color} object from a hexadecimal string representation.
     *
     * @param hex a hexadecimal string representing the color, with or without a leading '#'.
     *            The string may be 6 characters long (RGB) or 8 characters long (ARGB).
     * @return a new {@code Color} instance corresponding to the supplied hexadecimal value.
     */
    @Contract("_ -> new")
    public static @NotNull Color fromHex(@NotNull String hex) {
        if (hex.startsWith("#")) hex = hex.substring(1);
        if (hex.length() == 8) {
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            int a = Integer.parseInt(hex.substring(6, 8), 16);
            return new Color(r, g, b, a);
        } else if (hex.length() == 6) {
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new Color(r, g, b);
        } else {
            throw new IllegalArgumentException("Format hex invalide");
        }
    }

    /**
     * Parses a color from a string representation in HSV (Hue, Saturation, Value) format.
     *
     * @param hsv a string representing the color in HSV format, e.g., "hsv(120, 50%, 75%)"
     * @return a new {@code Color} instance corresponding to the supplied HSV value
     */
    public static @NotNull Color parseHSV(@NotNull String hsv) {
        hsv = hsv.toLowerCase();

        // Remove "hsv(" and ")"
        String content = hsv.replaceAll("hsv\\(|\\)", "");
        String[] parts = content.split(",");

        float h = Float.parseFloat(parts[0].trim().replace("°", ""));
        float s = Float.parseFloat(parts[1].trim().replace("%", "")) / 100f;
        float v = Float.parseFloat(parts[2].trim().replace("%", "")) / 100f;

        return Color.getHSBColor(h / 360f, s, v);
    }

    /**
     * Parses a color string in various formats into a {@code Color} object.
     *
     * @param color a string representing the color, either in hexadecimal format <br>
     *              (e.g., "#RRGGBB" or "#RRGGBBAA") or HSV format <br>
     *              (e.g., "hsv(H, S%, V%)")
     * @return a new {@code Color} instance corresponding to the supplied color value
     */
    public static @NotNull Color parseColor(@NotNull String color) {
        if (color.startsWith("#")) return fromHex(color);
        if (color.startsWith("hsv")) return parseHSV(color);
        throw new IllegalArgumentException("Unsupported color format: " + color);
    }

}
