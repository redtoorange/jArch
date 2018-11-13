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
package engine.rendering.graphics2d;

import engine.rendering.camera.Camera;
import engine.rendering.material.Texture;
import engine.rendering.shader.ShaderProgram;

import java.util.ArrayList;

public class SpriteRenderer {
    private ShaderProgram currentShader;
    private Texture currentTexture;
    private ArrayList< Sprite > sprites;
    private Camera currentCamera;

    public SpriteRenderer() {
        sprites = new ArrayList< Sprite >();
    }

    public void begin( Camera camera ) {
        currentShader = null;
        currentTexture = null;
        currentCamera = camera;
        sprites.clear();
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
        if( sprites.isEmpty() || currentTexture == null || currentShader == null || currentCamera == null ) {
            return;
        }

        currentTexture.bind();
        currentShader.bind();
        currentShader.setUniformMat4f( "projection", currentCamera.getProjectionMatrix() );
        currentShader.setUniformMat4f( "view", currentCamera.getViewMatrix() );

        for( Sprite s : sprites ) {
            currentShader.setUniformMat4f( "modelTransform", s.getModelTransform() );
            s.render();
        }
    }

    public void end() {
        flush();
        currentShader = null;
        currentTexture = null;
        currentCamera = null;
        sprites.clear();
    }
}
