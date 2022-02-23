package keeki.jade;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance = null;
    private boolean[] keys = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (instance == null) {
            instance = new KeyListener();
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keys[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keys[key] = false;
        }
    }

    public static boolean isKeyDown(int key) {
        return get().keys[key];
    }
}
