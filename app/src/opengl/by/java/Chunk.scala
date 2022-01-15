package opengl.by.java
import scala.collection.mutable

class Chunk(x:Int,z:Int) {
  var boxes = mutable.ArrayBuffer[Box]()
  def +=(box: Box): Unit = boxes += box

  override def toString =
    s"[${x*16},${z*16}]~[${(x+1)*16},${(z+1)*16}]"

  // 対象物と当たっているか
  def isCollision(physicalBox: PhysicalBox): Boolean = {
    boxes.foreach(box => if (box.physicalBox ? physicalBox) return true)
    false
  }
}
