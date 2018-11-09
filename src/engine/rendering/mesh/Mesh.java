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

    public Mesh(int[] indexData, float[] posData, float[] uvData) {
        // Create the VAO
        meshVAO = generateVAO();
        glBindVertexArray(meshVAO);

        // Buffer the data
        // Indices
        IBO = generateVBO();
        bufferIndexData(indexData, IBO);

        // Vertex Pos
        posVBO = generateVBO();
        bufferFloatData(posData, posVBO, 3, 0);

        // UV coords
        uvVBO = generateVBO();
        bufferFloatData(uvData, uvVBO, 2, 1);

        // Cleanup
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private int generateVAO() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pInt = stack.mallocInt(1);
            glGenVertexArrays(pInt);
            return pInt.get(0);
        }
    }

    private int generateVBO() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pInt = stack.mallocInt(1);
            glGenBuffers(pInt);
            return pInt.get(0);
        }
    }

    private void bufferIndexData(int[] data, int bufferObject) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer dataBuffer = stack.mallocInt(data.length);
            dataBuffer.put(data);
            dataBuffer.flip();

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferObject);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
            vertCount = data.length;
        }
    }

    private void bufferFloatData(float[] data, int bufferObject, int dataSize, int attribLocation) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer dataBuffer = stack.mallocFloat(data.length);
            dataBuffer.put(data);
            dataBuffer.flip();

            glBindBuffer(GL_ARRAY_BUFFER, bufferObject);
            glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(attribLocation, dataSize, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(attribLocation);
        }
    }

    public void draw() {
        glBindVertexArray(meshVAO);
        glDrawElements(GL_TRIANGLES, vertCount, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void dispose() {
        glDeleteBuffers(posVBO);
        glDeleteBuffers(uvVBO);

        glDeleteVertexArrays(meshVAO);
    }
}
