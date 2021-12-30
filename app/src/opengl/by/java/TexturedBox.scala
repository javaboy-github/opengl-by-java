package opengl.by.java

import org.lwjgl.opengl.GL11.{GL_FLOAT, GL_TEXTURE_2D, GL_TRIANGLES, GL_UNSIGNED_INT, glBindTexture, glDrawElements}
import org.lwjgl.opengl.GL13.{GL_TEXTURE0, glActiveTexture}
import org.lwjgl.opengl.GL15.{GL_ARRAY_BUFFER, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW, glBindBuffer, glBufferData, glDeleteBuffers, glGenBuffers}
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._


/** テクスチャ付きのボックスを表すクラスです。 drawメソッドで描画でき、このインスタンスを破棄する場合は必ずcloseメソッドを呼んでください。
 */
object TexturedBox {
  private val FLOAT_SIZE = 4
  private val indicies = Array[Int](
    2, 1, 3, 1, 0, 3,
    2 + 4,     1 + 4,     3 + 4,     1 + 4,     4,     3 + 4,
    2 + 4 * 2, 1 + 4 * 2, 3 + 4 * 2, 1 + 4 * 2, 4 * 2, 3 + 4 * 2,
    2 + 4 * 3, 1 + 4 * 3, 3 + 4 * 3, 1 + 4 * 3, 4 * 3, 3 + 4 * 3,
    2 + 4 * 4, 1 + 4 * 4, 3 + 4 * 4, 1 + 4 * 4, 4 * 4, 3 + 4 * 4,
    2 + 4 * 5, 1 + 4 * 5, 3 + 4 * 5, 1 + 4 * 5, 4 * 5, 3 + 4 * 5
  )
}

class TexturedBox(val pos: Vec3, val boxSize: Vec3,
                  /** シェーダープログラム */
                  var program: Program, var textures: Array[Texture]) extends Box {
  /** 頂点の数 */
  val vertexCount = 8
  /** 頂点のインデックス数 */
  val indexCount = 36
  val s = new Vec3(boxSize.x / 2, boxSize.y / 2, boxSize.z / 2)
  val vertex: Array[Float] = Array[Float](
    -s.x + pos.x, -s.y + pos.y, -s.z + pos.z, 0f,   0f,   0f,   0f, 0f,
    -s.x + pos.x, -s.y + pos.y,  s.z + pos.z, 0f,   0f,   0.8f, 1f, 0f,
    -s.x + pos.x,  s.y + pos.y,  s.z + pos.z, 0f,   0.8f, 0f,   1f, 1f,
    -s.x + pos.x,  s.y + pos.y, -s.z + pos.z, 0f,   0.8f, 0.8f, 0f, 1f,

     s.x + pos.x, -s.y + pos.y, -s.z + pos.z, 0.8f, 0f,   0.8f, 0f, 0f,
    -s.x + pos.x, -s.y + pos.y, -s.z + pos.z, 0f,   0f,   0f,   1f, 0f,
    -s.x + pos.x,  s.y + pos.y, -s.z + pos.z, 0f,   0.8f, 0.8f, 1f, 1f,
     s.x + pos.x,  s.y + pos.y, -s.z + pos.z, 0.8f, 0f,   0f,   0f, 1f,

    -s.x + pos.x, -s.y + pos.y, -s.z + pos.z, 0f,   0f,   0f,   0f, 0f,
     s.x + pos.x, -s.y + pos.y, -s.z + pos.z, 0f,   0f,   0f,   1f, 0f,
     s.x + pos.x, -s.y + pos.y,  s.z + pos.z, 0f,   0f,   0f,   1f, 1f,
    -s.x + pos.x, -s.y + pos.y,  s.z + pos.z, 0f,   0f,   0f,   0f, 1f,

     s.x + pos.x, -s.y + pos.y,  s.z + pos.z, 0.8f, 0.8f, 0f,   0f, 0f,
     s.x + pos.x, -s.y + pos.y, -s.z + pos.z, 0.8f, 0.8f, 0f,   1f, 0f,
     s.x + pos.x,  s.y + pos.y, -s.z + pos.z, 0.8f, 0.8f, 0f,   1f, 1f,
     s.x + pos.x,  s.y + pos.y,  s.z + pos.z, 0.8f, 0.8f, 0f,   0f, 1f,

    -s.x + pos.x, -s.y + pos.y,  s.z + pos.z, 0.8f, 0.8f, 0f,   0f, 0f,
     s.x + pos.x, -s.y + pos.y,  s.z + pos.z, 0.8f, 0.8f, 0f,   1f, 0f,
     s.x + pos.x,  s.y + pos.y,  s.z + pos.z, 0.8f, 0.8f, 0f,   1f, 1f,
    -s.x + pos.x,  s.y + pos.y,  s.z + pos.z, 0.8f, 0.8f, 0f,   0f, 1f,

    -s.x + pos.x,  s.y + pos.y, -s.z + pos.z, 0f,   0f,   0f,   0f, 0f,
     s.x + pos.x,  s.y + pos.y, -s.z + pos.z, 0f,   0f,   0f,   1f, 0f,
     s.x + pos.x,  s.y + pos.y,  s.z + pos.z, 0f,   0f,   0f,   1f, 1f,
    -s.x + pos.x,  s.y + pos.y,  s.z + pos.z, 0f,   0f,   0f,   0f, 1f
  )

  /** 頂点配列オブジェクト*/
  private val vao = glGenVertexArrays
  glBindVertexArray(vao)
  /** 頂点バッファオブジェクト */
  private val vbo = glGenBuffers
  glBindBuffer(GL_ARRAY_BUFFER, vbo)
  glBufferData(GL_ARRAY_BUFFER, vertex, GL_STATIC_DRAW)
  /** index buffer object */
  private val ibo = glGenBuffers
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
  glBufferData(GL_ELEMENT_ARRAY_BUFFER, TexturedBox.indicies, GL_STATIC_DRAW)
  glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * TexturedBox.FLOAT_SIZE, 0)
  glEnableVertexAttribArray(0)
  glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * TexturedBox.FLOAT_SIZE, 3 * TexturedBox.FLOAT_SIZE)
  glEnableVertexAttribArray(1)
  glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * TexturedBox.FLOAT_SIZE, 6 * TexturedBox.FLOAT_SIZE)
  glEnableVertexAttribArray(2)
  glUniform1i(glGetUniformLocation(program.program, "texture1"), textures(0).getId)

  def this(pos: Vec3, boxSize: Vec3, program: Program, texture: Texture) {
    this(pos, boxSize, program, Array[Texture](texture, texture, texture, texture, texture, texture))
  }

  override def draw(): Unit = {
    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, textures(0).getId)
    program.use()
    glBindVertexArray(vao)
    glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
    // glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount); // 折線で描画
  }

  // 事実上のデストラクタ
  override def close(): Unit = {
    // 頂点配列オブジェクトを削除する
    glDeleteVertexArrays(vao)
    // 頂点バッファオブジェクトを削除する
    glDeleteBuffers(vbo)
    // インデックスの頂点バッファオブジェクトを削除する
    glDeleteBuffers(ibo)
  }
}
