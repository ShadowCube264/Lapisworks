package com.luxof.lapisworks.items.shit;

public interface FullyAmelInterface {
    int getRequiredAmelToMakeFromBase();
    default boolean noPartAmelPhase() { return false; };
}
