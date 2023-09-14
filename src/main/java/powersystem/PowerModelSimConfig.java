package powersystem;

import gov.nasa.jpl.aerie.merlin.framework.annotations.AutoValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Template;

@AutoValueMapper.Record
public record PowerModelSimConfig(Double arrayCellArea) {

    public static final Double DEFAULT_CELL_AREA = 5.0; //m^2

    public static @Template PowerModelSimConfig defaultConfiguration() {
        return new PowerModelSimConfig(DEFAULT_CELL_AREA);
    }

}

