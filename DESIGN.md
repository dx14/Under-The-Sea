# game

High level designs: 

The battle screen uses turn based combat and monitors when the enemy can attack. Many helper methods have been added with simple names to state their purpose and make the code more readable. Enemy fish locations in the roam screen are randomized, and attacks all have an accuracy rate, making the game more unpredictable and different each time it is played.

How to add new features: 

Many of the helper methods are useful in adding new features, such as adding an enemy character and adjusting its location and animations. Game length can be extended by increasing the number of stages cleared (in the moveToNextStage method in RoamGame) necessary. New attacks can also be added by adding a new button (methods exist), styling and locating it, and adding its effect on the health bars and its accuracy. To make the game more difficult, more enemies can be added and the damage and accuracies of the enemies can be increased, all inside the BattleGame class.

Design choices: 

I made many helper methods in the classes RoamGame and BattleGame, and a few of them contain one liners within the helper method. However, I did this because the code would be more readable with a proper method name that explains its functionality (i.e. reflectImage(img) versus img.setScaleX(-1)). There is a bit of duplicated code (methods that have to be called repeatedly, if statements that have a slight difference), but helper methods greatly help lower the number of lines used.

Assumptions or decisions to resolve ambiguity in functionality: 

When playing the game, it is assumed that the player waits for the enemy to make an attack before attacking again (there is a slight delay after the user attacks and before the enemy attacks). This way, the battle is effectively a turn based game, and it would not be if the player could use more than one ability per turn.