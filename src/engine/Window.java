package engine;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;


public class Window {
    private long window;

    /**
     * Create a new GLFW Window with an OpenGL context.
     * @param title  {@link String} to display as the window's title.
     * @param width  int width of the window.
     * @param height int height of the window.
     * @param vsync  True to run the window with vertical sync, False to disable it.
     */
    public Window( String title, int width, int height, boolean vsync ) {
        glfwDefaultWindowHints();

        glfwWindowHint( GLFW_VISIBLE, GLFW_FALSE );
        glfwWindowHint( GLFW_RESIZABLE, GLFW_TRUE );

        glfwWindowHint( GLFW_CONTEXT_VERSION_MAJOR, 4 );
        glfwWindowHint( GLFW_CONTEXT_VERSION_MINOR, 0 );
        glfwWindowHint( GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE );

        window = glfwCreateWindow( width, height, title, 0, 0 );

        glfwMakeContextCurrent( window );

        if( vsync ) {
            glfwSwapInterval( 1 );
        }
        else {
            glfwSwapInterval( 0 );
        }

        glfwShowWindow( window );
        GL.createCapabilities();
    }

    /**
     * Get the GLFW window pointer for this window.
     * @return long GLFW window pointer.
     */
    public long getNativeID() {
        return window;
    }
}
