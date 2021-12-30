package opengl.by.java

// 三角関数
import java.lang.Math.sin
import java.lang.Math.cos
import java.lang.Math.tan

// square root
import java.lang.Math.sqrt

import javax.lang.model.util.Types

/**アフィン変換に使う行列を簡単に生成するヘルパークラス */
object AffineTransformHelper {
	/**平行移動できる行列を生成。
	 * @param x 移動するxの量
	 * @param y 移動するyの量
	 * @param z 移動するzの量
	 * @return 生成された行列
	 */
	def translate(x:Float, y:Float, z:Float) =
		new Mat4(Array(
			1, 0, 0, x,
			0, 1, 0, y,
			0, 0, 1, z,
			0, 0, 0, 1
	))

	/**拡大/縮小できる行列を生成。
	 * @param x 拡大/縮小するxの量
	 * @param y 拡大/縮小するyの量
	 * @param z 拡大/縮小するzの量
	 * @return 生成された行列
	 */
	def scale(x:Float, y:Float, z:Float) =
		new Mat4(Array(
			x, 0, 0, 0,
			0, y, 0, 0,
			0, 0, z, 0,
			0, 0, 0, 1
		))

	/** X軸を中心に回転のための行列を作る。
	 * @param r 回転する量。ラジアン値を入れてください。
	 * @return 生成された行列
	 */
	def rotateByXAxis(r: Double) =
		new Mat4(Array(
			1, 0, 0, 0,
			0, cos(r).toFloat, -sin(r).toFloat, 0,
			0, sin(r).toFloat,  cos(r).toFloat, 0,
			0, 0, 0, 1
		))

	/** y軸を中心に回転のための行列を作る。
	 *
	 * @param r 回転する量。ラジアン値を入れてください。
	 * @return 生成された行列
	 */
	def rotateByYAxis(r: Double) = new Mat4(Array(
		cos(r).toFloat, 0, sin(r).toFloat, 0,
		0, 1, 0, 0,
		-sin(r).toFloat, 0, cos(r).toFloat, 0,
		0, 0, 0, 1
	))

	/** Z軸を中心に回転のための行列を作る。
	 *
	 * @param r 回転する量。ラジアン値を入れてください。
	 * @return 生成された行列
	 */
	def rotateByZAxis(r: Double) = new Mat4(Array(
		cos(r).toFloat, -sin(r).toFloat, 0, 0,
		sin(r).toFloat, cos(r).toFloat, 0, 0,
		0, 0, 1, 0,
		0, 0, 0, 1
	))

	/** (x, y, z)を軸にr回転するための行列を作成する。
	 *
	 * @param x 軸にする行列のx座標
	 * @param y 軸にする行列のy座標
	 * @param z 軸にする行列のz座標
	 * @return 生成された行列 */
	def rotate(x: Float, y: Float, z: Float, r: Float): Mat4 = {
		val d = sqrt(x * x + y * y + z * z).asInstanceOf[Float]
		if (0.0 < d) {
			val l = x / d
			val m = y / d
			val n = z / d
			val l2 = l * l
			val m2 = m * m
			val n2 = n * n
			val lm = l * m
			val mn = m * n
			val nl = n * l
			val c = cos(r).toFloat
			val c1 = 1.0f - c
			val s = sin(r).toFloat
			return new Mat4(Array(
				(1.0f - l2) * c + l2, lm * c1 + n * s, nl * c1 - m * 2, 0,
				lm * c1 - n * s, (1.0f - m2) * c + m2, mn * c1 + l * s, 0,
				nl * c1 + m * s, mn * c1 - l * 2, (1.0f - n2) * c + n2, 0,
				0, 0, 0, 1
			))
		}
		new Mat4(Array(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1))
	}

	/** ビュー変換行列を作成する
	 *
	 * @param e 視点の位置
	 * @param g 目標点の位置。つまり見ている方向。
	 * @param u 上方向のベクトル。つまり画面の上の部分がどれに当たるかを示す。
	 * @return 生成された行列
	 */
	def lookAt(e: Vec3, g: Vec3, u: Vec3): Mat4 = {
		val tv = translate(-e.x, -e.y, -e.z)
		var t = e - g
		var r = u * t
		var s = t * r
		s = s.normalize // 正規化

		r = r.normalize
		t = t.normalize
		val rv = new Mat4(Array(
			r.x, r.y, r.z, 0,
			s.x, s.y, s.z, 0,
			t.x, t.y, t.z, 0,
			0, 0, 0, 1
		))
		rv - tv
	}

	/** 透視投影変換行列を作成する。
	 *
	 * @param fovy   画角
	 * @param aspect アスペクト比。すなわち、横/縦。
	 * @param near   表示する範囲(視錐台)で最も手前のもの
	 * @param far    表示する範囲(視錐台)で最も奥のもの
	 * @return 変換行列
	 */
	def frustum(fovy: Float, aspect: Float, near: Float, far: Float): Mat4 = {
		if (near == far) return new Mat4(Array(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)) // 単位行列
		new Mat4(Array(
			1 / tan(fovy / 2).toFloat / aspect, 0, 0, 0,
			0, 1 / tan(fovy / 2).asInstanceOf[Float], 0, 0,
			0, 0, -(far + near) / (far - near), -2 * far * near / (far - near),
			0, 0, -1, 0
		))
	}
}
