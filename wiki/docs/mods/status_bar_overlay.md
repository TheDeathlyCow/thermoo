---
title: 💖 Status Bar Overlay API
---

While Thermoo is primarily a server-side-only API, the Status Bar Overlay API is a purely client-side API provided by Thermoo. It is intended to help unify the heart overlay used to display temperature in both [Frostiful](https://github.com/TheDeathlyCow/frostiful) and [Scorchful](https://github.com/TheDeathlyCow/scorchful).

# Events

* `StatusBarOverlayRenderEvents.AFTER_HEALTH_BAR`: Renders after the player health bar. Provides information necessary for drawing textures perfectly on top of each heart of the health bar.
* `StatusBarOverlayRenderEvents.AFTER_MOUNT_HEALTH_BAR`: Similar to `AFTER_HEALTH_BAR`, but for the mount or vehicle health bar. 