package opengl.by.java.math

/**
* 不変な2次元のベクトルクラス
*/
class Vec2(
	/** ベクトルの1つ目の成分 */
	val x :Double,
	/** ベクトルの2つ目の成分 */
	val y :Double
) {
	/** ベクトルの要素を変換する。
		@param callback コールバック。要素を受け取ってdouble型で返す。
		@return 変換したベクトル
	*/
	fun map(callback: (e: Double) -> Double):Vec2 {
		return Vec2(callback(this.x), callback(this.y))
	}

	/** 自分自身を返す。
		@return 自分自身
	*/
	operator fun unaryPlus(): Vec2 = this

	/** 要素の符号をマイナスに変換します。これは、`map{-it}`と同義です。
		@return 変換したベクトル
	*/
	operator fun unaryMinus(): Vec2 = Vec2(-this.x, -this.y)
	/** ベクトル同士を加算する。
		@param vec 足す数
		@return 加算したベクトル
	*/
	operator fun plus(vec: Vec2): Vec2 = Vec2(this.x + vec.x, this.y+ vec.y)
	/** ベクトル同士を減算する。
		@param vec 引く数
		@return 減算したベクトル
	*/
	operator fun minus(vec: Vec2): Vec2 = this + -vec
	/** スカラ倍。これは、`map{it * number}`と同義です。
		@return 結果
*/
	operator fun times(num: Double): Vec2 = this.map{it * num}
	/** オブジェクトが等価か判定する
	 	@return 透過かどうか
	*/
	override fun equals(other: Any?) :Boolean{
        if (this === other) return true
		if (other == null) return false
        if (javaClass != other?.javaClass) return false

        other as Vec2

        return this.x== other.x && this.y == other.y
	}
}
