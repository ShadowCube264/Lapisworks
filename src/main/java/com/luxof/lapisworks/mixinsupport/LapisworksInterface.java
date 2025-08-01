package com.luxof.lapisworks.mixinsupport;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;

public interface LapisworksInterface {
    double getAmountOfAttrJuicedUpByAmel(EntityAttribute attribute);
    void setAmountOfAttrJuicedUpByAmel(EntityAttribute attribute, double value);
    void setAllJuicedUpAttrsToZero();

    boolean checkFireyFists();
    void setFireyFists(boolean value);

    int checkLightningBending();
    void setLightningBending(int level);

    AttributeContainer getLapisworksAttributes();
    void setLapisworksAttributes(AttributeContainer attributes);

    //void properlyInitiateAndLoadFieldsIfNotInitiated();
    //void updateLapisworksHashMap();
}
