package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.commons.TriState;
import dev.yumi.commons.event.Event;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

/// Events related to [temperature statuses][TemperatureStatus].
public final class TemperatureStatusEvents {
    /// Allows listeners to override whether an enabled temperature status is applied this tick.
    ///
    /// - Return [TriState#FALSE] to suppress the status this tick.
    /// - Return [TriState#TRUE] to explicitly allow it.
    /// - Return [TriState#DEFAULT] to abstain; the status will be allowed unless another listener suppresses it.
    ///
    /// If any listener returns a non-[DEFAULT][TriState#DEFAULT] result, subsequent listeners are not invoked.
    ///
    /// This event only fires for statuses that are already [enabled][TemperatureStatusLookup#isEnabled]. It cannot be
    /// used to forcibly apply a disabled status. Use [TemperatureStatusLookup#setEnabled] for persistent control.
    public static final Event<Identifier, AllowTemperatureStatus> ALLOW_TEMPERATURE_STATUS = Thermoo.EVENT_MANAGER.create(
            AllowTemperatureStatus.class,
            listeners -> (entity, statusReference) -> {
                for (AllowTemperatureStatus listener : listeners) {
                    TriState result = listener.allow(entity, statusReference);

                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }

                return TriState.DEFAULT;
            }
    );

    @FunctionalInterface
    public interface AllowTemperatureStatus {
        TriState allow(LivingEntity entity, Holder.Reference<TemperatureStatus> statusReference);
    }

    private TemperatureStatusEvents() {

    }
}