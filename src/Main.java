import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Dennis Xu
 */

public class Main extends Application {
	public static final int WIDTH = 600, HEIGHT = 300, START_WIDTH = 574, START_HEIGHT = 480;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private StartGame startGame;

	/**
	 * Set things up at the beginning.
	 */
	@Override
	public void start (Stage s) {

		startGame = new StartGame();
		s.setTitle(startGame.getTitle());

		Scene openingScene = startGame.initStartScreen(s, START_WIDTH, START_HEIGHT);;
		s.setScene(openingScene);
		startGame.pressStart(s);
		s.show();
	}

	/**
	 * Start the program.
	 */
	public static void main (String[] args) {
		launch(args);
	}
}
