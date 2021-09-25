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
        glShaderSource(vobj, """
#version 150

in vec4 position;
uniform mat4 modelview;

void main()
{
    gl_Position = modelview * position;
}
""");
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
        glShaderSource(fobj, """
#version 150 core

out vec4 fragment;

void main()
{
    fragment = vec4(1.0, 0.0, 0.0, 1.0);
}
        """, "");
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

        var camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(0, 1, 0));


        glClearColor(0, 0, 0, 0);
        glEnable(GL_TEXTURE_2D); //テクスチャ表示
        glEnable(GL_DEPTH_TEST); // 重ならない

        var pointOfView = new Vector3f(0, 0, 0);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (glfwGetKey(window, GLFW_KEY_UP) != GLFW_RELEASE) pointOfView.y += 1;
            if (glfwGetKey(window, GLFW_KEY_DOWN) != GLFW_RELEASE) pointOfView.y -= 1;

            float[] array = new float[16];
            camera.viewMatrix().get(array);
            var pointer = FloatBuffer.wrap(array, 0, array.length);
            var modelview = AffineTransformHelper.lookAt(
                new Vector3f(0, 0, 0),    // position
                new Vector3f(-1, -1, -1), // target
                new Vector3f(0, 1, 0)     // up
            );

            // glUniformMatrix4fv(modelViewLoc, false, pointer);
            var data = modelview.get(new float[16]);
            glUniformMatrix4fv(modelViewLoc, false, data);
            for (Triangle triangle : triangles)
                triangle.draw();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        for (Triangle triangle : triangles)
            triangle.close();
    }
}
