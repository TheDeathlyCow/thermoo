---
title: 📦 Setup
---
# Setup

Ensure you have the correct version of Thermoo installed for your Minecraft version. Usually, each major version of
Thermoo corresponds to a Minecraft version with breaking changes.

| Minecraft Version Range | Corresponding Thermoo versions |
|-------------------------|--------------------------------|
| 1.21.5                  | 6.x (planned)                  |
| 1.21.2-1.21.4           | 5.x                            |
| 1.21-1.21.1             | 4.x                            |
| 1.20.5-1.20.6           | Skipped                        |
| 1.20.2-1.20.4           | 3.x                            |
| 1.20-1.20.1             | 1.6-2.x                        |
| 1.19.4                  | 1.5.x                          |
| 1.19.2                  | 1.3.1-1.4                      |

[![](https://jitpack.io/v/TheDeathlyCow/thermoo.svg)](https://jitpack.io/#TheDeathlyCow/thermoo)

## Mods

Add the following to your gradle build script:

=== "`build.gradle`"
    ```groovy
    repositories {
        maven {
            url "https://jitpack.io/"
        }
    // Needed as Thermoo uses Cardinal Components
        maven {
            name = "Ladysnake Mods"
            url = "https://maven.ladysnake.org/releases"
        }
    }
    
    dependencies {
        // Your other dependencies
        // get version from Jitpack: https://jitpack.io/#TheDeathlyCow/thermoo
        modImplementation "com.github.thedeathlycow:thermoo:<VERSION>"
    }
    ```

=== "`build.gradle.kts`"
    ```kotlin
    repositories {
        maven {
            url = uri("https://jitpack.io/")
        }
        // Needed as Thermoo uses Cardinal Components
        maven {
            name = "Ladysnake Mods"
            url = "https://maven.ladysnake.org/releases"
        }
    }
    
    dependencies {
        // Your other dependencies
        // get version from Jitpack: https://jitpack.io/#TheDeathlyCow/thermoo
        modImplementation("com.github.thedeathlycow:thermoo:<VERSION>")
    }
    ```

## Datapacks

You must first install either the [Fabric](https://fabricmc.net/) or [Quilt](https://quiltmc.org/) mod loaders to use
Thermoo. Thermoo may run on the NeoForge platform when using Sinytra Connector, however this usage is not supported.
Support queries and issues from users not using Fabric or Quilt will be closed/ignored.

Ensure Thermoo is installed in your `mods` directory, along with its
dependencies [Fabric API](https://github.com/FabricMC/fabric) (
or [QSL](https://github.com/QuiltMC/quilt-standard-libraries/) if using Quilt)
and [Cardinal Components API](https://github.com/Ladysnake/Cardinal-Components-API).