package wasik.fastcars;

import static wasik.fastcars.Constants.UISKIN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class SettingsScreen implements Screen {

    boolean gyroscopeAvail = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
    private FastCars game;

    private SpriteBatch batch;
    private Stage stage;

    private Viewport viewport;

    private Skin skin;
    private final String[] steeringSettings = {"tap tap tap", "Sbinnala", "Spin Phone"};

    Texture texture = new Texture("settingsPage.png");

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    public SettingsScreen(FastCars game) {
        setupSettings(game);
    }

    private void setupSettings(FastCars game) {
        this.game = game;
        OrthographicCamera camera = new OrthographicCamera();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        this.stage = new Stage(viewport);
        this.batch = new SpriteBatch();
        this.skin = new Skin(Gdx.files.internal(UISKIN));
        addButtons();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    private void addButtons() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        style.font.getData().setScale(3.5f);

        Button button1 = new TextButton("tap tap tap", skin);
        Button button3 = new TextButton("Spin Phone", skin);
        Button button2 = new TextButton("MUSIC", skin);
        Button button4 = new TextButton("Go Back", skin);

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.center();
        buttonTable.defaults().size(750f, 150f).pad(10f);

        String tmp = "current Option: " + steeringSettings[game.getSteering()];
        Button label = new TextButton(tmp, skin);

        buttonTable.add(label).row();
        buttonTable.add(button1).row();
        if (gyroscopeAvail) {
            buttonTable.add(button3).row();
        }
        buttonTable.add(button2).row();

        buttonTable.add(button4).row();


        button1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setSteering(0);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.getMenuMusic().isPlaying()) {
                    game.stopMusic();
                } else {
                    game.playMusic();
                }
                game.setScreen(new MainMenuScreen(game));
            }
        });
        button3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setSteering(2);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        button4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });


        stage.addActor(buttonTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
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
