package com.github.thedeathlycow.thermoo.api.environment.attribute;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeasonState;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * A wrapper class for an optional season state, for use in {@linkplain ThermooEnvironmentAttributes environment attributes}.
 *
 * @param <S> The season type.
 */
public final class SeasonStateAttribute<S extends ThermooSeason> {
    private static final SeasonStateAttribute<?> EMPTY = new SeasonStateAttribute<>(null);

    @Nullable
    private final ThermooSeasonState<S> state;

    private SeasonStateAttribute(@Nullable ThermooSeasonState<S> state) {
        this.state = state;
    }

    /**
     * @return The season state of this attribute
     */
    public Optional<ThermooSeasonState<S>> state() {
        return Optional.ofNullable(this.state);
    }

    /**
     * Creates a season state attribute out of an existing season state. If the provided {@code state} is {@code null},
     * then the returned state will be empty.
     *
     * @param state The season state.
     * @param <S>   The season type.
     * @return A new season state attribute. This attribute will be empty if the provided {@code state} is {@code null}.
     */
    public static <S extends ThermooSeason> SeasonStateAttribute<S> ofNullable(@Nullable ThermooSeasonState<S> state) {
        return state != null ? of(state) : empty();
    }

    /**
     * Creates a season state attribute out of an existing season state.
     *
     * @param state The season state. May not be {@code null}.
     * @param <S>   The season type.
     * @return A new season state attribute.
     */
    public static <S extends ThermooSeason> SeasonStateAttribute<S> of(ThermooSeasonState<S> state) {
        Objects.requireNonNull(state, "A state must be provided!");
        return new SeasonStateAttribute<>(state);
    }

    /**
     * Creates an empty season state attribute.
     *
     * @param <S> The season type.
     * @return An empty season state attribute.
     */
    public static <S extends ThermooSeason> SeasonStateAttribute<S> empty() {
        @SuppressWarnings("unchecked")
        SeasonStateAttribute<S> attribute = (SeasonStateAttribute<S>) EMPTY;

        return attribute;
    }

    /**
     * Creates a codec for a season state attribute.
     *
     * @param seasonCodec The codec of the underlying season type.
     * @param <S>         The season type.
     * @return A new codec for the season state.
     */
    public static <S extends ThermooSeason> Codec<SeasonStateAttribute<S>> codec(Codec<S> seasonCodec) {
        Codec<Optional<ThermooSeasonState<S>>> stateCodec = ThermooSeasonState.codec(seasonCodec)
                .optionalFieldOf("state")
                .codec();

        return stateCodec.xmap(
                state -> ofNullable(state.orElse(null)),
                SeasonStateAttribute::state
        );
    }
}