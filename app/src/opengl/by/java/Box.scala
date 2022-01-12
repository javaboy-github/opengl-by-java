package opengl.by.java

trait Box extends AutoCloseable {
  var program: Program
  def draw(): Unit

  override def close(): Unit
}
