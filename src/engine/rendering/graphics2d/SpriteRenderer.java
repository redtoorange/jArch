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
    private boolean started;

    public SpriteRenderer() {
        sprites = new ArrayList< Sprite >();
        started = false;
    }

    /**
     * Begin a new Rendering Batch.  The camera passed in will be used for the
     * projection and view matrices for the shader.
     * @param camera        Camera to use for sending matrix data to the shader.
     * @param shaderProgram Shader to use when rendering this batch.
     */
    public void begin( Camera camera, ShaderProgram shaderProgram ) {
        assert !started : "SpriteRenderer already started.";

        started = true;
        currentTexture = null;
        sprites.clear();

        currentShader = shaderProgram;
        currentCamera = camera;
    }

    /**
     * Change the batch's ShaderProgram to a new shader, this will cause the
     * batch to be flushed and rendered using the OLD shader before the new
     * one is set.
     * @param shaderProgram new ShaderProgram for this batch to use.
     */
    public void setShader( ShaderProgram shaderProgram ) {
        if( shaderProgram != currentShader ) {
            flush();
        }

        currentShader = shaderProgram;
    }

    /**
     * Add a Sprite to the rendering batch.  The SpriteRenderer attempts to reduce
     * the number of texture swaps and draw calls.  If the texture for the new Sprite
     * is different then the one used by the rest of the batch, the batch will be flushed
     * and rendered.
     * @param sprite Sprite to add to the batch.
     */
    public void draw( Sprite sprite ) {
        assert started : "SpriteRendered not started.";

        if( sprite.getSpriteTexture() != currentTexture ) {
            flush();
            currentTexture = sprite.getSpriteTexture();
        }

        sprites.add( sprite );
    }

    /** Complete the batch and cleanup. */
    public void end() {
        flush();
        currentShader = null;
        currentTexture = null;
        currentCamera = null;
        sprites.clear();
        started = false;
    }

    /** Validate the rendering state and render all Sprites in the batch. */
    private void flush() {
        if( !isStateValid() ) {
            return;
        }

        prepBatch();

        for( Sprite sprite : sprites )
            renderSprite( sprite );

        sprites.clear();
    }

    /** Bind the Shader and Texture, upload uniforms. */
    private void prepBatch() {
        currentTexture.bind();
        currentShader.bind();
        currentShader.setUniformMat4f( "projection", currentCamera.getProjectionMatrix() );
        currentShader.setUniformMat4f( "view", currentCamera.getViewMatrix() );
    }

    /** Check the state of the rendered is valid. */
    private boolean isStateValid() {
        return started && !sprites.isEmpty() && currentTexture != null && currentShader != null && currentCamera != null;
    }

    /**
     * Make the OpenGL draw call for a Sprite.
     * @param sprite Sprite to render.
     */
    private void renderSprite( Sprite sprite ) {
        currentShader.setUniformMat4f( "modelTransform", sprite.getModelTransform() );
        sprite.render();
    }
}
