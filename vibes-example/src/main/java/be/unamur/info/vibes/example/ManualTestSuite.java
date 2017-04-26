
package be.unamur.info.vibes.example;

import be.unamur.transitionsystem.dsl.test.TestSuiteDefinition;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class ManualTestSuite extends TestSuiteDefinition{

    @Override
    protected void define() {
        id("testRefund").action("pay").action("change").action("cancel").action("return").end();
        id("testFreeTea").action("free", "tea", "serveTea", "take");
        id("testNoFreeSoda").action("pay", "change").action("soda", "serveSoda").action("open", "take", "close");
    }

}
