package genericpowersystem.generated.activities.power;

import genericpowersystem.Mission;
import genericpowersystem.activities.power.UpdatePowerLoad;
import gov.nasa.jpl.aerie.contrib.serialization.rulesets.BasicValueMappers;
import gov.nasa.jpl.aerie.merlin.framework.ActivityMapper;
import gov.nasa.jpl.aerie.merlin.framework.ModelActions;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.driver.Topic;
import gov.nasa.jpl.aerie.merlin.protocol.model.InputType;
import gov.nasa.jpl.aerie.merlin.protocol.model.OutputType;
import gov.nasa.jpl.aerie.merlin.protocol.model.TaskFactory;
import gov.nasa.jpl.aerie.merlin.protocol.types.InstantiationException;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.UnconstructableArgumentException;
import gov.nasa.jpl.aerie.merlin.protocol.types.Unit;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.processing.Generated;

@Generated("gov.nasa.jpl.aerie.merlin.processor.MissionModelProcessor")
public final class UpdatePowerLoadMapper implements ActivityMapper<Mission, UpdatePowerLoad, Unit> {
  private final Topic<UpdatePowerLoad> inputTopic = new Topic<>();

  private final Topic<Unit> outputTopic = new Topic<>();

  @Override
  public InputType<UpdatePowerLoad> getInputType() {
    return new InputMapper();
  }

  @Override
  public OutputType<Unit> getOutputType() {
    return new OutputMapper();
  }

  @Override
  public Topic<UpdatePowerLoad> getInputTopic() {
    return this.inputTopic;
  }

  @Override
  public Topic<Unit> getOutputTopic() {
    return this.outputTopic;
  }

  @Override
  public TaskFactory<Unit> getTaskFactory(final Mission model, final UpdatePowerLoad activity) {
    return ModelActions.threaded(() -> {
      ModelActions.emit(activity, this.inputTopic);
      activity.run(model);
      ModelActions.emit(Unit.UNIT, this.outputTopic);
      return Unit.UNIT;
    });
  }

  @Generated("gov.nasa.jpl.aerie.merlin.processor.MissionModelProcessor")
  public final class InputMapper implements InputType<UpdatePowerLoad> {
    private final ValueMapper<Double> mapper_powerReq;

    private final ValueMapper<Long> mapper_duration;

    @SuppressWarnings("unchecked")
    public InputMapper() {
      this.mapper_powerReq =
          BasicValueMappers.$double();
      this.mapper_duration =
          BasicValueMappers.$long();
    }

    @Override
    public List<String> getRequiredParameters() {
      return List.of();
    }

    @Override
    public ArrayList<InputType.Parameter> getParameters() {
      final var parameters = new ArrayList<InputType.Parameter>();
      parameters.add(new InputType.Parameter("powerReq", this.mapper_powerReq.getValueSchema()));
      parameters.add(new InputType.Parameter("duration", this.mapper_duration.getValueSchema()));
      return parameters;
    }

    @Override
    public Map<String, SerializedValue> getArguments(final UpdatePowerLoad input) {
      final var arguments = new HashMap<String, SerializedValue>();
      arguments.put("powerReq", this.mapper_powerReq.serializeValue(input.powerReq));
      arguments.put("duration", this.mapper_duration.serializeValue(input.duration));
      return arguments;
    }

    @Override
    public UpdatePowerLoad instantiate(final Map<String, SerializedValue> arguments) throws
        InstantiationException {
      final var template = new UpdatePowerLoad();
      Optional<Double> powerReq = Optional.ofNullable(template.powerReq);
      Optional<Long> duration = Optional.ofNullable(template.duration);

      final var instantiationExBuilder = new InstantiationException.Builder("UpdatePowerLoad");

      for (final var entry : arguments.entrySet()) {
        try {
          switch (entry.getKey()) {
            case "powerReq":
              powerReq = Optional.ofNullable(template.powerReq = this.mapper_powerReq.deserializeValue(entry.getValue())
                  .getSuccessOrThrow(failure -> new UnconstructableArgumentException("powerReq", failure)));
              break;
            case "duration":
              duration = Optional.ofNullable(template.duration = this.mapper_duration.deserializeValue(entry.getValue())
                  .getSuccessOrThrow(failure -> new UnconstructableArgumentException("duration", failure)));
              break;
            default:
              instantiationExBuilder.withExtraneousArgument(entry.getKey());
          }
        } catch (final UnconstructableArgumentException e) {
          instantiationExBuilder.withUnconstructableArgument(e.parameterName, e.failure);
        }
      }

      powerReq.ifPresentOrElse(
          value -> instantiationExBuilder.withValidArgument("powerReq", this.mapper_powerReq.serializeValue(value)),
          () -> instantiationExBuilder.withMissingArgument("powerReq", this.mapper_powerReq.getValueSchema()));
      duration.ifPresentOrElse(
          value -> instantiationExBuilder.withValidArgument("duration", this.mapper_duration.serializeValue(value)),
          () -> instantiationExBuilder.withMissingArgument("duration", this.mapper_duration.getValueSchema()));

      instantiationExBuilder.throwIfAny();
      return template;
    }

    @Override
    public List<InputType.ValidationNotice> getValidationFailures(final UpdatePowerLoad input) {
      final var notices = new ArrayList<InputType.ValidationNotice>();
      if (!input.checkPowerReq()) notices.add(new InputType.ValidationNotice(List.of(), "powerReq has to be positive"));
      return notices;
    }
  }

  public static final class OutputMapper implements OutputType<Unit> {
    private final ValueMapper<Unit> computedAttributesValueMapper = BasicValueMappers.$unit();

    @Override
    public ValueSchema getSchema() {
      return this.computedAttributesValueMapper.getValueSchema();
    }

    @Override
    public SerializedValue serialize(final Unit returnValue) {
      return this.computedAttributesValueMapper.serializeValue(returnValue);
    }
  }
}
