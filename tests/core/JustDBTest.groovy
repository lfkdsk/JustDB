package core

import buffer.BufferManager
import logger.LogManager
import storage.FileManager

/**
 * Created by liufengkai on 2017/4/30.
 */
class JustDBTest extends GroovyTestCase {

    JustDB justDB = JustDB.Factory.create()

    @Override
    protected void setUp() throws Exception {
        super.setUp()
//        justDB.init("lfkdsk")
    }

    void testInit() {
        println("JustDB init ===> " + justDB.toString())
    }

    void testGetService() {
        println("JustDB getService ===> " + (justDB.Factory.getService(JustDBService.FILE_MANAGER) as FileManager).toString())
        println("JustDB getService ===> " + (justDB.Factory.getService(JustDBService.LOGGER_MANAGER) as LogManager).toString())
        println("JustDB getService ===> " + (justDB.Factory.getService(JustDBService.BUFFER_MANAGER) as BufferManager).toString())
    }

    void testGet() {
        println("JustDB getService ===> " + (justDB.Factory[JustDBService.FILE_MANAGER] as FileManager).toString())
        println("JustDB getService ===> " + (justDB.Factory[JustDBService.LOGGER_MANAGER] as LogManager).toString())
        println("JustDB getService ===> " + (justDB.Factory[JustDBService.BUFFER_MANAGER] as BufferManager).toString())
    }
}
