package opengl.by.java

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL30.glBindFragDataLocation
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL

import java.lang.Math.cos
import java.lang.Math.sin
import opengl.by.java.AffineTransformHelper._

import java.nio.IntBuffer



object Main {
  var window: Long = 0
  def main(args: Array[String]) {
    init()
    loop()
    terminate()
  }

  def init() = {
    GLFWErrorCallback.createPrint(System.err).set()
    if (!glfwInit())
      throw new IllegalStateException("Cannot initialize GLFW")

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

    val width = 1260
    val height = 960
    window = glfwCreateWindow(width, height, "OpenGL by java", NULL, NULL)
    if (window == NULL) throw new RuntimeException("GLFWウィンドウを作成できません")

    glfwSetKeyCallback(window,
      (window: Long, key: Int, _: Int, action: Int, _: Int) =>
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
          glfwSetWindowShouldClose(window, true)) // We will detect this in the rendering loop

    // Get the thread stack and push a new frame
    val stack = stackPush
    try {
      val pWidth = stack.mallocInt(1) // int*
      val pHeight = stack.mallocInt(1)
      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight)
      // Get the resolution of the primary monitor
      val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
      // Center the window
      glfwSetWindowPos(window, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)
    } finally if (stack != null) stack.close()

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)
  }
  def loop(): Unit = {
    GL.createCapabilities

    val pogram = Program.createFromSourcefile("src/main.vert", "src/main.frag")
    val programId = program.program;
    glBindAttribLocation(programId, 0, "position")
    glBindAttribLocation(programId, 1, "color")
    glBindFragDataLocation(programId, 0, "fragment")
    program.link()

    val texture = Texture.loadTexture("src/texture/png")

    val program2 = Program.createFromSourcefile("src/main2.vert", "src/main2.frag")
    program2.link()

    val boxes = Array(
      // new NormalBox(new Vec3(0, 0, 0), new Vec3(100, 0.1f, 0.1f), Program.createFromSourcefile("src/xyz.vert", "src/x.frag").link()), // x
      // new NormalBox(new Vec3(0, 0, 0), new Vec3(0.1f, 100, 0.1f), Program.createFromSourcefile("src/xyz.vert", "src/x.frag").link()), // y
      // new NormalBox(new Vec3(0, 0, 0), new Vec3(0.1f, 0.1f, 100), Program.createFromSourcefile("src/xyz.vert", "src/x.frag").link()), // z
      // new NormalBox(new Vec3(0, 0, 0), new Vec3(2, 2, 2), program),
      // new NormalBox(new Vec3(4, 0, 0), new Vec3(2, 2, 2), program),
      new TexturedBox(new Vec3(-4, 0, 0), new Vec3(2, 2, 2), program2, texture),
    )

    val modelViewLoc = glGetUniformLocation(programId, "modelview")
    val projectionLoc = glGetUniformLocation(programId, "projection")
    val tLoc = glGetUniformLocation(programId, "t")

    glClearColor(1, 1, 1, 0)
    glEnable(GL_TEXTURE_2D) //テクスチャ表示

    glEnable(GL_DEPTH_TEST) // 重ならない


    // veiwport
    val size = Array[Int](1260, 770) // size[0] is width and size[1] is height
    val size2 = Array[Int](1260, 770)
    val resize = (window: Long, width: Int, height: Int) => {
      size2(0) = width
      size2(1) = height
      val widthPointer = new Array[Int](1)
      val heightPointer = new Array[Int](1)
      glfwGetFramebufferSize(window, widthPointer, heightPointer)
      size(0) = widthPointer(0)
      size(1) = heightPointer(0)
      glViewport(0, 0, widthPointer(0), heightPointer(0))
    }
    glfwSetWindowSizeCallback(window, (a1, a2, a3) => resize(a1, a2, a3))
    resize(window, size(0), size(1))
    glViewport(0, 0, size(0), size(1))


    val isFirst = Array(true)
    var cursorPos = Array(size(0).toDouble, size(1).toDouble)
    var offsetPos = Array(0.0, 0.0)
    val angle = Array(100.0, -0.5)
    if (glfwRawMouseMotionSupported)
      glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)
    glfwSetCursorPosCallback(window, (_: Long, x: Double, y: Double) => {
      System.out.println(isFirst(0))
      if (!isFirst(0)) {
        offsetPos(0) = x - cursorPos(0)
        offsetPos(1) = y - cursorPos(1)
        angle(0) += offsetPos(0) * 0.1
        angle(1) += offsetPos(1) * 0.1
      }
      cursorPos(0) = x
      cursorPos(1) = y
      isFirst(0) = false
    })


    var position = new Vec3(3, 4, 5)
    var t = 0.0

    while ( {
      !glfwWindowShouldClose(window)
    }) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

      val yaw = angle(0)
      val pitch = angle(1)

      val pointOfView = new Vec3(
        cos(yaw).toFloat * Math.cos(pitch).toFloat,
        sin(pitch).toFloat,
        sin(yaw).toFloat * Math.cos(pitch).toFloat
      ).normalize
      position = position + move pointOfView

      val width = size(0)
      val height = size(1)
      val model = translate(0, 0, 0)
      val view = lookAt(
        position,               // position
        pointOfView + position, // point of view
        new Vec3(0, 1, 0)       // UP
      )
      // var modelview = model.mul(view);
      val modelview = view * model
      // glUniformMatrix4fv(modelViewLoc, false, pointer);
      glUniformMatrix4fv(modelViewLoc, true, modelview.toArray)
      val projection = frustum(1f, width.toFloat / height.toFloat, 0.1f, 100f)
      glUniformMatrix4fv(projectionLoc, true, projection.toArray)
      glUniform1f(tLoc, t.toFloat)
      boxes.foreach(box => box.draw())
      glfwSwapBuffers(window)
      glfwPollEvents()
      t += 0.1
    }
    boxes.foreach(box => box.close())
  }
  def terminate() = {
    // 後始末
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)

    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

   private var isMoveBy3D = false

  def move(pointOfView: Vec3): Vec3 = {
    var result = new Vec3(0,0,0)
    val speed = 0.5f
    var tmp = pointOfView.normalize
    if (glfwGetKey(window, GLFW_KEY_W) != GLFW_RELEASE) { // W
      result = result + tmp
    }
    if (glfwGetKey(window, GLFW_KEY_S) != GLFW_RELEASE) { // S
      result = result - tmp
    }
    // tmp = new Vec3(pointOfView.length() * (float) sin(angle), 0, -pointOfView.length() * (float) cos(angle)).normalize();
    tmp = pointOfView.normalize * new Vec3(0, -1, 0)
    if (glfwGetKey(window, GLFW_KEY_A) != GLFW_RELEASE) { // A
      result = result + tmp
    }
    if (glfwGetKey(window, GLFW_KEY_D) != GLFW_RELEASE) { // D
      result = result - tmp
    }
    if (glfwGetKey(window, GLFW_KEY_SPACE) != GLFW_RELEASE) { // SPACE
      result = result + new Vec3(0, 1, 0)
    }
    if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) != GLFW_RELEASE) { // SHIFT (LEFT)
      result = result - new Vec3(0, 1, 0)
    }
    if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS) { // Qキーによって見ている方向へ三次元空間的に動くか変える。ちょうどマイクラのクリエイティブのように
      isMoveBy3D = !isMoveBy3D
    }
    if (!isMoveBy3D) result = new Vec3(result.x, 0, result.z)
    if (result.len() != 0) result = result.normalize * speed
    result
  }
}