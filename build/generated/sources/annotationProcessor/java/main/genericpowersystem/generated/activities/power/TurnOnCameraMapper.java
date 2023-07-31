package genericpowersystem.generated.activities.power;

import genericpowersystem.Mission;
import genericpowersystem.activities.power.TurnOnCamera;
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
public final class TurnOnCameraMapper implements ActivityMapper<Mission, TurnOnCamera, Unit> {
  private final Topic<TurnOnCamera> inputTopic = new Topic<>();

  private final Topic<Unit> outputTopic = new Topic<>();

  @Override
  public InputType<TurnOnCamera> getInputType() {
    return new InputMapper();
  }

  @Override
  public OutputType<Unit> getOutputType() {
    return new OutputMapper();
  }

  @Override
  public Topic<TurnOnCamera> getInputTopic() {
    return this.inputTopic;
  }

  @Override
  public Topic<Unit> getOutputTopic() {
    return this.outputTopic;
  }

  @Override
  public TaskFactory<Unit> getTaskFactory(final Mission model, final TurnOnCamera activity) {
    return ModelActions.threaded(() -> {
      ModelActions.emit(activity, this.inputTopic);
      activity.run(model);
      ModelActions.emit(Unit.UNIT, this.outputTopic);
      return Unit.UNIT;
    });
  }

  @Generated("gov.nasa.jpl.aerie.merlin.processor.MissionModelProcessor")
  public final class InputMapper implements InputType<TurnOnCamera> {
    private final ValueMapper<Long> mapper_duration;

    @SuppressWarnings("unchecked")
    public InputMapper() {
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
      parameters.add(new InputType.Parameter("duration", this.mapper_duration.getValueSchema()));
      return parameters;
    }

    @Override
    public Map<String, SerializedValue> getArguments(final TurnOnCamera input) {
      final var arguments = new HashMap<String, SerializedValue>();
      arguments.put("duration", this.mapper_duration.serializeValue(input.duration));
      return arguments;
    }

    @Override
    public TurnOnCamera instantiate(final Map<String, SerializedValue> arguments) throws
        InstantiationException {
      final var template = new TurnOnCamera();
      Optional<Long> duration = Optional.ofNullable(template.duration);

      final var instantiationExBuilder = new InstantiationException.Builder("TurnOnCamera");

      for (final var entry : arguments.entrySet()) {
        try {
          switch (entry.getKey()) {
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

      duration.ifPresentOrElse(
          value -> instantiationExBuilder.withValidArgument("duration", this.mapper_duration.serializeValue(value)),
          () -> instantiationExBuilder.withMissingArgument("duration", this.mapper_duration.getValueSchema()));

      instantiationExBuilder.throwIfAny();
      return template;
    }

    @Override
    public List<InputType.ValidationNotice> getValidationFailures(final TurnOnCamera input) {
      final var notices = new ArrayList<InputType.ValidationNotice>();
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
