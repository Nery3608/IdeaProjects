package io.github.found1;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class AnimationDemoScreen implements Screen {

    enum PlayerState {IDLE, RUNNING, JUMPING}

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Texture playerSheet, enemySheet, coinSheet;

    private Animation<TextureRegion> idleAnim, runAnim, jumpAnim;

    private Animation<TextureRegion> slimeAnim, coinAnim;

    private float stateTime = 0f;
    private PlayerState state = PlayerState.IDLE;
    private boolean facingRight = true;
    private float playerX = 100f, playerY = 100f;


    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 480);

        playerSheet = new Texture("player.png");
        TextureRegion[][] pGrid = TextureRegion.split(playerSheet, 64, 64);

        idleAnim = new Animation<>(0.2f, pGrid[0]);
        runAnim = new Animation<>(0.1f, pGrid[1]);
        jumpAnim = new Animation<>(0.15f, pGrid[2]);

        idleAnim.setPlayMode(Animation.PlayMode.LOOP);
        runAnim.setPlayMode(Animation.PlayMode.LOOP);
        jumpAnim.setPlayMode(Animation.PlayMode.NORMAL);

        enemySheet = new Texture("enemy-slime.png");
        TextureRegion[][] eGrid = TextureRegion.split(enemySheet, 64, 64);
        slimeAnim = new Animation<>(0.15f, eGrid[0]);
        slimeAnim.setPlayMode(Animation.PlayMode.LOOP);

        coinSheet = new Texture("coin.png");
        TextureRegion[][] cGrid = TextureRegion.split(coinSheet, 32, 32);
        coinAnim = new Animation<>(0.08f, cGrid[0]);
        coinAnim.setPlayMode(Animation.PlayMode.LOOP);

    }

    @Override
    public void render(float delta) {
        stateTime += delta;

        PlayerState previous = state;

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            state = PlayerState.JUMPING;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            state = PlayerState.RUNNING;
            facingRight = false;
            playerX -= 100 * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            state = PlayerState.RUNNING;
            facingRight = true;
            playerX += 100 * delta;
        }else {
            state = PlayerState.IDLE;

        }

        if (previous != state){
            stateTime = 0f;
        }

        Animation<TextureRegion> current;
        switch(state){
            case IDLE -> current = idleAnim;
            case RUNNING -> current = runAnim;
            case JUMPING -> current = jumpAnim;
            default -> current = idleAnim
        }

        boolean looping = state != PlayerState.JUMPING;
        TextureRegion playerFrame = current.getKeyFrame(stateTime, looping);

        if (!facingRight && !playerFrame.isFlipX()) {
            playerFrame.flip(true, false);
        } else if (facingRight && playerFrame.isFlipX()) {
            playerFrame.flip(true, false);
        }

        TextureRegion slimeFrame = slimeAnim.getKeyFrame(stateTime, true);
        TextureRegion coinFrame = coinAnim.getKeyFrame(stateTime, true);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(playerFrame, playerX, playerY);
        batch.draw(slimeFrame, 400f, 100f);
        batch.draw(coinFrame, 300f, 2500f);
        batch.draw(coinFrame, 340f, 250f);
        batch.draw(coinFrame, 380f, 250f);


    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);

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
        playerSheet.dispose();
        enemySheet.dispose();
        coinSheet.dispose();

    }

}
