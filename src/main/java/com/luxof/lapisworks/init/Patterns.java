package com.luxof.lapisworks.init;

import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import com.luxof.lapisworks.Lapisworks;
import com.luxof.lapisworks.actions.CheckAttr;
import com.luxof.lapisworks.actions.ImbueLap;
import com.luxof.lapisworks.actions.SwapAmel;
import com.luxof.lapisworks.actions.MoarAttr;
import com.luxof.lapisworks.actions.great.EnchantFallDmgRes;
import com.luxof.lapisworks.actions.great.EnchantFire;
import com.luxof.lapisworks.actions.great.EnchantLightning;
import com.luxof.lapisworks.actions.great.EnchantLongBreath;
import com.luxof.lapisworks.actions.CheckEnchant;

public class Patterns {
    public static void init() {
        MoarAttr MoarHealthAction = new MoarAttr(
            EntityAttributes.GENERIC_MAX_HEALTH,
            2.0,
            0,
            2,
            false
        );
        MoarAttr MoarAttackAction = new MoarAttr(
            EntityAttributes.GENERIC_ATTACK_DAMAGE,
            4.0, // the barbarian and the monk fistfighting a demon because magic is for nerds
            0,
            5,
            false
        );
        // i'd add armor but narratively it makes no sense, you can already enchant your skin
        // so what else is there to enchant to make yourself stronger?
        // man i wish i could, i had such a cool ass fucking pattern too
        // north east wwwaqeeeqawww
        // yeah just gonna use CheckAttr instead
        MoarAttr MoarSpeedAction = new MoarAttr(
            EntityAttributes.GENERIC_MOVEMENT_SPEED,
            3.0,
            0,
            50, // movement speed is base 0.1 and i want 5 amel per 0.1 movement speed
            false
        );
        MoarAttr GibDexterityAction = new MoarAttr(
            EntityAttributes.GENERIC_ATTACK_SPEED,
            1,
            4,
            16,
            true
        );
        register("imbue_lap", "qadwawdaqqeae", HexDir.NORTH_EAST, new ImbueLap());
        register("swap_amel", "wqwawwqwaqeq", HexDir.EAST, new SwapAmel());
        register("moar_health", "wqadaqwwawwwqwwawdwawwqwwwwa", HexDir.NORTH_EAST, MoarHealthAction);
        register("moar_attack", "qaqwweaeaqwww", HexDir.EAST, MoarAttackAction);
        register("moar_speed", "ddqwaqeqa", HexDir.WEST, MoarSpeedAction);
        register("gib_dexterity", "aeaqqdeeeqewdwqwdwe", HexDir.WEST, GibDexterityAction);
        register("check_attr", "wwwaqeeqawww", HexDir.NORTH_EAST, new CheckAttr());
        register("fireyfists", "wwewdawdewqewedadad", HexDir.EAST, new EnchantFire());
        register("lightningbending", "wewdawdewqewdqqeedqe", HexDir.EAST, new EnchantLightning());
        register("falldmgres", "qqwwqqqadwewdeq", HexDir.SOUTH_WEST, new EnchantFallDmgRes());
        register("longbreath", "wewdwewewewewdwew", HexDir.SOUTH_EAST, new EnchantLongBreath());
        register("checkenchant", "aqawwqqwqqw", HexDir.SOUTH_EAST, new CheckEnchant());
    }

    private static void register(
        String name,
        String signature,
        HexDir startDir,
        Action action
    ) {
        Registry.register(HexActions.REGISTRY, new Identifier(Lapisworks.MOD_ID, name), new ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action));
    }
}
