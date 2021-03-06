package keeki.jade;

import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import keeki.components.FontRenderer;
import keeki.components.SpriteRenderer;
import keeki.renderer.Shader;
import keeki.renderer.Texture;
import keeki.util.Time;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
        // position                 // color                    // UV coords
         50.5f,  -100.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f,     1, 1,   // Bottom right  0
        -200.5f,  100.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,     0, 0,   // Top left      1
         400.5f,  100.5f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f,     1, 0,   // Top right     2
        -100.5f, -100.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f,     0, 1    // Bottom left   3

    };

    //! IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
        2, 1, 0, // Top right triangle
        0, 1, 3
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;

    private Texture testTexture;

    GameObject testObj;
    private boolean firstTime = false;

    public LevelEditorScene() {
        

    }

    @Override
    public void init() {
        System.out.println("Creating test object");
        this.testObj = new GameObject("testObj");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj);
        
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/testImage.png");


        //* Generate VAO, VBO and EBO buffer objects, and send to GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        // Setting the pos attribute pointer
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        // Setting the color attribute pointer
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize*Float.BYTES);
        glEnableVertexAttribArray(1);

        // Setting the UV coords attribute pointer
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize+colorSize)*Float.BYTES);
        glEnableVertexAttribArray(2);


    }

    
    @Override
    public void Update(float dt) {
        camera.position.x -= dt * 50.0f;
        camera.position.y -= dt * 50.0f;

        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProj", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        if (!firstTime) {
            System.out.println("Creating game object");
            GameObject go = new GameObject("Game test 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }
        
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }
    
}
