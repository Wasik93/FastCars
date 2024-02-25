package wasik.fastcars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class FastCars extends Game {

	private final DatabaseInterface databaseInterface;

	private Integer steering;

	Music menuMusic;

	FastCars(DatabaseInterface databaseInterface){
		this.databaseInterface = databaseInterface;
	}


	@Override
	public void create () {
		this.setScreen(new MainMenuScreen(this));
		steering = 0;

		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("song.wav"));
		menuMusic.setLooping(true);
		menuMusic.play();
	}

	@Override
	public void render() {
		super.render();
	}

	public void setSteering(Integer steering) {
		this.steering = steering;
	}

	public Integer getSteering() {
		return steering;
	}

	public DatabaseInterface getDatabase(){
		return databaseInterface;
	}


	public void playMusic(){
		menuMusic.play();
	}

	public void stopMusic(){
		menuMusic.stop();
	}

	public Music getMenuMusic(){
		return menuMusic;
	}

}
