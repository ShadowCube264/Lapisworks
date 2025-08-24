package com.luxof.lapisworks.init;

import vazkii.patchouli.api.PatchouliAPI;

// really evil fucking dark magic bullshit to circumvent some stuff that was there for a reason
// still probably better then Hexical's panic()
// why?
// i like pretty shit
// i guess you could also make the excuse for originality too, afaik no one has done
// per-world patterns that change shape
public class ThemConfigFlags {
    // i dunno what happens if i Identifier.toString();
    public static String enchsent1 = "lapisworks:enchsent1";
    public static String enchsent2 = "lapisworks:enchsent2";
    public static String enchsent3 = "lapisworks:enchsent3";
    public static String enchsent4 = "lapisworks:enchsent4";
    public static String enchsent5 = "lapisworks:enchsent5";
    public static String enchsent6 = "lapisworks:enchsent6";
    public static Integer chosenEnchSent = null;
    public static String[] allEnchSentFlags = {
        enchsent1, enchsent2, enchsent3, enchsent4, enchsent5, enchsent6
    };

    public static void declareEm() {
        PatchouliAPI.IPatchouliAPI api = PatchouliAPI.get();
        api.setConfigFlag(enchsent1, false);
        api.setConfigFlag(enchsent2, false);
        api.setConfigFlag(enchsent3, false);
        api.setConfigFlag(enchsent4, false);
        api.setConfigFlag(enchsent5, false);
        api.setConfigFlag(enchsent6, false);
    }
}
