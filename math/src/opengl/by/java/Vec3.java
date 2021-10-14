package opengl.by.java;

/** Vector **/
public class Vec3 {
	public final float x, y, z;
	public final Vec3 ZERO = new Vec3(0, 0, 0);

	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

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

	public float dot(Vec3 target) {
		return x * target.x + y * target.y + z * target.z;
	}

	public Vec3 plus(Vec3 target) {
		return new Vec3(
			this.x + target.x,
			this.y + target.y,
			this.z + target.z
		);
	}

	public Vec3 minus(Vec3 target) {
		return new Vec3(
			this.x - target.x,
			this.y - target.y,
			this.z - target.z
		);
	}

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

	public float length() {
		return (float)Math.sqrt(x + y + z);
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "," + z + "]";
	}
}
