# FabricClassCache Mod

What is this?
=============

Class cache for Fabric, to speed up class loading time.

---

Requirements:
=============

- Patched Fabric loader, may be also found on my GitHub

---

How to:
=============

- Install patched Fabric loader
- Edit version .json file to prevent the launcher from downloading loader, or... create new profile
- Drop mod inside mods directory
- Start the client for the first time, join singleplayer, jump, walk, etc
- Close the game
- The game should now start faster!

---

Building:
==========

Install JDK 16, if not already  
Run `./gradlew build`, get files from `buid/libs`

---