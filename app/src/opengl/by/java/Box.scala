package opengl.by.java

trait Box extends AutoCloseable {
  def draw(): Unit

  override def close(): Unit
}
