package wasik.fastcars;

import static wasik.fastcars.Constants.UISKIN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class MainMenuScreen implements Screen {
    private FastCars game;

    private SpriteBatch batch;

    private Stage stage;
    private Skin skin;
    Texture xdd = new Texture("mainpage.png");

    private final float w = Gdx.graphics.getWidth();
    private final float h = Gdx.graphics.getHeight();


    MainMenuScreen(FastCars game){
        setupMainMenu(game);
        addButtons();
    }

    private void setupMainMenu(FastCars game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        addButtons();
    }

    public void addButtons(){
        skin = new Skin(Gdx.files.internal(UISKIN));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        style.font.getData().setScale(4f);

        TextButton playButton = new TextButton("PLAY", skin);
        TextButton statsButton = new TextButton("STATS", skin);
        TextButton settingsButton = new TextButton("SETTINGS", skin);
        TextButton exitButton = new TextButton("EXIT", skin);

        playButton.setWidth(500f);
        playButton.setHeight(200f);
        playButton.setPosition(1.5f * (Gdx.graphics.getWidth() - playButton.getWidth())/5, 3.5f *(Gdx.graphics.getHeight() - playButton.getHeight())/5);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new ModeChoosingScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        statsButton.setWidth(500f);
        statsButton.setHeight(200f);
        statsButton.setPosition(3.5f * (Gdx.graphics.getWidth() - statsButton.getWidth())/5, 3.5f*(Gdx.graphics.getHeight() - statsButton.getHeight())/5);
        statsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new StatsScreen(game));
            }
        });

        settingsButton.setWidth(500f);
        settingsButton.setHeight(200f);
        settingsButton.setPosition(1.5f*(Gdx.graphics.getWidth() - settingsButton.getWidth())/5, 1.5f*(Gdx.graphics.getHeight() - settingsButton.getHeight())/5);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        exitButton.setWidth(500f);
        exitButton.setHeight(200f);
        exitButton.setPosition(3.5f*(Gdx.graphics.getWidth() - exitButton.getWidth())/5, 1.5f*(Gdx.graphics.getHeight() - exitButton.getHeight())/5);

        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(playButton);
        stage.addActor(statsButton);
        stage.addActor(settingsButton);
        stage.addActor(exitButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        draw(delta);
    }

    private void draw(float delta) {
        batch.begin();
        batch.draw(xdd, 0f, 0f, w, h);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        skin.dispose();
        batch.dispose();
        xdd.dispose();
    }
}
