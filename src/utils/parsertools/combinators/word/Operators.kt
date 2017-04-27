package combinators.word

import java.util.*


/**
 * Created by liufengkai on 2017/4/24.
 */

class Precedence(internal var value: Int, internal var leftAssoc: Boolean)

/**
 * 标志符
 */
class Operators : HashMap<String, Precedence>() {

	/**
	 * 添加保留字

	 * @param name      保留字Token
	 * *
	 * @param pres      优先级
	 * *
	 * @param leftAssoc 结合性
	 */
	fun add(name: String, pres: Int, leftAssoc: Boolean) {
		put(name, Precedence(pres, leftAssoc))
	}

	enum class ASSOC {
		// 结合性
		LEFT,
		RIGHT
	}
}
