import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * StartGame class is responsible for the opening scene with the game title and start
 * button. Clicking on the start button initiates the roam scene and begins the game.
 */

class StartGame {
	
	public static final String TITLE = "Under the Sea";
	public Scene startScene;
	public ImageView startScreen, title, startButton;
	public RoamGame roamGame;

	public String getTitle () {
		return TITLE;
	}

	// returns splash screen with game title, background and start button
	public Scene initStartScreen(Stage s, int width, int height) {

		Group startGroup = new Group();
		startScene = new Scene(startGroup, width, height);

		Image startBg = new Image(getClass().getClassLoader().getResourceAsStream("start.jpg"));
		Image titleWords = new Image(getClass().getClassLoader().getResourceAsStream("title.png"));
		Image pressStart = new Image(getClass().getClassLoader().getResourceAsStream("startbutton.png"));
		startScreen = new ImageView(startBg);
		startButton = new ImageView(pressStart);
		title = new ImageView(titleWords);

		title.setX(240);
		title.setY(200);
		startButton.setX(340);
		startButton.setY(380);;

		startGroup.getChildren().add(startScreen);
		startGroup.getChildren().add(title);
		startGroup.getChildren().add(startButton);

		return startScene;
	}

	// if start button clicked, go to roam scene
	public void pressStart (Stage s) {
		startButton.setOnMouseClicked(e -> handleMouseInput(s));
	}

	public void handleMouseInput (Stage s) {
		roamGame = new RoamGame();
		s.setScene(roamGame.initRoamScreen(s, Main.WIDTH, Main.HEIGHT, 1));
	}
}