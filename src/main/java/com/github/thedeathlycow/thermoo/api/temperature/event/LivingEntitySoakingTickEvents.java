package com.github.thedeathlycow.thermoo.api.temperature.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.LivingEntity;

public final class LivingEntitySoakingTickEvents {
    public static final Event<AllowSoakingUpdate> ALLOW_SOAKING_UPDATE = EventFactory.createArrayBacked(
            AllowSoakingUpdate.class,
            listeners -> context -> {
                for (AllowSoakingUpdate listener : listeners) {
                    TriState result = listener.allowUpdate(context);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    public static final Event<GetSoakingChange> GET_SOAKING_CHANGE = EventFactory.createArrayBacked(
            GetSoakingChange.class,
            listeners -> context -> {
                int total = 0;
                for (GetSoakingChange listener : listeners) {
                    total += listener.addSoaking(context);
                }
                return total;
            }
    );

    public static final Event<AllowSoakingChange> ALLOW_SOAKING_CHANGE = EventFactory.createArrayBacked(
            AllowSoakingChange.class,
            listeners -> (context, soakingChange) -> {
                for (AllowSoakingChange listener : listeners) {
                    TriState result = listener.allowChange(context, soakingChange);
                    if (result != TriState.DEFAULT) {
                        return result;
                    }
                }
                return TriState.DEFAULT;
            }
    );

    @FunctionalInterface
    public interface AllowSoakingUpdate {
        TriState allowUpdate(TickContext<LivingEntity> context);
    }

    @FunctionalInterface
    public interface GetSoakingChange {
        int addSoaking(TickContext<LivingEntity> context);
    }

    @FunctionalInterface
    public interface AllowSoakingChange {
        TriState allowChange(TickContext<LivingEntity> context, int soakingChange);
    }

    private LivingEntitySoakingTickEvents() {

    }
}