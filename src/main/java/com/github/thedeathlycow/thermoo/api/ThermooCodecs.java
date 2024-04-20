package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.mixin.common.accesor.EntityAttributeModiferAccessor;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.ApiStatus;

/**
 * Helpful codecs used by Thermoo. May be changed between MC versions as equivalent vanilla codecs are added.
 * <p>
 * Exposed in API for the convenience of API users.
 */
@ApiStatus.Experimental
public class ThermooCodecs {


    public static final Codec<EntityAttributeModifier> ATTRIBUTE_MODIFIER_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Uuids.STRING_CODEC
                            .fieldOf("uuid")
                            .orElseGet(() -> MathHelper.randomUuid(Random.createLocal()))
                            .forGetter(EntityAttributeModifier::getId),
                    Codec.STRING
                            .fieldOf("name")
                            .forGetter(modifier -> ((EntityAttributeModiferAccessor) modifier).thermoo$name()),
                    Codec.DOUBLE
                            .fieldOf("value")
                            .forGetter(EntityAttributeModifier::getValue),
                    EntityAttributeModifier.Operation.CODEC
                            .fieldOf("operation")
                            .forGetter(EntityAttributeModifier::getOperation)
            ).apply(instance, EntityAttributeModifier::new)
    );

    /**
     * Creates a codec for an Enum. Either uses the enum ordinal or the name, but prefers the ordinal for more efficient
     * storage.
     *
     * @param clazz The class of the enum.
     * @param <E>   The enum type
     * @return Returns a codec for the enum class
     */
    public static <E extends Enum<E>> Codec<E> createEnumCodec(Class<E> clazz) {
        return Codec.either(
                Codec.INT.xmap(
                        ordinal -> clazz.getEnumConstants()[ordinal],
                        Enum::ordinal
                ),
                Codec.STRING.xmap(
                        name -> Enum.valueOf(clazz, name),
                        Enum::name
                )
        ).xmap(
                either -> either.left().orElseGet(() -> either.right().orElseThrow()),
                Either::left
        );
    }

    private ThermooCodecs() {

    }

}
