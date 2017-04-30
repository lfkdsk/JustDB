package buffer

import storage.ExPage

/**
 * Created by liufengkai on 2017/4/30.
 */
interface PageFormatter {
	fun format(page: ExPage)
}