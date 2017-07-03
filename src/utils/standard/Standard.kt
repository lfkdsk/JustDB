package utils.standard

/**
 * Created by liufengkai on 2017/7/3.
 */

inline fun <T, R> T.If(case: (T) -> Boolean,
                       ifBlock: (T) -> R,
                       elseBlock: (T) -> R): R {
	return if (case(this)) ifBlock(this) else elseBlock(this)
}