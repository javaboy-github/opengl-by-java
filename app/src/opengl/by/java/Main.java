package opengl.by.java;

// LWJGL3

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.swing.text.Position;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.file.Paths;
import java.nio.file.Files;


public class Main {
    // windowの値
    private long window;

    // 幅と高さ
    static final int width = 1280;
    static final int height = 960;

    public static void main(String[] args) {
        final Main main = new Main();
        main.run();
    }

    private void run() {
        init();
        loop();

        // 後始末
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("GLFWを初期化できません");
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        window = glfwCreateWindow(width, height, "OpenGL by java", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("GLFWウィンドウを作成できません");
        }

        glfwSetKeyCallback(
            window, (window, key, scancode, action, mods
        ) ->{
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose( window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                window,
                (vidmode.width() - pWidth.get(0)) / 2,
                (vidmode.height() - pHeight.get(0)) / 2
            );
        };

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();
        float[] cubeVertex = {
          -1.0f, -1.0f, -1.0f,  0.0f,  0.0f,  0.0f,  // (0)
          -1.0f, -1.0f,  1.0f,  0.0f,  0.0f,  0.8f,  // (1)
          -1.0f,  1.0f,  1.0f,  0.0f,  0.8f,  0.0f,  // (2)
          -1.0f,  1.0f, -1.0f,  0.0f,  0.8f,  0.8f,  // (3)
           1.0f,  1.0f, -1.0f,  0.8f,  0.0f,  0.0f,  // (4)
           1.0f, -1.0f, -1.0f,  0.8f,  0.0f,  0.8f,  // (5)
           1.0f, -1.0f,  1.0f,  0.8f,  0.8f,  0.0f,  // (6)
           1.0f,  1.0f,  1.0f,  0.8f,  0.8f,  0.8f    // (7)
        };
        int[] solidCubeIndex = {
            0, 1, 2, 0, 2, 3, // 左
            0, 3, 4, 0, 4, 5, // 裏
            0, 5, 6, 0, 6, 1, // 下
            7, 6, 5, 7, 5, 4, // 右
            7, 4, 3, 7, 3, 2, // 上
            7, 2, 1, 7, 1, 6  // 前
        };
        Triangle[] triangles = {
            new Triangle(3, 8, cubeVertex, 36, solidCubeIndex)
        };

        // Create program
        var program = glCreateProgram();
        int vobj = glCreateShader(GL_VERTEX_SHADER);
        var vertexShaderSourceFile = Paths.get("src/main.vert");
        String vertexShaderSource = null;
        try {
            vertexShaderSource = Files.readString(vertexShaderSourceFile);
        } catch(IOException e) {
            System.err.println("Failed to load main.vert.");
            System.exit(1);
        }
        glShaderSource(vobj, vertexShaderSource, "");
        glCompileShader(vobj);
        var status = new int[1];
        glGetShaderiv(vobj, GL_COMPILE_STATUS, status);
        if (status[0] == GL_FALSE){
            var bufSize = new int[1];
            glGetShaderiv(vobj, GL_INFO_LOG_LENGTH, bufSize);
            if (bufSize[0] > 1) {
                throw new RuntimeException("vertex shaderのコンパイルに失敗しました。エラーメッセージ:"
                    + glGetShaderInfoLog(vobj, bufSize[0]));
            }
            else throw new RuntimeException("vertex shaderのコンパイルに失敗しました");
        }
        glAttachShader(program, vobj);
        glDeleteShader(vobj);
        int fobj = glCreateShader(GL_FRAGMENT_SHADER);
        var fragmentShaderSourceFile = Paths.get("src/main.frag");
        String fragmentShaderSource = null;
        try {
            fragmentShaderSource = Files.readString(fragmentShaderSourceFile);
        } catch(IOException e) {
            System.err.println("Failed to load main.frag.");
            System.exit(1);
        }
        glShaderSource(fobj, fragmentShaderSource, "");
        glCompileShader(fobj);
        status = new int[1];
        glGetShaderiv(vobj, GL_COMPILE_STATUS, status);
        if (status[0] == GL_FALSE){
            var bufSize = new int[1];
            glGetShaderiv(vobj, GL_INFO_LOG_LENGTH, bufSize);
            if (bufSize[0] > 1) {
                throw new RuntimeException("fragment shaderのコンパイルに失敗しました。エラーメッセージ:"
                    + glGetShaderInfoLog(vobj, bufSize[0]));
            }
            else throw new RuntimeException("vertex shaderのコンパイルに失敗しました");
        }
        glAttachShader(program, fobj);
        glDeleteShader(fobj);
        glBindAttribLocation(program, 0, "position");
        glBindFragDataLocation(program, 0, "fragment");
        glLinkProgram(program);
        glUseProgram(program);

        var modelViewLoc = glGetUniformLocation(program, "modelview");
        var projectionLoc = glGetUniformLocation(program, "projection");
        var tLoc = glGetUniformLocation(program, "t");

        glClearColor(0, 0, 0, 0);
        glEnable(GL_TEXTURE_2D); //テクスチャ表示
        glEnable(GL_DEPTH_TEST); // 重ならない

        var pointOfView = new Vector3f(0, 0, 0);

        float t = 0;

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (glfwGetKey(window, GLFW_KEY_W) != GLFW_RELEASE) pointOfView.y += 0.01;
            if (glfwGetKey(window, GLFW_KEY_S) != GLFW_RELEASE) pointOfView.y -= 0.01;
            if (glfwGetKey(window, GLFW_KEY_D) != GLFW_RELEASE) pointOfView.x += 0.01;
            if (glfwGetKey(window, GLFW_KEY_A) != GLFW_RELEASE) pointOfView.x -= 0.01;
            if (glfwGetKey(window, GLFW_KEY_SPACE) != GLFW_RELEASE) pointOfView.z += 0.01;
            if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) != GLFW_RELEASE) pointOfView.z -= 0.01;

            var modelview = AffineTransformHelper.lookAt(
                pointOfView,
                // new Vector3f(-1, -1, -1), // target
                new Vector3f(pointOfView).add(-1, -1, -1),
                new Vector3f(0, 1, 0)     // up
            );

            // glUniformMatrix4fv(modelViewLoc, false, pointer);
            glUniformMatrix4fv(modelViewLoc, true, modelview.get(new float[16]));
            var projection = AffineTransformHelper.frustum(-width / 2f, width / 2f, -height / 2f, height / 2f, 1f, 10f);
            System.out.println(projection);
            glUniformMatrix4fv(projectionLoc, true, projection.get(new float[16]));
            glUniform1f(tLoc, (float) t);
            for (Triangle triangle : triangles)
                triangle.draw();
            glfwSwapBuffers(window);
            glfwPollEvents();
            t+=0.1;
        }
        for (Triangle triangle : triangles)
            triangle.close();
    }
}
