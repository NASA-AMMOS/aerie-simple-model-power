# Power Model Behavior Description

## Solar Array

The power delivered by the solar arrays to the spacecraft is estimated using the following relationship:

$P_s = S_{1AU} (1/R_{sc})^2 A_{mech} L_{PF} L_{cell} L_{pointing} L_{degradation} L_{conversion} L_{other}$

where \
$P_s$ is the source power delivered to the spacecraft $(W)$ \
$S_{1AU}$ is the solar irradiance from the Sun at 1 AU $(1360.8\ W/m^2)$ \
$R_{sc}$ is the spacecraft distance from the Sun $(AU)$ \
$A_{mech}$ is the mechanical area of the solar arrays $(m^2)$ \
$L_{PF}$ is loss due to array packing factor (ratio between active cell and mechanical area of arrays) \
$L_{cell}$ is the loss due to solar cell efficiency \
$L_{pointing}$ is the loss due to off-Sun solar array orientation based on spacecraft orientation \
$L_{degradation}$ is the loss due to array degradation over time \
$L_{conversion}$ is the loss due to power conversion efficiency (e.g. peak power tracking) \
$L_{other}$ is the loss due to other phenomenon/inefficiencies (e.g. shadowing, cell mismatch, cover glass)

Loss due to pointing, $L_{pointing}$, is simply a function of the angle between the solar array normal vector and the Sun:

$L_{pointing} = cos(\theta)$

#### Notes

- Degradation loss is currently not implemented and assumed to be 1.0
- While solar array operating temperature also plays a role in array power output, this model currently does take this effect into account.

## Battery

The battery model provided describes the behavior of a simple rechargeable ("secondary") battery that provides energy to the spacecraft when insufficient power is available from the power source (e.g. solar arrays).

### Assumptions

- The model currently assumes a fixed bus voltage, $V_{bus}$, which is provided as input to the model via simulation configuration
- Battery fade (degradation) is currently not modeled, and therefore a fixed capacity is assumed

### Behavior Description

The battery charge from time $t_{0}$ to $t_{f}$ can be described with the following equation:

$Q_{batt} = Q_{i} + \displaystyle \int_{t_{0}}^{t_{f}}I_{batt} $

where \
$Q_{batt}$ is the battery charge $(Ah)$ \
$Q_{i}$ is the initial battery charge at $t_{0}$ $(Ah)$ \
$I_{batt}$ is the battery current

The initial battery charge, $Q_{i}$, can be determined based on the initial battery state of charge (SOC) provided to the model via sim configuration:

$Q_{i} = SOC_{batt}C_{batt}$

where \
$SOC_{batt}$ is the battery state of charge as a fraction between 0 and 1 \
$C_{batt}$ is the battery capacity

The battery current, $I_{batt}$, is determined based on the difference between power production and power demand (net power):

$I*{batt} = \displaystyle \frac{P_s - P_d}{V_{bus}} $

where \
$P_s$ is the source power delivered to the spacecraft $(W)$ \
$P_d$ is the power demanded by the spacecraft components $(W)$ \
$V_{bus}$ is the spacecraft bus voltage $(V)$

When the battery is already full and the net power is positive, no more charge can enter into the battery and the battery current becomes 0. Similarly, when the battery is empty and the net power is negative, no more charge can exit the battery and the battery current becomes 0.
