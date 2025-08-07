package com.luxof.lapisworks.mixinsupport;

import java.util.List;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;

public interface LapisworksInterface {
    double getAmountOfAttrJuicedUpByAmel(EntityAttribute attribute);
    void setAmountOfAttrJuicedUpByAmel(EntityAttribute attribute, double value);
    void setAllJuicedUpAttrsToZero();
    
    AttributeContainer getLapisworksAttributes();
    void setLapisworksAttributes(AttributeContainer attributes);

    int checkFireyFists();
    void setFireyFists(int level);

    int checkLightningBending();
    void setLightningBending(int level);

    int checkFallDmgRes();
    void setFallDmgRes(int level);

    int checkLongBreath();
    void setLongBreath(int level);

    void setEnchantmentLevel(int whatEnchant, int level);
    List<Integer> getEnchantments();
    void setEnchantments(int[] levels); // this is meant for nbt shit
    void setAllEnchantsToZero();
}
