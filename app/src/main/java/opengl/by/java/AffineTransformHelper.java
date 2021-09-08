package opengl.by.java;

import org.joml.Matrix4f;

// 三角関数
import static java.lang.Math.sin;
import static java.lang.Math.cos;

// square root
import static java.lang.Math.sqrt;

/**アフィン変換に使う行列を簡単に生成するヘルパークラス */
public class AffineTransformHelper {
    /**平行移動できる行列を生成。
     * @param x 移動するxの量
     * @param y 移動するyの量
     * @param z 移動するzの量
     * @return 生成された行列
     */
    public static Matrix4f translate(float x, float y, float z) {
        return new Matrix4f(
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
    public static Matrix4f scale(float x, float y, float z) {
        return new Matrix4f(
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
    public static Matrix4f rotateByXAxis(double r) {
        return new Matrix4f(
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
    public static Matrix4f rotateByYAxis(double r) {
        return new Matrix4f(
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
    public static Matrix4f rotateByZAxis(double r) {
        return new Matrix4f(
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
    public static Matrix4f rotate(double x, double y, double z, double r) {
        var result = new Matrix4f(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
        if (0.0 < sqrt(x * x + y * y + z * z)) {
            // TODO: ノルムが0以上の時の処理を書いておく
        }
        return result;
    } 
}