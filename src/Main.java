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
import engine.rendering.graphics2d.Sprite;
import engine.rendering.graphics2d.SpriteRenderer;
import engine.rendering.camera.OrthographicCamera;
import engine.rendering.material.Texture;
import engine.rendering.shader.ShaderProgram;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {


    public static void main( String[] args ) {
        VideoSettings settings = VideoSettings.LoadFromFile();

        Global.EnableGLFW();
        Window window = new Window( settings.title, settings.width, settings.height, settings.vsync );


        Texture t = new Texture( "assets/textures/Kirby.png" );
        Sprite sprite1 = new Sprite( t );
        sprite1.scale( -0.5f, -0.5f, -0.5f );
        sprite1.translate( -0.5f, 0, 0 );

        Sprite sprite2 = new Sprite( t );
        sprite2.scale( -0.5f, -0.5f, -0.5f );
        sprite2.translate( 0.5f, 0, 0 );

        SpriteRenderer spriteRenderer = new SpriteRenderer();
        OrthographicCamera camera = new OrthographicCamera();
        ShaderProgram shader = new ShaderProgram( "assets/shaders/sample.vert", "assets/shaders/sample.frag" );

        glClearColor( 0.4f, 0.4f, 0.4f, 1.0f );
        glEnable( GL_BLEND );
        glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );

        while( !glfwWindowShouldClose( window.getNativeID() ) ) {
            glfwPollEvents();
            glClear( GL_COLOR_BUFFER_BIT );

            spriteRenderer.begin( camera );
            spriteRenderer.setShader( shader );

            spriteRenderer.draw( sprite1 );
            spriteRenderer.draw( sprite2 );

            spriteRenderer.end();

            glfwSwapBuffers( window.getNativeID() );
        }

        t.dispose();
//        sprite.dispose();
//        spriteRenderer.dispose();
//        camera.dispose();
        shader.dispose();

        Global.DisableGLFW();
    }
}
