@MissionModel(model = Mission.class)
@WithMappers(BasicValueMappers.class)
@WithConfiguration(Configuration.class)

@WithActivityType(SolarArrayDeployment.class)
@WithActivityType(TurnOnCamera.class)
@WithActivityType(TurnOnTelecomm.class)
@WithActivityType(ChangeGNCState.class)
@WithActivityType(Drive.class)

package demosystem;

import demosystem.activities.ChangeGNCState;
import demosystem.activities.Drive;
import powersystem.activities.power.SolarArrayDeployment;
import demosystem.activities.TurnOnTelecomm;
import demosystem.activities.TurnOnCamera;
import gov.nasa.jpl.aerie.contrib.serialization.rulesets.BasicValueMappers;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithConfiguration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithMappers;
