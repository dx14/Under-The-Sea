import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * LoseGame class is responsible for the defeat scene, saying "Game Over." This scene is
 * initiated when the player's HP bar drops to 0 in the battle scene.
 */

public class LoseGame {

	private Scene gameOverScene;
	private RoamGame roamGame;
	private ImageView bg, text;

	// returns scene with background and words "Game Over"
	public Scene initEndScreen(Stage s, int width, int height) {

		Group gameOverGroup;
		gameOverGroup = new Group();
		gameOverScene = new Scene(gameOverGroup, width, height);

		roamGame = new RoamGame();
		bg = roamGame.getImage("start.jpg");
		text = roamGame.getImage("gameover.png");

		gameOverGroup.getChildren().add(bg);
		gameOverGroup.getChildren().add(text);

		roamGame.setLocation(text, width/2, height/2);

		return gameOverScene;
	}
}
