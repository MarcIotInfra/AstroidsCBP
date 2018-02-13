package dk.sdu.mmmi.cbse.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.ammo.AmmoCollisionDetection;
import dk.sdu.mmmi.cbse.ammo.AmmoControlSystem;
import dk.sdu.mmmi.cbse.ammo.AmmoPlugin;
import dk.sdu.mmmi.cbse.astroid.AstroidCollisionDetection;
import dk.sdu.mmmi.cbse.astroid.AstroidControlSystem;
import dk.sdu.mmmi.cbse.astroid.AstroidPlugin;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.SHIFT;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.enemysystem.EnemyCollisionDetection;
import dk.sdu.mmmi.cbse.managers.GameInputProcessor;
import dk.sdu.mmmi.cbse.playersystem.PlayerPlugin;
import dk.sdu.mmmi.cbse.playersystem.PlayerControlSystem;
import dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;
import dk.sdu.mmmi.cbse.enemysystem.EnemyControlSystem;
import dk.sdu.mmmi.cbse.playersystem.PlayerCollisionDetection;
import dk.sdu.mmmi.cbse.splitobject.SplitAstroidCollisionDetection;
import dk.sdu.mmmi.cbse.splitobject.SplitAstroidControlSystem;
import dk.sdu.mmmi.cbse.splitobject.SplitAstroidPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game implements ApplicationListener {

    private static OrthographicCamera cam;

    private final GameData gameData = new GameData();
    private ShapeRenderer sr;
    private List<IEntityProcessingService> entityProcessors = new ArrayList<>();
    private List<IGamePluginService> entityPlugins = new ArrayList<>();
    private List<IPostEntityProcessingService> entityPostProcessors = new ArrayList<>();
    private World world = new World();

    @Override
    public void create() {

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        sr = new ShapeRenderer();
        
        Gdx.input.setInputProcessor(
                new GameInputProcessor(gameData)
        );
        
        loadPlugins();
        loadProcesses();
        loadPostProcesses();

        // Lookup all Game Plugins using ServiceLoader
        for (IGamePluginService iGamePlugin : entityPlugins) {
            iGamePlugin.start(gameData, world);
        }
    }
    
    public void loadPlugins(){
        IGamePluginService enemyPlugin = new EnemyPlugin();
        IGamePluginService playerPlugin = new PlayerPlugin();
        IGamePluginService astroidPlugin = new AstroidPlugin();
        IGamePluginService splitAstroidPlugin = new SplitAstroidPlugin();
        IGamePluginService ammoPlugin = new AmmoPlugin();
        
        entityPlugins.add(ammoPlugin);
        entityPlugins.add(playerPlugin);
        entityPlugins.add(enemyPlugin);
        entityPlugins.add(astroidPlugin);
        entityPlugins.add(splitAstroidPlugin);
    }
    
    public void loadProcesses(){
        IEntityProcessingService playerProcess = new PlayerControlSystem();
        IEntityProcessingService enemyProcess = new EnemyControlSystem();
        IEntityProcessingService astroidProcess = new AstroidControlSystem();
        IEntityProcessingService splitAstroidProcess = new SplitAstroidControlSystem();
        IEntityProcessingService ammoProcess = new AmmoControlSystem();

        entityProcessors.add(ammoProcess);
        entityProcessors.add(playerProcess);
        entityProcessors.add(enemyProcess);
        entityProcessors.add(astroidProcess);
        entityProcessors.add(splitAstroidProcess);
    }
    
    public void loadPostProcesses(){
        IPostEntityProcessingService playerPostProcess = new PlayerCollisionDetection();
        IPostEntityProcessingService enemyPostProcess = new EnemyCollisionDetection();
        IPostEntityProcessingService astroidPostProcess = new AstroidCollisionDetection();
        IPostEntityProcessingService splitAstroidPostProcess = new SplitAstroidCollisionDetection();
        IPostEntityProcessingService ammoPostProcess = new AmmoCollisionDetection();
        
        entityPostProcessors.add(ammoPostProcess);
        entityPostProcessors.add(playerPostProcess);
        entityPostProcessors.add(enemyPostProcess);
        entityPostProcessors.add(astroidPostProcess);
        entityPostProcessors.add(splitAstroidPostProcess);
    }

    @Override
    public void render() {

        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        update();

        draw();

        gameData.getKeys().update();
    }

    private void update() {
        if(gameData.getKeys().isDown(SHIFT) == true){
            pause();
        } else {   
            // Update
            for (IEntityProcessingService entityProcessorService : entityProcessors) {
               entityProcessorService.process(gameData, world);
            }

            for(IPostEntityProcessingService entityPostProcessingService: entityPostProcessors){
                entityPostProcessingService.process(gameData, world);
            }
        }
    }

    private void draw() {
        for (Entity entity : world.getEntities()) {
            int[] color = entity.getColor();
            sr.setColor(color[0], color[1], color[2], color[3]);

            sr.begin(ShapeRenderer.ShapeType.Line);

            List<Float> shapex = entity.getShapeX();
            List<Float> shapey = entity.getShapeY();

            for (int i = 0, j = shapex.size() - 1;
                    i < shapex.size();
                    j = i++) {

                sr.line(shapex.get(i), shapey.get(i), shapex.get(j), shapey.get(j));
            }

            sr.end();
        }
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
    public void dispose() {
    }
}
