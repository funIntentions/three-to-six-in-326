package com.mygdx.projectMeta.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.projectMeta.Assets;
import com.mygdx.projectMeta.components.TextComponent;
import com.mygdx.projectMeta.components.TextureComponent;
import com.mygdx.projectMeta.components.TransformComponent;
import com.mygdx.projectMeta.utils.Constants;

public class RenderingSystem extends IteratingSystem {
    //static final float FRUSTUM_WIDTH = 10;
    //static final float FRUSTUM_HEIGHT = 15;
    //static final float PIXELS_TO_METRES = 1.0f / 32.0f;

    private SpriteBatch batch;
    private World world;
    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private Box2DDebugRenderer physicsDebugRenderer;

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;

    public RenderingSystem(SpriteBatch batch, World world) {
        super(Family.getFor(TransformComponent.class, TextureComponent.class));

        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        tiledMapRenderer = new OrthogonalTiledMapRenderer((TiledMap)Assets.map, Constants.SCALE);
        physicsDebugRenderer = new Box2DDebugRenderer();

        renderQueue = new Array<Entity>();

        comparator = new Comparator<Entity>() {
            @Override
            public int compare(Entity entityA, Entity entityB) {
                return (int)Math.signum(transformM.get(entityB).position.z -
                        transformM.get(entityA).position.z);
            }
        };

        this.batch = batch;
        this.world = world;

        setupCamera();
    }

    private void setupCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_WIDTH * (h / w));
        Vector3 position = new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.translate(position);
        camera.zoom = Constants.CAMERA_ZOOM;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        renderQueue.sort(comparator);
        camera.update();

        tiledMapRenderer.setView(camera); // render map
        tiledMapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Entity entity : renderQueue) {
            TextureComponent tex = textureM.get(entity);

            if (tex.textureRegion == null) {
                continue;
            }

            TransformComponent transform = transformM.get(entity);

            float width = tex.textureRegion.getRegionWidth();
            float height = tex.textureRegion.getRegionHeight();
            float originX = width * 0.5f;
            float originY = height * 0.5f;

            batch.draw(tex.textureRegion,
                    transform.position.x * Constants.PIXELS_PER_UNIT - originX,
                    transform.position.y * Constants.PIXELS_PER_UNIT - originY,
                    originX,
                    originY,
                    width,
                    height,
                    transform.scale.x,
                    transform.scale.y,
                    MathUtils.radiansToDegrees * transform.rotation);

        }

        batch.end();
        renderQueue.clear();

        physicsDebugRenderer.render(world, camera.combined);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
