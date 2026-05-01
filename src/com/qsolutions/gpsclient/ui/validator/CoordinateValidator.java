package com.qsolutions.gpsclient.ui.validator;

/**
 * Utility class with static validators for GPS coordinates and schedule times.
 *
 * <p>All methods return {@code false} for {@code null}, empty, or
 * non-parseable input rather than throwing exceptions, making them safe to
 * call directly from JavaFX {@code textProperty} listeners.</p>
 */
public final class CoordinateValidator {

    private CoordinateValidator() {}

    /**
     * Returns {@code true} if {@code input} represents a valid latitude.
     *
     * <p>A valid latitude is a parseable decimal number in the closed range
     * {@code [-90, 90]}.</p>
     *
     * <pre>
     *   isValidLatitude("-17.0526") → true
     *   isValidLatitude("90")       → true
     *   isValidLatitude("90.1")     → false
     *   isValidLatitude("abc")      → false
     *   isValidLatitude("")         → false
     * </pre>
     *
     * @param input the string to validate
     * @return {@code true} if the value is a valid latitude, {@code false} otherwise
     */
    public static boolean isValidLatitude(String input) {
        if (input == null || input.isBlank()) return false;
        try {
            double value = Double.parseDouble(input.trim());
            return value >= -90.0 && value <= 90.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns {@code true} if {@code input} represents a valid longitude.
     *
     * <p>A valid longitude is a parseable decimal number in the closed range
     * {@code [-180, 180]}.</p>
     *
     * <pre>
     *   isValidLongitude("-101.2345") → true
     *   isValidLongitude("180")       → true
     *   isValidLongitude("180.01")    → false
     *   isValidLongitude("")          → false
     * </pre>
     *
     * @param input the string to validate
     * @return {@code true} if the value is a valid longitude, {@code false} otherwise
     */
    public static boolean isValidLongitude(String input) {
        if (input == null || input.isBlank()) return false;
        try {
            double value = Double.parseDouble(input.trim());
            return value >= -180.0 && value <= 180.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns {@code true} if {@code input} is a valid schedule time.
     *
     * <p>Accepts either the literal string {@code "Manual"} (case-insensitive)
     * or a time in {@code HH:MM} 24-hour format where hours are {@code 00–23}
     * and minutes are {@code 00–59}.</p>
     *
     * <pre>
     *   isValidTime("06:00")   → true
     *   isValidTime("23:59")   → true
     *   isValidTime("Manual")  → true
     *   isValidTime("24:00")   → false
     *   isValidTime("6:00")    → false  (missing leading zero)
     *   isValidTime("")        → false
     * </pre>
     *
     * @param input the string to validate
     * @return {@code true} if the value represents a valid time or "Manual"
     */
    public static boolean isValidTime(String input) {
        if (input == null || input.isBlank()) return false;
        if (input.trim().equalsIgnoreCase("Manual")) return true;

        if (!input.matches("\\d{2}:\\d{2}")) return false;

        String[] parts = input.split(":");
        int hours   = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
    }
}
