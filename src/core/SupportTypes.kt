package core

import metadata.data.Schema
import storage.ExPage


/**
 * Created by liufengkai on 2017/7/8.
 */

/**
 * Support Value Types
 */
enum class Types(value: Int) {
	INTEGER(4),
	VARCHAR(12)
}

class UnSupportException(type: String) : Exception("UnSupport Type Value $type")


/**
 * Schema support type API
 * @param fieldName fieldType
 * @return length of Type
 */
fun Schema.lengthOfType(fieldName: String): Int {
	when (infoType(fieldName)) {
		Types.INTEGER -> return ExPage.INT_SIZE
		Types.VARCHAR -> return ExPage.strSize(this[fieldName].length)
		else -> throw UnSupportException(infoType(fieldName).toString())
	}
}