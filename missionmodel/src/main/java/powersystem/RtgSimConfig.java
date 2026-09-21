package powersystem;

import gov.nasa.jpl.aerie.merlin.framework.annotations.AutoValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import java.time.Instant;

@AutoValueMapper.Record
public record RtgSimConfig(Integer numRTGs,
                           Double bolPowerPerRTG,
                           Double decayRate,
                           Instant decayStart,

                           Boolean sampleRtgPower,
                           Duration sampleRate) {

    // Number of RTGs on the spacecraft
    public static final Integer DEFAULT_NUM_RTGS = 1;

    // Beginning of Life (BOL) Power per RTG (W)
    public static final Double DEFAULT_POWER_PER_RTG = 110.0;

    // Power decay rate in percentage per year
    public static final Double DEFAULT_DECAY_RATE = 1.2;

    // Start time of the power decay
    public static final Instant DEFAULT_DECAY_START = Instant.now();

    public static final Boolean DEFAULT_SAMPLE_RTG_POWER = Boolean.TRUE;

    public static final Duration DEFAULT_SAMPLE_RATE = Duration.of(24, Duration.HOURS);

    public static @Export.Template RtgSimConfig defaultConfiguration() {
        return new RtgSimConfig(
                DEFAULT_NUM_RTGS,
                DEFAULT_POWER_PER_RTG,
                DEFAULT_DECAY_RATE,
                DEFAULT_DECAY_START,
                DEFAULT_SAMPLE_RTG_POWER,
                DEFAULT_SAMPLE_RATE);
    }
}
