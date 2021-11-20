package opengl.by.java;

/**4x4の行列を表す。 */
public class Mat4 {
  /**行列の成分 */
  public final float 
    m11, m12, m13, m14,
  	m21, m22, m23, m24,
  	m31, m32, m33, m34,
  	m41, m42, m43, m44;

  /**インスタンス化する。
    @param m11 成分
    @param m12 成分
    @param m13 成分
    @param m14 成分
    @param m11 成分
    @param m22 成分
    @param m23 成分
    @param m24 成分
    @param m31 成分
    @param m32 成分
    @param m33 成分
    @param m34 成分
    @param m41 成分
    @param m42 成分
    @param m43 成分
    @param m44 成分
  */
  public Mat4(
      float m11, float m12, float m13, float m14,
      float m21, float m22, float m23, float m24,
      float m31, float m32, float m33, float m34,
      float m41, float m42, float m43, float m44
  ) {
    this.m11 = m11;
    this.m12 = m12;
    this.m13 = m13;
    this.m14 = m14;
    this.m21 = m21;
    this.m22 = m22;
    this.m23 = m23;
    this.m24 = m24;
    this.m31 = m31;
    this.m32 = m32;
    this.m33 = m33;
    this.m34 = m34;
    this.m41 = m41;
    this.m42 = m42;
    this.m43 = m43;
    this.m44 = m44;
  }

  /**この行列に行列を足して、結果を返す。
    @param target 足す行列
    @return 結果
  */
  public Mat4 plus(Mat4 target) {
    return new Mat4(
      m11 + target.m11, m12 + target.m12, m13 + target.m13, m14 + target.m14, 
      m21 + target.m21, m22 + target.m22, m23 + target.m23, m24 + target.m24, 
      m31 + target.m31, m32 + target.m32, m33 + target.m33, m34 + target.m34, 
      m41 + target.m41, m42 + target.m42, m43 + target.m43, m44 + target.m44 
    );
  }

  /**この行列に行列を引いて、結果を返す。
    @param target 引く行列
    @return 結果
  */
  public Mat4 minus(Mat4 target) {
    return new Mat4(
      m11 - target.m11, m12 - target.m12, m13 - target.m13, m14 - target.m14, 
      m21 - target.m21, m22 - target.m22, m23 - target.m23, m24 - target.m24, 
      m31 - target.m31, m32 - target.m32, m33 - target.m33, m34 - target.m34, 
      m41 - target.m41, m42 - target.m42, m43 - target.m43, m44 - target.m44
    );
  }

  /**この行列に行列を掛けて、結果を返す。
    @param target 掛ける行列
    @return 結果
  */
  public Mat4 mul(Mat4 target) {
    var m = target;
    return new Mat4(
      m11 * m.m11 + m12 * m.m21 + m13 * m.m31 + m14 * m.m41,
      m11 * m.m12 + m12 * m.m22 + m13 * m.m32 + m14 * m.m42,
      m11 * m.m13 + m12 * m.m23 + m13 * m.m33 + m14 * m.m43,
      m11 * m.m14 + m12 * m.m24 + m13 * m.m34 + m14 * m.m44,
      m21 * m.m11 + m22 * m.m21 + m23 * m.m31 + m24 * m.m41,
      m21 * m.m12 + m22 * m.m22 + m23 * m.m32 + m24 * m.m42,
      m21 * m.m13 + m22 * m.m23 + m23 * m.m33 + m24 * m.m43,
      m21 * m.m14 + m22 * m.m24 + m23 * m.m34 + m24 * m.m44,
      m31 * m.m11 + m32 * m.m21 + m33 * m.m31 + m34 * m.m41,
      m31 * m.m12 + m32 * m.m22 + m33 * m.m32 + m34 * m.m42,
      m31 * m.m13 + m32 * m.m23 + m33 * m.m33 + m34 * m.m43,
      m31 * m.m14 + m32 * m.m24 + m33 * m.m34 + m34 * m.m44,
      m41 * m.m11 + m42 * m.m21 + m43 * m.m31 + m44 * m.m41,
      m41 * m.m12 + m42 * m.m22 + m43 * m.m32 + m44 * m.m42,
      m41 * m.m13 + m42 * m.m23 + m43 * m.m33 + m44 * m.m43,
      m41 * m.m14 + m42 * m.m24 + m43 * m.m34 + m44 * m.m44
    );
  }

	/**ベクトルと積を取る。このベクトルは一時的に(x, y, z, 1)のベクトルに変形される。なお、対象の行列はアフィン変換行列のもの出ないと正しく機能しない。 
   * @param target 掛けるベクトル
   * @return 結果
  */
	public Vec3 mul(Vec3 target) {
		return new Vec3(
      m11 * target.x + m12 * target.y + m13 * target.z + m14,
      m21 * target.x + m22 * target.y + m23 * target.z + m24,
      m31 * target.x + m32 * target.y + m33 * target.z + m34
    );
	}

  /** @inheritDoc */
  @Override
  public String toString() {
	return String.format("[%d,%d,%d,%d\n%d,%d,%d,%d\n%d,%d,%d,%d\n%d,%d,%d,%d\n]", m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41, m42, m43, m44);
  }

  /**行列を配列に変換する。 
    @return 変換した配列
  */
  public float[] toArray() {
    return new float[]{
      m11, m12, m13, m14,
      m21, m22, m23, m24,
      m31, m32, m33, m34,
      m41, m42, m43, m44
	  } ;
  }
}
