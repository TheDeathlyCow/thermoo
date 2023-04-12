package com.github.thedeathlycow.thermoo.mixin;

import com.github.thedeathlycow.thermoo.api.predicate.TemperaturePredicate;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityPredicate.class)
public class EntityPredicateMixin {

    private TemperaturePredicate thermoo$temperaturePredicate = TemperaturePredicate.ANY;

    @Inject(
            method = "test(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/Entity;)Z",
            at = @At(
                    value = "TAIL",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void testTemperature(ServerWorld world, Vec3d pos, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof TemperatureAware temperatureAware) {
            if (!thermoo$temperaturePredicate.test(temperatureAware)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
            method = "fromJson",
            at = @At(
                    value = "TAIL"
            )
    )
    private static void fromJson(JsonElement json, CallbackInfoReturnable<EntityPredicate> cir) {
        var predicate = cir.getReturnValue();

        JsonObject jsonObject = JsonHelper.asObject(json, "entity");

        ((EntityPredicateMixin) (Object) predicate).thermoo$temperaturePredicate = TemperaturePredicate.fromJson(jsonObject.get("thermoo.temperature"));
    }


    @Inject(
            method = "toJson",
            at = @At(
                    value = "TAIL",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void toJson(CallbackInfoReturnable<JsonElement> cir, JsonObject jsonObject) {
        jsonObject.add("thermoo.temperature", this.thermoo$temperaturePredicate.toJson());
    }

}
