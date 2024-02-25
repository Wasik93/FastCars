package wasik.fastcars.tools;


import static wasik.fastcars.Constants.*;

import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.io.File;


public class MapLoader implements Disposable {
    private final World world;
    private final TiledMap map;

    public MapLoader(World world, String nameMap, int mode) {
        this.world = world;
        if(mode == 0) {
            map = new TmxMapLoader().load(nameMap);
        }
        else{
            map = new TmxMapLoader(new ExternalFileHandleResolver()).load(nameMap);
            //the file needs to be in specific folder
        }

        Array<RectangleMapObject> walls = map.getLayers().get(MAP_WALL).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject mapObject : walls) {
            Rectangle rectangle = mapObject.getRectangle();
            ShapeFactory.createRectangle(new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2),
                    new Vector2(rectangle.getWidth() / 2, rectangle.getHeight() / 2),
                    BodyDef.BodyType.StaticBody, world, 1f);
        }
    }

    public Body getCar(){
        Rectangle rectangle = map.getLayers().get(MAP_CAR).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        return ShapeFactory.createRectangle(new Vector2(rectangle.getX() + rectangle.getWidth()/2, rectangle.getY() + rectangle.getHeight() / 2),
                new Vector2(rectangle.getWidth()/2, rectangle.getHeight()/2),
                BodyDef.BodyType.DynamicBody, world, 0.4f);
    }

    public void getFinishLine(){
        Rectangle finishLineRect = map.getLayers().get(MAP_FINISH).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((finishLineRect.getX() + finishLineRect.getWidth()/2)/PPM, (finishLineRect.getY() + finishLineRect.getHeight()/2)/PPM);
        Body finishLineBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(finishLineRect.getWidth() * 0.5f /PPM, finishLineRect.getHeight() * 0.5f/PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        finishLineBody.setUserData(MAP_FINISH);
        finishLineBody.createFixture(fixtureDef);


        shape.dispose();
    }

    public void getSector1Line(){
        Rectangle finishLineRect = map.getLayers().get(SECTOR_1).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((finishLineRect.getX() + finishLineRect.getWidth()/2)/PPM, (finishLineRect.getY() + finishLineRect.getHeight()/2)/PPM);
        Body finishLineBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(finishLineRect.getWidth() * 0.5f /PPM, finishLineRect.getHeight() * 0.5f/PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        finishLineBody.setUserData(SECTOR_1);
        finishLineBody.createFixture(fixtureDef);


        shape.dispose();
    }

    public void getSector2Line(){
        Rectangle finishLineRect = map.getLayers().get(SECTOR_2).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((finishLineRect.getX() + finishLineRect.getWidth()/2)/PPM, (finishLineRect.getY() + finishLineRect.getHeight()/2)/PPM);
        Body finishLineBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(finishLineRect.getWidth() * 0.5f /PPM, finishLineRect.getHeight() * 0.5f/PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        finishLineBody.setUserData(SECTOR_2);
        finishLineBody.createFixture(fixtureDef);


        shape.dispose();
    }

    @Override
    public void dispose() {
        map.dispose();
    }

    public TiledMap getMap() {
        return map;
    }
}
