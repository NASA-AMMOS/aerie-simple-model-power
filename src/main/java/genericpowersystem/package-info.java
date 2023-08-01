@MissionModel(model = Mission.class)
@WithMappers(BasicValueMappers.class)
@WithConfiguration(Configuration.class)

@WithActivityType(SolarPowerGeneration.class)
@WithActivityType(TurnOnCamera.class)
@WithActivityType(TurnOnTelecomm.class)
@WithActivityType(GNCStateTurning.class)
@WithActivityType(GNCStateNominal.class)
package genericpowersystem;

import genericpowersystem.activities.GNCStateTurning;
import genericpowersystem.activities.GNCStateNominal;
import genericpowersystem.activities.power.SolarPowerGeneration;
import genericpowersystem.activities.TurnOnTelecomm;
import genericpowersystem.activities.TurnOnCamera;
import genericpowersystem.activities.GNCStateTurning;
import gov.nasa.jpl.aerie.contrib.serialization.rulesets.BasicValueMappers;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithConfiguration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithMappers;
