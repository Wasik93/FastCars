package wasik.fastcars;

import static wasik.fastcars.Constants.UISKIN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class StatsScreen implements Screen {
    private FastCars game;

    private DatabaseInterface databaseInterface;

    private Skin skin;

    private Stage stage;

    private Viewport viewport;

    private Table data;
    private final float screenHeight = Gdx.graphics.getHeight();
    private final float tableHeight = screenHeight * 0.75f;

    private String[] strings = {"Id",  "Map Name", "Race Times", "Laps", "Best Lap"};

    StatsScreen(FastCars game){
        setupStats(game);
        addButtons();
    }

    private void setupStats(FastCars game) {
        this.game = game;
        this.databaseInterface = game.getDatabase();
        this.skin = new Skin(Gdx.files.internal(UISKIN));
        OrthographicCamera camera = new OrthographicCamera();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        this.stage = new Stage(viewport);
        this.data = new Table();
        this.data.setPosition(0, screenHeight - tableHeight);
        this.data.setSize(Gdx.graphics.getWidth(), tableHeight);
    }

    @Override
    public void show() {
    }

    private void addButtons() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        style.font.getData().setScale(3.5f);

        Label.LabelStyle Lstyle = new Label.LabelStyle((skin.get(Label.LabelStyle.class)));
        Lstyle.font.getData().setScale(3.5f);

        Gdx.input.setInputProcessor(stage);

        Button backButton = new TextButton("Go Back", skin);

        Table table = new Table();
        table.setPosition(375f,75f);
        table.defaults().size(750f, 150f).pad(10f);

        table.add(backButton).row();

        Button resetButton = new TextButton("Reset", skin);
        Table table2 = new Table();
        table2.setPosition(Gdx.graphics.getWidth() - 375f, 75f);
        table2.defaults().size(750f, 150f).pad(10f);
        table2.add(resetButton).row();

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                databaseInterface.resetDatabase();
            }
        });

        ArrayList<FiveElementTuple> database = databaseInterface.getFromDatabase();

        String[] tmp = {"", "", "", "", ""};

        for (int col = 0; col < 5; col++) {
            Label label = new Label(strings[col],skin);
            data.add(label).expand().fill().pad(5f);
        }
        data.row();
        stage.addActor(table);
        stage.addActor(table2);
        if(database == null){
            stage.addActor(data);
            return;
        }
        for (int row = 0; row < database.size(); row++) {
            tmp[0] = database.get(row).id.toString();
            tmp[1] = database.get(row).mapName;
            tmp[2] = database.get(row).raceTime;
            tmp[3] = database.get(row).numberOfLaps.toString();
            tmp[4] = database.get(row).bestLapTime;

            for (int col = 0; col < 5; col++) {
                Label label = new Label(tmp[col], skin);
                data.add(label).expand().fill().pad(5f);
            }
            data.row();
        }
        stage.addActor(data);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
