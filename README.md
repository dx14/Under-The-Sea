# game
First project for CompSci 308 Fall 2015

Name: Dennis Xu

Date started: 8/29/15

Date finished: 9/4/15

Hours worked: ~20

Resources used: Various websites (for images), StackOverflow, Java docs

Main class file: Main.java

Data (non Java) files needed: images (folder)

How to play the game: The game opens with a splash screen. Click the START button to begin the game.  In the roaming screen, the player controls a clownfish. The goal is to move to the right side of the roam screen until the fish reaches his home. After the player moves to the end of the current screen, that stage is considered cleared. The fish movement is controlled with the arrow keys. If the fish touches one of the other fish (blue or yellow), a battle screen appears. The player has four moves: Blow Bubbles (low damage, high accuracy), Tail Attack (high damage, low accuracy), Eat Seawood (restore health), and Flee (returns player back to roam screen, low probability). Unless the player flees, he must defeat the enemy so that the enemy HP bar is empty. After, the player is brought back to the roam scene, but the previously cleared stages are still accounted for. On the third stage, the fish's home will appear, and touching it will result in a victory scene. If the player's HP bar is depleted to 0 in a battle with an enemy, then the game is over.

Keys/Mouse input: In the splash screen, click START (must actually click on the letters themselves) to begin the game. In the roam screen, use the arrow keys left, right, up, down, to move in the corresponding directions. In the battle screen, use the mouse to click on the attack that the player chooses.

Cheat Keys: Roam screen: Press 'Z' to skip to the last stage with the fish home. On the battle screen: Press 'W' to win the battle, press 'L' to lose the battle

Known bugs: In the battle screen, if the player rapidly to rapidly click the moves, it causes the player to use more than 1 move per turn, while the enemy makes one move. In the battle screen, after the battle wins in a victory, extra text sometimes shows up above the enemy (does not affect gameplay). 

Extra features: Locations of enemy fish are randomly generated on the roam screen. All abilities have accuracies so misses are possible during battle.

Impressions/Suggestions: I enjoyed this project very much. Even though it was very intimidating as none of the students had experience making a game or with JavaFX, this project helped us understand how to use JavaFX very well. In addition, the flexibility of how our game can be customized allowed for creativity. This project took a long time to complete, but the process of making the game was enjoyable and not boring at any time. 
