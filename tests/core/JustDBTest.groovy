package core

import buffer.BufferManager
import logger.LogManager
import storage.FileManager

/**
 * Created by liufengkai on 2017/4/30.
 */
class JustDBTest extends GroovyTestCase {

    static JustDB justDB

    @Override
    protected void setUp() throws Exception {
        super.setUp()
        justDB = JustDB.INSTANCE
        justDB.init("lfkdsk")
    }

    void testInit() {
        println("JustDB init ===> " + justDB.toString())
    }

    void testGetService() {
        println("JustDB getService ===> " + (justDB.getService(JustDB.FILE_MANAGER) as FileManager).toString())
        println("JustDB getService ===> " + (justDB.getService(JustDB.LOGGER_MANAGER) as LogManager).toString())
        println("JustDB getService ===> " + (justDB.getService(JustDB.BUFFER_MANAGER) as BufferManager).toString())
    }

    void testGet() {
        println("JustDB getService ===> " + (justDB[JustDB.FILE_MANAGER] as FileManager).toString())
        println("JustDB getService ===> " + (justDB[JustDB.LOGGER_MANAGER] as LogManager).toString())
        println("JustDB getService ===> " + (justDB[JustDB.BUFFER_MANAGER] as BufferManager).toString())
    }
}
