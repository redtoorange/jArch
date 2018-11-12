package engine;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Global {
    private static boolean GLFW_ENABLED = false;

    public static void EnableGLFW() {
        if( !GLFW_ENABLED ) {
            GLFW_ENABLED = glfwInit();
            GLFWErrorCallback.createPrint( System.err ).set();
        }
    }

    public static void DisableGLFW() {
        if( GLFW_ENABLED ) {
            GLFW_ENABLED = false;

            glfwTerminate();
            glfwSetErrorCallback( null ).free();
        }
    }
}
