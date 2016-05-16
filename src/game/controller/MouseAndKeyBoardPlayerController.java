package game.controller;

import game.character.Player;

import game.character.Zombie;
import game.state.LevelState;
import game.weapons.Bullet;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.Random;

import static game.state.LevelState.zombies;
import static game.state.LevelState.zombieControllers;

public class MouseAndKeyBoardPlayerController extends PlayerController {

    public MouseAndKeyBoardPlayerController(Player player) {
        super(player);
    }

    public void handleInput(Input i, int delta) {
        //handle any input from the keyboard
        handleKeyboardInput(i,delta);
    }

    private void handleKeyboardInput(Input i, int delta){
        //we can both use the WASD or arrow keys to move around, obviously we can't move both left and right simultaneously
        if(i.isKeyDown(Input.KEY_A) || i.isKeyDown(Input.KEY_LEFT)){
            if(i.isKeyDown(Input.KEY_W) || i.isKeyDown(Input.KEY_UP)) {
                player.moveUpLeft(delta);
            } else if (i.isKeyDown(Input.KEY_S) || i.isKeyDown(Input.KEY_DOWN)) {
                player.moveDownLeft(delta);
            } else {
                player.moveLeft(delta);
            }
        }else if(i.isKeyDown(Input.KEY_D) || i.isKeyDown(Input.KEY_RIGHT)){
            if(i.isKeyDown(Input.KEY_W) || i.isKeyDown(Input.KEY_UP)) {
                player.moveUpRight(delta);
            } else if (i.isKeyDown(Input.KEY_S) || i.isKeyDown(Input.KEY_DOWN)) {
                player.moveDownRight(delta);
            } else {
                player.moveRight(delta);
            }
        } else if (i.isKeyDown(Input.KEY_W) || i.isKeyDown(Input.KEY_UP)) {
            player.moveUp(delta);
        } else if (i.isKeyDown(Input.KEY_S) || i.isKeyDown(Input.KEY_DOWN)) {
            player.moveDown(delta);
        }else{
            //we don't move if we don't press left or right, this will have the effect that our player decelerates
            player.setMoving(false);
            player.setXVelocity(0);
            player.setYVelocity(0);
        }

        if(i.isKeyPressed(Input.KEY_Z)) {
            Random rand = new Random();
            try {
                zombies.add(new Zombie(rand.nextInt(LevelState.containerHeight), rand.nextInt(LevelState.containerWidth)));
            } catch (SlickException e) {
                e.printStackTrace();
            }
            LevelState.level.addCharacter(zombies.get(zombies.size()-1));
            zombieControllers.add(new ZombieController(zombies.get(zombies.size()-1)));
            System.out.println("Zombie spawned.");
        }

        if (i.isKeyPressed(Input.KEY_P)) {
            LevelState.spawnNew = !LevelState.spawnNew;
        }

        if (i.isKeyPressed(Input.KEY_O)) {
            LevelState.attackMe = !LevelState.attackMe;
        }

        //zombie killer
//        if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
//            double mx = (player.getX() - ((640-i.getMouseX())/(2.5)));
//            double my = (player.getY() - ((360-i.getMouseY())/(2.5)));
//            for (Zombie zombie: zombies) {
//                if (mx >= zombie.getX() - 0 && mx <= zombie.getX() + 20 && my >= zombie.getY() - 10 && mx <= zombie.getY() + 10) {
//                    zombie.damage(1);
//                    System.out.println("Health: " + zombie.getHealth());
//                }
//            }
//        }
        if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            //make a new bullet at the location of the player
            //point bullet at direction of cursor
            //when zombie gets hit by bullet they take damage
            //bullet disappears when it hits a zombie
            int ccc = 0;
            while ((ccc % 2000) == 0) {
                Bullet bullet = null;
                try {
                    bullet = new Bullet(player.getX(), player.getY());
                } catch (SlickException e) {
                    e.printStackTrace();
                }
                bullet.setYVelocity(-0.1f * delta);
                LevelState.bullets.add(bullet);
                ccc++;
            }
        }
    }

}
