package fr.remiguitreau.aroeven.lucine.impl;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import fr.remiguitreau.aroeven.lucine.api.AroevenSite;
import fr.remiguitreau.aroeven.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeven.lucine.api.LucineAccessDescriptor;
import fr.remiguitreau.aroeven.lucine.api.StaySeason;
import fr.remiguitreau.aroeven.lucine.api.dsl.AroevenStaysDescriptorBuilder;
import fr.remiguitreau.aroeven.lucine.impl.LucineAPIRequestFactoryImpl;

public class LucineAPIRequestFactoryImplTest {

    private LucineAPIRequestFactoryImpl lucineAPIRequestFactory;

    private LucineAccessDescriptor lucineAccessDescriptorForTest;

    @Before
    public void init_test() {
        lucineAPIRequestFactory = new LucineAPIRequestFactoryImpl();
        lucineAccessDescriptorForTest = new LucineAccessDescriptor("userName", "password", "http://test.fr");
    }

    @Test
    public void test_build_retrieve_stays_method() throws IOException {
        final AroevenStaysDescriptor aroevenStaysDescriptor = AroevenStaysDescriptorBuilder.newStayDescriptor().organizedBy(
                AroevenSite.CAEN).onSeason(StaySeason.BAFA).fromYear(2011).build();
        final HttpMethod method = lucineAPIRequestFactory.buildRetrieveStaysMethod(
                lucineAccessDescriptorForTest, aroevenStaysDescriptor);
        Assert.assertTrue(method instanceof PostMethod);
        Assert.assertEquals("http://test.fr/inscription/actions.php", method.getURI().toString());
        Assert.assertEquals(String.valueOf(AroevenSite.CAEN.code),
                ((PostMethod) method).getParameter(LucineAPIRequestFactoryImpl.ID_ORGA_NAME).getValue());
        Assert.assertEquals(StaySeason.BAFA.code,
                ((PostMethod) method).getParameter(LucineAPIRequestFactoryImpl.SEASON_NAME).getValue());
        Assert.assertEquals("2011",
                ((PostMethod) method).getParameter(LucineAPIRequestFactoryImpl.STAY_YEAR_NAME).getValue());
    }
}
