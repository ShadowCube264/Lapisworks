Ah hell naw chat you're tweaking I can't do this fucking marathon
Luxof didn't even DREAM of this shit in his prime (1.1.0-1.2.0)
1.5.1
- Updated Cubic Exaltation and Spherical Exaltation's out-of-date names
- Updated Cubic Exaltation's arguments to be `[pattern], vec, vec, bool` instead of `[pattern], vec, num, bool`
- Fixed bug where Cubic Exaltation wouldn't clear the arguments after use
- Added the Simple Mind Container (and its scrying lens info overlay)
- Added Thought Sieving
- Added Mind Liquefaction
- Added Cognition Purification
1.5.1.5
- fixed bug where Simple Mind Container wouldn't gain Mind (tied to bug below, actually)
- fixed bug where just by existing for long enough you could nullify villager consumption cd
- fixed bug where Mind Liquefaction wouldn't take anything but a full container
1.5.2
- Patchouli and hexdoc interop for per world shape patterns
- Refactored like 40% of the code to be less painful to fw lmao
- fixed a capitalization mistake and "Format Error:" in some places of the book
- fixed no translation for the individual variants of Summon Enchanted Sentinel
- fixed weird ass bug with Enchant X body part patterns where they'd try to take negative Amel
  (i dunno if this one was purely in the devving or not)
- fixed enchanted attrs not carrying over across logins
- Added what I forgor to the sword descs
- made Thought Sieve consume 50% of the mind, and not just 25%
- Added Live Jukebox and it's companion pattern "Teach Song"
- Added Imbue Mind with recipes:
    - Amethyst Block -> Budding Amethyst
    - Jukebox -> Live Jukebox
1.5.3
- Fixed enchantments being able to take too much Amel and making negative nums
- Fixed Imbue Amel taking too much media
- Fixed Imbue Amel not properly doing it's fucking thing of repairing shit
- Fixed a potential crash in Imbue Amel
- Fixed LivingEntity mixin crashing because i can't mixin to constructors for shit (just removed the inject)
- Made the Patchouli book read better in some places
- Renamed the old Imbue Mind (the one that recharges stuff) to Mind Liquefaction
- Mainhand-reading patterns have been generalized to any hand
- Also generalized many patterns to take any hand
- generalization has allowed most patterns to work on casting circles too
- Mainhand mishap has been generalized to any hand as well
- Not Enough Items In Offhand mishap has been generalized as well
- Added Equivalent Block D.
- Added Equal Block Dist.
- Added Hastenature
- Imbue Amel can now lowkey make enchanted books more powerful
1.5.4
- Spell Circle wireless media transmission (costs media per block)
- Spell Circle wireless ambit extender (costs media per layer)
