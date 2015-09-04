
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import java.util.*;

/**
 * RoamGame is responsible for controlling the mechanics of the scene at which the player 
 * moves around in relation to the other NPC fish. The game enters this scene after the
 * START button is clicked, or after a battle is won. It allows movement of the fish
 * up, down, left, or right depending on the corresponding keys. The game keeps track
 * of the number of stages previously cleared, and the last (third) stage contains
 * the fish's home. 
 * 
 * Intersection with the NPC fish triggers battle scene. Intersection with the fish home
 * triggers the win scene.
 */

public class RoamGame {

	private WinGame winGame;
	public final int KEY_INPUT_SPEED = 15;
	private int BLUEFISH_SPEED = 40;
	private int YELLOWFISH_SPEED = 80;
	private Scene roamScene;
	private BattleGame battleGame = new BattleGame();
	private ImageView myNemo, yellowFish, blueFish, fishHome, arrow;
	private ImageView mySea;
	private int randomIntXblue, randomIntYblue, randomIntXyellow, randomIntYyellow;
	private int stagesCleared;
	private Group roamGroup;
	private Timeline roamAnimation;

	
	// sets up and returns roam scene with background, player, NPC fish (2) with random 
	// locations, and fish home if on the third stage of roam
	public Scene initRoamScreen (Stage s, int width, int height, int myStagesCleared) {

		startRoamLoop(s);

		stagesCleared = myStagesCleared;

		roamGroup = new Group();
		roamScene = new Scene(roamGroup, width, height);

		myNemo = new ImageView();
		blueFish = new ImageView();
		yellowFish = new ImageView();
		mySea = new ImageView();

		// set images to corresponding variables
		mySea = getImage("background.png");
		blueFish = getImage("fish1.png");
		yellowFish = getImage("fish2.png");
		myNemo = getImage("nemo.png");

		roamGroup.getChildren().add(mySea);
		roamGroup.getChildren().add(blueFish);
		roamGroup.getChildren().add(yellowFish);
		roamGroup.getChildren().add(myNemo);

		if (stagesCleared < 3) {
			arrow = new ImageView();
			arrow = getImage("arrow.png");
			roamGroup.getChildren().add(arrow);
			setLocation(arrow, Main.WIDTH - 50, Main.HEIGHT - 40);
		}

		if (stagesCleared >= 3) {
			fishHome = new ImageView();
			fishHome = getImage("coral.gif");
			roamGroup.getChildren().add(fishHome);
			setLocation(fishHome, Main.WIDTH - 100, Main.HEIGHT - 100);
		}

		Random random = new Random();
		randomIntXblue = random.nextInt(width/4)+200;
		randomIntYblue = random.nextInt(height/2);
		randomIntXyellow = random.nextInt(width/4)+(width/4);
		randomIntYyellow = random.nextInt(height/2);

		setLocation(myNemo, 0, height/2);
		setLocation(blueFish, randomIntXblue, randomIntYblue);
		setLocation(yellowFish, randomIntXyellow, randomIntYyellow);

		if (BLUEFISH_SPEED < 0) reflectImage(blueFish, -1);
		if (YELLOWFISH_SPEED < 0) reflectImage(yellowFish, -1);

		roamScene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), s)); 
		return roamScene;
	}

	// check for intersections with home or other fish, controls fish animations
	public void roamStep (Stage s, double elapsedTime) {

		checkIfGameWon(s);
		checkIfEncounterFish(s);

		boolean reachedEndOfStage = myNemo.getX() >= Main.WIDTH - 100;
		if (reachedEndOfStage && stagesCleared != 3) {
			moveToNextStage(s);
		}

		makeFishMove(blueFish, BLUEFISH_SPEED, elapsedTime);
		makeFishMove(yellowFish, YELLOWFISH_SPEED, elapsedTime);

		if (fishNeedsToTurn(blueFish)) {
			BLUEFISH_SPEED = changeDirection(blueFish, BLUEFISH_SPEED);
			reflectImage(blueFish, blueFish.getScaleX()*-1);
		}

		if (fishNeedsToTurn(yellowFish)) {
			YELLOWFISH_SPEED = changeDirection(yellowFish, YELLOWFISH_SPEED);
			reflectImage(yellowFish, yellowFish.getScaleX()*-1);
		}
	}

	// key presses determine player movement and change image attributes
	// press Z to skip to the last stage
	public void handleKeyInput (KeyCode code, Stage s) {
		switch (code) {
		case RIGHT:
			boolean rightOutOfBounds = myNemo.getX() >= Main.WIDTH - 100;
			boolean nemoMustTurnFaceRight = myNemo.getScaleX() < 0;
			if (rightOutOfBounds) break;
			setLocation(myNemo, (int) myNemo.getX() + KEY_INPUT_SPEED, 0);
			if (nemoMustTurnFaceRight) reflectImage(myNemo, myNemo.getScaleX()*-1);
			rotateImage(myNemo, 0);
			break;
		case LEFT:
			boolean leftOutOfBounds = myNemo.getX() <= Main.WIDTH - 580;
			boolean nemoMustTurnFaceLeft = myNemo.getScaleX() > 0;
			if (leftOutOfBounds) break;
			setLocation(myNemo, (int) myNemo.getX() - KEY_INPUT_SPEED, 0);
			if (nemoMustTurnFaceLeft) reflectImage(myNemo, -1);
			rotateImage(myNemo, 0);
			break;
		case UP:
			boolean upOutOfBounds = myNemo.getY() <= Main.HEIGHT - 280;
			boolean nemoMustTurnFaceUp1 = myNemo.getScaleX() > 0;
			boolean nemoMustTurnFaceUp2 = myNemo.getScaleX() < 0;
			if (upOutOfBounds) break;
			setLocation(myNemo, 0, (int) myNemo.getY() - KEY_INPUT_SPEED);
			if (nemoMustTurnFaceUp1) rotateImage(myNemo, -20);
			if (nemoMustTurnFaceUp2) rotateImage(myNemo, 20);
			break;
		case DOWN:
			boolean downOutOfBounds = myNemo.getY() >= Main.HEIGHT - 100;
			boolean nemoMustTurnFaceDown1 = myNemo.getScaleX() > 0;
			boolean nemoMustTurnFaceDown2 = myNemo.getScaleX() < 0;
			if (downOutOfBounds) break;
			setLocation(myNemo, 0, (int) myNemo.getY() + KEY_INPUT_SPEED);
			if (nemoMustTurnFaceDown1) rotateImage(myNemo, 20);
			if (nemoMustTurnFaceDown2) rotateImage(myNemo, -20);
			break;
		case Z:
			stagesCleared = 4;
			moveToNextStage(s);
		default:
			// do nothing
		}
	}

	public void startRoamLoop(Stage s) {
		roamAnimation = new Timeline();
		KeyFrame frame = new KeyFrame(Duration.millis(Main.MILLISECOND_DELAY),
				e -> roamStep(s, Main.SECOND_DELAY));
		roamAnimation.setCycleCount(Timeline.INDEFINITE);
		roamAnimation.getKeyFrames().add(frame);
		roamAnimation.play();
	}

	// if player intersects home, go to victory scene
	public void checkIfGameWon(Stage s) {
		if (stagesCleared >= 3) {
			boolean reachedHome = 
					myNemo.getBoundsInParent().intersects(fishHome.getBoundsInParent());
			if (reachedHome) {
				winGame = new WinGame();
				s.setScene(winGame.initWinScreen(s, Main.START_WIDTH, Main.START_HEIGHT));
				roamAnimation.stop();
			}
		}
	}

	// if player encounters fish, initiate battle scene with corresponding fish
	public void checkIfEncounterFish(Stage s) {
		if (myNemo.getBoundsInParent().intersects(blueFish.getBoundsInParent())) {
			startBattle(s, "BLUE");
		}
		if (myNemo.getBoundsInParent().intersects(yellowFish.getBoundsInParent())) {
			startBattle(s, "YELLOW");
		}
	}

	public void startBattle(Stage s, String enemyFish) {
		battleGame = new BattleGame();
		s.setScene(battleGame.initBattleScreen(s, Main.WIDTH, Main.HEIGHT, enemyFish, stagesCleared));
		roamAnimation.stop();
	}

	// helper method to make picture in images into Image
	public ImageView getImage(String imgLink) {
		Image tempImg = new Image(getClass().getClassLoader().getResourceAsStream(imgLink));
		return new ImageView(tempImg);
	}

	public void setLocation(ImageView img, int x, int y) {
		if (x != 0) img.setX(x);
		if (y != 0) img.setY(y);
	}

	public void rotateImage(ImageView img, int degrees) {
		img.setRotate(degrees);
	}

	public void reflectImage(ImageView img, double direction) {
		img.setScaleX(direction);
	}

	public int changeDirection(ImageView img, int speed) {
		return speed*-1;
	}

	// determines if fish needs to reflect body when moving left/right
	public boolean fishNeedsToTurn(ImageView fish) {
		return (blueFish.getX() > randomIntXblue+100 || blueFish.getX() < randomIntXblue-100);
	}

	public void makeFishMove(ImageView img, int speed, double elapsedTime) {
		img.setX(img.getX() + speed * elapsedTime);
	}

	// if player reaches right end of roam scene, initiate new roam scene (next stage)
	// if on last stage (3), add fish home image 
	public void moveToNextStage(Stage s) {
		if (stagesCleared >= 3) {
			stagesCleared++;
			roamAnimation.stop();
			s.setScene(initRoamScreen(s, Main.WIDTH, Main.HEIGHT, stagesCleared+1));
		}
		if (stagesCleared < 3) {
			roamAnimation.stop();
			s.setScene(initRoamScreen(s, Main.WIDTH, Main.HEIGHT, stagesCleared+1));
		}
	}
}

