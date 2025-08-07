package com.luxof.lapisworks.items;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import at.petrak.hexcasting.common.items.ItemStaff;
import at.petrak.hexcasting.common.lib.HexAttributes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class AmelStaff extends ItemStaff {
    public EntityAttributeModifier GRID_ZOOM = new EntityAttributeModifier(
        /* "Nani the fuck are you doing?"
         * Staffs or something. You need to generate a random UUID once
         * and use it for every attributemodifer.
         * "why??"
         * i don't know either.
         * "what the fuck."
         * yeah.
         */
        UUID.fromString("a897e19e-b03f-43ee-970f-d0f657b88a49"),
        "Amel Staff Zoom",
        0.5,
        EntityAttributeModifier.Operation.MULTIPLY_BASE
    );

    public AmelStaff(FabricItemSettings props) { super(props); }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        HashMultimap<EntityAttribute, EntityAttributeModifier> out = HashMultimap.create(super.getAttributeModifiers(slot));
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            out.put(HexAttributes.GRID_ZOOM, GRID_ZOOM);
        }// else if (slot == EquipmentSlot.OFFHAND) {
        //    out.put(HexAttributes.GRID_ZOOM, GRID_ZOOM_OFFHAND);
        //}
        return out;
    }
}
