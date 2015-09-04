import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * WinGame class is responsible for the victory scene. This scene shows "You Win!" and 
 * is initiated after the player intersects the fish home in the third stage of the
 * roam scene.
 */

public class WinGame {
	
	public Scene victoryScene;
	public ImageView bg, text;

	// return victory scene with background and words "You win!"
	public Scene initWinScreen(Stage s, int width, int height) {

		Group winGroup;
		winGroup = new Group();
		victoryScene = new Scene(winGroup, width, height);
		Image background = new Image(getClass().getClassLoader().getResourceAsStream("start.jpg"));
		Image victory = new Image(getClass().getClassLoader().getResourceAsStream("victory.png"));
		bg = new ImageView(background);
		text = new ImageView(victory);
		winGroup.getChildren().add(bg);
		winGroup.getChildren().add(text);
		text.setX(width/2);
		text.setY(height/2);

		return victoryScene;
	}
}
