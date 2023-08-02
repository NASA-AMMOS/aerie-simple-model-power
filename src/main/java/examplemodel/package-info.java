@MissionModel(model = Mission.class)
@WithMappers(BasicValueMappers.class)
@WithConfiguration(Configuration.class)

@WithActivityType(SolarPowerGeneration.class)
@WithActivityType(TurnOnCamera.class)
@WithActivityType(TurnOnTelecomm.class)
@WithActivityType(GNCStateTurning.class)
@WithActivityType(GNCStateNominal.class)
package examplemodel;

import examplemodel.activities.GNCStateTurning;
import examplemodel.activities.GNCStateNominal;
import examplemodel.activities.power.SolarPowerGeneration;
import examplemodel.activities.TurnOnTelecomm;
import examplemodel.activities.TurnOnCamera;
import gov.nasa.jpl.aerie.contrib.serialization.rulesets.BasicValueMappers;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithConfiguration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithMappers;
