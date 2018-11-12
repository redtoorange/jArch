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
package engine.rendering.material;

import engine.util.Disposable;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

/**
 * Wrapper for an OpenGL Texture that is loaded to the GPU.  The texture is kept in local memory to allow it's use
 * by TextureRegions.
 */
public class Texture implements Disposable {
    private int textureID;
    private int width;
    private int height;
    private int channels;

    /** Maintain a copy of the texture in RAM so we can use if for texture regions. */
    private ByteBuffer imageData;

    /** Have mipmaps been created for this texture yet? */
    private boolean mips = false;

    public Texture( String texturePath ) {
        try( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer pWidth = stack.mallocInt( 1 );
            IntBuffer pHeight = stack.mallocInt( 1 );
            IntBuffer pChannels = stack.mallocInt( 1 );

            // Load texture
            imageData = stbi_load( texturePath, pWidth, pHeight, pChannels, 0 );
            width = pWidth.get( 0 );
            height = pHeight.get( 0 );
            channels = pChannels.get( 0 );

            // Generate Texture
            IntBuffer pTexID = stack.mallocInt( 1 );
            glGenTextures( pTexID );
            textureID = pTexID.get( 0 );

            // Load data to the GPU
            bind();

            glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
            glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );
            glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
            glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );

            if( channels >= 4 ) {
                glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData );
            }
            else {
                glTexImage2D( GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, imageData );
            }

            unbind();
        }
    }

    /** Bind the texture to GL_TEXTURE0. */
    public void bind() {
        glActiveTexture( GL_TEXTURE0 );
        glBindTexture( GL_TEXTURE_2D, textureID );
    }

    /**
     * Bind the texture to the specified GL_TEXTURE
     * @param index bind on GL_TEXTURE0 + index
     */
    public void bind( int index ) {
        glActiveTexture( GL_TEXTURE0 + index );
        glBindTexture( GL_TEXTURE_2D, textureID );
    }


    /** Unbind the texture on GL_TEXTURE0. */
    public void unbind() {
        glActiveTexture( GL_TEXTURE0 );
        glBindTexture( GL_TEXTURE_2D, 0 );
    }

    /**
     * Unbind the texture on the specified GL_TEXTURE
     * @param index unbind on GL_TEXTURE0 + index
     */
    public void unbind( int index ) {
        glActiveTexture( GL_TEXTURE0 + index );
        glBindTexture( GL_TEXTURE_2D, 0 );
    }

    /** Create mipmap for this texture. */
    public void generateMipMap() {
        if( !mips ) {
            bind();
            glGenerateMipmap( GL_TEXTURE_2D );
            unbind();
        }
    }

    /** Get the GL ID of this texture on the GPU. */
    public int getTextureID() {
        return textureID;
    }

    /**
     * Get the image data from RAM for this texture.
     * @return ByteBuffer that contains the image data.
     */
    public ByteBuffer getImageData() {
        return imageData;
    }

    /** Delete this texture from RAM and the GPU. */
    @Override
    public void dispose() {
        stbi_image_free( imageData );
        glDeleteTextures( textureID );
    }
}
