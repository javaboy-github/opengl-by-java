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

import java.nio.file.Paths;
import java.nio.file.Files;


public class Main {
    // windowの値
    private long window;

    public static void main(String[] args) {
        final Main main = new Main();
        main.run();
    }

    private void run() {
        init();
        loop();
        terminate();
    }

    private void terminate() {
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
        var width = 1260;
        var height = 960;
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
        
        Box[] boxes = {
            new Box(new Vec3(0, 0, 0))
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
        glBindAttribLocation(program, 1, "color");
        glBindFragDataLocation(program, 0, "fragment");
        glLinkProgram(program);
        glUseProgram(program);

        var modelViewLoc = glGetUniformLocation(program, "modelview");
        var projectionLoc = glGetUniformLocation(program, "projection");
        var tLoc = glGetUniformLocation(program, "t");

        glClearColor(1, 1, 1, 0);
        glEnable(GL_TEXTURE_2D); //テクスチャ表示
        glEnable(GL_DEPTH_TEST); // 重ならない

        // veiwport
        var size = new int[]{1260, 960}; // size[0] is width and size[1] is height
        glfwSetWindowSizeCallback(window, (window, width, height) -> {
            glViewport(0, 0, width, height);
            size[0] = width * 4;
            size[1] = height * 4;
        });
        glViewport(0, 0, size[0], size[1]);

        var pointOfView = new Vec3(3, 4, 5);

        float t = 0;

		final var foward = new Vec3(0.01f, 0, 0);
		final var up = new Vec3(0, 0.01f, 0);
		final var right = new Vec3(0, 0, 0.01f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (glfwGetKey(window, GLFW_KEY_W) != GLFW_RELEASE) pointOfView = pointOfView.plus(right);
            if (glfwGetKey(window, GLFW_KEY_S) != GLFW_RELEASE) pointOfView = pointOfView.minus(right);
            if (glfwGetKey(window, GLFW_KEY_D) != GLFW_RELEASE) pointOfView = pointOfView.plus(foward);
            if (glfwGetKey(window, GLFW_KEY_A) != GLFW_RELEASE) pointOfView = pointOfView.minus(foward);
            if (glfwGetKey(window, GLFW_KEY_SPACE) != GLFW_RELEASE) pointOfView = pointOfView.plus(up);
            if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) != GLFW_RELEASE) pointOfView = pointOfView.minus(up);

            var width = size[0];
            var height = size[1];
            var model = AffineTransformHelper.scale(1 / width, 1 / height, 1);
            var view = AffineTransformHelper.lookAt(
                pointOfView,
                // new Vec3(-1, -1, -1), // target
                pointOfView.plus(new Vec3(-3, -4, -5)),
                new Vec3(0, 1, 0)     // up
            );
            // var modelview = model.mul(view);
            var modelview = view;

            // glUniformMatrix4fv(modelViewLoc, false, pointer);
            glUniformMatrix4fv(modelViewLoc, true, modelview.toArray());
            var projection = AffineTransformHelper.frustum(1f, width / height, 1f, 10f);
            glUniformMatrix4fv(projectionLoc, true, projection.toArray());
            glUniform1f(tLoc, (float) t);
            for (Box box : boxes)
                box.draw();
            glfwSwapBuffers(window);
            glfwPollEvents();
            t+=0.1;
        }
        for (Box box : boxes)
            box.close();
    }
}
