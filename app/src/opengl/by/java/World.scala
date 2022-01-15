package opengl.by.java
import opengl.by.java.World.chunks

import scala.annotation.tailrec
import scala.collection.mutable

object World {
  var chunks = mutable.Map[Int, mutable.Map[Int, Chunk]]()

  // boxを追加
  def +=(box: Box): Unit = {
    chunks.getOrElseUpdate(box.pos.x.toInt / 16, mutable.Map[Int, Chunk]())
       .getOrElseUpdate(box.pos.z.toInt / 16, new Chunk(box.pos.x.toInt / 16, box.pos.z.toInt / 16)) += box
  }

  def isCollision(physicalBox: PhysicalBox): Boolean = {
    chunks(physicalBox.start.x.toInt / 16)(physicalBox.start.z.toInt / 16).isCollision(physicalBox)
  }

  def forEach(callback: Box =>Unit): Unit = chunks.foreach(chunks => chunks._2.foreach(chunk => chunk._2.boxes.foreach(callback)))

  def clear(): Unit = {
    chunks = mutable.Map[Int, mutable.Map[Int, Chunk]]()
  }
}
