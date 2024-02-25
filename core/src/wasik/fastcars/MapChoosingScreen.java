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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MapChoosingScreen implements Screen {
    private Stage stage;

    private FastCars game;

    private Skin skin;

    private SpriteBatch batch;
    Texture texture = new Texture("choosePage.png");

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    public MapChoosingScreen(FastCars game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);

        skin = new Skin(Gdx.files.internal(UISKIN));

        List.ListStyle listStyle = new List.ListStyle(skin.get(List.ListStyle.class));
        listStyle.font.getData().setScale(3.5f);
        final List<String> mapList = new List<>(skin);

        mapList.setItems("Map1", "Map2", "Singapur", "Special");
        table.add(mapList).width(500).height(400).expandX().top().padTop(50).row();

        final TextField lapsTextField = new TextField("2", skin);
        lapsTextField.setAlignment(Align.center);
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(skin.get(TextField.TextFieldStyle.class));
        textFieldStyle.font.getData().setScale(4f);
        table.add(lapsTextField).width(500).height(200).center().padTop(20).row();

        TextButton playButton = new TextButton("Play", skin);
        TextButton uploadButton = new TextButton("Upload", skin);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        buttonStyle.font.getData().setScale(4f);

        uploadButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new UploadScreen(game));
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String selectedMap = mapList.getSelected();
                int laps = 2;
                try {
                    laps = Integer.parseInt(lapsTextField.getText());
                }
                catch (NumberFormatException ignored){
                }
                if(laps <= 0 || laps > 100){
                    return;
                }
                startGame(selectedMap, laps);
            }
        });
        table.add(playButton).width(500).height(200).bottom().padBottom(50).row();
        table.add(uploadButton).width(500).height(200).bottom().padBottom(50).row();


        TextButton backButton = new TextButton("Go back", skin);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(backButton).width(500).height(200).bottom().padBottom(50);



        stage.addActor(table);
    }

    private void startGame(String mapName, Integer laps) {
        if(mapName == null || laps == null){
            return;
        }
        String nameMap = "";
        if(mapName.equals("Singapur")){
            nameMap = "singapur.tmx";
        }
        else if(mapName.equals("Map1")){
            nameMap = "map1.tmx";
        }
        else if(mapName.equals("Map2")){
            nameMap = "map2.tmx";
        } else if (mapName.equals("Special")) {
            nameMap = "amogus.tmx";
        }
        game.setScreen(new GameScreen(game, nameMap, laps, MODE_SOLO, 0));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 10, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(texture, 0f, 0f, w, h);
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
    }
}
