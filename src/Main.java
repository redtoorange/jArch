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

import engine.Global;
import engine.VideoSettings;
import engine.Window;
import engine.rendering.material.Texture;
import engine.rendering.mesh.Mesh;
import engine.rendering.shader.ShaderProgram;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    private static float[] vertices = {
            0.5f, 0.5f, 0.0f,       // top right
            0.5f, -0.5f, 0.0f,      // bottom right
            -0.5f, -0.5f, 0.0f,     // bottom left
            -0.5f, 0.5f, 0.0f       // top left
    };

    private static int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    private static float[] uvData = {
            1.0f, 0.0f,     // top right
            1.0f, 1.0f,     // bottom right
            0.0f, 1.0f,     // bottom left
            0.0f, 0.0f      // top left
    };

    public static void main( String[] args ) {
        VideoSettings settings = VideoSettings.LoadFromFile();

        Global.EnableGLFW();
        Window window = new Window( settings.title, settings.width, settings.height, settings.vsync );

        ShaderProgram sp = new ShaderProgram( "assets/shaders/sample.vert", "assets/shaders/sample.frag" );
        Mesh m = new Mesh( indices, vertices, uvData );
        Texture t = new Texture( "assets/textures/Kirby.png" );

        glClearColor( 0.4f, 0.4f, 0.4f, 1.0f );

        glEnable( GL_BLEND );
        glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );

        while( !glfwWindowShouldClose( window.getNativeID() ) ) {
            glfwPollEvents();


            glClear( GL_COLOR_BUFFER_BIT );

            t.bind();
            sp.bind();
            m.draw();

            glfwSwapBuffers( window.getNativeID() );
        }

        t.dispose();
        sp.dispose();
        m.dispose();

        Global.DisableGLFW();
    }
}
