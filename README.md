# Thermoo

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
