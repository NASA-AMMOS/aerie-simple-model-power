package genericpowersystem.generated;

import genericpowersystem.Mission;
import genericpowersystem.activities.power.ChangeGNCState;
import genericpowersystem.activities.power.SolarPowerGeneration;
import genericpowersystem.activities.power.TurnOnCamera;
import genericpowersystem.activities.power.TurnOnTelecomm;
import genericpowersystem.activities.power.UpdatePowerLoad;
import gov.nasa.jpl.aerie.merlin.framework.ModelActions;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import javax.annotation.processing.Generated;

@Generated("gov.nasa.jpl.aerie.merlin.processor.MissionModelProcessor")
public class ActivityActions extends ModelActions {
  private ActivityActions() {
  }

  public static void spawn(final Mission model, final SolarPowerGeneration activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_SolarPowerGenerationMapper;
    ModelActions.spawn(mapper.getTaskFactory(model, activity));
  }

  public static void defer(final Duration duration, final Mission model,
      final SolarPowerGeneration activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_SolarPowerGenerationMapper;
    ModelActions.defer(duration, mapper.getTaskFactory(model, activity));
  }

  public static void defer(final long quantity, final Duration unit, final Mission model,
      final SolarPowerGeneration activity) {
    defer(unit.times(quantity), model, activity);
  }

  public static void call(final Mission model, final SolarPowerGeneration activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_SolarPowerGenerationMapper;
    ModelActions.call(mapper.getTaskFactory(model, activity));
  }

  public static void spawn(final Mission model, final UpdatePowerLoad activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_UpdatePowerLoadMapper;
    ModelActions.spawn(mapper.getTaskFactory(model, activity));
  }

  public static void defer(final Duration duration, final Mission model,
      final UpdatePowerLoad activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_UpdatePowerLoadMapper;
    ModelActions.defer(duration, mapper.getTaskFactory(model, activity));
  }

  public static void defer(final long quantity, final Duration unit, final Mission model,
      final UpdatePowerLoad activity) {
    defer(unit.times(quantity), model, activity);
  }

  public static void call(final Mission model, final UpdatePowerLoad activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_UpdatePowerLoadMapper;
    ModelActions.call(mapper.getTaskFactory(model, activity));
  }

  public static void spawn(final Mission model, final TurnOnCamera activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_TurnOnCameraMapper;
    ModelActions.spawn(mapper.getTaskFactory(model, activity));
  }

  public static void defer(final Duration duration, final Mission model,
      final TurnOnCamera activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_TurnOnCameraMapper;
    ModelActions.defer(duration, mapper.getTaskFactory(model, activity));
  }

  public static void defer(final long quantity, final Duration unit, final Mission model,
      final TurnOnCamera activity) {
    defer(unit.times(quantity), model, activity);
  }

  public static void call(final Mission model, final TurnOnCamera activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_TurnOnCameraMapper;
    ModelActions.call(mapper.getTaskFactory(model, activity));
  }

  public static void spawn(final Mission model, final TurnOnTelecomm activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_TurnOnTelecommMapper;
    ModelActions.spawn(mapper.getTaskFactory(model, activity));
  }

  public static void defer(final Duration duration, final Mission model,
      final TurnOnTelecomm activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_TurnOnTelecommMapper;
    ModelActions.defer(duration, mapper.getTaskFactory(model, activity));
  }

  public static void defer(final long quantity, final Duration unit, final Mission model,
      final TurnOnTelecomm activity) {
    defer(unit.times(quantity), model, activity);
  }

  public static void call(final Mission model, final TurnOnTelecomm activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_TurnOnTelecommMapper;
    ModelActions.call(mapper.getTaskFactory(model, activity));
  }

  public static void spawn(final Mission model, final ChangeGNCState activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_ChangeGNCStateMapper;
    ModelActions.spawn(mapper.getTaskFactory(model, activity));
  }

  public static void defer(final Duration duration, final Mission model,
      final ChangeGNCState activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_ChangeGNCStateMapper;
    ModelActions.defer(duration, mapper.getTaskFactory(model, activity));
  }

  public static void defer(final long quantity, final Duration unit, final Mission model,
      final ChangeGNCState activity) {
    defer(unit.times(quantity), model, activity);
  }

  public static void call(final Mission model, final ChangeGNCState activity) {
    final var mapper = ActivityTypes.genericpowersystem_generated_activities_power_ChangeGNCStateMapper;
    ModelActions.call(mapper.getTaskFactory(model, activity));
  }
}
