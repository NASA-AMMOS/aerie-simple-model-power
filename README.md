# Aerie Simple Power Model

This repository provides a simple, configurable spacecraft power model for use within the [Aerie framework](https://github.com/NASA-AMMOS/aerie). The power model includes basic components to represent a power source (e.g. solar arrays), spacecraft power loads (represented as a power equipment list), and energy storage (e.g. battery). A detailed description of the equations and variables that drive the model behavior is available [here](docs/ModelBehaviorDescription.md).

## Quick Start

Interested in giving the power model a quick spin? We've pre-built a very simple example spacecraft model, demosystem, that uses the power model. This model has a few activities in it that turn on/off various spacecraft loads. To try out this model, simply load [demosystem.jar](demosystem.jar) into Aerie. If you have never used Aerie before and need some help getting it deployed and uploading a model, start [here](https://nasa-ammos.github.io/aerie-docs/introduction/#fast-track).

Once you have loaded [demosystem.jar](demosystem.jar) into Aerie, make a plan with a time range of your choosing (a plan length of a day should be reasonable). Once you have created a plan, add a couple of activities to it (e.g. TurnOnCamera) and hit the simulate button. Once the simulation completes you should see a green check appear next to the Simulation icon and timelines populate in the view below. Viola! You have successfully run the power model!

To see the results of the simulation you ran in a slightly more organized manner, you can load a pre-built [basic power model view](PowerModelBasicView.json) that will order a subset of the resources on to the timeline for (power load, battery state of charge, etc.). 

There are number of configuration variables available in the "Simulation" pane that you can adjust to produce different behavior with the power model. Feel free to play around with these configurations and re-simulate to see how the results change.

## Organization

The core power model is in the [power system package](src/main/java/powersystem/) in this repo. This is what the mission modeler would integrate into their own spacecraft model if they needed a power model. The [demosystem package](src/main/java/demosystem/) in this repo is an example to show how a mission modeler can integrate this power model into their model, specifically by changing their package.info file and their top-level mission class.

## Power Equipment List (PEL) 

Example activities within the [demosystem package](src/main/java/demosystem/) effect the power model by making state or load changes on hardware defined within the [PEL Model Package](src/main/java/demosystem/models/pel). A simple python script, [pel_java_generator.py](pel_java_generator.py), included in this repo can generate all of the files within that package for you based on the [pel.json](pel.json) file. If you want to add new/different loads, just updated the `power_loads` property in that file.

## Prerequisites

- Install [OpenJDK Temurin LTS](https://adoptium.net/temurin/releases/?version=19). If you're on macOS, you can install [brew](https://brew.sh/) instead and then use the following command to install JDK 19:

  ```sh
  brew tap homebrew/cask-versions
  brew install --cask temurin19
  ```

  Make sure you update your `JAVA_HOME` environment variable. For example with [Zsh](https://www.zsh.org/) you can update your `.zshrc` with:

  ```sh
  export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-19.jdk/Contents/Home"
  ```

- Ensure you have docker installed on your machine. If you do not, install it [here](https://docs.docker.com/desktop/).

- Navigate to a directory on your local machine where you want to keep this repo and clone the repo using the following command:

  ```sh
  git clone https://github.jpl.nasa.gov/yendamuri/power
  ```

- Set `GITHUB_USER` and `GITHUB_TOKEN` environment variables to your credentials inside this directory (first you need to create a [personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic) in your GitHub account) so you can download the Aerie Maven packages from the [GitHub Maven package registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry). For example with Zsh you can update your `.zshrc` to set the variables with:

  ```sh
  export GITHUB_USER="your github user"
  export GITHUB_TOKEN="your github token"
  ```

## Building

To build the mission model JAR you can type in this command in the directory of the repo:

```sh
./gradlew build --refresh-dependencies # Outputs 'build/libs/demosystem.jar'
```

You can deploy Aerie on your local machine by first opening Docker Desktop and then you can start the Aerie services using the following command in the directory of the repo:

```sh
docker compose up
```

You can then upload the JAR to Aerie using either the [UI](http://localhost/) or API.

## Testing

To run unit tests under [./src/test](./src/test) against your mission model you can do:

```sh
./gradlew test
```
