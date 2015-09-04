// This entire file is part of my masterpiece.
// DENNIS XU

import java.util.Random;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * BattleGame class is responsible for controlling the mechanics of the battle between
 * two fish, including turn based combat, attack commands, accuracy, and effects, and adjustment
 * of health bars in the battle scene. Game enters this battle scene after roam scene
 * (in RoamGame class) if player intersects NPC fish.
 */

public class BattleGame {

	private Scene battleScene;
	private RoamGame roamGame;
	private LoseGame loseGame;
	private ImageView battleBg, myNemo, yellowFish, blueFish;
	private Group battleGroup = new Group();
	private Button bubbleMove, tailMove, eatMove, fleeMove;
	private ProgressBar hpBarNemo, hpBarEnemy;
	private boolean nextPlayer = true;
	private boolean canFlee = false;
	private int BOUNCE_SPEED = 20, ENEMY_DELAY = 0;
	private int LOSS_DELAY = 0, WIN_DELAY = 0, FLEE_DELAY = 0;
	private Timeline battleAnimation;
	private int stagesCleared;

	
	// returns the battle scene with background, player, enemy fish, HP bars, and attacks
	public Scene initBattleScreen(Stage s, int width, int height, String enemy, int stages) {

		startBattleLoop(s);
		stagesCleared = stages;

		battleScene = new Scene(battleGroup, width, height);

		roamGame = new RoamGame();
		battleBg = new ImageView();
		myNemo = new ImageView();
		yellowFish = new ImageView();
		blueFish = new ImageView();

		battleBg = roamGame.getImage("battlebg.png");
		blueFish = roamGame.getImage("fish1.png");
		yellowFish = roamGame.getImage("fish2.png");
		myNemo = roamGame.getImage("nemo.png");

		bubbleMove = new Button("Blow Bubbles");
		tailMove = new Button("Tail Attack");
		eatMove = new Button("Eat Seaweed");
		fleeMove = new Button("Run Away!");

		battleGroup.getChildren().add(battleBg);
		battleGroup.getChildren().add(myNemo);
		battleGroup.getChildren().add(bubbleMove);
		battleGroup.getChildren().add(tailMove);
		battleGroup.getChildren().add(eatMove);
		battleGroup.getChildren().add(fleeMove);

		myNemo.setX(width/6);
		myNemo.setY(height/2);

		pickEnemyFish(enemy, battleGroup, width, height);

		styleButton(bubbleMove);
		styleButton(tailMove);
		styleButton(eatMove);
		styleButton(fleeMove);

		setButtonLocation(bubbleMove, width/13, height/2 + height/4);
		setButtonLocation(tailMove, width/4, height/2 + height/4);
		setButtonLocation(eatMove, width/13, height/2 + height/3);
		setButtonLocation(fleeMove, width/4, height/2 + height/3);

		hpBarNemo = new ProgressBar(1);
		hpBarNemo.setStyle("-fx-accent: green;");
		hpBarEnemy = new ProgressBar(1);
		hpBarEnemy.setStyle("-fx-accent: red;");

		battleGroup.getChildren().add(hpBarNemo);
		battleGroup.getChildren().add(hpBarEnemy);

		setBarLocation(hpBarNemo, width/7, (height/2) - height/6);
		setBarLocation(hpBarEnemy, (width/2) + width/7, (height/2) - height/6);

		battleScene.setOnKeyPressed(e -> cheatBattleKey(e.getCode()));

		return battleScene;
	}

	// makes fish move up and down and continues battle if no HP bar is 0
	public void battleStep (Stage s, double elapsedTime) {

		myNemo.setY(myNemo.getY() - BOUNCE_SPEED*elapsedTime);
		blueFish.setY(blueFish.getY() - BOUNCE_SPEED*elapsedTime);
		yellowFish.setY(yellowFish.getY() - BOUNCE_SPEED*elapsedTime);
		boolean nemoOutOfBoundsUp = myNemo.getY() < Main.HEIGHT/2 - (Main.HEIGHT/13);
		boolean nemoOutOfBoundsDown = myNemo.getY() > Main.HEIGHT - (Main.HEIGHT)/2;
		if ((nemoOutOfBoundsUp) || (nemoOutOfBoundsDown)) {
			BOUNCE_SPEED = BOUNCE_SPEED*-1;
		}
		checkForFlee(s);
		checkForLoseGame(s);
		checkForWinBattle(s);
		continueGame(s);
	}
	
	// places enemy fish on scene depending on which one the player intersects in roam scene
	public void pickEnemyFish(String enemyName, Group gp, int width, int height) {
		if (enemyName == "BLUE") {
			blueFish = roamGame.getImage("fish1.png");
			gp.getChildren().add(blueFish);
			setEnemyLocation(blueFish, width, height);
		}

		if (enemyName == "YELLOW") {
			yellowFish = roamGame.getImage("fish2.png");
			gp.getChildren().add(yellowFish);
			setEnemyLocation(yellowFish, width, height);
		}
	}

	// each button clicked triggers an effect and text
	public void nemoMove(Stage s) {
		Random random = new Random();
		int moveAccuracy = random.nextInt(100);
		bubbleMove.setOnMouseClicked(e -> attackEffects(s, "BUBBLES", hpBarEnemy, moveAccuracy));
		tailMove.setOnMouseClicked(e -> attackEffects(s, "TAIL", hpBarEnemy, moveAccuracy));
		eatMove.setOnMouseClicked(e -> attackEffects(s, "HEAL", hpBarNemo, moveAccuracy));
		fleeMove.setOnMouseClicked(e -> attackEffects(s, "FLEE", hpBarEnemy, moveAccuracy));
	}

	// attack effects, accuracies, and text for each attack scenario depending
	// on which attack was selected, updates HP bar
	public void attackEffects(Stage s, String action, ProgressBar hpBar, int acc) {

		resetText();

		Text missText = new Text(10, 10, "Your attack missed!");
		Text hitText = new Text(10, 10, "Your attack hit!");
		Text fullHealthText = new Text(10, 10, "You are full health!");
		Text healText = new Text(10, 10, "You restored health!");
		Text fleeFailedText = new Text(10, 10, "You could not flee!");	    
		Text fleeSuccessText = new Text(10, 10, "You fled!");

		stylePlayerText(missText);
		stylePlayerText(hitText);
		stylePlayerText(fullHealthText);
		stylePlayerText(healText);
		stylePlayerText(fleeFailedText);
		stylePlayerText(fleeSuccessText);

		boolean bubblesHit = action.equals("BUBBLES") && acc >= 30;
		boolean bubblesMiss = action.equals("BUBBLES") && acc < 30;
		boolean tailHit = action.equals("TAIL") && acc >= 50;
		boolean tailMiss = action.equals("TAIL") && acc < 40;
		boolean fullHealth = action.equals("HEAL") && hpBar.getProgress() == 1f;
		boolean heal = action.equals("HEAL") && hpBar.getProgress() != 1f;
		boolean fleeSuccess = action.equals("FLEE") && acc <= 20;
		boolean fleeFail = action.equals("FLEE") && acc > 20;

		if (bubblesHit) {
			battleGroup.getChildren().add(hitText);
			loseHealth(hpBar, .2);
			switchTurns(s);
		}
		if (bubblesMiss) {
			battleGroup.getChildren().add(missText);
			switchTurns(s);
		}
		if (tailHit) {
			battleGroup.getChildren().add(hitText);
			loseHealth(hpBar, .4);			
			switchTurns(s);
		}
		if (tailMiss) {
			battleGroup.getChildren().add(missText);
			switchTurns(s);
		}
		if (fullHealth) {
			battleGroup.getChildren().add(fullHealthText);
			switchTurns(s);
		}
		if (heal) {
			battleGroup.getChildren().add(healText);
			gainHealth(hpBar, .2);
			switchTurns(s);
		}
		if (fleeSuccess) {
			battleGroup.getChildren().add(fleeSuccessText);
			canFlee = true;
		}
		if (fleeFail) {
			battleGroup.getChildren().add(fleeFailedText);
			switchTurns(s);
		}
	}

	
	// enemy move accuracy and enemy attack text
	public void enemyMove(Stage s) {

		Random random = new Random();
		int moveAccuracy = random.nextInt(100);

		Text enemyAtkSuccessText = new Text(10, 10, "The enemy attacked you!");
		Text enemyAtkFailText = new Text(10, 10, "The enemy's attack missed!");
		styleEnemyText(enemyAtkSuccessText);
		styleEnemyText(enemyAtkFailText);

		if (moveAccuracy >= 40) {
			battleGroup.getChildren().add(enemyAtkSuccessText);
			loseHealth(hpBarNemo, .25);
			switchTurns(s);
		}
		if (moveAccuracy < 40) {
			battleGroup.getChildren().add(enemyAtkFailText);
			switchTurns(s);
		}
	}

	public void switchTurns(Stage s) {
		nextPlayer = !nextPlayer;
	}

	// refreshes text after each new attack
	public void resetText() {
		battleGroup.getChildren().removeAll(battleGroup.getChildren().stream()
				.filter(node -> node instanceof Text)
				.collect(Collectors.toList()));
	}

	public void startBattleLoop(Stage s) {
		battleAnimation = new Timeline();
		KeyFrame frame = new KeyFrame(Duration.millis(Main.MILLISECOND_DELAY),
				e -> battleStep(s, Main.SECOND_DELAY));
		battleAnimation.setCycleCount(Timeline.INDEFINITE);
		battleAnimation.getKeyFrames().add(frame);
		battleAnimation.play();
	}

	// keeps track of turns and when enemy attacks
	public void continueGame(Stage s) {
		if ((hpBarNemo.getProgress() != 0) && hpBarEnemy.getProgress() != 0) {
			if (nextPlayer) {
				nemoMove(s); 
			}

			if (!nextPlayer) {
				if (ENEMY_DELAY >= 60) {
					enemyMove(s);
					ENEMY_DELAY = 0;	
				} 
				ENEMY_DELAY++;
			}
		}
	}

	// if HP bar = 0, show game over scene
	public void checkForLoseGame(Stage s) {
		if (hpBarNemo.getProgress() < .01) {
			resetText();
			Text defeatText = new Text(10, 10, "You are defeated!");
			stylePlayerText(defeatText);
			battleGroup.getChildren().add(defeatText);

			if (LOSS_DELAY >= 60) {
				battleAnimation.stop();
				loseGame = new LoseGame();
				s.setScene(loseGame.initEndScreen(s, Main.START_WIDTH, Main.START_HEIGHT));
			}
			LOSS_DELAY++;
		}
	}

	// if enemy HP bar is 0, battle is won and go back to roam scene
	public void checkForWinBattle(Stage s) {
		if (hpBarEnemy.getProgress() < .01) {
			resetText();
			Text victoryText = new Text(10, 10, "You defeated the enemy!");
			stylePlayerText(victoryText);
			battleGroup.getChildren().add(victoryText);
			if (WIN_DELAY >= 60) {
				s.setScene(roamGame.initRoamScreen(s, Main.WIDTH, Main.HEIGHT, stagesCleared));
				battleAnimation.stop();
			}
			WIN_DELAY++;
		}
	}

	public void stylePlayerText(Text text) {
		text.setFont(Font.font ("Arial", 20));
		text.setFill(Color.WHITE);
		text.setTranslateX((Main.WIDTH)/12);
		text.setTranslateY((Main.HEIGHT)/5);
	}

	public void styleEnemyText(Text text) {
		text.setFont(Font.font ("Arial", 20));
		text.setFill(Color.WHITE);
		text.setTranslateX((Main.WIDTH) - (Main.WIDTH)/2);
		text.setTranslateY((Main.HEIGHT)/5);
	}

	public void styleButton (Button bt) {
		bt.setStyle("-fx-font: bold 12 arial; -fx-base: #ff6600; "
				+ "-fx-text-fill: white; -fx-background-radius: 6em;");
	}

	public void setButtonLocation(Button bt, int x, int y) {
		bt.setLayoutX(x);
		bt.setLayoutY(y);
	}

	public void setBarLocation(ProgressBar pb, double x, int y) {
		pb.setLayoutX(x);
		pb.setLayoutY(y);
	}

	// updates HP bar so HP increases
	public void gainHealth(ProgressBar pb, double change) {
		pb.setProgress(pb.getProgress() + change);
	}

	// updates HP bar so HP decreases
	public void loseHealth(ProgressBar pb, double change) {
		pb.setProgress(pb.getProgress() - change);
	}

	public void setEnemyLocation(ImageView enemy, double width, double height) {
		enemy.setX((width/2) + width/6);
		enemy.setY(height/2);
		enemy.setScaleX(-1);
	}

	// check if flee is successful-- if so, show success text and go back to roam scene
	public void checkForFlee(Stage s) {
		if (canFlee) {
			resetText();
			Text fleeText = new Text(10, 10, "You fled!");
			stylePlayerText(fleeText);
			battleGroup.getChildren().add(fleeText);
			if (FLEE_DELAY >= 60) {
				s.setScene(roamGame.initRoamScreen(s, Main.WIDTH, Main.HEIGHT, stagesCleared));
				battleAnimation.stop();
			}
			FLEE_DELAY++;
		}
	}

	// press W during battle to win, L to lose
	public void cheatBattleKey(KeyCode code) {
		switch (code) {
		case W:
			hpBarEnemy.setProgress(0);
			break;
		case L:
			hpBarNemo.setProgress(0);
			break;
		default:
			break;
		}
	}
}
