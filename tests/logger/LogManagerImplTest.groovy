package logger

/**
 * Created by liufengkai on 2017/4/30.
 */
class LogManagerImplTest extends GroovyTestCase {
    static LogManager manager

    void setUp() {
        super.setUp()
        manager = new LogManagerImpl()
    }

    void testFlush() {
        manager.flush(10)
    }

    void testAppend() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i + " + message")
        }
        manager.append(list.toArray())
        manager.flush(11)
    }

    void testIterator() {
        for (LogRecord record : manager) {
            println("=====> " + record.toString())
        }
    }
}
