# Generic/Multi-Mission Power Model

This repo provides a simple Aerie power mission model. Below are the steps on how to upload and use this model in Aerie.

# Organization

The core power model is in the power system package in this repo. This is what the mission modeler would integrate into their own spacecraft model if they needed a power model. The demosystem package in this repo is an example to show how a mission modeler can integrate this power model into their model, specifically by changing their package.info file and their top-level mission class.

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

- Set `GITHUB_USER` and `GITHUB_TOKEN` environment variables to your credentials (first you need to create a [personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic) in your GitHub account) so you can download the Aerie Maven packages from the [GitHub Maven package registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry). For example with Zsh you can update your `.zshrc` to set the variables with:

  ```sh
  export GITHUB_USER="your github user"
  export GITHUB_TOKEN="your github token"
  ```

## Building

To build the mission model JAR you can do:

```sh
./gradlew build --refresh-dependencies # Outputs 'build/libs/demosystem.jar'
```

You can deploy Aerie on your local machine by first opening Docker Desktop and then you can start the Aerie services using the following command:

```sh
docker compose up
```

You can then upload the JAR to Aerie using either the [UI](http://localhost/) or API. 

## Testing

To run unit tests under [./src/test](./src/test) against your mission model you can do:

```sh
./gradlew test
```
