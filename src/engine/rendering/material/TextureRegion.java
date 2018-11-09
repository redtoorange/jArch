package engine.rendering.material;

import static org.lwjgl.opengl.GL40.*;

public class TextureRegion {
    private Texture texture;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;

    public void bind(){
        texture.bind();
    }
}
