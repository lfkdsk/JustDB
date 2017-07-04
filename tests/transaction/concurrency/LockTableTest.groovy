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

        // unlock under MAX time
        new Timer(false)
                .schedule(new TimerTask() {
            @Override
            void run() {
                lockTable.unlock(block)
            }
        }, 5000)

        lockTable.writeLock(block)
    }

    void testReadLock() {
        Block block = new Block("test-read-block", 0)
        // read - shared
        lockTable.readLock(block)
        lockTable.readLock(block)
        lockTable.readLock(block)
    }

    void testGet() {
        Block block1 = new Block("testStr_1", 0)
        Block block2 = new Block("testStr_2", 0)
        Block block3 = new Block("testStr_3", 0)
        lockTable.readLock(block1)
        lockTable.writeLock(block2)
        lockTable.writeLock(block3)

        println(lockTable.get(block1))
        println(lockTable.get(block2))
        println(lockTable.get(block3))
    }
}
