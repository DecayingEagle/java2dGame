package keeki.jade;

import org.joml.Vector2f;
import org.joml.Vector4f;

import keeki.components.SpriteRenderer;

public class LevelEditorScene extends Scene {


    public LevelEditorScene() {
        
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));

        int xOffset = 10;
        int yOffset = 10;

        float totalW = (float)(600 - xOffset * 2);
        float totalH = (float)(600 - yOffset * 2);
        float sizeX = totalW / 100.0f;
        float sizeY = totalH / 100.0f;
        float padding = 3;

        for (int x=0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (sizeX * x)+ (padding * x);
                float yPos = yOffset + (sizeY * y)+ (padding * y);

                GameObject go = new GameObject("Obj" + x + "" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalW, yPos / totalH, 1, 1)));
                this.addGameObjectToScene(go);
            }
        }
    }

    
    @Override
    public void Update(float dt) {
        System.out.println("FPS: " + (1.0f / dt));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
    
}
