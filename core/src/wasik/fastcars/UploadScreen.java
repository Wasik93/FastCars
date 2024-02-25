package wasik.fastcars;

import static wasik.fastcars.Constants.UISKIN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.io.File;
import java.util.Arrays;

public class UploadScreen implements Screen {
    private Stage stage;

    private SpriteBatch batch;

    private final float w = Gdx.graphics.getWidth();
    private final float h = Gdx.graphics.getHeight();
    private FastCars game;
    private TextButton uploadButton;

    Texture xdd = new Texture("mainpage.png");

    public UploadScreen(final FastCars game) {
        this.game = game;
        stage = new Stage();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        Skin skin = new Skin(Gdx.files.internal(UISKIN));

        final TextField mapFile = new TextField(".tmx", skin);
        mapFile.setAlignment(Align.center);

        uploadButton = new TextButton("Upload File", skin);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(skin.get(TextField.TextFieldStyle.class));
        textFieldStyle.font.getData().setScale(4f);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        buttonStyle.font.getData().setScale(4f);


        uploadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try{
                    game.setScreen(new GameScreen(game, mapFile.getText(), 3, 0, 1));
                }
                catch (Exception e){

                }
            }
        });

        TextButton backButton = new TextButton("Go back", skin);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(mapFile).width(500).height(200).center().padTop(20).row();
        table.add(uploadButton).width(500).height(200).bottom().padBottom(50).row();
        table.add(backButton).width(500).height(200).bottom().padBottom(50);

        stage.addActor(table);
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
        batch.dispose();
        stage.dispose();
    }
}