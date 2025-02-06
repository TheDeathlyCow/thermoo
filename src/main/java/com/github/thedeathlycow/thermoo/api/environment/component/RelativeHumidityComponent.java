package com.github.thedeathlycow.thermoo.api.environment.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;

import java.util.Collection;

public final class RelativeHumidityComponent implements MergeableComponent<RelativeHumidityComponent> {
    public static final Codec<RelativeHumidityComponent> CODEC = Codec.doubleRange(0, 1)
            .xmap(RelativeHumidityComponent::new, RelativeHumidityComponent::value);
    public static final double COMFORTABLE_HUMIDITY = 0.5;
    public static final RelativeHumidityComponent DEFAULT = new RelativeHumidityComponent(COMFORTABLE_HUMIDITY);

    private final double value;

    public RelativeHumidityComponent(double value) {
        this.value = value;
    }

    public double value() {
        return this.value;
    }

    @Override
    public RelativeHumidityComponent mergeWith(Collection<ComponentMap> modifiers) {
        double total = 0;
        int count = 0;
        for (ComponentMap modifer : modifiers) {
            RelativeHumidityComponent component = modifer.get(EnvironmentComponentTypes.RELATIVE_HUMIDITY);
            if (component != null) {
                total += component.value();
                count++;
            }
        }

        return count > 0
                ? new RelativeHumidityComponent(total / count)
                : DEFAULT;
    }
}