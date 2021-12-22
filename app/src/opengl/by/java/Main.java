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

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.acos;
import static opengl.by.java.AffineTransformHelper.*;

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
        
        // Create program
        var program = Program.createFromSourcefile("src/main.vert", "src/main.frag");
        var programId = program.program;
        glBindAttribLocation(programId, 0, "position");
        glBindAttribLocation(programId, 1, "color");
        glBindFragDataLocation(programId, 0, "fragment");
        program.link();

        Box[] boxes = {
            new NormalBox(new Vec3(0, 0, 0), new Vec3(100, 0.1f, 0.1f), Program.createFromSourcefile("src/xyz.vert", "src/x.frag").link()), // x
            new NormalBox(new Vec3(0, 0, 0), new Vec3(0.1f, 100, 0.1f), Program.createFromSourcefile("src/xyz.vert", "src/x.frag").link()), // y
            new NormalBox(new Vec3(0, 0, 0), new Vec3(0.1f, 0.1f, 100), Program.createFromSourcefile("src/xyz.vert", "src/x.frag").link()), // z
            new NormalBox(new Vec3(0, 0, 0), new Vec3(2, 2, 2), program),
            new NormalBox(new Vec3(4, 0, 0), new Vec3(2, 2, 2), program),
        };


        var modelViewLoc = glGetUniformLocation(programId, "modelview");
        var projectionLoc = glGetUniformLocation(programId, "projection");
        var tLoc = glGetUniformLocation(programId, "t");

        glClearColor(1, 1, 1, 0);
        glEnable(GL_TEXTURE_2D); //テクスチャ表示
        glEnable(GL_DEPTH_TEST); // 重ならない

        // veiwport
        var size = new int[]{1260, 770}; // size[0] is width and size[1] is height
        var size2 = new int[]{1260, 770};
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


        boolean[] isFirst = {true};
        double[] cursorPos = {size[0], size[1]};
        double[] offsetPos = {0, 0};
        double[] angle = {100, -0.5};
        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPosCallback(window, (window, x, y) -> {
            if (!isFirst[0]) {
                offsetPos[0] = x - cursorPos[0];
                offsetPos[1] = y - cursorPos[1];

                angle[0] += offsetPos[0] * 0.005;
                angle[1] += offsetPos[1] * 0.005;
            }
            cursorPos[0] = x;
            cursorPos[1] = y;
            isFirst[0] = false;
        });


        var position = new Vec3(3, 4, 5);
        float t = 0;
        final var up = new Vec3(0, 1, 0);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            var yaw = angle[0];
            var pitch = angle[1];

            // 視線
            var pointOfView = 
              new Vec3(
                  (float) cos(yaw) * (float) Math.cos(pitch),
                  (float) sin(pitch),
                  (float) sin(yaw) * (float) Math.cos(pitch)
              ).normalize();
            position = position.plus(move(pointOfView));

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

    boolean isMoveBy3D = false;
    public Vec3 move(Vec3 pointOfView) {
        var result = Vec3.ZERO;

        var speed = 0.5f;

        var tmp = pointOfView.normalize();
        if (glfwGetKey(window, GLFW_KEY_W) != GLFW_RELEASE) // W
            result = result.plus(tmp);
        if (glfwGetKey(window, GLFW_KEY_S) != GLFW_RELEASE) // S
            result = result.minus(tmp);

        var angle = acos(pointOfView.x) + (pointOfView.z > 0 ? 0 : Math.PI);
        // tmp = new Vec3(pointOfView.length() * (float) sin(angle), 0, -pointOfView.length() * (float) cos(angle)).normalize();
        tmp = pointOfView.normalize().cross(new Vec3(0, -1, 0));
        if (glfwGetKey(window, GLFW_KEY_A) != GLFW_RELEASE) // A
            result = result.plus(tmp);
        if (glfwGetKey(window, GLFW_KEY_D) != GLFW_RELEASE) // D
            result = result.minus(tmp);

        if (glfwGetKey(window, GLFW_KEY_SPACE) != GLFW_RELEASE) // SPACE
            result = result.plus(new Vec3(0, 1, 0));
        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) != GLFW_RELEASE) // SHIFT (LEFT)
            result = result.minus(new Vec3(0, 1, 0));

        if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS) // Qキーによって見ている方向へ三次元空間的に動くか変える。ちょうどマイクラのクリエイティブのように
            isMoveBy3D = !isMoveBy3D;
        if (!isMoveBy3D)
            result = new Vec3(result.x, 0, result.z);
        if (result.length() != 0)
            result = result.normalize().scala(speed);


        return result;
    }
}
