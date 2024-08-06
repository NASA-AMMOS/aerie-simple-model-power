package demosystem;

import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class CustomValueMappers {
  public static ValueMapper<Instant> instant() {
    return new ValueMapper<>() {
      @Override
      public ValueSchema getValueSchema() {
        return ValueSchema.STRING;
      }

      @Override
      public Result<Instant, String> deserializeValue(SerializedValue serializedValue) {
        try {
          return serializedValue
            .asString()
            .map(Instants::parseLeniently)
            .map(Result::<Instant, String>success)
            .orElse(Result.failure("Expected instant string, got " + serializedValue));
        } catch (final DateTimeParseException ex) {
          return Result.failure("Could not parse instant " + serializedValue);
        } catch (Exception e) {
          return Result.failure("exception during deserialization " + e);
        }
      }

      @Override
      public SerializedValue serializeValue(Instant value) {
        return SerializedValue.of(Instants.formatToDOYStringWithoutZone(value));
      }
    };
  }
}
