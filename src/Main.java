import engine.Global;
import engine.VideoSettings;
import engine.Window;
import engine.rendering.material.Texture;
import engine.rendering.mesh.Mesh;
import engine.rendering.shader.ShaderProgram;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    private static float[] vertices = {
            0.5f, 0.5f, 0.0f,       // top right
            0.5f, -0.5f, 0.0f,      // bottom right
            -0.5f, -0.5f, 0.0f,     // bottom left
            -0.5f, 0.5f, 0.0f       // top left
    };

    private static int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    private static float[] uvData = {
            1.0f, 0.0f,     // top right
            1.0f, 1.0f,     // bottom right
            0.0f, 1.0f,     // bottom left
            0.0f, 0.0f      // top left
    };

    public static void main(String[] args) {
        VideoSettings settings = VideoSettings.LoadFromFile();

        Global.EnableGLFW();
        Window window = new Window(settings.title, settings.width, settings.height, settings.vsync);

        ShaderProgram sp = new ShaderProgram("assets/shaders/sample.vert", "assets/shaders/sample.frag");
        Mesh m = new Mesh(indices, vertices, uvData);
        Texture t = new Texture("assets/textures/Kirby.png");

        glClearColor(0.4f, 0.4f, 0.4f, 1.0f);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        while (!glfwWindowShouldClose(window.getNativeID())) {
            glfwPollEvents();


            glClear(GL_COLOR_BUFFER_BIT);

            t.bind();
            sp.bind();
            m.draw();

            glfwSwapBuffers(window.getNativeID());
        }

        t.dispose();
        sp.dispose();
        m.dispose();

        Global.DisableGLFW();
    }
}
