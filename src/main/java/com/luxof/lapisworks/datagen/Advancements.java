package com.luxof.lapisworks.datagen;

import java.util.function.Consumer;

import com.luxof.lapisworks.Lapisworks;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Advancements implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(AdvancementsProvider::new);
    }

    static class AdvancementsProvider extends FabricAdvancementProvider {
        protected AdvancementsProvider(FabricDataOutput dataGenerator) {
            super(dataGenerator);
        }

        @Override
        public void generateAdvancement(Consumer<Advancement> consumer) {
            Advancement gotLapis = Advancement.Builder.create()
                .display(
                    Items.LAPIS_LAZULI,
                    Text.translatable("advancements.lapisworks.gotlapis.name"),
                    Text.translatable("advancements.lapisworks.gotlapis.description"),
                    new Identifier("textures/gui/advancements/backgrounds/adventure.png"), // whatever
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
                )
                .criterion("gotlapis", InventoryChangedCriterion.Conditions.items(Items.LAPIS_LAZULI))
                .build(consumer, Lapisworks.MOD_ID + "/gotlapis");
        }
    }
}
