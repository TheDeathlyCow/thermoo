# Thermoo

[![](https://jitpack.io/v/TheDeathlyCow/thermoo.svg)](https://jitpack.io/#TheDeathlyCow/thermoo)

Thermoo is a temperature and environment library mod for Minecraft, targeting the Fabric and Quilt ecosystems. It is meant to help provide compatibility between mods and datapacks that use temperature as a core mechanic, such as Frostiful or Scorchful. Using this mod on its own will have no gameplay or visual effects. It is designed to be used by Mods written in both Java and Kotlin, as well as Datapacks through Commands and other registries.

## License

Thermoo is free software licensed under [LGPLv3](./LICENSE). You may freely link Thermoo into your own mods, however redistribution of this library requires that you follow the terms of the LGPLv3 license.

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

Replace 'VERSION' with the version you want to use. See the available versions on [Jitpack](https://jitpack.io/#TheDeathlyCow/thermoo)

## Thermoo Developer Wiki

Usage of Thermoo, for both mods and datapacks, is documented on the [developer wiki](https://thermoo.thedeathlycow.com/).

The Wiki source can be found at https://github.com/TheDeathlyCow/thermoo-docs/, contributions are welcome!

## LTS Policy

The Thermoo LTS policy can be found on the wiki: https://thermoo.thedeathlycow.com/#lts-policy

## Repository Structure

* [changelogs](./changelogs) - A record of all production changelogs
* [docs](./docs) - Public assets used for mod pages/wikis
* [modpages](./modpages) - Markdown files for the CurseForge and Modrinth mod pages
* [src](./src) - The source code of Thermoo
* [src/main](./src/main) - The Java and Kotlin APIs for Thermoo
* [src/test](./src/test) - The unit tests for Thermoo
* [src/gametest](./src/gametest) - The game tests for Thermoo
* [wiki](./wiki) - The MkDocs wiki source for the Developer Wiki

## Build and Test Instructions

Thermoo is built using [Gradle](https://gradle.org/) with the [Fabric Loom Gradle plugin](https://github.com/FabricMC/fabric-loom). Information about how to create tests for Thermoo can be found in the [Test Policy](./TESTING.md).

To make a production build of Thermoo, run the following command:
```bash
gradlew build
# On windows add a ./
./gradlew build
```
The built jars will appear in `/build/libs/`, and will include a regular jar, a `-sources` jar, and a `-javadoc` jar. To play Thermoo, install [Fabric API](https://github.com/FabricMC/fabric) and[Cardinal Components API](https://github.com/Ladysnake/Cardinal-Components-API) and install the regular into your Minecraft `/mods` folder.

To run Thermoo's unit tests, run the following command:
```bash
gradlew check
```

Thermoo also includes some game tests, to automatically run them use the following command:
```bash
gradlew runGameTest
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
