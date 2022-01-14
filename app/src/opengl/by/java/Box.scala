package opengl.by.java

trait Box extends AutoCloseable {
  var program: Program
  var pos: Vec3;
  var physicalBox: PhysicalBox = new PhysicalBox(pos, new Vec3(1,1,1))
  def draw(): Unit

  override def close(): Unit
}
