package logger

/**
 * Logger 单独测试没有问题
 * 但是 All Test 似乎有时序性问题
 * Created by liufengkai on 2017/4/30.
 */
class LogManagerImplTest extends GroovyTestCase {
    static LogManager manager

    void setUp() {
        super.setUp()
        manager = new LogManagerImpl()
    }

    void testIterator() {
        for (LogRecord record : manager) {
            println("=====> " + record.toString())
        }
    }

    void testAppend() {
        for (int j = 0; j < 5; j++) {
            ArrayList<String> list = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                list.add(i + " + message")
            }
            manager.append(list.toArray())
            manager.flush(10)
        }
    }

    void testFlush() {
//        manager.flush(10)
    }
}
