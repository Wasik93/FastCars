package wasik.fastcars.tools;

import static wasik.fastcars.Constants.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class ShapeFactory {
    private ShapeFactory(){}

    public static Body createRectangle(Vector2 position, Vector2 size, BodyDef.BodyType type, World world, float density){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position.x / PPM, position.y / PPM);
        bodyDef.type = type;
        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(size.x / PPM, size.y / PPM );
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = density;

        body.createFixture(fixtureDef);
        polygonShape.dispose();

        return body;
    }
}
