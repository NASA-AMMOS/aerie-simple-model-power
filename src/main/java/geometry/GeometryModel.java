package geometry;

import gov.nasa.jpl.aerie.contrib.streamline.core.Resource;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.discrete.Discrete;

import static gov.nasa.jpl.aerie.contrib.streamline.modeling.discrete.monads.DiscreteResourceMonad.map;
import static gov.nasa.jpl.aerie.contrib.streamline.modeling.polynomial.PolynomialResources.multiply;

public class GeometryModel {

  public String name; // Name of the model (creates prefixes on resource names)
  public SpacecraftSimConfig spacecraftSimConfig;
  public UniverseSimConfig universeSimConfig;

  private JNISpice jniSpice;


  public GeometryModel(String name, SpacecraftSimConfig spacecraftSimConfig, UniverseSimConfig universeSimConfig) {
    this.spacecraftSimConfig = spacecraftSimConfig;
    this.universeSimConfig = universeSimConfig;
    this.name = name;
  }

  public GeometryModel(String name) {
    this.name = name;
  }

  public void setupSpice(String kernelsPath) throws SpiceKernelLoadingError {
    jniSpice.furnsh(kernelsPath);

  }


}
