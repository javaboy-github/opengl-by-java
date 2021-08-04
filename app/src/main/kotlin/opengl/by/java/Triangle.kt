package opengl.by.java

import opengl.by.java.math.Vec2
import opengl.by.java.math.Vec3

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.BufferUtils
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

// 参考:https://tus-ricora.com/programming/lwjgl/
class Triangle(
    path: String,
    val wh: Vec2,
    val pos: Vec3,
    val rot: Vec3=Vec3(0.0, 0.0, 0.0),
    val col: Vec3=Vec3(1.0, 1.0, 1.0),
    val scl: Double=1.0,
    val alp:Double =1.0,
):AutoCloseable  {
    val textureId: Int
    init {
        // ※IOExceptionをスローする場合あり
        // イメージファイルの読み込み
        val bi = ImageIO.read(File(path))
        val width = bi.width
        val height = bi.height

        // ピクセルを保存する配列
        val pixelsRaw = bi.getRGB(0, 0, width, height, null, 0, width)
        var pixels = BufferUtils.createByteBuffer(width * height * 4)

        // 一ピクセルごとに読み込む
        for (i in 0..(height -1)) {
            for (j in 0..(width -1)) {
                val p = pixelsRaw[i * width + j]
                pixels.put((p shr 16 and 0xFF).toByte())// byte型のサイズは、int型の1/4
                pixels.put((p shr 8 and 0xFF).toByte())
                pixels.put((p and 0xFF).toByte())
                pixels.put((p shr 24 and 0xFF).toByte())
            }
        }
        pixels.flip()

        // テクスチャの作成
        textureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels)

        // テクスチャの設定
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST.toFloat())
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST.toFloat())

        // テクスチャの解除
        glBindTexture(GL_TEXTURE_2D, 0)

    }
    
    var t = 0f;
    fun draw() {
        t += 0.01f
        glBindTexture(GL_TEXTURE_2D, textureId)//　テクスチャのバインド

        glPushMatrix()

        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()
        glTranslated(pos.x, pos.y, pos.z)
        glRotated(rot.x, 1.0, 0.0, 0.0)
        glRotated(rot.y, 0.0, 1.0, 0.0)
        glRotated(rot.z, 0.0, 0.0, 1.0)
        glScaled(scl, scl, scl)

        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glFrustum(-5.0, 5.0, -5.0, 5.0, 10.0, 100.0) // 透視投影

        glViewport(0, 0, Main.width, Main.height)

        glColor4d(col.x, col.y, col.z, alp)

            
            
        glBegin(GL_TRIANGLE_STRIP)
        glTexCoord2d(0.0, 0.0)
        glVertex2d(-wh.x / 2, wh.y / 2)
        glTexCoord2d(0.0, 1.0)
        glVertex2d(-wh.x / 2, -wh.y / 2)
        glTexCoord2d(1.0, 0.0)
        glVertex2d(wh.x / 2, wh.y / 2)
        glTexCoord2d(1.0, 1.0)
        glVertex2d(wh.x / 2, -wh.y / 2)
        glEnd()

        glPopMatrix()

        // バインドを解除
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    // 事実上のデストラクタ
    override fun close() {
        glDeleteTextures(textureId)
    }
}
