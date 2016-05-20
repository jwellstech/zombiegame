package game.state;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.Font;
import java.awt.*;
import java.io.InputStream;

public class SettingsMenuState extends BasicGameState
{
    private Font font;
    private TrueTypeFont ttf;
    private Font smallFont;
    private TrueTypeFont smallTtf;
    private StateBasedGame game;
    /*
    -volume
    ***new levelState
    -controls
    ***new levelState
    -back
    ***return to previous levelState
     */

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException
    {
        this.game = game;
        try
        {
            InputStream inputStream = ResourceLoader.getResourceAsStream("data/fonts/vtks-distress.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            font = font.deriveFont(62f);
            ttf = new TrueTypeFont(font, false);
        }catch(Exception ex){}
        smallFont = new Font("Verdana", Font.BOLD, 18);
        smallTtf = new TrueTypeFont(smallFont, false);
        LevelState.paused = true;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

        //g.setFont(ttf);
//        ttf.drawString(450, 50, "ZombieGame");
        int center = container.getWidth()/2;

        ttf.drawString(center - 190, 50, "ZombieGame");
        //g.setFont(smallTtf);
        if(Mouse.getX() <= center-115 && Mouse.getX() >= center-160 && Mouse.getY() >= 545 && Mouse.getY() <= 565) {
            g.setColor(Color.green);
            smallTtf.drawString(center-180, 150, "> Volume", Color.green);
        }else{
//            smallTtf.drawString(center-180, 150, "> Play", Color.white);
            smallTtf.drawString(center-160, 150, "Volume", Color.white);
        }

        if(Mouse.getX() <= center-120 && Mouse.getX() >= center-160 && Mouse.getY() >= 500 && Mouse.getY() <= 515) {
            g.setColor(Color.green);
            smallTtf.drawString(center-180, 200, "> Controls", Color.green);
        }else {
            smallTtf.drawString(center-160, 200, "Controls", Color.white);
        }

        if(Mouse.getX() <= center-115 && Mouse.getX() >= center-160 && Mouse.getY() >= 455 && Mouse.getY() <= 465) {
            g.setColor(Color.green);
            smallTtf.drawString(center-180, 250, "> Back", Color.green);
        }else {
            smallTtf.drawString(center-160, 250, "Back", Color.white);
        }
    }
    public static Point getMousePos() {
        return new Point(Mouse.getX(), Mouse.getY());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

        int center = container.getWidth()/2;

        if(Mouse.isButtonDown(0))
        {
            //volume
            if(Mouse.getX() <= center-115 && Mouse.getX() >= center-160 && Mouse.getY() >= 545 && Mouse.getY() <= 565) {
                game.enterState(4);
            }
            //controls
            if(Mouse.getX() <= center-120 && Mouse.getX() >= center-160 && Mouse.getY() >= 500 && Mouse.getY() <= 515) {
//                game.enterState(4);
            }
            //back
            if(Mouse.getX() <= center-115 && Mouse.getX() >= center-160 && Mouse.getY() >= 455 && Mouse.getY() <= 465) {
                game.enterState(2);

            }

            // System.out.println(getMousePos());
        }

    }

    @Override
    public int getID()
    {
        return 3;
    }
}
