@MissionModel(model = Mission.class)
@WithMappers(BasicValueMappers.class)
@WithConfiguration(Configuration.class)

@WithActivityType(SolarPowerGeneration.class)
@WithActivityType(UpdatePowerLoad.class)
@WithActivityType(TurnOnCamera.class)
@WithActivityType(TurnOnTelecomm.class)
@WithActivityType(ChangeGNCState.class)
package genericpowersystem;

import genericpowersystem.activities.power.SolarPowerGeneration;
import genericpowersystem.activities.power.TurnOnTelecomm;
import genericpowersystem.activities.power.TurnOnCamera;
import genericpowersystem.activities.power.ChangeGNCState;
import genericpowersystem.activities.power.UpdatePowerLoad;
import gov.nasa.jpl.aerie.contrib.serialization.rulesets.BasicValueMappers;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithConfiguration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithMappers;
