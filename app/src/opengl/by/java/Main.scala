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



object Main {
  val window: Long
  def main(args: Array[Float]) {
    init()
    loop()
    terminate()
  }

  def init() = {
    GLFWErrorCallback.createPrint(System.err).set()
    if (!glfwInit())
      throw new IllegalStateException("Cannot initialize GLFW")
  }
  def loop()
  def terminate()
}