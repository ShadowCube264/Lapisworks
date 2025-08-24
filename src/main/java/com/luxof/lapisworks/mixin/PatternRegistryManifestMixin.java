package com.luxof.lapisworks.mixin;

import at.petrak.hexcasting.api.casting.PatternShapeMatch;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.casting.PatternRegistryManifest;

import com.luxof.lapisworks.init.Patterns;
import com.luxof.lapisworks.init.ThemConfigFlags;

import static com.luxof.lapisworks.Lapisworks.LOGGER;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PatternRegistryManifest.class, remap = false)
public abstract class PatternRegistryManifestMixin {
    
    @Inject(at = @At("HEAD"), method = "matchPattern", cancellable = true)
    private static void matchPattern(
        HexPattern pat,
        CastingEnvironment environment,
        boolean checkForAlternateStrokeOrders,
        CallbackInfoReturnable<PatternShapeMatch> cir
    ) {
        // i only have to invalidate the ones that aren't chosen
        // because hex will automatically validate the one that is chosen if this is it.
        if (ThemConfigFlags.chosenEnchSent != null) {
            String sig = pat.anglesSignature();
            for (int i = 0; i < 6; i++) {
                if (i == ThemConfigFlags.chosenEnchSent) { continue; }
                else if (sig == Patterns.all_create_enchsent[i]) {
                    cir.setReturnValue(new PatternShapeMatch.Nothing());
                }
            }
        } else {
            LOGGER.warn("Why the fuck has an enchanted sentinel pattern not been chosen yet?!");
        }
    }
}
