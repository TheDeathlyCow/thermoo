package com.github.thedeathlycow.thermoo.api.util.component;

import com.github.thedeathlycow.thermoo.mixin.common.accessor.ComponentMapBuilderAccessor;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper for {@link ComponentMap.Builder} that can reduce existing components that implement the
 * {@link ReducibleComponent} when they are added to this builder, instead of replacing them.
 * <p>
 * <strong>Usage example</strong>
 * <p>
 * Say you have two {@link com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent}s
 * (which implements the reducible interface), and you want to have a base record be shifted by modifier records,
 * instead of being replaced. You can use this builder to do this like so:
 *
 * <pre>{@code
 * ReducibleComponentMapBuilder builder = ReducibleComponentMapBuilder.create();
 *
 * // First time adding this reducible component type. The temperature value is now 20 Celsius.
 * builder.add(EnvironmentComponentTypes.TEMPERATURE, new TemperatureRecordComponent(20, CELSIUS));
 *
 * // Adding another reducible component of the same type now applies the reduction function instead of replacing the existing value.
 * // The temperature component is now 30 Celsius.
 * builder.add(EnvironmentComponentTypes.TEMPERATURE, new TemperatureRecordComponent(10, CELSIUS));
 *
 * }</pre>
 */
public class ReducibleComponentMapBuilder {
    private final ComponentMap.Builder builder;

    private ReducibleComponentMapBuilder(ComponentMap.Builder builder) {
        this.builder = builder;
    }

    /**
     * Creates a new reducible builder with an empty builder
     *
     * @return Returns a new reducible builder
     */
    @Contract("->new")
    public static ReducibleComponentMapBuilder create() {
        return new ReducibleComponentMapBuilder(ComponentMap.builder());
    }

    /**
     * Creates a new reducible builder with an existing builder. This builder is not copied into the map, so you may
     * still use it if you want to continue to have replacing behaviour.
     *
     * @param builder The exist builder to wrap with this builder
     * @return Returns a new reducible builder
     */
    @Contract("_->new")
    public static ReducibleComponentMapBuilder create(ComponentMap.Builder builder) {
        return new ReducibleComponentMapBuilder(builder);
    }

    /**
     * Adds a value to this builder. If the type is already mapped to a value in this builder, and the value implements
     * the {@link ReducibleComponent} interface, then applies and inserts the result of {@link ReducibleComponent#reduceWith(ReducibleComponent)}
     * on the existing value with the new value. Otherwise, if the type is not mapped to a value, or if the existing
     * value is not reducible, then the new value is inserted as-is, replacing whatever else may be there.
     * <p>
     * If the new value is {@code null}, then any existing value for this type will be removed.
     *
     * @param type  The name/type of the component
     * @param value The value of the component to either reduce with the existing value, or to insert
     * @param <T>   The type of the value
     * @return Returns this builder
     */
    @Contract("_,_->this")
    public <T> ReducibleComponentMapBuilder add(ComponentType<T> type, @Nullable T value) {
        this.put(type, value);
        return this;
    }

    /**
     * Adds a set of values to this builder. If a type is already mapped to a value in this builder, and the new value
     * implements the {@link ReducibleComponent} interface, then applies and inserts the result of {@link ReducibleComponent#reduceWith(ReducibleComponent)}
     * on the existing value with the new value. Otherwise, if the type is not mapped to a value, or if the existing
     * value is not reducible, then the new value is inserted as-is, replacing whatever else may be there.
     *
     * @param componentSet The set of components to insert
     * @return Returns this builder
     */
    @Contract("_->this")
    public ReducibleComponentMapBuilder addAll(ComponentMap componentSet) {
        for (Component<?> component : componentSet) {
            this.put(component.type(), component.value());
        }

        return this;
    }

    private <T> void put(ComponentType<T> type, @Nullable Object value) {
        ComponentMapBuilderAccessor accesor = (ComponentMapBuilderAccessor) builder;
        if (value != null && accesor.thermoo$getComponents().get(type) instanceof ReducibleComponent<?> rBase) {
            value = forceReduce(rBase, (ReducibleComponent<?>) value);
        }
        accesor.thermoo$invokePut(type, value);
    }

    /**
     * Builds this map into a new component map
     *
     * @return Returns a new component map
     */
    public ComponentMap build() {
        return this.builder.build();
    }

    /**
     * Used to sneakily get around the generic type checking of the reduceWith method
     */
    @SuppressWarnings("unchecked")
    private static <T extends ReducibleComponent<T>> T forceReduce(ReducibleComponent<T> base, ReducibleComponent<?> other) {
        return base.reduceWith((T) other);
    }
}