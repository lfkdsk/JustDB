package transaction.concurrency

import storage.Block

/**
 * Created by liufengkai on 2017/5/1.
 */
class LockTableTest extends GroovyTestCase {
    static LockTable lockTable

    void setUp() {
        super.setUp()
        lockTable = new LockTable()
    }

    void testWriteLock() {
        Block block = new Block("testStr", 0)
        lockTable.readLock(block)
        lockTable.readLock(block)
        lockTable.unlock(block)
        lockTable.writeLock(block)
    }

    void testReadLock() {

    }

    void testUnlock() {

    }

    void testGet() {

    }
}
