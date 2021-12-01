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

<<<<<<< HEAD
import static java.lang.Math.cos;
import static java.lang.Math.sin;
=======
import static opengl.by.java.AffineTransformHelper.*;
>>>>>>> 0352bf6bfc8936e1fdcf76010f59598e7d6dc407

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
            new Box(new Vec3(0, 0, 0)),
            new Box(new Vec3(4, 0, 0)),
        };

        // Create program
        var program = Program.createFromSourcefile("src/main.vert", "src/main.frag");
        var programId = program.program;
        glBindAttribLocation(programId, 0, "position");
        glBindAttribLocation(programId, 1, "color");
        glBindFragDataLocation(programId, 0, "fragment");
        program.link();
        program.use();

        var modelViewLoc = glGetUniformLocation(programId, "modelview");
        var projectionLoc = glGetUniformLocation(programId, "projection");
        var tLoc = glGetUniformLocation(programId, "t");

        glClearColor(1, 1, 1, 0);
        glEnable(GL_TEXTURE_2D); //テクスチャ表示
        glEnable(GL_DEPTH_TEST); // 重ならない

        // veiwport
        var size = new int[]{1260, 770}; // size[0] is width and size[1] is height
        var size2 = new int[]{1260, 770};
        float[] cursorPos = {size[0], size[1]};
        org.lwjgl.glfw.GLFWWindowSizeCallbackI resize = (window, width, height) -> {
            size2[0] = width;
            size2[1] = height;
            int[] widthPointer = new int[1];
            int[] heightPointer = new int[1];
            glfwGetFramebufferSize(window, widthPointer, heightPointer);
            size[0] = widthPointer[0];
            size[1] = heightPointer[0];
            glViewport(0, 0, widthPointer[0], heightPointer[0]);
        };
        glfwSetWindowSizeCallback(window, resize);
        resize.invoke(window, size[0], size[1]);
        glViewport(0, 0, size[0], size[1]);


        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
        glfwSetCursorPosCallback(window, (window, x, y) -> {
            cursorPos[0] = ((float) x / size2[0] * 2 - 1);
            cursorPos[1] = ((float) y / size2[1] * 2 - 1);
        });


        var position = new Vec3(3, 4, 5);
        float t = 0;
        final var up = new Vec3(0, 1, 0);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            var pointOfView = 
              new Vec3(
                  (float) cos(yaw) * (float) Math.cos(pitch),
                  (float) sin(pitch),
                  (float) sin(yaw) * (float) Math.cos(pitch)
              ).normalize();
            if (glfwGetKey(window, GLFW_KEY_W) != GLFW_RELEASE) position = position.plus(pointOfView.scala(0.5f));
            if (glfwGetKey(window, GLFW_KEY_S) != GLFW_RELEASE) position = position.minus(pointOfView.scala(0.5f));
            if (glfwGetKey(window, GLFW_KEY_A) != GLFW_RELEASE) position = position.plus(new Vec3(pointOfView.y, pointOfView.x, 0).scala(0.5f));
            if (glfwGetKey(window, GLFW_KEY_D) != GLFW_RELEASE) position = position.minus(new Vec3(pointOfView.y, pointOfView.x, 0).scala(0.5f));
            if (glfwGetKey(window, GLFW_KEY_SPACE) != GLFW_RELEASE) position = position.plus(up);
            if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) != GLFW_RELEASE) position = position.minus(up);

            var width = size[0];
            var height = size[1];
            // var model = AffineTransformHelper.translate(0,  (t * t + 10) % 20 - 10, 0); // 単位行列
            /*var model = AffineTransformHelper.rotateByYAxis(t)
                .mul(AffineTransformHelper.translate((float) Math.sin(t) * 3, 0, (float) Math.cos(t) * 3));
            */var model = translate(0, 0, 0);

            var view = lookAt(
                position, // position
                pointOfView.plus(position), // point of view
                new Vec3(0, 1, 0)     // up
            );
            // var modelview = model.mul(view);
            var modelview = view.mul(model);

            // glUniformMatrix4fv(modelViewLoc, false, pointer);
            glUniformMatrix4fv(modelViewLoc, true, modelview.toArray());
            var projection = frustum(1f, (float) width / (float) height, 0.1f, 100f);
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
