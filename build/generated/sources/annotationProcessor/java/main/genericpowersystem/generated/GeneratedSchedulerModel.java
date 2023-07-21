package genericpowersystem.generated;

import gov.nasa.jpl.aerie.merlin.protocol.model.SchedulerModel;
import gov.nasa.jpl.aerie.merlin.protocol.types.DurationType;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("gov.nasa.jpl.aerie.merlin.processor.MissionModelProcessor")
public final class GeneratedSchedulerModel implements SchedulerModel {
  @Override
  public Map<String, DurationType> getDurationTypes() {
    final var result = new HashMap<String, DurationType>();
    result.put("SolarPowerGeneration", DurationType.uncontrollable());
    result.put("UpdatePowerLoad", DurationType.uncontrollable());
    return result;
  }
}
