package game;

import game.settings.SettingsGame;
import game.state.*;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

    //set the window width and then the height according to a aspect ratio
    public static final int     WINDOW_WIDTH  = 1280;
    public static final int     WINDOW_HEIGTH = WINDOW_WIDTH / 16 * 9;
    public static final boolean FULLSCREEN = false;

    //1280x720 is our base, we use 32x32 tiles but we want it to be 40x40 at 1280x720
    //so our base scale is not 1 but 1.25 actually
    public static final float  SCALE         = (float) (2.5*((double)WINDOW_WIDTH/1280));
    public static final String GAME_NAME     = "zombiegame";

    public Game() {
        super(GAME_NAME);
    }

    public void initStatesList(GameContainer gc) throws SlickException {

        //create a level state, this state will do the whole logic and rendering for individual levels
        this.addState(new MainMenuState());
        this.addState(new LevelState());
        this.addState(new PauseMenuState());
        this.addState(new SettingsMenuState());
        this.addState(new VolumeMenuState());
        this.addState(new ControlsMenuState());
//        this.addState(new ExampleMenuState());
        this.addState(new LevelSelectMenuState());
        this.enterState(0);

    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game());
        //set the size of the display to the width and height and fullscreen or not
        app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGTH, FULLSCREEN);
        //this will attempt to create a framerate of approximately 60 frames per second
        app.setTargetFrameRate(SettingsGame.targetFrameRate);

        app.start();
    }

}