package opengl.by.java;

/** 2つの要素を持つベクトル **/
public class Vec3 {
	/**要素 */
	public final float x, y, z;
	/**0ベクトル */
	public static final Vec3 ZERO = new Vec3(0, 0, 0);

	/**コンストラクタ。
	@param x 1つめの要素
	@param y 2つめの要素
	@param z 3つめの要素
	*/
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**ベクトルとクロス積を取る。
	@param target 掛けるベクトル
	@return 結果 */
	public Vec3 cross(Vec3 target) {
        float rx = Math.fma(y, target.z, -z * target.y);
        float ry = Math.fma(z, target.x, -x * target.z);
        float rz = Math.fma(x, target.y, -y * target.x);
		return new Vec3(
			Math.fma(y, target.z, -z * target.y),
			Math.fma(z, target.x, -x * target.z),
			Math.fma(x, target.y, -y * target.x)
		);
	}

	/**ベクトルとドット積を取る。
	@param targeet 掛けるベクトル
	@return 結果 */
	public float dot(Vec3 target) {
		return x * target.x + y * target.y + z * target.z;
	}

	/**ベクトルを足す。
	@param target 足すベクトル
	@return 結果 */
	public Vec3 plus(Vec3 target) {
		return new Vec3(
			this.x + target.x,
			this.y + target.y,
			this.z + target.z
		);
	}

	/**ベクトルを引く。
	@param target 引くベクトル
	@return 結果 */
	public Vec3 minus(Vec3 target) {
		return new Vec3(
			this.x - target.x,
			this.y - target.y,
			this.z - target.z
		);
	}

	/**ベクトルを正規化する。
	@return 正規化した後のベクトル */
	public Vec3 normalize() {
		final var norm = length();
		if (norm == 1) return this;
		if (norm == 0) throw new ArithmeticException("normalize by [0,0,0]");
		return new Vec3(
			x / norm,
			y / norm,
			z / norm
		);
	}

	/**ベクトルの長さを求める.
	@return ベクトルの長さ */
	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}

	/**@inheritDoc */
	@Override
	public String toString() {
		return "[" + x + "," + y + "," + z + "]";
	}
}
