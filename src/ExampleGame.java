import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


/**
 * Separate the game code from some of the boilerplate code.
 * 
 * @author Robert C. Duvall
 */
class ExampleGame {
    public static final String TITLE = "Example JavaFX";
    public static final int KEY_INPUT_SPEED = 5;
    private static final double GROWTH_RATE = 1.1;
    private int BOUNCER_SPEED = 30;

    private Group myRoot;
    private Scene myScene;
    private ImageView myBouncer;
    private Rectangle myTopBlock;
    private Rectangle myBottomBlock;


    /**
     * Returns name of the game.
     */
    public String getTitle () {
        return TITLE;
    }

    /**
     * Create the game's scene
     */
    
    // init sets up something that you only want to do once
    	// i.e. if you want to initialize a banner that stays for the whole game
    public Scene init (int width, int height) {
        // Create a scene graph to organize the scene
        Group myRoot = new Group();
        // Create a place to see the shapes
        myScene = new Scene(myRoot, width, height, Color.WHITE);
        // Make some shapes and set their properties
        // below opens up a file with the image
        // MAKE SURE THE IMAGE IS IN CONTAINED IN A FOLDER IN THE PROJECT
        Image image = new Image(getClass().getClassLoader().getResourceAsStream("duke.gif"));
        myBouncer = new ImageView(image);
        // x and y represent the top left corner, so center it
        myBouncer.setX(width / 2 - myBouncer.getBoundsInLocal().getWidth() / 2);
        myBouncer.setY(height / 2  - myBouncer.getBoundsInLocal().getHeight() / 2);
        myTopBlock = new Rectangle(width / 2 - 25, height / 2 - 100, 50, 50);
        myTopBlock.setFill(Color.RED);
        myBottomBlock = new Rectangle(width / 2 - 25, height / 2 + 50, 50, 50);
        myBottomBlock.setFill(Color.BISQUE);
        // order added to the group is the order in whuch they are drawn
        // since the bouncer is added first, the other images overlap over it
        myRoot.getChildren().add(myBouncer);
        myRoot.getChildren().add(myTopBlock);
        myRoot.getChildren().add(myBottomBlock);
        // Respond to input
        // Whenever e is done, do whatever it arrows to
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode())); // e = key pressed
        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY())); // e = mouse click
        return myScene;
    }

    /**
     * Change properties of shapes to animate them
     * 
     * Note, there are more sophisticated ways to animate shapes,
     * but these simple ways work too.
     */
    public void step (double elapsedTime) {
        // update attributes
        myBouncer.setX(myBouncer.getX() + BOUNCER_SPEED);
        myTopBlock.setRotate(myBottomBlock.getRotate() - 1);
        myBottomBlock.setRotate(myBottomBlock.getRotate() + 1);
        
        // bounce duke.gif
        if (myBouncer.getX() > 300 || myBouncer.getX() < 50) {
        	BOUNCER_SPEED = BOUNCER_SPEED*-1;
        }
        
        // check for collisions
        // with shapes, can check precisely
        // CAN CHECK FOR INTERSECTIONS, ONLY WORKS FOR SHAPES
        // Shape intersect = smallest rectangle that can fit, checks for intersection
        // -1 means that there is no box (no width)
        Shape intersect = Shape.intersect(myTopBlock, myBottomBlock);
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            myTopBlock.setFill(Color.MAROON);
        }
        else {
            myTopBlock.setFill(Color.RED);
        }
        // with images can only check bounding box
        // USE THIS FOR CHECKING INTERSECTIONS WITH IMAGES (not shapes)
        // getBoundsInParent() checks for bounds of object in relation to other objects
        if (myBottomBlock.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
            myBottomBlock.setFill(Color.BURLYWOOD);
        }
        else {
            myBottomBlock.setFill(Color.BISQUE);
        }
    }


    // What to do each time a key is pressed
    // make sure to include breaks or the program will go through every direction
    private void handleKeyInput (KeyCode code) {
    	// if code == RIGHT, do case RIGHT
        switch (code) {
            case RIGHT:
                myTopBlock.setX(myTopBlock.getX() + KEY_INPUT_SPEED);
                break;
            case LEFT:
                myTopBlock.setX(myTopBlock.getX() - KEY_INPUT_SPEED);
                break;
            case UP:
                myTopBlock.setY(myTopBlock.getY() - KEY_INPUT_SPEED);
                break;
            case DOWN:
                myTopBlock.setY(myTopBlock.getY() + KEY_INPUT_SPEED);
                break;
            default:
                // do nothing
        }
    }

    // What to do each time a key is pressed
    private void handleMouseInput (double x, double y) {
        if (myBottomBlock.contains(x, y)) {
            myBottomBlock.setScaleX(myBottomBlock.getScaleX() * GROWTH_RATE);
            myBottomBlock.setScaleY(myBottomBlock.getScaleY() * GROWTH_RATE);
        }
    }
}
