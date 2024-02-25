package wasik.fastcars;

import static wasik.fastcars.Constants.UISKIN;
import static wasik.fastcars.GameConstants.MODE_SOLO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Collections;

public class RaceEndScreen implements Screen {
    private FastCars game;

    private final DatabaseInterface databaseInterface;
    private final Skin skin;

    private final Stage stage;

    private final String map;

    private String nameMap;

    private final Race race;

    private final Integer laps;

    private String raceTime;
    private String bestLapTimeString;


    Texture texture = new Texture("mainpage.png");

    private final SpriteBatch batch;

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();
    RaceEndScreen(FastCars game, Race race){
        this.game = game;
        this.databaseInterface = game.getDatabase();
        this.race = race;
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal(UISKIN));
        this.map = race.getMap();
        this.laps = race.getNumberLaps();
        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);

        long raceTimeM = (race.getRaceEnd() - race.getRaceStart())/60000;
        long raceTimeS = ((race.getRaceEnd() - race.getRaceStart())/1000) % 60;
        long raceTimeMS = ((race.getRaceEnd() - race.getRaceStart())/10) % 100;

        long bestLapTime = Collections.min(race.getLapTimes());

        long lapTimeM = bestLapTime/60000;
        long lapTimeS = (bestLapTime/1000) % 60;
        long lapTimeMS = (bestLapTime)/10 % 100;

        raceTime = raceTimeM + ":" + raceTimeS + ":" + raceTimeMS;
        bestLapTimeString = lapTimeM + ":" + lapTimeS + ":" + lapTimeMS;

        Label raceLabel = new Label("Race Time: " + raceTimeM + ":" + raceTimeS + ":" + raceTimeMS , skin);
        table.add(raceLabel).expandX().top().padTop(50).row();

        Label lapLabel = new Label("Best Lap Time: " + lapTimeM + ":" + lapTimeS + ":" + lapTimeMS, skin);
        table.add(lapLabel).expandX().top().padTop(50).row();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        buttonStyle.font.getData().setScale(4f);

        TextButton popupButton = new TextButton("Save", skin);
        popupButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(map.equals("singapur.tmx")){
                    nameMap = "Singapur";
                }
                else if(map.equals("map1.tmx")){
                    nameMap = "Map1";
                }
                else if(map.equals("map2.tmx")){
                    nameMap = "Map2";
                } else if (map.equals("amogus.tmx")) {
                    nameMap = "Special";
                }
                else{
                    return;
                }
                FiveElementTuple fiveElementTuple = new FiveElementTuple(2137, nameMap, raceTime, laps, bestLapTimeString);
                databaseInterface.insertIntoDatabase(fiveElementTuple);
            }
        });
        table.add(popupButton).width(500).height(200).center().padTop(20).row();

        TextButton restartButton = new TextButton("Restart", skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, map, laps, MODE_SOLO, 0));
            }
        });
        table.add(restartButton).width(500).height(200).bottom().padTop(20).row();

        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.add(mainMenuButton).width(500).height(200).center().padTop(20);

        stage.addActor(table);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 10, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw(delta);
    }

    private void draw(float delta) {
        batch.begin();
        batch.draw(texture, 0f, 0f, w, h);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
