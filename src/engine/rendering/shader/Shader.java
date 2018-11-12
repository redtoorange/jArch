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

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;

import static org.lwjgl.opengl.GL40.*;

public class Shader implements Disposable {
    private ShaderType type;
    private String shaderSource;
    private int shaderID;
    private boolean valid = false;

    /**
     * Create a new {@link Shader} from a shader source file.
     * @param filePath {@link String} filepath to load the shader's source from.
     * @param type     The {@link ShaderType} of this shader.
     */
    public Shader( String filePath, ShaderType type ) {
        this.type = type;

        if( readFileSource( filePath ) ) {
            compileShaderSource();

            if( errorsPresent() ) {
                System.err.println( getErrors() );
            }
            else {
                valid = true;
            }
        }
    }


    /**
     * Open a file and load the text from it as this Shader's source code.
     * @param filePath {@link String} filepath to load the shader's source from.
     * @return True if the file was read successfully.
     */
    private boolean readFileSource( String filePath ) {
        boolean success = false;

        try {
            Scanner fileScanner = new Scanner( new File( filePath ) );
            StringBuilder builder = new StringBuilder();

            while( fileScanner.hasNextLine() ) {
                builder.append( fileScanner.nextLine() + "\n" );
            }

            shaderSource = builder.toString();
            fileScanner.close();
            success = true;
        }
        catch( FileNotFoundException fnf ) {
            System.err.println( "Error reading shader file <" + filePath + ">;" );
            System.err.println( fnf );
        }

        return success;
    }


    /**
     * Call the GL code to compile this shader and load it onto the GPU.
     */
    private void compileShaderSource() {
        if( type == ShaderType.FRAGMENT ) {
            shaderID = glCreateShader( GL_FRAGMENT_SHADER );
        }
        else if( type == ShaderType.VERTEX ) {
            shaderID = glCreateShader( GL_VERTEX_SHADER );
        }

        glShaderSource( shaderID, shaderSource );
        glCompileShader( shaderID );
    }

    /**
     * @return Were there errors in the compiling process?
     */
    private boolean errorsPresent() {
        boolean errors = false;

        try( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer pInt = stack.mallocInt( 1 );
            glGetShaderiv( shaderID, GL_COMPILE_STATUS, pInt );

            if( pInt.get() != GL_TRUE ) {
                errors = true;
            }
        }

        return errors;
    }

    /**
     * @return {@link String} containing compiler errors.
     */
    private String getErrors() {
        StringBuilder error = new StringBuilder();

        try( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer pLength = stack.mallocInt( 1 );
            ByteBuffer pBuffer = stack.malloc( 512 );
            glGetShaderInfoLog( shaderID, pLength, pBuffer );

            for( int i = 0; i < pLength.get( 0 ); i++ ) {
                error.append( ( char ) pBuffer.get( i ) );
            }
        }

        return error.toString();
    }

    /**
     * @return int native ID for this {@link Shader} on the GPU.
     */
    public int getShaderID() {
        return shaderID;
    }

    /**
     * @return Is this {@link Shader} a valid gl shader.
     */
    public boolean isValid() {
        return valid;
    }


    /**
     * Delete this {@link Shader} from the GPU.
     */
    @Override
    public void dispose() {
        glDeleteShader( shaderID );
    }
}
