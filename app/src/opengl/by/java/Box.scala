package opengl.by.java

import org.lwjgl.opengl.GL11.{GL_FLOAT, GL_TRIANGLES, GL_UNSIGNED_INT, glDrawElements}
import org.lwjgl.opengl.GL15.{GL_ARRAY_BUFFER, GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW, glBindBuffer, glBufferData, glDeleteBuffers, glGenBuffers}
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._

/** 三角形を表すクラスです。 drawメソッドで描画でき、このインスタンスを破棄する場合は必ずcloseメソッドを呼んでください。
 */
object Box {
  val index: Array[Int] = Array(
    0, 1, 2, 0, 2, 3, // 左
    0, 3, 4, 0, 4, 5, // 裏
    0, 5, 6, 0, 6, 1, // 下
    7, 6, 5, 7, 5, 4, // 右
    7, 4, 3, 7, 3, 2, // 上
    7, 2, 1, 7, 1, 6  // 前
  )
}

class Box(val pos: Vec3) extends AutoCloseable {
  val size: Int = 3
  /** 頂点のインデックス数 */
  private val indexCount: Int = 36
  val vertex: Array[Float] = Array[Float](
    -1f + pos.x, -1f + pos.y, -1f + pos.z, 0f, 0f, 0f, // (0)
    -1f + pos.x, -1f + pos.y, 1f + pos.z, 0f, 0f, 0.8f, // (1)
    -1f + pos.x, 1f + pos.y, 1f + pos.z, 0f, 0.8f, 0f, // (2)
    -1f + pos.x, 1f + pos.y, -1f + pos.z, 0f, 0.8f, 0.8f, // (3)
    1f + pos.x, 1f + pos.y, -1f + pos.z, 0.8f, 0f, 0f, // (4)
    1f + pos.x, -1f + pos.y, -1f + pos.z, 0.8f, 0f, 0.8f, // (5)
    1f + pos.x, -1f + pos.y, 1f + pos.z, 0.8f, 0.8f, 0f, // (6)
    1f + pos.x, 1f + pos.y, 1f + pos.z, 0.8f, 0.8f, 0.8f // (7)
  )

  /**頂点配列オブジェクト*/
  private val vao = glGenVertexArrays
  glBindVertexArray(vao)
  /** 頂点バッファオブジェクト*/
  private val vbo = glGenBuffers
  glBindBuffer(GL_ARRAY_BUFFER, vbo)
  glBufferData(GL_ARRAY_BUFFER, vertex, GL_STATIC_DRAW)
  // 結合されている頂点バッファオブジェクトを in 変数から参照できるようにする
  val vertexSize = 24
  glVertexAttribPointer(0, size, GL_FLOAT, false, vertexSize, 0)
  glEnableVertexAttribArray(0)
  glVertexAttribPointer(1, 3, GL_FLOAT, false, vertexSize, 0.toChar + 2 * 4)
  glEnableVertexAttribArray(1)
  /** インデックスの頂点バッファオブジェクト */
  private val ibo = glGenBuffers
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
  // glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexCount * 4, GL_STATIC_DRAW);
  glBufferData(GL_ELEMENT_ARRAY_BUFFER, Box.index, GL_STATIC_DRAW)



  def draw(): Unit = {
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
