package engine.rendering.material;

import engine.util.Disposable;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL40.*;

public class Texture implements Disposable {
    private int textureID;
    private int width;
    private int height;
    private int channels;

    public Texture(String texturePath) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            IntBuffer pChannels = stack.mallocInt(1);

            // Load texture
            ByteBuffer data = stbi_load(texturePath, pWidth, pHeight, pChannels, 0);
            width = pWidth.get(0);
            height = pHeight.get(0);
            channels = pChannels.get(0);

            // Generate Texture
            IntBuffer pTexID = stack.mallocInt(1);
            glGenTextures(pTexID);
            textureID = pTexID.get(0);

            // Load data to the GPU
            bind();

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
            unbind();

            // Free texture
            stbi_image_free(data);
        }
    }

    public void bind(){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind(){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getTextureID(){
        return textureID;
    }

    @Override
    public void dispose() {
        glDeleteTextures(textureID);
    }
}
