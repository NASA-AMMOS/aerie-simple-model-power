package demosystem;

import com.google.common.collect.ImmutableList;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class Instants {

  private Instants() {
    throw new UnsupportedOperationException("This class is non-instantiable");
  }

  /**
   * Number of decimal places on seconds field in date-time formats defined below.
   * Since standard accuracy is 1 microsecond, 6 decimal places is sufficient.
   */
  private static final int TIME_PRECISION = 6;

  /**
   * ISO date format with an implied UTC time zone which is omitted from the string representation.
   */
  public static final DateTimeFormatter ISO_FORMATTER = new DateTimeFormatterBuilder()
    .appendPattern("uuuu-MM-dd'T'HH:mm:ss")
    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, TIME_PRECISION, true)
    .appendZoneId()
    .toFormatter()
    .withZone(ZoneOffset.UTC);

  /**
   * ISO date format with an implied UTC time zone which is omitted from the string representation.
   */
  public static final DateTimeFormatter ISO_WITHOUT_ZONE_FORMATTER = new DateTimeFormatterBuilder()
    .appendPattern("uuuu-MM-dd'T'HH:mm:ss")
    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, TIME_PRECISION, true)
    .toFormatter()
    .withZone(ZoneOffset.UTC);

  /**
   * Day-of-year format with explicit time zone.
   */
  public static final DateTimeFormatter DOY_FORMATTER = new DateTimeFormatterBuilder()
    .appendPattern("uuuu-DDD'T'HH:mm:ss")
    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, TIME_PRECISION, true)
    .appendZoneId()
    .toFormatter()
    .withZone(ZoneOffset.UTC);

  /**
   * Day-of-year format with an implied UTC time zone which is omitted from the string representation.
   */
  public static final DateTimeFormatter DOY_WITHOUT_ZONE_FORMATTER = new DateTimeFormatterBuilder()
    .appendPattern("uuuu-DDD'T'HH:mm:ss")
    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, TIME_PRECISION, true)
    .toFormatter()
    .withZone(ZoneOffset.UTC);

  /**
   * Same as {@code DOY_WITHOUT_ZONE_FORMATTER}, except that
   * times must be padded out to {@code TIME_PRECISION} decimal places.
   * Useful for consistent format when printing, since lax version will not print trailing zeros.
   */
  public static final DateTimeFormatter STRICT_DOY_WITHOUT_ZONE_FORMATTER = new DateTimeFormatterBuilder()
    .appendPattern("uuuu-DDD'T'HH:mm:ss")
    .appendFraction(ChronoField.MICRO_OF_SECOND, TIME_PRECISION, TIME_PRECISION, true)
    .toFormatter()
    .withZone(ZoneOffset.UTC);

  /**
   * Time format not adhering to a specific widely-recognized standard, but in common use across JPL.
   * Uses a written but abbreviated month and an implied UTC time zone.
   * Update this with a better name if such a name is discovered.
   */
  public static final DateTimeFormatter JPL_TIMESTAMP_FORMATTER = new DateTimeFormatterBuilder()
    .appendPattern("uuuu LLL d HH:mm:ss")
    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, TIME_PRECISION, true)
    .toFormatter()
    .withZone(ZoneOffset.UTC);

  /**
   * All formats recognized by {@code parseLeniently}.
   * Formats should be unambiguous; any string parsed by more than one formatter
   * should be parsed the same way by all matching formatters.
   */
  private static final ImmutableList<DateTimeFormatter> allFormattersForParsing = ImmutableList.of(
    ISO_WITHOUT_ZONE_FORMATTER, DOY_FORMATTER, DOY_WITHOUT_ZONE_FORMATTER, JPL_TIMESTAMP_FORMATTER);

  /**
   * Utility method for parsing ISO-8601-style timestamps, DOY-string-style timestamps, or JPL-style timestamps.
   *
   * <p>This will throw a {@link DateTimeParseException} if the input string is not conformant to
   * either style.
   *
   * @param str a string in {@link DateTimeFormatter#ISO_INSTANT} format OR a DOY-style timestamp
   *     ({@code YYYY-DOYThh:mm:ss[.ssssss]}) OR a JPL-style timestamp ({@code uuuu LLL d HH:mm:ss[.ssssss]})
   * @return the parsed instant
   */
  public static Instant parseLeniently(String str) {
    for (var formatter : allFormattersForParsing) {
      try {
        return parseWithFormatter(formatter, str);
      } catch (DateTimeParseException e) {
        // continue to next parser
      }
    }
    try {
      return Instant.parse(str);
    } catch (DateTimeParseException e) {
      throw e;
    }
  }

  /**
   * Utility method for parsing DOY-string-style timestamps
   *
   * <p>The expected format is: {@code YYYY-DOYThh:mm:ss[.ssssss]Z}, where {@code DOY} is the day of
   * year (rather than month and day) and Z is the timezone name ("Z" for "Zulu", ie GMT / UTC).
   *
   * <p>This will throw a {@link DateTimeParseException} if the input string is not conformant.
   *
   * @param str an input timestamp string in the DOY format
   * @return the parsed instant
   */
  public static Instant parseFromDOYString(String str) {
    return parseWithFormatter(DOY_FORMATTER, str);
  }

  private static Instant parseWithFormatter(DateTimeFormatter formatter, String str) {
    return Instant.from(formatter.parse(str));
  }

  /**
   * Utility method for parsing DOY-string-style timestamps with an implicit +00:00 timezone
   *
   * <p>The expected format is: {@code YYYY-DOYThh:mm:ss[.ssssss]}, where {@code DOY} is the day of
   * year (rather than month and day).
   *
   * <p>This will throw a {@link DateTimeParseException} if the input string is not conformant.
   *
   * @param str an input timestamp string in the DOY format
   * @return the parsed instant
   */
  public static Instant parseFromDOYStringWithoutZone(String str) {
    return Instant.from(DOY_WITHOUT_ZONE_FORMATTER.parse(str));
  }

  /**
   * Utility method for outputting an {@code Instant} as a ISO 8601-style string
   *
   * <p>The output format is: {@code YYYY-MM-ddThh:mm:ss}. where Z is the timezone
   * name ("Z" for "Zulu", ie GMT / UTC).
   *
   * @param instant the input instant
   * @return the instant represented as a ISO 8601 string
   */
  public static String formatToISOString(Instant instant) {
    return ISO_FORMATTER.format(instant);
  }

  /**
   * Utility method for outputting an {@code Instant} as a ISO 8601-style string with an implicit +00:00 timezone
   *
   * <p>The output format is: {@code YYYY-MM-ddThh:mm:ss}.
   *
   * @param instant the input instant
   * @return the instant represented as a ISO 8601 string
   */
  public static String formatToISOStringWithoutZone(Instant instant) {
    return ISO_WITHOUT_ZONE_FORMATTER.format(instant);
  }

  /**
   * Utility method for outputting an {@code Instant} as a DOY-style string
   *
   * <p>The output format is: {@code YYYY-DOYThh:mm:ss[.ssssss]Z}, where {@code DOY} is the day of
   * year (rather than month and day) and Z is the timezone name ("Z" for "Zulu", ie GMT / UTC).
   *
   * @param instant the input instant
   * @return the instant represented as a DOY string
   */
  public static String formatToDOYString(Instant instant) {
    return DOY_FORMATTER.format(instant);
  }

  /**
   * Utility method for outputting an {@code Instant} as a DOY-style string with an implicit +00:00 timezone
   *
   * <p>The output format is: {@code YYYY-DOYThh:mm:ss[.ssssss]}, where {@code DOY} is the day of
   * year (rather than month and day).
   *
   * @param instant the input instant
   * @return the instant represented as a DOY string
   */
  public static String formatToDOYStringWithoutZone(Instant instant) {
    return DOY_WITHOUT_ZONE_FORMATTER.format(instant);
  }

  /**
   * Utility method for outputting an {@code Instant} as a DOY-style string with an implicit +00:00 timezone
   *
   * <p>The output format is: {@code YYYY-DOYThh:mm:ss.ssssss}, where {@code DOY} is the day of
   * year (rather than month and day).
   *
   * @param instant the input instant
   * @return the instant represented as a DOY string
   */
  public static String formatToStrictDOYStringWithoutZone(Instant instant) {
    return STRICT_DOY_WITHOUT_ZONE_FORMATTER.format(instant);
  }
}
