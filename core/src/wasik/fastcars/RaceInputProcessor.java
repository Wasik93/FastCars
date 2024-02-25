package wasik.fastcars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class RaceInputProcessor implements InputProcessor {
    private boolean isLeftTouched = false;
    private boolean isRightTouched = false;

    private boolean isMiddleTouched = false;

    private FastCars game;
    private GameScreen gameScreen;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            game.setScreen(new MainMenuScreen(game));
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (gameScreen.getSteering() == 2) {
            isMiddleTouched = true;
        } else if (gameScreen.getSteering() == 0) {
            if (screenX < Gdx.graphics.getWidth() / 3) {
                isLeftTouched = true;
            } else if (screenX > 2 * Gdx.graphics.getWidth() / 3) {
                isRightTouched = true;
            } else {
                isMiddleTouched = true;
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (gameScreen.getSteering() == 2) {
            isMiddleTouched = false;
        } else if (gameScreen.getSteering() == 0) {
            if (screenX < Gdx.graphics.getWidth() / 3) {
                isLeftTouched = false;
                gameScreen.setSteering(0);
            } else if (screenX > 2 * Gdx.graphics.getWidth() / 3) {
                isRightTouched = false;
                gameScreen.setSteering(0);
            } else {
                isMiddleTouched = false;
                gameScreen.setSteering(0);
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public boolean isLeftTouched() {
        return isLeftTouched;
    }

    public boolean isRightTouched() {
        return isRightTouched;
    }

    public boolean isMiddleTouched() {
        return isMiddleTouched;
    }

    public void setGame(FastCars game) {
        this.game = game;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.game = gameScreen.game;
    }
}
