package me.frostingly.gencore;

import org.bukkit.ChatColor;

import java.util.*;

public class Utilities {

    /**
     * Formats the given string with legacy only color codes.
     *
     * @param string the string that should be formatted
     * @return a formatted string
     */
    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Formats the given array list with legacy only color codes.
     *
     * @param input the array list that should be formatted
     * @return a formatted array list
     */
    public static List<String> formatList(List<String> input) {
        List<String> ret = new ArrayList<>();
        for (String line : input) ret.add(ChatColor.translateAlternateColorCodes('&', line));
        return ret;
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatNumber(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatNumber(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatNumber(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static boolean canFormat(long value) {
       for (Long longValue : suffixes.keySet()) {
            String stringified = String.valueOf(longValue).replace("_", "");
            long updatedLong = Long.parseLong(stringified);
            if (value >= updatedLong) return true;
        }
        return false;
    }

}
