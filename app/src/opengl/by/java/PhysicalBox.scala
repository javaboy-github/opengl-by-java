package opengl.by.java

class PhysicalBox(var start: Vec3, var size: Vec3) {

  if (size.x < 0 || size.y < 0 || size.z  < 0)
    throw new RuntimeException("sizeに負の数を入れることはできません")

  def ?(target: PhysicalBox): Boolean = {
    !(start.x > target.start.x + target.size.x ||
    start.x + size.x < target.start.x ||
    start.y > target.start.y + target.size.y ||
    start.y + size.y < target.start.y ||
    start.z > target.start.z + target.size.z ||
    start.z + size.z < target.start.z )
  }
}
