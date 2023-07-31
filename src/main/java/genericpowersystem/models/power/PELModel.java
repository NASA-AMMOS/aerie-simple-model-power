package genericpowersystem.models.power;

import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;

public class PELModel {
	public SettableState<GNC_State> gncState;
	public SettableState<Telecomm_State> telecommState;
	public SettableState<Avionics_State> avionicsState;
	public SettableState<Camera_State> cameraState;
    public PELModel() {
		this.gncState = SettableState.builder(GNC_State.class).initialValue(GNC_State.NOMINAL).build();
		this.telecommState = SettableState.builder(Telecomm_State.class).initialValue(Telecomm_State.OFF).build();
		this.avionicsState = SettableState.builder(Avionics_State.class).initialValue(Avionics_State.ON).build();
		this.cameraState = SettableState.builder(Camera_State.class).initialValue(Camera_State.OFF).build();
	}
    public void registerStates(Registrar registrar) {
		registrar.discrete("gncState",gncState, new EnumValueMapper<>(GNC_State.class));
		registrar.discrete("telecommState",telecommState, new EnumValueMapper<>(Telecomm_State.class));
		registrar.discrete("avionicsState",avionicsState, new EnumValueMapper<>(Avionics_State.class));
		registrar.discrete("cameraState",cameraState, new EnumValueMapper<>(Camera_State.class));
	}
}