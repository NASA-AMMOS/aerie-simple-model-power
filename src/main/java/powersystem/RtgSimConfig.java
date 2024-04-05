package powersystem;

import gov.nasa.jpl.aerie.merlin.framework.annotations.AutoValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export;

import java.time.Instant;

@AutoValueMapper.Record
public record RtgSimConfig(Integer numRTGs,
                           Double bolPowerPerRTG,
                           Double decayRate,
                           Instant decayStart) {

    // Number of RTGs on the spacecraft
    public static final Integer DEFAULT_NUM_RTGS = 1;

    // Beginning of Life (BOL) Power per RTG (W)
    public static final Double DEFAULT_POWER_PER_RTG = 110.0;

    // Power decay rate in percentage per year
    public static final Double DEFAULT_DECAY_RATE = 1.2;

    // Start time of the power decay
    public static final Instant DEFAULT_DECAY_START = Instant.now();

    public static @Export.Template RtgSimConfig defaultConfiguration() {
        return new RtgSimConfig(
                DEFAULT_NUM_RTGS,
                DEFAULT_POWER_PER_RTG,
                DEFAULT_DECAY_RATE,
                DEFAULT_DECAY_START);
    }
}
