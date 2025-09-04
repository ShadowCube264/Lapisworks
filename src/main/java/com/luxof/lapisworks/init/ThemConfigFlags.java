package com.luxof.lapisworks.init;

import java.util.ArrayList;
import java.util.List;

import vazkii.patchouli.api.PatchouliAPI;

// really evil fucking dark magic bullshit to circumvent some stuff that was there for a reason
// (still probably better then Hexical's panic())
// why?
// i like pretty shit
// i guess you could also make the excuse for originality too, afaik no one has done
// per-world patterns that change shape
public class ThemConfigFlags {
    // i dunno what happens if i Identifier.toString();
    public static List<String> allEnchSentFlags = List.of(
        "lapisworks:enchsent1", "lapisworks:enchsent2", "lapisworks:enchsent3",
        "lapisworks:enchsent4", "lapisworks:enchsent5", "lapisworks:enchsent6"
    );

    // feel free to fuck with this stuff
    // NOTE: TO REMOVE COMPILER WARNINGS, THIS ISN'T FILLED UNTIL declareEm() IS CALLED!
    public static List<Integer> chosenFlags = new ArrayList<Integer>(List.of());
    public static List<List<String>> allConfigFlags = new ArrayList<List<String>>(List.of(
        allEnchSentFlags
    ));
    // this makes sense as well in Patterns.java, but I'd rather people not have 5000 editor tabs open
    public static List<List<String>> allPerWorldShapePatterns = new ArrayList<List<String>>(List.of(
        List.of(
            "aqaeawdwwwdwqwdwwwdweqqaqwedeewqded",
            "aqaeawdwwwdwqwdwwwdwewweaqa",
            "wdwewdwwwdwwwdwqwdwwwdw",
            "aqaeawdwwwdwqwdwwwdweqaawddeweaqa",
            "wdwwwdwqwdwwwdweqaawdde",
            "wdwwwdwqwdwwwdwweeeee"
        )
    ));

    public static void declareEm() {
        allConfigFlags.forEach((List<String> list) -> {
            list.forEach((String flag) -> {
                PatchouliAPI.get().setConfigFlag(flag, false);
            });
            chosenFlags.add(null);
        });
    }
}
