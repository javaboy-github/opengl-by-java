package opengl.by.java

// LWJGL3

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL


object Main {
    // windowの値
    private var window: Long? = null

    fun run() {
        init()
        loop()

        // 後始末
        glfwFreeCallbacks(window!!)
        glfwDestroyWindow(window!!)

        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }

    fun init() {
        GLFWErrorCallback.createPrint(System.err).set()
        if (!glfwInit())
            throw IllegalStateException("GLFWを初期化できません")
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        window = glfwCreateWindow(600, 600, "OpenGL by java", NULL, NULL)
        if (window == NULL) {
            throw RuntimeException("GLFWウィンドウを作成できません")
        }

        glfwSetKeyCallback(
            window!!
        ) { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(
                window,
                true
            ) // We will detect this in the rendering loop
        }

        // Get the thread stack and push a new frame
        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window!!, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center the window
            glfwSetWindowPos(
                window!!,
                (vidmode!!.width() - pWidth[0]) / 2,
                (vidmode!!.height() - pHeight[0]) / 2
            )
        }

        glfwMakeContextCurrent(window!!)
        glfwSwapInterval(1)
        glfwShowWindow(window!!)
    }

    fun loop() {
        GL.createCapabilities()

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
        while (!glfwWindowShouldClose(window!!)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glfwSwapBuffers(window!!)
            glfwPollEvents()
        }
    }
}

fun main() {
    Main.run()
}
