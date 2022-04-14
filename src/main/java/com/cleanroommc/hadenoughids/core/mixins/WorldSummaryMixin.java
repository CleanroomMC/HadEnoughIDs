package com.cleanroommc.hadenoughids.core.mixins;

import com.cleanroommc.hadenoughids.HadEnoughIDs;
import net.minecraft.world.storage.WorldSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldSummary.class)
public class WorldSummaryMixin {

    @Shadow @Final private String versionName;

    @Inject(method = "askToOpenWorld", at = @At("RETURN"), cancellable = true)
    private void checkIfWorldIsHEID(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValueZ() && !this.versionName.equals(HadEnoughIDs.WORLD_INFO_VERSION_NAME));
    }

}
