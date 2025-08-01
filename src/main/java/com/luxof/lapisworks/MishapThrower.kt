package com.luxof.lapisworks

import at.petrak.hexcasting.api.casting.mishaps.Mishap

import net.fabricmc.api.ModInitializer

/* I'm too lazy to mixin into the hexcasting mishap catcher.
 * smh.
 */

class MishapThrower : ModInitializer {
    override fun onInitialize() {}

    companion object {
        @JvmStatic
        fun throwMishap(mishap: Mishap) {
            throw mishap
        }
    }
}
