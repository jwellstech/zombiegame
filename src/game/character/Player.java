package game.character;

import game.Game;
import game.enums.Facing;
import game.physics.AABoundingRect;
import game.settings.SettingsGame;
import game.state.LevelState;
import game.weapons.AmmoBar;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.util.Random;

public class Player extends Character {

    public static double rotate;
    public static boolean invincible = false;
//    protected static int maxHealth;
    public HealthBar healthBar;
    public static AmmoBar ammoBar;

    public static int hurtTimer = 800;

    public Player(float x, float y) throws SlickException {
        super(x, y);
        setSprite("data/images/player/player");
        setMovingAnimation("data/images/player/player", 3, 100);

        boundingShape = new AABoundingRect(x+6, y+21, 18, 10);

        originalMaxSpeed = 0.15f;
        originalDiagonalSpeed = 0.1f;
        maximumSpeed = 0.15f;
        diagonalSpeed = 0.1f;
        maxHealth = 1000;
        health = maxHealth;
        healthBar = new HealthBar(20, Game.WINDOW_HEIGTH/Game.SCALE-12);
        ammoBar = new AmmoBar(Game.WINDOW_WIDTH/Game.SCALE - 20, Game.WINDOW_HEIGTH/Game.SCALE-12);
    }

    public void updateBoundingShape(){
        boundingShape.updatePosition(x+6,y+21);
    }

    @Override
    public int getTimer() {
        return 0;
    }

    @Override
    public void setTimer(int delta) { }

    //TODO
    public void setMouseQuadrant()
    {
        double mouseX = LevelState.getMousePos().getX()/Game.SCALE;
        double mouseY = LevelState.getMousePos().getY()/Game.SCALE;

        double xPos = mouseX + offsetx - x - 14;
        double yPos = (LevelState.containerHeight/Game.SCALE - mouseY) + offsety - y - 24;

        double line1 = (0.5625)*xPos;//*Game.SCALE;
        double line2 = (-0.5625)*xPos+yPos/Game.SCALE;//+720;//*Game.SCALE + LevelState.containerHeight;

        if(yPos >= line1 && yPos > line2) {
            facing = Facing.DOWN;
        } else if(yPos <= line1 && yPos < line2) {
            facing = Facing.UP;
        } else if(yPos < line1 && yPos >= line2) {
            facing = Facing.RIGHT;
        } else {
            facing = Facing.LEFT;
        }
    }

    public void damage(int damage) {
        if (!invincible) {
            playSound(LevelState.playerHurt);
            health -= damage;
            updateHealthBar();
        }

    }

//    public float getMaxHealth() {
//        return maxHealth;
//    }

    public void updateHealthBar() {
        healthBar.setHealthBar(this);
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public AmmoBar getAmmoBar() {
        return ammoBar;
    }

    //this is used for new game
    public void reset() {
        invincible = false;
        originalMaxSpeed = 0.15f;
        originalDiagonalSpeed = 0.1f;
        maximumSpeed = 0.15f;
        diagonalSpeed = 0.1f;
        health = 1000;
        healthBar.reset();
    }

    public void playSound(Sound[] soundThing) {
        Random rand = new Random();
        boolean isPlaying = false;
        for (Sound sound: soundThing) {
            if (sound.playing()) {
                isPlaying = true;
            }
        }
        if (!isPlaying) {
            if (hurtTimer <= 0) {
                soundThing[rand.nextInt(soundThing.length)].play(1, SettingsGame.playerVolume);
                hurtTimer = 800;
            }
        }
    }

}