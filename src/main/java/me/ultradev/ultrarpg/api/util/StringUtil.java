package me.ultradev.ultrarpg.api.util;

import org.apache.commons.lang.WordUtils;

import java.util.*;

public class StringUtil {

    private StringUtil() {}

    public static final char[] VOWELS = new char[]{'a', 'e', 'i', 'o', 'u'};
    public static final Map<String, String> CACHED_WRAPPING = new HashMap<>();

    public static String getAOrAn(String s) {
        String stripped = ColorUtil.stripColor(s).toLowerCase();
        for (Character check : VOWELS) {
            if (stripped.charAt(0) == check) {
                return "an";
            }
        }
        return "a";
    }

    public static String replaceLast(String string, String toReplace, String replaceWith) {
        StringBuilder builder = new StringBuilder(string);
        int index = builder.lastIndexOf(toReplace);
        builder.replace(index, index + toReplace.length(), replaceWith);
        return builder.toString();
    }

    public static String toProperCase(String s) {
        List<String> words = new ArrayList<>();
        for (String word : s.split(" ")) {
            words.add(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase());
        }
        return String.join(" ", words);
    }

    public static String wrap(String s, int maxLineLength) {
        if (CACHED_WRAPPING.containsKey(s)) return CACHED_WRAPPING.get(s);
        if (s.length() <= maxLineLength && !s.contains("\n")) {
            CACHED_WRAPPING.put(s, s);
            return s;
        }
        if (s.contains("\n")) {
            StringBuilder sb = new StringBuilder();
            String lastColor = null;
            for (String line : s.split("\n")) {
                if (line.equals("\n")) sb.append("\n");
                else {
                    if (Objects.equals(ColorUtil.getFirstColor(line), lastColor)) {
                        sb.append(wrap(line, maxLineLength));
                    } else {
                        sb.append(wrap(lastColor + line, maxLineLength));
                    }
                }
                lastColor = ColorUtil.getLastColor(line);
            }
            String result = sb.toString();
            CACHED_WRAPPING.put(s, result);
            return result;
        }

        List<String> wrappedLines = new ArrayList<>(List.of(WordUtils.wrap(s, maxLineLength).replaceAll("\r", "").split("\n")));
        Iterator<String> it = wrappedLines.iterator();
        int i = 1;
        String lastColor = null;
        while (it.hasNext()) {
            String line = it.next();
            String nextLine = wrappedLines.get(i);
            String nextLastColor = ColorUtil.getLastColor(line);
            if (lastColor == null || !Objects.equals(lastColor, nextLastColor)) {
                lastColor = nextLastColor;
            }
            if (!Objects.equals(ColorUtil.getFirstColor(nextLine), nextLastColor)) {
                wrappedLines.set(i, lastColor + nextLine);
            }
            i++;
            if (i == wrappedLines.size()) break;
        }
        return String.join("\n", wrappedLines);
    }

}
