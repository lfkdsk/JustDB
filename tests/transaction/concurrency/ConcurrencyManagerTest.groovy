package transaction.concurrency

import storage.Block

/**
 * Created by liufengkai on 2017/5/1.
 */
class ConcurrencyManagerTest extends GroovyTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp()
    }

    void testReadLock() {
        ConcurrencyManager manager = new ConcurrencyManager()
        Block block = new Block("lfkdsk", 0)
        manager.readLock(block)
        manager.readLock(block)
        manager.release()
    }

    void testWriteLock() {
        ConcurrencyManager manager = new ConcurrencyManager()
        Block block = new Block("lfkdsk", 0)
        manager.writeLock(block)
        manager.readLock(block)
//        manager.writeLock(block)
        manager.release()
    }
}
