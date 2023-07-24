package genericpowersystem.generated;

import genericpowersystem.Configuration;
import gov.nasa.jpl.aerie.merlin.protocol.model.InputType;
import gov.nasa.jpl.aerie.merlin.protocol.types.InstantiationException;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("gov.nasa.jpl.aerie.merlin.processor.MissionModelProcessor")
public final class ConfigurationMapper implements InputType<Configuration> {
  @SuppressWarnings("unchecked")
  public ConfigurationMapper() {
  }

  @Override
  public List<String> getRequiredParameters() {
    return List.of();
  }

  @Override
  public ArrayList<InputType.Parameter> getParameters() {
    final var parameters = new ArrayList<InputType.Parameter>();
    return parameters;
  }

  @Override
  public Map<String, SerializedValue> getArguments(final Configuration input) {
    final var arguments = new HashMap<String, SerializedValue>();
    return arguments;
  }

  @Override
  public Configuration instantiate(final Map<String, SerializedValue> arguments) throws
      InstantiationException {
    final var template = Configuration.defaultConfiguration();

    final var instantiationExBuilder = new InstantiationException.Builder("Configuration");

    for (final var entry : arguments.entrySet()) {
      switch (entry.getKey()) {
        default:
          instantiationExBuilder.withExtraneousArgument(entry.getKey());
      }
    }


    instantiationExBuilder.throwIfAny();
    return new Configuration();
  }

  @Override
  public List<InputType.ValidationNotice> getValidationFailures(final Configuration input) {
    final var notices = new ArrayList<InputType.ValidationNotice>();
    return notices;
  }
}
