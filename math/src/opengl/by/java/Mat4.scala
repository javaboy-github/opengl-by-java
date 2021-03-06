package opengl.by.java

class Mat4(val m:Array[Float]) {
    def +(target: Mat4) = new Mat4((m zip target.m).map(e => e._1 + e._2))
    def -(target: Mat4) = this +target * -1
    def *(target: Float) = new Mat4(m.map(e=>e*target))
    def *(target: Mat4) =
        new Mat4(Array(
            get(1,1) * target.get(1,1) + get(1,2) * target.get(2,1) + get(1,3) * target.get(3,1) + get(1,4) * target.get(4,1),
            get(1,1) * target.get(1,2) + get(1,2) * target.get(2,2) + get(1,3) * target.get(3,2) + get(1,4) * target.get(4,2),
            get(1,1) * target.get(1,3) + get(1,2) * target.get(2,3) + get(1,3) * target.get(3,3) + get(1,4) * target.get(4,3),
            get(1,1) * target.get(1,4) + get(1,2) * target.get(2,4) + get(1,3) * target.get(3,4) + get(1,4) * target.get(4,4),
            get(2,1) * target.get(1,1) + get(2,2) * target.get(2,1) + get(2,3) * target.get(3,1) + get(2,4) * target.get(4,1),
            get(2,1) * target.get(1,2) + get(2,2) * target.get(2,2) + get(2,3) * target.get(3,2) + get(2,4) * target.get(4,2),
            get(2,1) * target.get(1,3) + get(2,2) * target.get(2,3) + get(2,3) * target.get(3,3) + get(2,4) * target.get(4,3),
            get(2,1) * target.get(1,4) + get(2,2) * target.get(2,4) + get(2,3) * target.get(3,4) + get(2,4) * target.get(4,4),
            get(3,1) * target.get(1,1) + get(3,2) * target.get(2,1) + get(3,3) * target.get(3,1) + get(3,4) * target.get(4,1),
            get(3,1) * target.get(1,2) + get(3,2) * target.get(2,2) + get(3,3) * target.get(3,2) + get(3,4) * target.get(4,2),
            get(3,1) * target.get(1,3) + get(3,2) * target.get(2,3) + get(3,3) * target.get(3,3) + get(3,4) * target.get(4,3),
            get(3,1) * target.get(1,4) + get(3,2) * target.get(2,4) + get(3,3) * target.get(3,4) + get(3,4) * target.get(4,4),
            get(4,1) * target.get(1,1) + get(4,2) * target.get(2,1) + get(4,3) * target.get(3,1) + get(4,4) * target.get(4,1),
            get(4,1) * target.get(1,2) + get(4,2) * target.get(2,2) + get(4,3) * target.get(3,2) + get(4,4) * target.get(4,2),
            get(4,1) * target.get(1,3) + get(4,2) * target.get(2,3) + get(4,3) * target.get(3,3) + get(4,4) * target.get(4,3),
            get(4,1) * target.get(1,4) + get(4,2) * target.get(2,4) + get(4,3) * target.get(3,4) + get(4,4) * target.get(4,4)
        ))
    def get(row: Int, column: Int): Float = {
        if (row > 4 || row < 1 || column > 4 || column < 1)
            throw new java.lang.IndexOutOfBoundsException()
         m(column -1 + (row-1)*4)
    }
    def toArray() = m
}