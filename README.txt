=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: simono
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Collections - There is a lot of information at any given moment in the game that needs to be 
  stored. I need to keep track of every tower on the screen (which changes as the player buys and 
  sells towers), every projectile currently on the screen (obviously changes as they are fired and 
  collide), and every balloon on screen (changes as they enter play, are destroyed, and leave play), 
  and temporary effects such as pops and explosions. A LinkedList(s) makes sense to represent this
   data because of the variable amount of objects and the frequent addition and removal. So, I have
   a LinkedList for each of Balloon, Tower, Projectile, and Effect. This allows for the tracking,
   updating, and drawing of every one of these objects on the screen. 

  2. FileIO - I developed a text file format to store data about balloon path data and level data 
  that is read in at the initialization of the game. This allows the balloons to follow an arbitrary
  path through space (follow the track on map) and informs the game of what balloons to spawn for 
  each level. Additionally, I implemented a storage system so the user can save/load their game.
   
  3. I have the GameObject class in which Towers, Effects, Balloons, and Projectiles are descended 
  from. These clearly require vastly different game logic to occur for their update functions, but 
  their base mechanics (drawing on the screen at a certain coordinate and rotation) is the same, so
  this structure makes sense. I selectively override methods all of the time in my code to make 
  specific towers (for example) behave slightly differently when their update() function is called 
  on each object in the List of Towers. Additionally, the update() function returns a 
  Collection<GameObject> to be added to the GameObjects on the screen, so this structure allows me
  to gather all different kinds of GameObjects together in one Collection.

  4. Complex Game Logic - This wasn't one of my original four concepts, but it emerged as I was 
  working on the project. There is so, so, so much going on at any given moment - bloons need to 
  follow the correct path at the correct speed, towers need to detect bloons that they can see, 
  aim and fire a projectile predictively at the bloons, then that projectile needs to move at the 
  correct trajectory and also check for collisions, etc. Honestly, I think the game speaks for 
  itself in this regard. It is a really good (in my opinion) recreation of Bloons TD5, it looks 
  quite alike and plays similarly. Regretfully, adding Upgrades proved to be too much additional 
  work, but all in all I think the game turned out really great and plenty complex.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Game.java - The main runner of everything, handles switching from the main menu, to instructions,
to in-game.

Field.java - Where the actual game takes place. Contains several Collections of GameObjects which 
are updated and drawn every tick (~10 ms), and the bottom and right HUDs for buying/selling towers,
advancing levels, saving and loading, etc.

GameObject.java - The abstract class parent of everything that appears on the screen, including 
Towers, Projectiles, Balloons, and Effects. Contains boilerplate code for an object that has a 
position in space, a rotation, and an image of itself such it can draw itself. Contains an update()
method that must be overriden by children.

Tower.java - The abstract parent class of every tower in the game. It provides functionality for 
having a field of view and finding balloons that enter its field of view, and then firing. 

DartMonkey.java, TackShooter.java, IceTower.java, BombTower.java, SuperMonkey.java, Spikes.java, 
Pineapple.java - Children of Tower, all have their own specific implementations for how they should 
fire and be drawn, and variations on attack speed, range, etc.

Bloon.java - An enum that represents type of balloon. This should probably be in Balloon.java.

Balloon.java - The class that represents a balloon object. Every balloon is created in accordance
with path data and a type from Bloon.java, which then informs the constructor how to build the 
balloon object.

Projectile.java - The abstract class representing projectiles that move across the screen.

TargetedProjectile.java - A child of Projectile, represents a projectile that takes in a target as 
a parameter and travels to that position. I never implemented other types of projectiles
 (e.g seeking projectiles), so this could probably just be in Projectile.

Dart.java, Tack.java, Bomb.java - Children of TargetedProjectile, contain specifically 
implementation for drawing, speed, damage, hitbox, etc. of each projectile in the game.

Effect.java - GameObjects that merely appear on the screen for some amount of time and then disappear
shortly after.

Explosion.java - Child of Effect, necessary because Explosions are effects that don't just appear,
they also balloons that they touch.

DataLoader.java - A generalized class meant to be used statically that handles all data input and 
output, used for FileIO saving and loading, reading path data, loading in images, parsing the XML
data used to cut out sprites, etc.

ResourceManager.java - A Singleton class that handles all manipulation of Images and sprites from 
the master sprite sheet, much of this is simply for convenience in dealing with images.

PathTraceUtility.java - A utility that tracks mouse movement and outputs the data to a file that can
later be read as path data for balloons to follow.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

Yes, overall it was quite difficult to implement. It seems like everything needs access to every
other thing in order to perform its function. For example, a Tower needs to know where every balloon
is to check if it can see any. Managing all these interdepencies while still having clean 
code proved to be quite a challenge.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

My design is alright, I think. It started out really good, but as time went on and the game got more
complex, it started to become kind of messy. The seperation of functionality is good, but private
state encapsulation is not great. This is partly due to my desire to save memory (it already uses
a lot), but also simply ease of use and design from passing around the actual object. I'm honestly
not sure how I would refactor this if given the chance, I guess I would just make sure each class
only strictly did what it is supposed to, sometimes there is overlap. For example, Field is full of
a lot of set up code that could probably be in Game, and Balloon and Projectile do similar things 
when they collide.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  I obtained every sprite from the game files of Bloons TD Battles, which is free to download on 
  Steam. I also got the XML file that instructs me how to cut out the sprites from here.
  
  I obtained the map image from screen-shotting the game Bloons TD5 on https://ninjakiwi.com, 
  which is free to play. 
  
  I obtained a handful of code snippets from StackOverflow, which are cited in the code and mostly
  have to do with manipulating images in the simplest way possible.
