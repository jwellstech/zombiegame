#TODO
- [ ] Game world
- [x] Character
- [x] Character moves
- [x] World moves around character
- [x] Collision detection
- [ ] Player knockback when zombie hits you so they don't just rek u
- [ ] Game Over menu
- [ ] Main menu
- [x] Water
- [x] Lava
- [x] Zombies can spawn
- [x] Zombies can move
- [x] Zombies follow you
- [x] Zombies don't spawn inside objects
- [ ] Zombies don't walk through each other
    - Kinda got this, but it's a bit glitchy and gets them stuck together. Check Physics.handleGameObject
- [x] Kill counter
- [x] Health meter
    - [x] Text health number bar thing first
- [x] Ammo bar
- [ ] To make zombie types, just make more "zombie" classes that extend Zombie
- [x] Weapons
    - [x] Guns shoot in direction of cursor (need help with the math here) Gun.shoot()
- [x] Health
- [ ] Upgrades
- [ ] Medpacks
- [ ] Buildings
- [ ] Items are random
- [ ] Points (upgrade system)
- [x] Character sprites
- [x] Zombie sprites
    - [ ] Multiple zombie types
- [ ] Game gets harder as you play
    - [x] More zombies as you play
    - [ ] Stronger zombies as you play
- [x] Destroy bullets when they hit outer walls (probably just make strength of these like 1000000000)
- [x] Make outer walls

##Bugfixes
- [x] Game crashes when you shoot wall
- [x] Game crashes when you kill zombies too quickly
- [x] Bullets turn into white boxes (.png is destroyed before bullet is removed)