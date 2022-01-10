package opengl.by.java

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
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
  for (i <- 0 until 6) {
    program.set("texture" + i, i);
  }

  def this(pos: Vec3, boxSize: Vec3, program: Program, texture: Texture) {
    this(pos, boxSize, program, Array[Texture](texture, texture, texture, texture, texture, texture))
  }

  override def draw(): Unit = {
    program.use()
    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, textures(0).getId)
    glActiveTexture(GL_TEXTURE1)
    glBindTexture(GL_TEXTURE_2D, textures(1).getId)
    glActiveTexture(GL_TEXTURE2)
    glBindTexture(GL_TEXTURE_2D, textures(2).getId)
    glActiveTexture(GL_TEXTURE3)
    glBindTexture(GL_TEXTURE_2D, textures(3).getId)
    glActiveTexture(GL_TEXTURE4)
    glBindTexture(GL_TEXTURE_2D, textures(4).getId)
    glActiveTexture(GL_TEXTURE5)
    glBindTexture(GL_TEXTURE_2D, textures(5).getId)

    glBindVertexArray(vao)
    glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
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
