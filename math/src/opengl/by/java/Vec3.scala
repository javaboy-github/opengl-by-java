package opengl.by.java

class Vec3(val x: Float, val y: Float, val z: Float) {
    def +(target: Vec3) = new Vec3(x + target.x, y + target.y, z + target.z)
    def -(target: Vec3) = new Vec3(x - target.x, y - target.y, z - target.z)
    def *(f: Float)  = new Vec3(x*f, y*f, z*f)
    def *(target: Vec3) =
        new Vec3(
            Math.fma(y, target.z, -z * target.y),
            Math.fma(z, target.x, -x * target.z),
            Math.fma(x, target.y, -y * target.x),
        )
    def dot(target: Vec3) = x*target.x + y*target.y + z*target.z
    def normalize() = new Vec3(x/len(), y/len(), z/len())
    def len() = (x + y + z) / 3
    override def toString() = s"[$x, $y, $z]"
}