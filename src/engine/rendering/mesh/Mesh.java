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
package engine.rendering.mesh;

import engine.util.Disposable;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL40.*;

public class Mesh implements Disposable {
    private int meshVAO;
    private int IBO;
    private int posVBO;
    private int uvVBO;
    private int vertCount = 0;

    public Mesh( int[] indexData, float[] posData, float[] uvData ) {
        // Create the VAO
        meshVAO = generateVAO();
        glBindVertexArray( meshVAO );

        // Buffer the data
        // Indices
        IBO = generateVBO();
        bufferIndexData( indexData, IBO );

        // Vertex Pos
        posVBO = generateVBO();
        bufferFloatData( posData, posVBO, 3, 0 );

        // UV coords
        uvVBO = generateVBO();
        bufferFloatData( uvData, uvVBO, 2, 1 );

        // Cleanup
        glBindBuffer( GL_ARRAY_BUFFER, 0 );
        glBindVertexArray( 0 );
    }

    private int generateVAO() {
        try( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer pInt = stack.mallocInt( 1 );
            glGenVertexArrays( pInt );
            return pInt.get( 0 );
        }
    }

    private int generateVBO() {
        try( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer pInt = stack.mallocInt( 1 );
            glGenBuffers( pInt );
            return pInt.get( 0 );
        }
    }

    private void bufferIndexData( int[] data, int bufferObject ) {
        try( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer dataBuffer = stack.mallocInt( data.length );
            dataBuffer.put( data );
            dataBuffer.flip();

            glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, bufferObject );
            glBufferData( GL_ELEMENT_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW );
            vertCount = data.length;
        }
    }

    private void bufferFloatData( float[] data, int bufferObject, int dataSize, int attribLocation ) {
        try( MemoryStack stack = MemoryStack.stackPush() ) {
            FloatBuffer dataBuffer = stack.mallocFloat( data.length );
            dataBuffer.put( data );
            dataBuffer.flip();

            glBindBuffer( GL_ARRAY_BUFFER, bufferObject );
            glBufferData( GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW );
            glVertexAttribPointer( attribLocation, dataSize, GL_FLOAT, false, 0, 0 );
            glEnableVertexAttribArray( attribLocation );
        }
    }

    public void draw() {
        glBindVertexArray( meshVAO );
        glDrawElements( GL_TRIANGLES, vertCount, GL_UNSIGNED_INT, 0 );
    }

    @Override
    public void dispose() {
        glDeleteBuffers( posVBO );
        glDeleteBuffers( uvVBO );

        glDeleteVertexArrays( meshVAO );
    }
}
