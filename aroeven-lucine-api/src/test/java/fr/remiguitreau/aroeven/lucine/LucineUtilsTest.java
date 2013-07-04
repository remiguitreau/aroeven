package fr.remiguitreau.aroeven.lucine;

import org.junit.Assert;
import org.junit.Test;

import fr.remiguitreau.aroeven.lucine.LucineUtils;
import fr.remiguitreau.aroeven.lucine.api.LucineAccessDescriptor;

public class LucineUtilsTest {

    @Test
    public void test_build_redirect_url() {
        Assert.assertEquals("http://lucine.aroeven.fr/inscription/../download/extraction_0LHi4H9T2.xls",
                LucineUtils.buildRedirectUrl(new LucineAccessDescriptor("user", "password",
                        "http://lucine.aroeven.fr"), "/inscription/actions.php",
                        "../download/extraction_0LHi4H9T2.xls"));
    }

}
