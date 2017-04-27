package sql.eval

/**
 * Created by liufengkai on 2017/4/28.
 */
interface Eval {
	fun eval(env: Env): Any
}