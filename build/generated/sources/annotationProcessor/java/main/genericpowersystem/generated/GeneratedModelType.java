package genericpowersystem.generated;

import genericpowersystem.Configuration;
import genericpowersystem.Mission;
import gov.nasa.jpl.aerie.merlin.framework.ActivityMapper;
import gov.nasa.jpl.aerie.merlin.framework.InitializationContext;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.protocol.driver.Initializer;
import gov.nasa.jpl.aerie.merlin.protocol.model.ModelType;
import java.time.Instant;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("gov.nasa.jpl.aerie.merlin.processor.MissionModelProcessor")
public final class GeneratedModelType implements ModelType<Configuration, Mission> {
  @Override
  public Map<String, ActivityMapper<Mission, ?, ?>> getDirectiveTypes() {
    return ActivityTypes.directiveTypes;
  }

  @Override
  public ConfigurationMapper getConfigurationType() {
    return new ConfigurationMapper();
  }

  @Override
  public Mission instantiate(final Instant planStart, final Configuration configuration,
      final Initializer builder) {
    ActivityTypes.registerTopics(builder);

    final var registrar = new Registrar(builder);

    return InitializationContext.initializing(builder, () -> new Mission(registrar, configuration));
  }
}
