package opengl.by.java;

/** Vector **/
public class Vec3 {
	public final float x, y, z;

	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
		final var norm = (float)Math.sqrt(x + y + z);
		if (norm === 1) return this;
		if (norm === 0) throw new ArithmeticException("normalize by [0,0,0]");
		return new Vec3(
			x / norm,
			y / norm,
			z / norm
		);
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "," + z + "]";
	}
}
