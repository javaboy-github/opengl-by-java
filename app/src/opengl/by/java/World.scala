package opengl.by.java
import scala.collection.mutable

object World {
  var chunks = mutable.Map[Int, mutable.Map[Int, Chunk]]()

  // boxを追加
  def +=(box: Box): Unit = {
    chunks getOrElseUpdate(box.pos.x.toInt / 16, mutable.Map[Int, Chunk]())
       ._2.getOrElseUpdate(box.pos.z.toInt / 16, new Chunk(box.pos.x.toInt/16,box.pos.z.toInt/16)) += box
  }

  def isCollision(physicalBox: PhysicalBox): Boolean = {
    return chunks(physicalBox.start.x.toInt / 16)(physicalBox.start.z.toInt / 16).isCollision(physicalBox)
  }
}
