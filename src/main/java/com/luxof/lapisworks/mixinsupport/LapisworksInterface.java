package com.luxof.lapisworks.mixinsupport;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;

public interface LapisworksInterface {
    double getAmountOfAttrJuicedUpByAmel(EntityAttribute attribute);
    void setAmountOfAttrJuicedUpByAmel(EntityAttribute attribute, double value);
    void setAllJuicedUpAttrsToZero();

    int checkFireyFists();
    void setFireyFists(int level);

    int checkLightningBending();
    void setLightningBending(int level);

    int checkEnchantment(int whatEnchant);
    void setEnchantmentLevel(int whatEnchant, int level);

    AttributeContainer getLapisworksAttributes();
    void setLapisworksAttributes(AttributeContainer attributes);
}
