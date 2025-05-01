# Thermoo for 1.21.1

[![](https://jitpack.io/v/TheDeathlyCow/thermoo.svg)](https://jitpack.io/#TheDeathlyCow/thermoo)

## Adding Thermoo to Your Mod

Add the repository to your `build.gradle`

```gradle
repositories {
    maven { url "https://jitpack.io/" }
    // Needed as Thermoo uses Cardinal Components 
    maven {
		name = "Ladysnake Mods"
		url = 'https://maven.ladysnake.org/releases'
    }
}
```

Add the dependency to your `build.gradle`

```gradle
dependencies {
    modImplementation "com.github.thedeathlycow:thermoo:VERSION"
}
```

Replace 'VERSION' with the version you want to use. See the available versions
on [Jitpack](https://jitpack.io/#TheDeathlyCow/thermoo)

## Using Thermoo

Usage of Thermoo, for both mods and datapacks, is documented on
the [wiki](https://github.com/TheDeathlyCow/thermoo/wiki/)

## LTS Policy

## LTS Policy

This version of Thermoo will only be supported according to the LTS Policy, which can be found [here](https://thermoo.thedeathlycow.com/#lts-policy).

## Build and Test Instructions

Thermoo is built using [Gradle](https://gradle.org/) with the [Fabric Loom Gradle plugin](https://github.com/FabricMC/fabric-loom).

To make a production build of Thermoo, run the following command:
```bash
gradlew build
# On windows add a ./
./gradlew build
```
The built jars will appear in `/build/libs/`, and will include a regular jar, a `-sources` jar, and a `-javadoc` jar. To
play Thermoo, install [Fabric API](https://github.com/FabricMC/fabric) and[Cardinal Components API](https://github.com/Ladysnake/Cardinal-Components-API)
and install the regular into your Minecraft `/mods` folder.

To run Thermoo's unit tests, run the following command:
```bash
gradlew check
```

Thermoo also includes some game tests, to automatically run them use the following command:
```bash
gradlew runGametest
```

Finally, you can manually test Thermoo in a Minecraft environment using the test mod:
```bash
gradlew runTestmodClient
```
This will launch a Minecraft instance with a test mod that includes implements Thermoo's API features for testing. 

If you instead want a clean test Minecraft instance with no gameplay modifications, run with the following:
```bash
gradlew runClient
```