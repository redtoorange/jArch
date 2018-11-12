package engine.rendering.material;

public class TextureRegion {
    private Texture texture;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;

    public void bind() {
        texture.bind();
    }
}
