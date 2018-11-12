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
package engine.rendering.shader;

import engine.util.Disposable;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL40.*;

public class ShaderProgram implements Disposable {
    private int shaderProgramID;
    private boolean valid = false;

    /**
     * Create a new ShaderProgram form existing {@link Shader}s.  Does NOT dispose of the {@link Shader}s.
     * @param vertexShader   {@link Shader} to use as the vertex shader.
     * @param fragmentShader {@link Shader} to use as the fragment shader.
     */
    public ShaderProgram( Shader vertexShader, Shader fragmentShader ) {
        process( vertexShader, fragmentShader );
    }

    /**
     * Create a new {@link ShaderProgram} from shader source files.  Loads and disposes of the {@link Shader}s.
     * @param vertexPath   {@link String} to use as the file path to the vertex shader.
     * @param fragmentPath {@link String} to use as the file path to the fragment shader.
     */
    public ShaderProgram( String vertexPath, String fragmentPath ) {
        Shader vertexShader = new Shader( vertexPath, ShaderType.VERTEX );
        Shader fragmentShader = new Shader( fragmentPath, ShaderType.FRAGMENT );

        process( vertexShader, fragmentShader );

//        vertexShader.dispose();
//        fragmentShader.dispose();
    }


    /**
     * Validate {@link Shader}s and link them, output errors if needed.
     * @param vertexShader   {@link Shader} to use as the vertex shader.
     * @param fragmentShader {@link Shader} to use as the fragment shader.
     */
    private void process( Shader vertexShader, Shader fragmentShader ) {
        if( vertexShader.isValid() && fragmentShader.isValid() ) {
            linkProgram( vertexShader, fragmentShader );

            if( errorsPresent() ) {
                System.err.println( getErrors() );
            }
            else {
                valid = true;
            }
        }
    }

    /**
     * Call the GL code to attach and link the shades and shaderProgram
     * @param vertexShader   {@link Shader} to use as the vertex shader.
     * @param fragmentShader {@link Shader} to use as the fragment shader.
     */
    private void linkProgram( Shader vertexShader, Shader fragmentShader ) {
        shaderProgramID = glCreateProgram();
        glAttachShader( shaderProgramID, vertexShader.getShaderID() );
        glAttachShader( shaderProgramID, fragmentShader.getShaderID() );
        glLinkProgram( shaderProgramID );
    }


    /**
     * @return Were there errors in the linking process?
     */
    private boolean errorsPresent() {
        boolean errors = false;

        try( MemoryStack stack = MemoryStack.stackPush() ) {
            // Check to see if it linked
            IntBuffer pInt = stack.mallocInt( 1 );
            glGetProgramiv( shaderProgramID, GL_LINK_STATUS, pInt );

            // Link failed, see why
            if( pInt.get() == GL_FALSE ) {
                errors = true;
            }
        }

        return errors;
    }

    /**
     * @return {@link String} containing linking errors.
     */
    private String getErrors() {
        StringBuilder error = new StringBuilder();

        try( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer pLength = stack.mallocInt( 1 );
            ByteBuffer pBuffer = stack.malloc( 512 );
            glGetShaderInfoLog( shaderProgramID, pLength, pBuffer );

            for( int i = 0; i < pLength.get( 0 ); i++ ) {
                error.append( ( char ) pBuffer.get( i ) );
            }
        }

        return error.toString();
    }


    /**
     * @return Is this {@link ShaderProgram} a valid GL shader program.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @return GLuint ID of the shader program on the GPU.
     */
    public int getShaderProgramID() {
        return shaderProgramID;
    }

    public void bind() {
        glUseProgram( shaderProgramID );
    }

    /**
     * Delete this {@link ShaderProgram} from the GPU.
     */
    @Override
    public void dispose() {
        glDeleteProgram( shaderProgramID );
    }
}
