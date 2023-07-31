package genericpowersystem.generated;

import genericpowersystem.Mission;
import genericpowersystem.generated.activities.power.ChangeGNCStateMapper;
import genericpowersystem.generated.activities.power.SolarPowerGenerationMapper;
import genericpowersystem.generated.activities.power.TurnOnCameraMapper;
import genericpowersystem.generated.activities.power.TurnOnTelecommMapper;
import genericpowersystem.generated.activities.power.UpdatePowerLoadMapper;
import gov.nasa.jpl.aerie.merlin.framework.ActivityMapper;
import gov.nasa.jpl.aerie.merlin.protocol.driver.Initializer;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("gov.nasa.jpl.aerie.merlin.processor.MissionModelProcessor")
public final class ActivityTypes {
  public static final SolarPowerGenerationMapper genericpowersystem_generated_activities_power_SolarPowerGenerationMapper = new SolarPowerGenerationMapper();

  public static final UpdatePowerLoadMapper genericpowersystem_generated_activities_power_UpdatePowerLoadMapper = new UpdatePowerLoadMapper();

  public static final TurnOnCameraMapper genericpowersystem_generated_activities_power_TurnOnCameraMapper = new TurnOnCameraMapper();

  public static final TurnOnTelecommMapper genericpowersystem_generated_activities_power_TurnOnTelecommMapper = new TurnOnTelecommMapper();

  public static final ChangeGNCStateMapper genericpowersystem_generated_activities_power_ChangeGNCStateMapper = new ChangeGNCStateMapper();

  public static final Map<String, ActivityMapper<Mission, ?, ?>> directiveTypes = Map.ofEntries(
      Map.entry("SolarPowerGeneration", genericpowersystem_generated_activities_power_SolarPowerGenerationMapper),
      Map.entry("UpdatePowerLoad", genericpowersystem_generated_activities_power_UpdatePowerLoadMapper),
      Map.entry("TurnOnCamera", genericpowersystem_generated_activities_power_TurnOnCameraMapper),
      Map.entry("TurnOnTelecomm", genericpowersystem_generated_activities_power_TurnOnTelecommMapper),
      Map.entry("ChangeGNCState", genericpowersystem_generated_activities_power_ChangeGNCStateMapper));

  public static void registerTopics(final Initializer initializer) {
    directiveTypes.forEach((name, mapper) -> registerDirectiveType(initializer, name, mapper));
  }

  private static <Input, Output> void registerDirectiveType(final Initializer initializer,
      final String name, final ActivityMapper<Mission, Input, Output> mapper) {
    initializer.topic("ActivityType.Input." + name, mapper.getInputTopic(), mapper.getInputAsOutput());
    initializer.topic("ActivityType.Output." + name, mapper.getOutputTopic(), mapper.getOutputType());
  }
}
