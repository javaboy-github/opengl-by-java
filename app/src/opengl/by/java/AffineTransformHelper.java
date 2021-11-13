package opengl.by.java;

// 三角関数
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.tan;

// square root
import static java.lang.Math.sqrt;

import javax.lang.model.util.Types;

/**アフィン変換に使う行列を簡単に生成するヘルパークラス */
public class AffineTransformHelper {
	/**平行移動できる行列を生成。
	 * @param x 移動するxの量
	 * @param y 移動するyの量
	 * @param z 移動するzの量
	 * @return 生成された行列
	 */
	public static Mat4 translate(float x, float y, float z) {
		return new Mat4(
			1, 0, 0, x,
			0, 1, 0, y,
			0, 0, 1, z,
			0, 0, 0, 1
		);
	}

	/**拡大/縮小できる行列を生成。
	 * @param x 拡大/縮小するxの量
	 * @param y 拡大/縮小するyの量
	 * @param z 拡大/縮小するzの量
	 * @return 生成された行列
	 */
	public static Mat4 scale(float x, float y, float z) {
		return new Mat4(
			x, 0, 0, 0,
			0, y, 0, 0,
			0, 0, z, 0,
			0, 0, 0, 1
		);
	}

	/** X軸を中心に回転のための行列を作る。
	 * @param r 回転する量。ラジアン値を入れてください。
	 * @return 生成された行列
	 */
	public static Mat4 rotateByXAxis(double r) {
		return new Mat4(
			1, 0, 0, 0,
			0, (float) cos(r), (float) -sin(r), 0,
			0, (float) sin(r), (float) cos(r), 0,
			0, 0, 0, 1
		);
	}

	/** y軸を中心に回転のための行列を作る。
	 * @param r 回転する量。ラジアン値を入れてください。
	 * @return 生成された行列
	 */
	public static Mat4 rotateByYAxis(double r) {
		return new Mat4(
			(float) cos(r), 0, (float) sin(r), 0,
			0, 1, 0, 0,
			(float) -sin(r), 0, (float) cos(r), 0,
			0, 0, 0, 1
		);
	}

	/** Z軸を中心に回転のための行列を作る。
	 * @param r 回転する量。ラジアン値を入れてください。
	 * @return 生成された行列
	 */
	public static Mat4 rotateByZAxis(double r) {
		return new Mat4(
			(float) cos(r), (float) -sin(r), 0, 0, 
			(float) sin(r), (float) cos(r), 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		);
	}

	/**(x, y, z)を軸にr回転するための行列を作成する。
	 * @param x 軸にする行列のx座標
	 * @param y 軸にする行列のy座標
	 * @param z 軸にする行列のz座標
	 * @return 生成された行列 */
	public static Mat4 rotate(float x, float y, float z, float r) {
		final float d = (float) sqrt(x * x + y * y + z * z);
		if (0.0 < d) {
			final float l = x / d;
			final float m = y / d;
			final float n = z / d;
			final float l2 = l * l;
			final float m2 = m * m;
			final float n2 = n * n;
			final float lm = l * m;
			final float mn = m * n;
			final float nl = n * l;
			final float c = (float) cos(r);
			final float c1 = 1.0f - c;
			final float s = (float) sin(r);

			return new Mat4(
							(1.0f - l2) * c + l2, lm * c1 + n * s,			nl * c1 - m * 2,			0,
							lm * c1 - n * s,			(1.0f - m2) * c + m2, mn * c1 + l * s,			0,
							nl * c1 + m * s,			mn * c1 - l * 2,			(1.0f- n2) * c + n2, 0,
							0,										0,										0,										1
			);
		}
		return new Mat4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
	} 

    /** ビュー変換行列を作成する 
     * @param e 視点の位置
     * @param g 目標点の位置。つまり見ている方向。
     * @param u 上方向のベクトル。つまり画面の上の部分がどれに当たるかを示す。
     * @return 生成された行列
    */
    public static Mat4 lookAt(Vec3 e, Vec3 g, Vec3 u) {
        var tv = translate(-e.x, -e.y, -e.z);
        var t = e.minus(g);
        var r = u.cross(t);
        var s = t.cross(r);
        s = s.normalize(); // 正規化
		r = r.normalize();
        t = t.normalize();

        final var rv = new Mat4(
            r.x, r.y, r.z, 0,
            s.x, s.y, s.z, 0,
            t.x, t.y, t.z, 0,
            0, 0, 0, 1
        );


        return rv.mul(tv);
	}
	
	/**透視投影変換行列を作成する。
	  * @param fovy 画角
	  * @param aspect アスペクト比。すなわち、横/縦。
	  * @param near 表示する範囲(視錐台)で最も手前のもの
	  * @param far 表示する範囲(視錐台)で最も奥のもの
	  * @return 変換行列
	  */
	public static Mat4 frustum(float fovy, float aspect, float near, float far) {
		if (near == far)
			return new Mat4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1); // 単位行列
		return new Mat4(
			1  / (float) tan(fovy / 2) / aspect, 0, 0, 0,
			0, 1 / (float) tan(fovy / 2), 0, 0,
			0, 0, -(far + near) / (far - near), -2 * far * near / (far - near),
			0, 0, -1, 0
		);
	}
}
