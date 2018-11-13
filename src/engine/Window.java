/******************************************************************************
 * Copyright 2018 Andrew James McGuiness
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *****************************************************************************/
package engine;

import org.lwjgl.opengl.GL;

import java.util.Locale;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;


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

        String OS = System.getProperty( "os.name", "generic" ).toLowerCase( Locale.ENGLISH );
        if( (OS.indexOf( "mac" ) >= 0) || (OS.indexOf( "darwin" ) >= 0) ) {
            glfwWindowHint( GLFW_CONTEXT_VERSION_MAJOR, 3 );
            glfwWindowHint( GLFW_CONTEXT_VERSION_MINOR, 2 );
            glfwWindowHint( GLFW_OPENGL_PROFILE, GLFW_OPENGL_FORWARD_COMPAT );
        }
        else if( OS.indexOf( "win" ) >= 0 ) {
            glfwWindowHint( GLFW_CONTEXT_VERSION_MAJOR, 4 );
            glfwWindowHint( GLFW_CONTEXT_VERSION_MINOR, 0 );
            glfwWindowHint( GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE );
        }
        else if( OS.indexOf( "nux" ) >= 0 ) {
            glfwWindowHint( GLFW_CONTEXT_VERSION_MAJOR, 3 );
            glfwWindowHint( GLFW_CONTEXT_VERSION_MINOR, 2 );
            glfwWindowHint( GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE );
        }

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

        glfwSetWindowSizeCallback( window, this::handleResize );
    }

    /**
     * Get the GLFW window pointer for this window.
     * @return long GLFW window pointer.
     */
    public long getNativeID() {
        return window;
    }

    private void handleResize( long window, int width, int height ) {
        glViewport( 0, 0, width, height );
    }
}
