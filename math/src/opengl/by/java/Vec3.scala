class Vec3(val x: Float, val y: Float, val z: Float) {
    def +(target: Vec3) = Vec3(x + target.x, y + target.y, z + target.z)
    def -(target: Vec3) = Vec3(x - target.x, y - target.y, z - target.z)
    def *(f: Float)  = Vec3(x*f, y*f, z*f)
    def *(target: Vec3) =
        Vec3(
            Math.fma(y, target.z, -z * target.y),
            Math.fma(z, target.x, -x * target.z),
            Math.fma(x, target.y, -y * target.x),
        )
    def dot(target: Vec3) = x*target.x + y*target.y + z*target.z
    def normalize() = Vec3()
    def length() = (x + y + z) / 3
    def toString() = s"[$x, $y, $z]"
}