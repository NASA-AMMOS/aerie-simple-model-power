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
