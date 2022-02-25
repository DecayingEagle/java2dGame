package keeki.renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL30.*;

public class Shader {

    private int shaderProgramID;
    private String vertexSrc;
    private String fragmentSrc;
    private String filepath;

    private int vertexID, fragmentID;
    
    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String src = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = src.split("(#type)( )+([a-zA-Z]+)");


            // Find the first patter after #type
            int index = src.indexOf("#type") + 6;
            int eol;
            if(System.getProperty("os.name").contains("Windows")) {
                eol = src.indexOf("\r\n", index);
            } else {
                eol = src.indexOf("\n", index);
            }
            String firstPattern = src.substring(index, eol).trim();


            // Find the second pattern after #type
            index = src.indexOf("#type", eol) + 6;
            if(System.getProperty("os.name").contains("Windows")) {
                eol = src.indexOf("\r\n", index);
            } else {
                eol = src.indexOf("\n", index);
            }
            String secondPattern = src.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSrc = splitString[1];
            } else if(firstPattern.equals("fragment")) {
                fragmentSrc = splitString[1];
            } else {
                throw new IOException("Unknown token: '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexSrc = splitString[2];
            } else if(secondPattern.equals("fragment")) {
                fragmentSrc = splitString[2];
            } else {
                throw new IOException("Unknown token: '" + secondPattern + "'");
            }
        } catch (IOException e) {
            //TODO: handle exception
            e.printStackTrace();
            assert false : "Error: Could not load shader file: '" + filepath + "'";
        }
    }

    public void compile() {
        //* Compile and link shaders

        // Fist load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source code to the GPU
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        // Check of errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compilation failed'");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Fist load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source code to the GPU
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        // Check of errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment shader compilation failed'");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking shaders failed'");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        // Bind shader program
        glUseProgram(shaderProgramID);

    }

    public void detach() {
        glUseProgram(0);
    }
}
