package opengl.by.java;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**カメラを表すクラス。ミュータブル */
public class Camera {
    /**視点の位置 */
    private Vector3f position;
    /**目標点の位置 */
    private Vector3f direction;
    /**上方向のベクトル */
    private Vector3f up;

    /**全て0のベクトルで各フィールドを初期化する。
     * @see opengl.by.java.Camera(Vector3f, Vector3f, Vector3f)
     */
    public Camera() {
        this(new Vector3f(), new Vector3f(), new Vector3f());
    }

    /**視点の位置、目標点の位置、上方向のベクトルを受け取って初期化する。
     * @param position 視点の位置
     * @param direction 目標点の位置
     * @param up 上方向のベクトル
     */
    public Camera(Vector3f position, Vector3f direction, Vector3f up) {
        this.position = position;
        this.direction = direction;
        this.up = up;
    }

    /**ビュー変換行列を計算しする。
     * @return 変換結果
     */
    public Matrix4f viewMatrix() {
        final var tv = new Matrix4f(
            1f, 0f, 0f, -position.x,
            0f, 1f, 0f, -position.y,
            0f, 0f, 1f, -position.z,
            0f, 0f, 0f, 1f
        );

        final var t = new Vector3f(position).sub(direction.x, direction.y, direction.z);
        final var r = new Vector3f(up).cross(t);
        final var s = new Vector3f(t).cross(r);

        final var s2 = s.length();
        if (s2 == 0f) {
            // sの長さが0の時
            return tv;
        }

        // 回転の変換行列
        final var rv = new Matrix4f(
            r.x / (float) Math.sqrt(r.length()), r.y / (float) Math.sqrt(r.length()), r.z / (float) Math.sqrt(r.length()), 0f,
            s.x / (float) Math.sqrt(s.length()), s.y / (float) Math.sqrt(s.length()), s.z / (float) Math.sqrt(s.length()), 0f,
            t.x / (float) Math.sqrt(t.length()), t.y / (float) Math.sqrt(t.length()), t.z / (float) Math.sqrt(t.length()), 0f,
            0f, 0f, 0f, 1f
        ); // 単位行列

        // 視点の平行移動の変換行列に視線の回転の変換行列を乗じる
        return rv.mul(tv);
    }

    /** @inheritDoc */
    @Override
    public String toString() {
        return String.format("[position:%s,direction:%s,up:%s]", position, direction, up);
    }
}
