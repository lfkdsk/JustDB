package storage

/**
 * Created by liufengkai on 2017/4/30.
 */
class ExPageTest extends GroovyTestCase {

    static ExPage exPage

    static int testInt = 100

    @Override
    protected void setUp() throws Exception {
        super.setUp()
        exPage = new ExPage()
    }

    void testAppend() {
        ExPage p2 = new ExPage();
        p2.setString(20, "hello");
        Block blk = p2.append("junk");
        ExPage p3 = new ExPage();
        p3.read(blk);
        String s = p3.getString(20);
        println("re-get ===> " + s)
        assertEquals("hello", s)
    }

    void testGetInt() {
        Block block = new Block("testInt", 0)
        exPage.read(block)
        int n = exPage.getInt(0);
        println("n ===> " + n)
    }

    void testSetInt() {
        Block block = new Block("testInt", 0)
        exPage.read(block)
        println("write ===> " + exPage.setInt(0, testInt))
        exPage.write(block);
    }

    void testGetString() {
        Block block = new Block("testStr", 0)
        exPage.read(block)
        String n = exPage.getString(0);
        println("n ===> " + n)
    }

    void testSetString() {
        Block block = new Block("testStr", 0)
        exPage.read(block)
        println("write ===> " + exPage.setString(0, "lfkdsk"))
        exPage.write(block);
    }
}
