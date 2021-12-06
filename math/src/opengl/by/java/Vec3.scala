class Vec3(val x: Float, val y: Float, val z: Float) {
    def +: (target: Vec3) = Vec3(x + target.x, y + target.y, z + target.z)
    def -: (target: Vec3) = Vec3(x - target.x, y - target.y, z - target.z)
}