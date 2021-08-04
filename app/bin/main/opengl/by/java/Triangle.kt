package opengl.by.java

import opengl.by.java.math.Vec2
import opengl.by.java.math.Vec3

import org.lwjgl.opengl.GL11.*;

// 参考:https://tus-ricora.com/programming/lwjgl/
class Triangle(
    val wh: Vec2,
    val pos: Vec3,
    val rot: Vec3=Vec3(0.0, 0.0, 0.0),
    val col: Vec3=Vec3(1.0, 1.0, 1.0),
    val scl: Double=1.0,
    val alp:Double =1.0
) {
    fun draw() {
        glPushMatrix()
        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()
        glTranslated(pos.x, pos.y, pos.z)
        glRotated(rot.x, 1., 0., 0.)
        glRotated(rot.y, 0., 1., 0.)
        glRotated(rot.z, 0., 0., 1.)
        glScaled(scl, scl, scl)

        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glFrustum(-5., 5., -5., 5., 10., 100.)

        glViewport(0., 0., 600, 600)
    }
}
