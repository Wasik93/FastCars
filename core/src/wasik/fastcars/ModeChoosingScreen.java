package wasik.fastcars;

import static java.lang.Thread.sleep;
import static wasik.fastcars.Constants.UISKIN;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ModeChoosingScreen implements Screen {
    private Stage stage;

    private Skin skin;

    private SpriteBatch batch;

    private final float w = Gdx.graphics.getWidth();
    private final float h = Gdx.graphics.getHeight();

    Texture xdd = new Texture("mainpage.png");

    FastCars game;
    public ModeChoosingScreen(FastCars game) {
        this.game = game;
        this.stage = new Stage();
        this.skin = new Skin(Gdx.files.internal(UISKIN));
        this.batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        addButtons();
    }

    public void addButtons(){
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.center();
        table.defaults().size(750f, 150f).pad(10f);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        style.font.getData().setScale(4f);

        TextButton soloButton = new TextButton("Solo", skin);
        TextButton backButton = new TextButton("Go Back", skin);

        soloButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MapChoosingScreen(game));
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(soloButton).padBottom(20f).row();
        table.add(backButton);

        table.center();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(xdd, 0f, 0f, w, h);
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
        batch.dispose();
    }

}
