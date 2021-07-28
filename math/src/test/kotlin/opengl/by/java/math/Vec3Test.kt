package opengl.by.java.math


import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.*;
import org.hamcrest.Matchers.*;

class Vec3Test {
	@Test
	fun インスタンス化できるか() {
		val vec = Vec3(2.0, 3.0, 4.0)
		// assertThat(vec.x, 2.0, "xが代入されていません")
		assertThat(vec.x, `is`(2.0))
		assertThat(vec.y, `is`(3.0))
		assertThat(vec.z, `is`(4.0))
	}

	@Test
	fun 正しく等価であると判定できるか() {
		val vec1 = Vec3(2.0, 3.0, 4.0)
		val vec2 = Vec3(2.0, 3.0, 4.0)
		assertThat(vec1, `is`(vec2))
	}

	@Test
	fun `+演算子(単項)が機能するか`() {
		val vec = Vec3(2.0, 3.0, 4.0)
		assertThat(vec, `is`(+vec))
	}

	@Test
	fun `-演算子(単項)が機能するか`() {
		val vec = Vec3(2.0, 3.0, 4.0)
		assertThat(Vec3(-2.0, -3.0, -4.0), `is`(-vec))
	}

	@Test
	fun `+演算子(二項)が機能するか`() {
		val vec = Vec3(2.0, 3.0, 4.0)
		assertThat(vec + vec, `is`(Vec3(4.0, 6.0, 8.0)))
	}

	@Test
	fun `-演算子(二項)が機能するかどうか`() {
		val vec = Vec3(2.0, 3.0, 4.0)
		assertThat(vec - vec, `is`(Vec3(0.0, 0.0, 0.0)))
	}

	@Test
	fun スカラ倍のテスト() {
		val vec = Vec3(2.0, 3.0, 4.0)
		assertThat(vec * 2.0, `is`(Vec3(4.0, 6.0, 8.0)))
	}
}
