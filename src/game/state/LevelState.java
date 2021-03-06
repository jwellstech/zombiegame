package game.state;

import game.Game;
import game.character.Player;
import game.character.Zombie;
import game.controller.MouseAndKeyBoardPlayerController;
import game.controller.PlayerController;
import game.controller.ZombieController;
import game.enums.Facing;
import game.level.Level;
import game.level.object.AmmoPickup;
import game.physics.Physics;

import game.settings.SettingsGame;
import game.weapons.Bullet;
import game.weapons.Gun;
import game.weapons.Pistol;
import game.weapons.Rifle;
import javafx.util.Pair;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Music;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.awt.Point;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LevelState extends BasicGameState {

    public StateBasedGame   game;
    public  static  Level   level;
    public  static  String  startinglevel;
    public  static  int     containerHeight;
    public  static  int     containerWidth;
    private         Physics physics = new Physics();

    private Font font;
    private TrueTypeFont ttf;
    public  static  List<String> spriteList = new ArrayList<>();
    public  static  List<Pair<String, HashMap<Facing,Image>>> spritesMaps = new ArrayList<>();
    public  static  List<String> animationList = new ArrayList<>();
    public  static  List<Pair<String, HashMap<Facing,Animation>>> animationMaps = new ArrayList<>();

    public  static Player    player;
    public  static Gun       playerGun;
    public  static Gun[]     playerGuns = new Gun[2];
    private PlayerController playerController;

    public  static List<Zombie>             zombies = new ArrayList<>();
    public  static List<ZombieController>   zombieControllers = new ArrayList<>();
    public  static List<Bullet>             bullets = new ArrayList<>();


    public  static boolean spawnNew = false;
    public  static boolean attackMe = true;
    public  static boolean paused   = false;
    public  static boolean gameOver = false;

    public  static int killCount                = SettingsGame.killCount;
    public  static int zombiesSpawned           = SettingsGame.zombiesSpawned;
    public  static int gunShootTime             = SettingsGame.gunShootTime;
    public  static int gamePlayTime             = SettingsGame.gamePlayTime;
    public  static int currentWave              = SettingsGame.startingWave;
    private static int zombiesBeforeThisWave    = SettingsGame.zombiesBeforeThisWave;
    public  static int zombieSpawnDelay         = SettingsGame.zombieSpawnDelay;
    private static int zombiesSpawnedThisWave   = SettingsGame.zombiesSpawnedThisWave;
    public  static int zombieWaveTimer          = SettingsGame.zombieWaveTimer;
    private static int zombieSpawnTimer         = SettingsGame.zombieWaveTimer;

    public  static int dropTimer                = SettingsGame.dropTimer;
    public  static int numberOfDrops            = SettingsGame.numberOfDrops;
    public  static boolean menuChange           = false;
    public  static int lastMenu;
    public  static boolean levelSelected = false;
    public  static ArrayList<String> levels = new ArrayList<>();

    public  static Music music;
    public  static Music openingMenuMusic;
    public  static Music gameOverMusic;
    public  static Sound zombieAlarm;
    public  static Sound[] zombieHurt = new Sound[6];
    public  static Sound[] zombieDeath = new Sound[5];
    public  static Sound[] playerHurt = new Sound[7];


    public LevelState(){
        levels.clear();
        levels.add("level_0");
        levels.add("ncssm");
    }

    public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
            game = sbg;
            font = new Font("Verdana", Font.BOLD, 10);
            ttf = new TrueTypeFont(font, true);
            //at the start of the game we don't have a player yet
            Pistol pistol = new Pistol();
            Rifle rifle = new Rifle();
            playerGuns[0] = pistol;
            playerGuns[1] = rifle;
            playerGun = playerGuns[0];
            player = new Player(SettingsGame.playerX, SettingsGame.playerY);

            //gets size of the screen
            containerHeight = container.getHeight();
            containerWidth = container.getWidth();

            //once we initialize our level, we want to load the right level
        if (levelSelected) {
            startLevel(startinglevel);
//            level = new Level(startinglevel, player);
        }

            //and we create a controller, for now we use the MouseAndKeyBoardPlayerController
            playerController = new MouseAndKeyBoardPlayerController(player);

            //this sets all of our music and sounds
            zombieAlarm = new Sound("data/audio/sounds/alerts/missile_alarm.ogg");
            zombieHurt = new Sound[]{
                    new Sound("data/audio/sounds/zombies/zombie-3.ogg"),
                    new Sound("data/audio/sounds/zombies/zombie-5.ogg"),
                    new Sound("data/audio/sounds/zombies/zombie-6.ogg"),
                    new Sound("data/audio/sounds/zombies/zombie-7.ogg"),
                    new Sound("data/audio/sounds/zombies/zombie-10.ogg"),
                    new Sound("data/audio/sounds/zombies/zombie-12.ogg"),};
            zombieDeath = new Sound[]{
                    new Sound("data/audio/sounds/zombies/zombie-4.ogg"),
                    new Sound("data/audio/sounds/zombies/zombie-9.ogg"),
                    new Sound("data/audio/sounds/zombies/zombie-10.ogg"),
                    new Sound("data/audio/sounds/zombies/zombie-16.ogg"),
                    new Sound("data/audio/sounds/zombies/zombie-20.ogg"),};
            playerHurt = new Sound[]{
                    new Sound("data/audio/sounds/player/mrk_breathing_hurt10.ogg"),
                    new Sound("data/audio/sounds/player/mrk_breathing_hurt11.ogg"),
                    new Sound("data/audio/sounds/player/mrk_breathing_hurt12.ogg"),
                    new Sound("data/audio/sounds/player/mrk_breathing_hurt13.ogg"),
                    new Sound("data/audio/sounds/player/mrk_breathing_hurt14.ogg"),
                    new Sound("data/audio/sounds/player/mrk_breathing_hurt15.ogg"),
                    new Sound("data/audio/sounds/player/mrk_breathing_hurt16.ogg"),};

            openingMenuMusic = new Music("data/audio/music/menu_theme_by_dubwolfer.ogg");
            gameOverMusic = new Music("data/audio/music/game_over_theme_by_dubwolfer.ogg");
            music = openingMenuMusic;
            music.setVolume(SettingsGame.musicVolume);
            music.loop();
    }

    public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {

        //Pause the music if the alarm is playing and don't play before the first wave
        if (zombieAlarm.playing() || gamePlayTime < SettingsGame.delayBeforeFirstWave) {
            music.pause();
        } else if (!music.playing()) {
            zombieAlarm.stop();
            music.resume();
        }

        //every update we have to handle the input from the player
        playerController.handleInput(container.getInput(), delta);

        //only do these things when we're playing
        if (!paused) {
            if (!gameOver) {
                //Pause timers when we pause the game or die
                gamePlayTime += delta;
                gunShootTime -= delta;
                zombieSpawnTimer -= delta;
                player.hurtTimer -= delta;
                dropTimer -= delta;
                zombieWave(delta);

                //periodically adds bullets to your gun to prevent fully running out of ammo
                playerGun.restore();

                //press P to spawn zombies
                if (spawnNew) {
                    spawnZombie();
                }
            }

            //handle our physics
            physics.handlePhysics(level, delta);

            //this checks to see if bullets hit things
            //and removes them if they're "dead"
            bulletCheck();

        }


        //this gets zombies off your case
        for (ZombieController controller: zombieControllers) {
            if (attackMe) {
                controller.handleWalk(player, level, delta);
            }
            else {
                controller.handleWalk(zombies.get(zombies.size() - 1), level, delta);
            }
        }

    }

    public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {

        g.scale(Game.SCALE, Game.SCALE);

        //render the level
        level.render();

        //displays our dev stuff onscreen
        g.setFont(ttf);
        if(SettingsGame.devSettings) {
            g.drawString("Health: " + player.getHealth(), 5, 15);
            g.drawString("Kills: " + killCount, 5, 27);
            g.drawString("Zombies remaining: " + (zombiesSpawned - killCount), 5, 39);
            g.drawString("Zombies spawn: " + spawnNew, 5, 51);
            g.drawString("Attack me: " + attackMe, 5, 63);
            g.drawString("Ammo: " + playerGun.getCurrentAmmo(), 5, 75);
            g.drawString("Clip: " + playerGun.getClip(), 5, 87);
            g.drawString("Wave: " + currentWave, 5, 99);
        }

        //game over screen
        if (gameOver) {
            g.drawString("GAME OVER", containerWidth / 6, 20);
            g.drawString("Restart?", containerWidth / 6 + 10, 100);
        }

        //next wave screen
        if (zombieAlarm.playing() && zombieWaveTimer > 2000 && zombieWaveTimer < SettingsGame.zombieWaveAlarm) {
            if (zombiesSpawned > 0) {
                g.drawString("Wave " + (currentWave + 1), containerWidth/6 + 20, 100);
            }
            else {
                g.drawString("Wave " + currentWave, containerWidth/6 + 20, 100);
            }
        }
        //KEEP THESE IN ORDER. HEALTH BAR COLORING DEPENDS ON IT
        player.getHealthBar().draw(g);
        player.getAmmoBar().draw(g);
        //COLOR IS WHITE BEYOND HERE
    }

    public static void startLevel(String startinglevel) throws SlickException {
        if (levelSelected) {
            level = new Level(startinglevel, player);
        }
    }
    //this method is overriden from basicgamestate and will trigger once you press any key on your keyboard
    public void keyPressed(int key, char code){
        //if the key is escape, close our application
        if(key == Input.KEY_ESCAPE){
            //System.exit(0);
            game.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));


        }
    }

    public int getID() {
        //this is the id for changing states
        return 1;
    }

    public static Point getMousePos() {
        return new Point(Mouse.getX(), Mouse.getY());
    }

    public static void spawnZombie() {
        Random rand = new Random();
        try {
            int x;
            int y;
            //spawn zombie randomly
            do {
                x = rand.nextInt(LevelState.containerWidth);
            } while (x > player.offsetx - containerWidth/2 && x < player.offsetx + containerWidth/2);
            do {
                y = rand.nextInt(LevelState.containerHeight);
            } while (y > player.offsety - containerHeight/2 && y < player.offsety + containerHeight/2);
            zombies.add(new Zombie(x, y));

            //if our zombie hits something, kill it and try again
            if (Physics.checkCollision(zombies.get(zombies.size() - 1), level.getTiles())
                    && Physics.checkTerrainCollision(zombies.get(zombies.size() - 1), level.getTiles()).equals("false")) {
                zombies.remove(zombies.size() - 1);
            } else {
                zombiesSpawned++;
                LevelState.level.addCharacter(zombies.get(zombies.size() - 1));
                zombieControllers.add(new ZombieController(zombies.get(zombies.size() - 1)));
            }
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    private void zombieWave(int delta) {

        //if we have more to spawn this wave, let's do it
        if (zombiesSpawnedThisWave < (currentWave * SettingsGame.zombiesPerWave - 1) && zombieSpawnTimer <= 0) {

            //get the number of zombies spawned this wave and then spawn one
            zombiesSpawnedThisWave = zombiesSpawned - zombiesBeforeThisWave;
            spawnZombie();

            //we need to reset these timers
            zombieSpawnTimer = zombieSpawnDelay;
            zombieWaveTimer = SettingsGame.zombieWaveDelay;
        }

        //if we're out of zombies to kill, prepare for next wave
        else if (zombiesSpawned - killCount == 0) {
            zombieWaveTimer -= delta;

            //this plays our alarm before a wave starts
            if (zombieWaveTimer <= SettingsGame.zombieWaveAlarm && !zombieAlarm.playing()) { //&& !waveStarted) {
                zombieAlarm.play(1, SettingsGame.zombieAlarmVolume);
            }

            //start new wave
            if (zombieWaveTimer <= 0) {
                currentWave++;
                zombiesSpawnedThisWave = 0;
                zombiesBeforeThisWave = zombiesSpawned;
                if (zombieSpawnDelay >= SettingsGame.minimumZombieSpawnDelay) {
                    zombieSpawnDelay -= SettingsGame.zombieSpawnDelayDecrement;
                }
                zombieWaveTimer = SettingsGame.zombieWaveDelay;
            }
        }

    }

    //checks if bullets hit things
    private void bulletCheck() throws SlickException {
        //take care of our bullets
        int k = 0;
        List<Integer> gone = new ArrayList<>();
        for (Bullet bullet: bullets) {

            //see if our bullets hit a zombie, and if so, hurt them
            if (bullet.getX() > 0 && bullet.getY() > 0 && bullet.getX() < (level.getTiles().length-1)*16 && bullet.getY() < (level.getTiles().length-1)*16) {
                Physics.checkBulletCollision(bullet, level.getTiles());
            }
            else {
                bullet.setHealth(0);
            }
            for (Zombie zombie: zombies) {
                if (bullet.getBoundingShape().checkCollision(zombie.getBoundingShape())) {
                    zombie.damage(playerGun.getDamage());
                    bullet.damage(1);
                }
            }

            //kill the bullet if it's dead
            if (bullet.getHealth() < 1) {
                if (bullets.size() > k ) {
                    gone.add(k);
                }
                bullet.kill();
            }
            k++;

        }

        //remove bullets that hit stuff
        for (Integer i: gone) {
            if (bullets.size() > i) {
                bullets.set(i, bullets.get(bullets.size() - 1));
                bullets.remove(bullets.size() - 1);
            }
        }

    }

    //restarts the game (@game over)
    public static void restart() {

        for (Zombie zombie: zombies) {
            zombie.setHealth(0);
        }
        zombieControllers.clear();
        zombies.clear();
        bullets.clear();
        level.removeObjects(level.getLevelObjects());
        zombieAlarm.stop();

        paused                      = false;
        gameOver                    = false;
        spawnNew                    = false;
        attackMe                    = true;

        killCount                   = SettingsGame.killCount;
        zombiesSpawned              = SettingsGame.zombiesSpawned;
        gunShootTime                = SettingsGame.gunShootTime;
        gamePlayTime                = SettingsGame.gamePlayTime;
        currentWave                 = SettingsGame.currentWave;
        zombiesBeforeThisWave       = SettingsGame.zombiesBeforeThisWave;
        zombieSpawnDelay            = SettingsGame.zombieSpawnDelay;
        zombiesSpawnedThisWave      = SettingsGame.zombiesSpawnedThisWave;
        zombieWaveTimer             = SettingsGame.zombieWaveTimer;
        zombieSpawnTimer            = SettingsGame.zombieSpawnTimer;

        player.reset();
        player.setX(SettingsGame.playerX);
        player.setY(SettingsGame.playerY);

        Pistol pistol               = new Pistol();
        Rifle  rifle                = new Rifle();
        playerGuns[0]               = pistol;
        playerGuns[1]               = rifle;
        playerGun                   = playerGuns[0];

        music                       = openingMenuMusic;
        music.loop();
        player.healthBar.reset();
    }
}