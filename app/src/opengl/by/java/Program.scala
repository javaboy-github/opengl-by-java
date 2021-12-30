package opengl.by.java

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import java.nio.file.Paths
import java.nio.file.Files
import java.io.IOException


/** This class has fragment shader and vertex shader */
object Program {
  def createFromSourcefile(vertexShaderSourceFilename: String, fragmentShaderSourceFilename: String): Program = {
    val vertexShaderSourceFile = Paths.get(vertexShaderSourceFilename)
    var vertexShaderSource:String = null
    try vertexShaderSource = Files.readString(vertexShaderSourceFile)
    catch {
      case e: IOException =>
        System.err.println("Failed to load " + vertexShaderSourceFilename)
        throw new RuntimeException(e)
    }
    val fragmentShaderSourceFile = Paths.get(fragmentShaderSourceFilename)
    var fragmentShaderSource:String = null
    try fragmentShaderSource = Files.readString(fragmentShaderSourceFile)
    catch {
      case e: IOException =>
        System.err.println("Failed to load " + fragmentShaderSourceFilename)
        throw new RuntimeException(e)
    }
    new Program(vertexShaderSource, fragmentShaderSource)
  }
}

class Program(val vertex: String, val fragment: String) {
  val program = glCreateProgram
  val vobj: Int = glCreateShader(GL_VERTEX_SHADER)
  glShaderSource(vobj, vertex, "")
  glCompileShader(vobj)
  var status = new Array[Int](1)
  glGetShaderiv(vobj, GL_COMPILE_STATUS, status)
  if (status(0) == GL_FALSE) {
    val bufSize = new Array[Int](1)
    glGetShaderiv(vobj, GL_INFO_LOG_LENGTH, bufSize)
    if (bufSize(0) > 1) throw new RuntimeException(s"vertex shaderのコンパイルに失敗しました。エラーメッセージ:${glGetShaderInfoLog(vobj, bufSize(0))}")
    else throw new RuntimeException("vertex shaderのコンパイルに失敗しました")
  }
  glAttachShader(program, vobj)
  glDeleteShader(vobj)
  val fobj: Int = glCreateShader(GL_FRAGMENT_SHADER)
  glShaderSource(fobj, fragment, "")
  glCompileShader(fobj)
  status = new Array[Int](1)
  glGetShaderiv(vobj, GL_COMPILE_STATUS, status)
  if (status(0) == GL_FALSE) {
    val bufSize = new Array[Int](1)
    glGetShaderiv(vobj, GL_INFO_LOG_LENGTH, bufSize)
    if (bufSize(0) > 1) throw new RuntimeException(s"fragment shaderのコンパイルに失敗しました。エラーメッセージ:${glGetShaderInfoLog(vobj, bufSize(0))}")
    else throw new RuntimeException("fragment shaderのコンパイルに失敗しました")
  }
  glAttachShader(program, fobj)
  glDeleteShader(fobj)

  def link(): Unit = {
    glLinkProgram(program)
  }

  def use(): Unit = {
    glUseProgram(program)
  }
}
