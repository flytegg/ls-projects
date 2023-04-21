# GrapplingHooks

Grappling hooks are a fantastic cosmetic or item for your server that allow players to throw themselves around like rag dolls.
The concept is fairly simple, however, there are a couple quirks that you may encounter while making it that this project can assist you with.

## How they work
Grappling hooks in Minecraft work by casting a fishing rod and then throwing the player in the direction of the hook when they reel it in, it's pretty much that simple :stuck_out_tongue:

## Required Features
* A Command to give a player a grappling hook
* The grappling hook throws players around :wink:
* Modifiable power (how far it throws you)

## Optional Features
* A cooldown between grapple uses

## Suggested Lectures
* 12 & 13 - Java Basic 3 & 4
* 16 & 17 - Events & Commands
* 25 - ItemStacks
* Vectors: <https://www.spigotmc.org/wiki/vector-programming-for-beginners/>

## Hints
* You should set the velocity of the player to the vector between the player's location and the fishing hook's location. You can convert  a location to a vector using Location#toVector().
* Spigot doesn't let you get the specific fishing rod used when reeling your rod in, therefore you will have to determine whether a player is using a grappling hook or not when they *cast* the rod.

This is a relatively short project but it covers the basics of vectors and due to some of spigot's limitations, makes you think (a little) outside the box. 
Also they're totally fun to use!
