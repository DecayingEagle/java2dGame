package keeki.jade;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener instance = null;
    private double scroll_x, scroll_y;
    private double xPos, yPos, xPosOld, yPosOld;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging = false;

    private MouseListener() {
        this.scroll_x = this.scroll_y = 0.0;
        xPos = yPos = xPosOld = yPosOld = 0.0;
        for (int i = 0; i < 3; i++) {
            mouseButtonPressed[i] = false;
        }
    }

    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }
        return instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().xPosOld = get().xPos;
        get().yPosOld = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scroll_x = xOffset;
        get().scroll_y = yOffset;
    }

    public static void endFrame() {
        get().scroll_x = get().scroll_y = 0.0;
        get().xPosOld = get().xPos;
        get().yPosOld = get().yPos;
    }

    public static float getXPos() {
        return (float) get().xPos;
    }
    public static float getYPos() {
        return (float) get().yPos;
    }
    public static float getDx() {
        return (float) (get().xPos - get().xPosOld);
    }
    public static float getDy() {
        return (float) (get().yPos - get().yPosOld);
    }

    public static float getScrollX() {
        return (float) get().scroll_x;
    }

    public static float getScrollY() {
        return (float) get().scroll_y;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean isMouseButtonPressed(int button) {
        if(button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        }
        return false;
    }
}
