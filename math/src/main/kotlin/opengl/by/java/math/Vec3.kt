package opengl.by.java.math

/**
* 不変な3次元のベクトルクラス
*/
class Vec3(
	/** ベクトルの1つ目の成分 */
	val x :Double,
	/** ベクトルの2つ目の成分 */
	val y :Double,
	/** ベクトルの3つ目の成分 */
	val z :Double
) {
	/** ベクトルの要素を変換する。
		@param callback コールバック。要素を受け取ってdouble型で返す。
		@return 変換したベクトル
	*/
	fun map(callback: (e: Double) -> Double):Vec3 {
		return Vec3(callback(this.x), callback(this.y), callback(this.z))
	}

	/** 自分自身を返す。
		@return 自分自身
	*/
	operator fun unaryPlus(): Vec3 = this

	/** 要素の符号をマイナスに変換します。これは、`map{-it}`と同義です。
		@return 変換したベクトル
	*/
	operator fun unaryMinus(): Vec3 = Vec3(-this.x, -this.y, -this.z)
	/** ベクトル同士を加算する。
		@param vec 足す数
		@return 加算したベクトル
	*/
	operator fun plus(vec: Vec3): Vec3 = Vec3(this.x + vec.x, this.y+ vec.y, this.z+vec.z)
	/** ベクトル同士を減算する。
		@param vec 引く数
		@return 減算したベクトル
	*/
	operator fun minus(vec: Vec3): Vec3 = this + -vec
	/** スカラ倍。これは、`map{it * number}`と同義です。
		@return 結果
*/
	operator fun times(num: Double): Vec3 = this.map{it * num}
	/** オブジェクトが等価か判定する
	 	@return 透過かどうか
	*/
	override fun equals(other: Any?) :Boolean{
        if (this === other) return true
		if (other == null) return false
        if (javaClass != other?.javaClass) return false

        other as Vec3

        return this.x== other.x && this.y == other.y && this.z == other.z
	}
}
