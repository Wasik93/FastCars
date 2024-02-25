package wasik.fastcars;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class AndroidLauncher extends AndroidApplication{
	private FastCars game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGyroscope = true;
		DatabaseImplementation databaseImplementation = new DatabaseImplementation(this);
		game = new FastCars(databaseImplementation);
		initialize(game, config);
	}

	public FastCars getGame() {
		return game;
	}

}
