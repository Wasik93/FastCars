package wasik.fastcars;

import static wasik.fastcars.Constants.*;
import static wasik.fastcars.GameConstants.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.jdi.IntegerValue;


import wasik.fastcars.tools.MapLoader;

public class GameScreen implements Screen {

    private int mode;
    private int steering;

    private Label label;

    private Label timeLabel;

    private Label speedLabel;
    private Stage stage;

    private SpriteBatch batch;

    private World world;
    private String string;
    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera camera;

    private Viewport viewport;

    private Body car;

    private Vector2 velocity;

    private float speed;

    private float carWidth;

    private float carHeight;

    private Vector2 opponentPosition;

    private float opponentAngle;


    private Vector2 carPosition;


    Sprite carSprite;

    Sprite opponentSprite;

    private Race race;

    private long time;

    private long currentTime;

    private int driveDirection = NONE;

    private int turnDirection = NONE;

    private MapLoader mapLoader;

    //g - gyroscope

    private float gPitch;

    private float gSteeringInput = 1.0f;

    private boolean startRace = false;

    private Skin skin;
    InputMultiplexer multiplexer;
    RaceInputProcessor inputProcessor;
    FastCars game;


    OrthogonalTiledMapRenderer tmr;

    GameScreen(FastCars game, String nameMap, Integer laps, int mode, int mapMode) {
        prepareGame(game, nameMap, laps, mode, mapMode);
        setupWorld();
        createInterface(laps);
    }


    private void prepareGame(FastCars game, String nameMap, Integer laps, int mode, int mapMode) {
        this.mode = mode;
        this.game = game;
        this.stage = new Stage();
        this.batch = new SpriteBatch();
        this.world = new World(GRAVITY, true);
        this.debugRenderer = new Box2DDebugRenderer();
        this.camera = new OrthographicCamera();
        this.camera.zoom = DEFAULT_ZOOM;
        this.viewport = new FitViewport(Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM, camera);
        this.mapLoader = new MapLoader(world, nameMap, mapMode);
        this.tmr = new OrthogonalTiledMapRenderer(mapLoader.getMap());
        this.car = mapLoader.getCar();
        this.multiplexer = new InputMultiplexer();
        this.inputProcessor = new RaceInputProcessor();
        this.inputProcessor.setGameScreen(this);
        this.gSteeringInput = 1.0f;
        this.race = new Race(game, nameMap, laps);
        this.carPosition = new Vector2();
        this.opponentPosition = new Vector2();
        multiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        car.setLinearDamping(0.5f);
        this.steering = game.getSteering();
        mapLoader.getFinishLine();
        mapLoader.getSector1Line();
        mapLoader.getSector2Line();
        skin = new Skin(Gdx.files.internal(UISKIN));
    }


    private void setupWorld() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                boolean isFinishLineCollision = isFinnishLineCollision(fixtureA) || isFinnishLineCollision(fixtureB);
                boolean isSector1LineCollision = isSector1Collision(fixtureA) || isSector1Collision(fixtureB);
                boolean isSector2LineCollision = isSector2Collision(fixtureA) || isSector2Collision(fixtureB);

                if (isFinishLineCollision) {
                    if (race.remainingLaps() == 0) {
                        race.endRace();
                    } else {
                        race.addLap();
                    }
                }
                if (isSector1LineCollision) {
                    race.acceptSector1();
                }
                if (isSector2LineCollision) {
                    race.acceptSector2();
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                boolean isFinishLineCollision = isFinnishLineCollision(fixtureA) || isFinnishLineCollision(fixtureB);
                boolean isSector1LineCollision = isSector1Collision(fixtureA) || isSector1Collision(fixtureB);
                boolean isSector2LineCollision = isSector2Collision(fixtureA) || isSector2Collision(fixtureB);

                if (isFinishLineCollision || isSector1LineCollision || isSector2LineCollision) {
                    contact.setEnabled(false);
                }
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }

            private boolean isFinnishLineCollision(Fixture fixture) {
                return fixture.getBody().getUserData() != null && fixture.getBody().getUserData().equals(MAP_FINISH);
            }

            private boolean isSector1Collision(Fixture fixture) {
                return fixture.getBody().getUserData() != null && fixture.getBody().getUserData().equals(SECTOR_1);
            }

            private boolean isSector2Collision(Fixture fixture) {
                return fixture.getBody().getUserData() != null && fixture.getBody().getUserData().equals(SECTOR_2);
            }
        });
    }

    private void createInterface(Integer laps) {
        label = new Label("0/" + laps, skin);
        stage.addActor(label);
        label.setSize(Gdx.graphics.getWidth() / 10.0f, Gdx.graphics.getHeight() / 10.0f);
        label.setPosition(0 + label.getWidth(), Gdx.graphics.getHeight() - label.getHeight());
        label.setFontScale(4f);

        timeLabel = new Label("00:00.00", skin);
        stage.addActor(timeLabel);
        timeLabel.setSize(Gdx.graphics.getWidth() / 10.0f, Gdx.graphics.getHeight() / 10.0f);
        timeLabel.setPosition(0 + timeLabel.getWidth(), Gdx.graphics.getHeight() - 2 * timeLabel.getHeight());
        timeLabel.setFontScale(4f);

        speedLabel = new Label("0 km/h", skin);
        stage.addActor(speedLabel);
        speedLabel.setSize(Gdx.graphics.getWidth() / 10.0f, Gdx.graphics.getHeight() / 10.0f);
        speedLabel.setPosition(Gdx.graphics.getWidth() - speedLabel.getWidth(), Gdx.graphics.getHeight() - speedLabel.getHeight());
        speedLabel.setFontScale(4f);


        Rectangle rectangle = mapLoader.getMap().getLayers().get(MAP_CAR).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        Texture texture = new Texture(Gdx.files.internal("car.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        Texture opponent = new Texture(Gdx.files.internal("car2.png"));
        TextureRegion opponentTexture = new TextureRegion(opponent);

        carWidth = (rectangle.getWidth()) / PPM;
        carHeight = (rectangle.getHeight()) / PPM;
        carPosition.x = car.getPosition().x - carWidth / 2;
        carPosition.y = car.getPosition().y - carHeight / 2;
        opponentPosition.x = carPosition.x;
        opponentPosition.y = carPosition.y;
        opponentAngle = (float) Math.toDegrees(car.getAngle());
        carSprite = new Sprite(textureRegion);
        carSprite.setPosition(carPosition.x, carPosition.y);
        carSprite.setSize(carWidth, carHeight);
        carSprite.setOrigin(carSprite.getWidth() / 2, carSprite.getHeight() / 2);
        opponentSprite = new Sprite(opponentTexture);
        opponentSprite.setPosition(carPosition.x, carPosition.y);
        opponentSprite.setSize(carWidth, carHeight);
        opponentSprite.setOrigin(carSprite.getWidth() / 2, carSprite.getHeight() / 2);

        tmr = new OrthogonalTiledMapRenderer(mapLoader.getMap(), 1 / PPM);
    }


    @Override
    public void show() {

    }

    private final int fps = 30;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        checkRaceStatus();

        handleInput();
        processInput();
        update(delta);
        handleDrift();

        draw();
        updateLabels(delta);
        //sleep(fps); //limit fps, it was only to help Bluetooth, but BT doesn't work
    }

    private long diff, start = System.currentTimeMillis();

    long targetDelay = 1000 / fps;

    public void sleep(int fps) {
        if (fps > 0) {
            diff = System.currentTimeMillis() - start;
            targetDelay = 1000 / fps;
            if (diff < targetDelay) {
                try {
                    Thread.sleep(targetDelay - diff);
                } catch (InterruptedException e) {
                }
            }
            start = System.currentTimeMillis();
        }
    }

    private void checkRaceStatus() {
        if (!startRace && (this.inputProcessor.isRightTouched() || this.inputProcessor.isMiddleTouched() || this.inputProcessor.isLeftTouched())) {
            startRace = true;
            time = System.currentTimeMillis();

        }
    }

    @SuppressWarnings("DefaultLocale")
    private void updateLabels(float delta) {
        label.setText(race.getCurrentLaps() + "/" + race.getNumberLaps());
        if (race.isRaceStarted()) {
            currentTime = System.currentTimeMillis();
            currentTime = currentTime - time;
            string = "";

            if (currentTime / 60000 < 10) {
                string += "0";
            }
            string += currentTime / 60000;
            string += ":";
            if ((currentTime / 1000) % 60 < 10) {
                string += "0";
            }
            string += ((currentTime / 1000) % 60);
            string += "." + ((currentTime / 10) % 10);
            timeLabel.setText(string);
        }
        velocity = car.getLinearVelocity();
        speed = velocity.len();
        speed = speed * 3.6f;

        speedLabel.setText((int) speed + " km/h");

        stage.act(delta);
        stage.draw();
    }

    private void handleInput() {
        if (!startRace) {
            return;
        }
        if (steering == BUTTONS) {
            if (!this.inputProcessor.isMiddleTouched()) {
                driveDirection = FORWARD;
            } else {
                driveDirection = BACKWARD;
            }

            if (this.inputProcessor.isLeftTouched()) {
                turnDirection = LEFT;
            } else if (this.inputProcessor.isRightTouched()) {
                turnDirection = RIGHT;
            } else {
                turnDirection = NONE;
            }
        } else if (steering == GYROSCOPE) {
            if (!this.inputProcessor.isMiddleTouched()) {
                driveDirection = FORWARD;
            } else {
                driveDirection = BACKWARD;
            }

            gPitch = Gdx.input.getPitch();
            gSteeringInput = gPitch * GYROSCOPE_SENSITIVITY;
            if (gPitch < 0) {
                turnDirection = LEFT;
            } else if (gPitch > 0) {
                turnDirection = RIGHT;
            } else {
                turnDirection = NONE;
            }
        } else if (steering == GAMEPAD) {

        }
    }

    private void handleDrift() {
        Vector2 forwardSpeed = getForwardVelocity();
        Vector2 lateralSpeed = getLateralVelocity();
        car.setLinearVelocity(forwardSpeed.x + lateralSpeed.x * DRIFT, forwardSpeed.y + lateralSpeed.y * DRIFT);
    }

    private void processInput() {
        Vector2 baseVector = new Vector2();
        if (turnDirection == RIGHT) {
            if (steering == 0) {
                car.setAngularVelocity(-TURN_SPEED);
                car.setAngularVelocity(-TURN_SPEED);
            }
            if (steering == 2) {
                car.setAngularVelocity(gSteeringInput);
            }
        } else if (turnDirection == LEFT) {
            if (steering == 0) {
                car.setAngularVelocity(TURN_SPEED);
            } else if (steering == 2) {
                car.setAngularVelocity(gSteeringInput);
            }
        } else if (turnDirection == NONE && car.getAngularVelocity() != 0) {
            car.setAngularVelocity(0.0f);
        }

        if (driveDirection == FORWARD) {
            baseVector.set(0, DRIVE_SPEED);
        } else if (driveDirection == BACKWARD) {
            baseVector.set(0, -DRIVE_SPEED / 6);
        }
        if (!baseVector.isZero() && car.getLinearVelocity().len() < MAX_SPEED) {
            car.applyForceToCenter(car.getWorldVector(baseVector), true);
        }
    }

    private Vector2 getForwardVelocity() {
        Vector2 currentNormal = car.getWorldVector(new Vector2(0, 1));
        float dotProduct = currentNormal.dot(car.getLinearVelocity());
        return multiply(dotProduct, currentNormal);
    }

    private Vector2 getLateralVelocity() {
        Vector2 currentNormal = car.getWorldVector(new Vector2(1, 0));
        float dotProduct = currentNormal.dot(car.getLinearVelocity());
        return multiply(dotProduct, currentNormal);
    }

    private Vector2 multiply(float s, Vector2 v) {
        return new Vector2(s * v.x, s * v.y);
    }

    private void draw() {
        batch.setProjectionMatrix(camera.combined);
        debugRenderer.render(world, camera.combined);

        tmr.render();
        batch.begin();
        //drawOpponent();
        carPosition.x = car.getPosition().x - carWidth / 2;
        carPosition.y = car.getPosition().y - carHeight / 2;

        carSprite.setRotation((float) Math.toDegrees(car.getAngle()));
        carSprite.setPosition(carPosition.x, carPosition.y);
        carSprite.draw(batch);

        batch.end();
    }

    private void update(float delta) {
        camera.position.set(car.getPosition(), 0);
        camera.update();
        tmr.setView(camera);

        world.step(delta, 6, 2);
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
        batch.dispose();
        world.dispose();
        debugRenderer.dispose();
        mapLoader.dispose();
        tmr.dispose();
        carSprite.getTexture().dispose();
        stage.dispose();
    }

    public void setSteering(int steering) {
        this.steering = steering;
    }

    public int getSteering() {
        return this.steering;
    }
}
