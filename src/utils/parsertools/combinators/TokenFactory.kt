package utils.parsertools.combinators

import utils.parsertools.ast.AstList
import utils.parsertools.ast.AstNode
import java.lang.reflect.Method

/**
 * Created by liufengkai on 2017/4/24.
 *
 * @author lfkdsk
 * @author ice1000
 */

abstract class TokenFactory {

	protected abstract fun make0(arg: Any): AstNode

	companion object FunctionCreator {
		val CREATE = "create"

		/**
		 * 直接创建一个AstList
		 *
		 * @param clazz 创建类
		 * @return 工厂
		 */
		fun getForAstList(clazz: Class<out AstNode>?): TokenFactory {
			var f: TokenFactory? = FunctionCreator[clazz, List::class.java]

			return object : TokenFactory() {
				override fun make0(arg: Any): AstNode {
					@Suppress("UNCHECKED_CAST")
					val results: List<AstNode> = arg as? List<AstNode> ?: emptyList<AstNode>()
					// 节点折叠
					if (results.size == 1) return results.first()
					else return AstList()
				}
			}.apply { if (null == f) f = this }
		}

		/**
		 * 静态构建工厂类
		 *
		 * @param clazz   类
		 * @param argType 参数 也是一个类
		 * @return 工厂
		 */
		operator fun get(clazz: Class<out AstNode>?, argType: Class<*>): TokenFactory? {
			if (null == clazz) return null
			// 这是调用了对象的create函数
			try {
				val method: Method = clazz.getMethod(CREATE, argType)
				return object : TokenFactory() {
					override fun make0(arg: Any): AstNode {
						return method.invoke(null, arg) as AstNode
					}
				}
			} catch (e: NoSuchMethodException) {
			}

			// 调用对象的构造
			val constructor = clazz.getConstructor(argType)
			return object : TokenFactory() {
				override fun make0(arg: Any): AstNode {
					return constructor.newInstance(arg)
				}
			}
		}
	}

	fun make(arg: Any) = make0(arg)
}