package kerho.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Michal
 * @version 30 Apr 2025
 *
 */
@Suite
@SelectClasses({ JasenetTest.class, JasenTest.class, KerhoTest.class,
        KokemuksetTest.class, KokemusTest.class, kanta.test.HetuTarkistusTest.class, ValineetTest.class })
public class AllTests {
    //
}
