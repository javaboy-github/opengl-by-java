package opengl.by.java

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20.{glGetUniformLocation, _}

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
        System.err.println(s"Failed to load $vertexShaderSourceFilename")
        throw new RuntimeException(e)
    }
    val fragmentShaderSourceFile = Paths.get(fragmentShaderSourceFilename)
    var fragmentShaderSource:String = null
    try fragmentShaderSource = Files.readString(fragmentShaderSourceFile)
    catch {
      case e: IOException =>
        System.err.println(s"Failed to load $fragmentShaderSourceFilename")
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
  glLinkProgram(program)

  def use(): Program = {
    glUseProgram(program)
    return this;
  }

  def getLoc(name: String): Int = {
    val location = glGetUniformLocation(program, name);
//    if (location == -1) throw new RuntimeException("The uniform not fount: " + name)
    return location;
  }

  def set(name: String, value: Boolean): Program = {
    glUniform1i(getLoc(name), if (value) 1 else 0);
    return this;
  }
  def set(name: String, value: Int): Program= {
    glUniform1i(getLoc(name), value);
    return this;
  }
  def set(name: String, value: Float): Program = {
    glUniform1f(getLoc(name), value);
    return this;
  }
  def set(name: String, value: Vec3): Program = {
    glUniform3fv(getLoc(name), Array(value.x, value.y, value.z))
    return this;
  }
  def set(name: String, value1: Vec3, value2: Float): Program = {
    glUniform4fv(getLoc(name), Array(value1.x, value1.y, value1.z, value2))
    return this;
  }
  def set(name: String, value: Mat4): Program = {
    glUniformMatrix4fv(getLoc(name), true, value.toArray())
    return this;
  }
}
