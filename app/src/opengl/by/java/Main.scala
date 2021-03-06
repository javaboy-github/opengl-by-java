package opengl.by.java

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30.glBindFragDataLocation
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL

import java.lang.Math.cos
import java.lang.Math.sin
import opengl.by.java.AffineTransformHelper._

import scala.language.postfixOps
import scala.math.pow

object Main {
  var window: Long = 0
  var isActive: Boolean = false;;
  var isFirst: Boolean = true; // マウスでの操作で視線を変える操作で使用している
  var downAndUp = 0.35

  def main(args: Array[String]): Unit = {
    init()
    loop()
    terminate()
  }

  def init(): Unit = {
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

    glfwSetKeyCallback(window, (_, key, _, action, _) =>
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
          glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
        }) // We will detect this in the rendering loop

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

    val program = Program.createFromSourcefile("src/main.vert", "src/main.frag")
    val programId = program.program;
    glBindAttribLocation(programId, 0, "position")
    glBindAttribLocation(programId, 1, "color")
    glBindFragDataLocation(programId, 0, "fragment")

    val texture = Texture.loadTexture("src/texture.png")

    val program2 = Program.createFromSourcefile("src/main2.vert", "src/main2.frag")
    val programs  = Array(program, program2)

    val seed = 202201191849L
    val random = new scala.util.Random(seed)

    val generateWorld = (x:Int,z:Int) => {
      x + z
    }


    for (x <- -100 until 100) {
      for (z <- -10 until  10)
      World += new TexturedBox(new Vec3(x,generateWorld(x,z),z),new Vec3(1,1,1), program2, texture)
    }


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

    if (glfwRawMouseMotionSupported)
      glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)

    var t = 0.0
    val lastCursorPos = Array(size(0).toDouble, size(1).toDouble)

    val up = new Vec3(0,1,0)
    var yaw = 0.0
    var pitch = 0.0

    val player = new PhysicalBox(new Vec3(0, 4, 0), new Vec3(1,2,1))

    while ( {
      !glfwWindowShouldClose(window)
    }) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

      // 視線を算出
      if (isActive) {
        val x = Array(0.0)
        val y = Array(0.0)
        glfwGetCursorPos(window, x, y);
        if (!isFirst) {
          val lastPitch = pitch;
          yaw += (x(0) - lastCursorPos(0)) * 0.005
          pitch += (y(0) - lastCursorPos(1)) * 0.005
          if (
            lastPitch >= -Math.PI/2 && pitch < -Math.PI/2 ||
            lastPitch <= Math.PI/2 && pitch > Math.PI/2
          )
            pitch =  lastPitch
        }
        lastCursorPos(0) = x(0)
        lastCursorPos(1) = y(0)
        isFirst = false
      }

      val pointOfView = new Vec3(
        cos(yaw).toFloat * Math.cos(pitch).toFloat,
        sin(pitch).toFloat,
        sin(yaw).toFloat * Math.cos(pitch).toFloat
      ).normalize
      var lastPos = player.start;
      player.start += move(pointOfView)
      if (World.isCollision(player)) {player.start = lastPos}
      lastPos = player.start;
      if (!isJumping) {
        player.start -= new Vec3(0, downAndUp.toFloat, 0) // 落 下
        if (World.isCollision(player)) {
          player.start = lastPos
          canJump = true;
        };
      }

      val width = size(0)
      val height = size(1)
      val model = translate(0, 0, 0)
      val view = lookAt(
        player.start + new Vec3(0.5f, 0.2f, 0.5f),               // position
        pointOfView + player.start + new Vec3(0.5f, 0.5f, 0.5f), // point of view
        up                      // UP
      )
      // var modelview = model.mul(view);
      val modelview = view * model
      val projection = frustum(1f, width.toFloat / height.toFloat, 0.01f, 100f)
      // glUniformMatrix4fv(modelViewLoc, false, pointer);
      World.forEach(box => {
        box.program.use()
          .set("modelview", modelview)
          .set("projection", projection)
        box.draw()
      })
      glfwSwapBuffers(window)
      glfwPollEvents()
      t += 0.1
      // isActiveに関する処理
      if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS && isActive) {
        // 有効
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
        isActive = false
        isFirst = true
      }
      if (glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS) {
        println(pitch)
      }
      if (!isActive && glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
        // 無効化
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        isActive = true
      }
    }
    World.forEach(box => box.close())
    World.clear()

  }
  def terminate(): Unit = {
    // 後始末
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)

    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

   private var isMoveBy3D = true
  private var jump: Double = 0;
  private var isJumping: Boolean = false;
  private var jumpSpeed= 0.0;
  private var canJump = true;

  def move(pointOfView: Vec3): Vec3 = {
    if (!isActive) return new Vec3(0,0,0)
    var result = new Vec3(0,0,0)
    val speed = 0.4f
    var tmp = pointOfView.normalize
    if (glfwGetKey(window, GLFW_KEY_W) != GLFW_RELEASE) { // W
      result += tmp
    }
    if (glfwGetKey(window, GLFW_KEY_S) != GLFW_RELEASE) { // S
      result -= tmp
    }
    // tmp = new Vec3(pointOfView.length() * (float) sin(angle), 0, -pointOfView.length() * (float) cos(angle)).normalize();
    tmp = pointOfView.normalize * new Vec3(0, -1, 0)
    if (glfwGetKey(window, GLFW_KEY_A) != GLFW_RELEASE) { // A
      result += tmp
    }
    if (glfwGetKey(window, GLFW_KEY_D) != GLFW_RELEASE) { // D
      result -= tmp
    }
    result = new Vec3(result.x, 0, result.z)
    if (glfwGetKey(window, GLFW_KEY_SPACE) != GLFW_RELEASE) { // SPACE
      if (canJump) {jump = 0;isJumping = true;canJump=false;jumpSpeed = 0}
    }
    if (jump > 1) isJumping = false;
    if (isJumping) {
      jumpSpeed += 0.1;
      result += new Vec3(0, jumpSpeed.toFloat , 0)
      jump += jumpSpeed
    }
    if (result.len() != 0)
      result = result.normalize * speed
    result
  }
}