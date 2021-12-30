package opengl.by.java

import org.lwjgl.opengl.GL30._
import de.matthiasmann.twl.utils.PNGDecoder
import org.lwjgl.opengl.GL11.{GL_LINEAR, GL_RGBA, GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_TEXTURE_MIN_FILTER, GL_UNPACK_ALIGNMENT, GL_UNSIGNED_BYTE, glBindTexture, glGenTextures, glPixelStorei, glTexImage2D, glTexParameterf}

import java.io.FileInputStream
import java.nio.ByteBuffer
import java.io.IOException
import java.nio.file.Paths


object Texture {
  @throws[IOException]
  def loadTexture(filename: String): Texture = {
    // PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(filename));
    val decoder = new PNGDecoder(new FileInputStream(Paths.get(filename).toFile))
    val buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth * decoder.getHeight)
    decoder.decode(buffer, decoder.getWidth * 4, PNGDecoder.Format.RGBA)
    buffer.flip
    val id = glGenTextures
    glBindTexture(GL_TEXTURE_2D, id)
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth, decoder.getHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
    glGenerateMipmap(GL_TEXTURE_2D)
    new Texture(id)
  }
}

class Texture(var id: Int) {
  def getId: Int = id
}
