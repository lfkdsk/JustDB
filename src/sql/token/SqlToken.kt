package sql.token

import utils.parsertools.ast.Token

/**
 * Created by liufengkai on 2017/4/27.
 */

open class SqlToken(override val lineNumber: Int,
                    override var tag: Int,
                    override var text: String) : Token {

	companion object default {

		val EOF = SqlToken(-1, SqlToken.EOF_TAG, "End of File")
		/**
		 * End of line
		 */
		val EOL = "\\n"

		val AND = 256
		val BASIC = 257
		val BREAK = 258
		val DO = 259
		val ELSE = 260
		val EQ = 261
		val FALSE = 262
		val GE = 263
		val ID = 264
		val IF = 265
		val INDEX = 266
		val LE = 267
		val MINUS = 268
		val NE = 269
		val NUM = 270
		val OR = 271
		val REAL = 272
		val TEMP = 273
		val TRUE = 274
		val WHILE = 275
		val STRING = 276
		val LIST = 277
		val BLOCK = 278
		val BINARY = 279
		val FUNCTION = 280
		val NEGATIVE = 281
		val NULL = 282
		val PARALIST = 283
		val POSTFIX = 284
		val PRIMARY = 285
		val FOR = 286
		val CLOSURE = 287
		val CLASS_TOKEN = 288
		val CLASS_BODY_TOKEN = 289
		val ARRAY = 290
		val CREATE_ARRAY = 291
		val OPTION = 292
		val IMPORT = 293
		val BOOL = 294
		val VAR = 295
		val INT = 296
		val FLOAT = 297
		val TYPE = 298
		val NEGATIVEBOOL = 295
		val RETURN = 296
		val CONSTANT_LIST = 297
		val FIELD = 298
		val FIELD_LIST = 299
		val FIELD_DEF = 300
		val FIELD_DEF_LIST = 301
		val CREATE_TABLE = 302
		val CREATE_VIEW = 303
		val CREATE_INDEX = 304
		val INSERT = 305
		val QUERY = 306
		val DELETE = 307
		val UPDATE = 308
		val TERM = 309
		val PREDICATE = 310
		val TABLE_LIST = 311
		val SELECT_LIST = 312
		val EOF_TAG = -1
		val EOL_TAG = -2
		val EMPTY = -100
	}

	constructor(lineNumber: Int, tag: Int)
			: this(lineNumber, tag, "")

	override fun isIdentifier(): Boolean {
		TODO("not implemented")
	}

	override fun isNumber(): Boolean {
		TODO("not implemented")
	}

	override fun isString(): Boolean {
		TODO("not implemented")
	}

	override fun isNull(): Boolean {
		TODO("not implemented")
	}

	override fun isType(): Boolean {
		TODO("not implemented")
	}

	override fun isBool(): Boolean {
		TODO("not implemented")
	}
}
