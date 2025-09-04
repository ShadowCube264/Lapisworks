package com.luxof.lapisworks.interop.patchouli;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.interop.patchouli.AbstractPatternComponent;
import at.petrak.hexcasting.xplat.IXplatAbstractions;

import com.google.gson.annotations.SerializedName;

import static com.luxof.lapisworks.init.ThemConfigFlags.chosenFlags;

import java.util.List;
import java.util.function.UnaryOperator;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import vazkii.patchouli.api.IVariable;

public class PerWorldShapePattern extends AbstractPatternComponent {
    @SerializedName("op_id")
    public String opNameRaw;
    @SerializedName("idx_in_flags")
    public String configFlagIdxRaw;
    public Identifier opName;
    public int configFlagIdx;

    @Override
    public List<HexPattern> getPatterns(UnaryOperator<IVariable> lookup) {
        RegistryKey<ActionRegistryEntry> key = RegistryKey.of(
            IXplatAbstractions.INSTANCE.getActionRegistry().getKey(),
            new Identifier(
                this.opName.getNamespace(),
                this.opName.getPath() + String.valueOf(chosenFlags.get(this.configFlagIdx) + 1)
            )
        );
        ActionRegistryEntry entry = IXplatAbstractions.INSTANCE.getActionRegistry().get(key);

        return List.of(entry.prototype());
    }

    @Override public boolean showStrokeOrder() { return true; }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        String opName = lookup.apply(IVariable.wrap(this.opNameRaw)).asString();
        this.opName = Identifier.tryParse(opName);
        // NO clue how to get it to be a num and get it to work in data
        this.configFlagIdx = Integer.parseInt(lookup.apply(IVariable.wrap(configFlagIdxRaw)).asString());

        super.onVariablesAvailable(lookup);
    }
}
