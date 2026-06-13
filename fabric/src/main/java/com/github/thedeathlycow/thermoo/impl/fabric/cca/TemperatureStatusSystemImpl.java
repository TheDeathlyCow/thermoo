package com.github.thedeathlycow.thermoo.impl.fabric.cca;

import com.github.thedeathlycow.thermoo.impl.ecs.TemperatureStatusSystem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

public class TemperatureStatusSystemImpl implements CardinalComponent, ServerTickingComponent {
    private final LivingEntity provider;

    public TemperatureStatusSystemImpl(LivingEntity provider) {
        this.provider = provider;
    }

    public void serverTick() {
        TemperatureStatusSystem.doTick(this.provider);
    }

    @Override
    public void readData(ValueInput readView) {
        // nothing here
    }

    @Override
    public void writeData(ValueOutput writeView) {
        // nothing here
    }
}