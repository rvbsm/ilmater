package dev.rvbsm.ilmater.time;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.rvbsm.ilmater.IlmaterTranslation;

public final class TickDuration {

    private static final Pattern DURATION_PATTERN = Pattern.compile(
        "(?=\\S)" +
            "(?:(?<day>[0-9]+)D)?" +
            "(?:(?<hour>[0-9]+)H)?" +
            "(?:(?<cycle>[0-9]+)C)?" +
            "(?:(?<minute>[0-9]+)M)?" +
            "(?:(?<second>[0-9]+)S)?" +
            "(?:(?<tick>[0-9]+)T?)?", Pattern.CASE_INSENSITIVE);

    private static final Map<String, Long> UNITS = ImmutableMap.ofEntries(
        Map.entry("day", 1728000L),
        Map.entry("hour", 72000L),
        Map.entry("minute", 1200L),
        Map.entry("second", 20L),
        Map.entry("cycle", 24000L),
        Map.entry("tick", 1L));

    private TickDuration() {
        throw new AssertionError("utility class");
    }

    public static long fromString(@NotNull CharSequence text) {
        final Matcher durationMatcher = DURATION_PATTERN.matcher(text);

        if (durationMatcher.matches()) {
            long ticks = 0;

            for (Map.Entry<String, Long> entry : UNITS.entrySet()) {
                final String unitName = entry.getKey();
                final long unitTicks = entry.getValue();

                final int groupStart = durationMatcher.start(unitName);
                if (groupStart >= 0) {
                    try {
                        final int groupEnd = durationMatcher.end(unitName);
                        final long duration = Long.parseLong(text, groupStart, groupEnd, 10);

                        ticks += Math.multiplyExact(duration, unitTicks);
                    } catch (NumberFormatException | ArithmeticException ignored) {
                    }
                }
            }

            return ticks;
        }

        throw new DateTimeParseException("Text cannot be parsed to ticks", text, 0);
    }

    public static @NotNull Text fromTicks(long ticks) {
        if (ticks < 20) {
            return IlmaterTranslation.translatable("duration", "soon");
        }

        final MutableText result = Text.empty();

        long remainingTicks = ticks;
        for (Map.Entry<String, Long> entry : UNITS.entrySet()) {
            final String unitName = entry.getKey();
            final long unitTicks = entry.getValue();

            if (remainingTicks >= unitTicks) {
                try {
                    final long duration = Math.divideExact(remainingTicks, unitTicks);
                    result.append(Long.toString(duration))
                        .append(" ")
                        .append(IlmaterTranslation.translatable("duration", unitName + (duration > 1 ? "s" : "")))
                        .append(" ");

                } catch (ArithmeticException ignored) {
                }

                remainingTicks %= unitTicks;
                if (remainingTicks < 20) {
                    break;
                }
            }
        }

        return result;
    }
}
