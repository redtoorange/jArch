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
package engine.rendering;

import engine.rendering.material.Texture;
import engine.rendering.shader.ShaderProgram;
import org.joml.Matrix4f;

import java.util.ArrayList;

public class SpriteRenderer {
    private ShaderProgram currentShader;
    private Texture currentTexture;
    private ArrayList< Sprite > sprites;
    private Matrix4f view;
    private Matrix4f proj;


    public SpriteRenderer() {
        sprites = new ArrayList< Sprite >();
    }

    public void begin( Matrix4f view, Matrix4f proj ) {
        currentShader = null;
        currentTexture = null;
        sprites.clear();

        this.view = view;
        this.proj = proj;
    }

    public void setShader( ShaderProgram sp ) {
        if( sp != currentShader ) {
            flush();
        }

        currentShader = sp;
    }

    public void draw( Sprite sprite ) {
        if( sprite.getSpriteTexture() != currentTexture ) {
            flush();
            currentTexture = sprite.getSpriteTexture();
        }

        sprites.add( sprite );
    }

    private void flush() {
        if( sprites.isEmpty() || currentTexture == null || currentShader == null ) {
            return;
        }

        currentTexture.bind();
        currentShader.bind();
        //currentShader.setUniformMat4f("projection", proj);
        //currentShader.setUniformMat4f("view", view);

        for( Sprite s : sprites ) {
            //currentShader.setUniformMat4f("transform", s.getModelTransform());
            s.render();
        }
    }

    public void end() {

    }
}
