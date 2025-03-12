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

Replace 'VERSION' with the version you want to use. See the available versions
on [Jitpack](https://jitpack.io/#TheDeathlyCow/thermoo)

## Using Thermoo

Usage of Thermoo, for both mods and datapacks, is documented on
the [wiki](https://github.com/TheDeathlyCow/thermoo/wiki/)

## LTS Policy

These are the current versions being supported by Thermoo.

| Minecraft Version | Support Status         |
|-------------------|------------------------|
| 1.21.3            | ✅ Supported            | 
| 1.21.1            | ✅ Supported            | 
| 1.20.6            | ❌ Unsupported          | 
| 1.20.4            | ❌ Unsupported          | 
| 1.20.2            | ❌ Unsupported          | 
| 1.20.1            | ⚠️ Critical fixes only |
| 1.19.4            | ❌ Unsupported          |
| 1.19.2            | ❌ Unsupported          | 

Status Definitions:

* ✅ Supported: This version is fully supported and will receive all new features, fixes, and updates (where possible)
* ⚠️ Critical fixes only: This version will receive only critical crash and security fixes, as well as minor features where they can be easily cherry-picked
* ❌ Unsupported: This version will receive no future updates, except for critical security fixes

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

## Wiki Build Instructions

The Thermoo Developer wiki is built with [MkDocs](https://www.mkdocs.org/) using the [Material Theme](https://squidfunk.github.io/mkdocs-material/). To install mkdocs, Python 3 is required: https://www.python.org/.

The following commands will install the requirements for mkdocs:
```bash
pip install mkdocs
pip install mkdocs-material
```

You can then run a local version of the Wiki:
```bash
cd wiki/
mkdocs serve
```

The wiki should then be available on http://localhost:8000/.

In order to deploy the wiki using GitHub pages, run:
```bash
mkdocs gh-deploy --force
```

This will deploy the site to https://your-username.github.io/thermoo, where it will be available on the public internet.
