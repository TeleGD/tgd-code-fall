package games.codeFall;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import app.AppFont;
import app.AppGame;
import app.AppInput;
import app.AppLoader;
import app.AppWorld;

public class World extends AppWorld {

	public final static String DIRECTORY_SOUNDS="/sounds/codeFall/";
	public final static String DIRECTORY_MUSICS="/musics/codeFall/";
	public final static String DIRECTORY_IMAGES="/images/codeFall/";
	public final static TrueTypeFont Font = AppLoader.loadFont("/fonts/vt323.ttf", AppFont.BOLD, 20);

	public static Image fondPlateforme;
	public static Image fond;
	public static Image[] casse = new Image[4];
	public static Image ammo;

	Platform platform;
	int startX;
	int startY;
	int taille;
	private ArrayList<Player> players;
	private ArrayList<Player> morts;
	ArrayList<Projectile> projectiles;

	static {
		fondPlateforme = AppLoader.loadPicture(DIRECTORY_IMAGES+"plateforme.png");
		fond = AppLoader.loadPicture(DIRECTORY_IMAGES+"fond.png");
		casse[0] = AppLoader.loadPicture(DIRECTORY_IMAGES+"destroy1.png");
		casse[1] = AppLoader.loadPicture(DIRECTORY_IMAGES+"destroy2.png");
		casse[2] = AppLoader.loadPicture(DIRECTORY_IMAGES+"destroy3.png");
		casse[3] = AppLoader.loadPicture(DIRECTORY_IMAGES+"destroy4.png");
		ammo = AppLoader.loadPicture(DIRECTORY_IMAGES+"c.png");
	}

	public World (int ID) {
		super (ID);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		context.drawImage(fond,startX,startY,startX+taille,startY+taille,0,0,fond.getWidth(),fond.getHeight());
		platform.render(container, game, context);
		for (Player p : players) {
			p.render(container, game, context);
		}
		for (Projectile p : projectiles) {
			p.render(container, game, context);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		super.update (container, game, delta);
		platform.update(container, game, delta);
		for (int i=players.size()-1; i>=0 ; i--) {
			players.get(i).update(container, game, delta);
			if (players.get(i).isDead()) {
				morts.add(players.get(i));
				players.remove(i);
			}
		}
		AppInput appInput = (AppInput) container.getInput();
		appInput.clearControlPressedRecord();
		for (int i=projectiles.size()-1; i>=0 ; i--) {
			projectiles.get(i).update(container, game, delta);
		}
		if (players.size()==0) {
			end();
		}
	}

	public void end() {
		HashMap<Integer,Integer> classement = new HashMap<Integer,Integer>();
		for (int i=0; i<morts.size(); i++) {
			classement.put(morts.get(i).getControllerID(), i);
		}
		System.out.println(classement.toString());
	}

	@Override
	public void play (GameContainer container, StateBasedGame game) {
		int width = container.getWidth();
		int height = container.getHeight();
		taille = (width>height)?height:width;
		startX = (width>height)?width/2-height/2:0;
		startY = (width<height)?height/2-width/2:0;
		int platformSize = 12;
		platform = new Platform(this, platformSize);
		platformSize=platform.getSize();

		AppGame appGame = (AppGame) game;
		this.players = new ArrayList<Player>();
		this.morts = new ArrayList<Player>();
		for (int i = 0; i < appGame.appPlayers.size(); i++) {
			int pStartX = 0;
			int pStartY = 0;
			int direction = 0;
			switch (i) {
			case 0:
				pStartX = 1;
				pStartY = platformSize/2;
				direction=3;
				break;
			case 1:
				pStartX = platformSize-2;
				pStartY = platformSize/2;
				direction=1;
				break;
			case 2:
				pStartX = platformSize/2;
				pStartY = 1;
				direction=2;
				break;
			case 3:
				pStartX = platformSize/2;
				pStartY = platformSize-2;
				direction=0;
				break;
			default:
				break;
			}
			this.players.add(new Player(this, pStartX, pStartY, direction, appGame.appPlayers.get(i)));
		}

		projectiles = new ArrayList<Projectile>();
	}

}
