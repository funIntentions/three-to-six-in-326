package com.mygdx.projectMeta.systems;

import java.util.Comparator;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.projectMeta.Assets;
import com.mygdx.projectMeta.Ray;
import com.mygdx.projectMeta.components.SteeringComponent;
import com.mygdx.projectMeta.components.TextureComponent;
import com.mygdx.projectMeta.components.TransformComponent;

import com.mygdx.projectMeta.utils.Constants;

public class RenderingSystem extends IteratingSystem {

    private SpriteBatch batch;
    private World world;
    private RayHandler rayHandler;
    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private Box2DDebugRenderer physicsDebugRenderer;
    private ShapeRenderer debugShapeRenderer;
    private FrameBuffer frameBufferObject;
    private TextureRegion frameBufferRegion;
    private SpriteBatch fbBatch;
    private ShaderProgram shaderProgram;
    private PointLight testLight;
    private float time;

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<SteeringComponent> steeringMapper;

    public RenderingSystem(SpriteBatch batch, World world, RayHandler rayHandler) {
        super(Family.getFor(TransformComponent.class, TextureComponent.class));

        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        steeringMapper = ComponentMapper.getFor(SteeringComponent.class);
        tiledMapRenderer = new OrthogonalTiledMapRenderer((TiledMap)Assets.map, Constants.SCALE);
        physicsDebugRenderer = new Box2DDebugRenderer();
        debugShapeRenderer = new ShapeRenderer();

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
        this.rayHandler = rayHandler;

        frameBufferObject = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), true);

        frameBufferRegion = new TextureRegion(frameBufferObject.getColorBufferTexture(), 0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //frameBufferRegion.flip(false, true);

        fbBatch = new SpriteBatch();

        //batch.setShader(createDefaultShader());
        //shaderProgram = createShader();
        //fbBatch.setShader(shaderProgram);
        time = 0;

        setupCamera();

        testLight = new PointLight(rayHandler, 128, new Color(255f, 197f, 143f, 0.8f), 16, 15, 15);
        rayHandler.setAmbientLight(0f, 0f, 0f, 0.1f);
    }

    private void setupCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_WIDTH * (h / w));
        Vector3 position = new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.translate(position);
        camera.zoom = Constants.CAMERA_ZOOM;
    }

    static public ShaderProgram createDefaultShader () {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_color.a = v_color.a * (255.0/254.0);\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying LOWP vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
                + "}";

        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }

    static public ShaderProgram createShader () {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_color.a = v_color.a * (255.0/254.0);\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = Assets.fragmentShader;

        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);

        //frameBufferObject.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.updateAndRender();

        debugShapeRenderer.setProjectionMatrix(camera.combined);
        debugShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugShapeRenderer.setColor(0, 0, 1, 1);
        for (Entity entity : renderQueue)
        {
            if (steeringMapper.has(entity))
            {
                SteeringComponent steeringComponent = steeringMapper.get(entity);

                for (Ray ray : steeringComponent.feelers)
                {
                    Vector2 p1 = new Vector2(ray.position);
                    Vector2 p2 = new Vector2(ray.position).add((new Vector2(ray.direction).scl(ray.length)));
                    debugShapeRenderer.line(p1.x, p1.y, p2.x, p2.y);
                }
            }

        }
        debugShapeRenderer.end();

        //frameBufferObject.end();

        renderQueue.clear();

        /*Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        time += deltaTime;
        shaderProgram.begin();
        shaderProgram.setUniformf("xOffset", 0);
        shaderProgram.setUniformf("yOffset", 0);
        //shaderProgram.setUniformf("time", time);
        shaderProgram.end();

        // draw for real
        fbBatch.setShader(shaderProgram);
        fbBatch.begin();
        fbBatch.draw(frameBufferRegion, 0, 0);
        fbBatch.end();*/

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
