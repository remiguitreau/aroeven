package fr.remiguitreau.aroeven.lucine.impl;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import fr.remiguitreau.aroeven.lucine.api.AroevenStay;

public class StaysExtractorFromCSVFormatTest {

    @Test
    public void test_extraction() {
        final List<AroevenStay> stays = new StaysExtractorFromCSVFormat().extractStays(StaysExtractorFromCSVFormatTest.class.getResourceAsStream("test"));
        Assert.assertEquals(2, stays.size());

        Assert.assertEquals("303147", stays.get(0).getProperty("Inscription"));
        Assert.assertEquals("NAME1 Prénom1", stays.get(0).getProperty("Jeune"));
        Assert.assertEquals("13 rue du Flair CP1 Ville1", stays.get(0).getProperty("Adresse"));
        Assert.assertEquals("CP1", stays.get(0).getProperty("CP"));
        Assert.assertEquals("Ville1", stays.get(0).getProperty("Ville"));
        Assert.assertEquals("13/08/2002", stays.get(0).getProperty("Date"));

        Assert.assertEquals("303148", stays.get(1).getProperty("Inscription"));
        Assert.assertEquals("NAME2 Prénom3", stays.get(1).getProperty("Jeune"));
        Assert.assertEquals("22 rue du Flair CP2 Ville2", stays.get(1).getProperty("Adresse"));
        Assert.assertEquals("CP2", stays.get(1).getProperty("CP"));
        Assert.assertEquals("Ville2", stays.get(1).getProperty("Ville"));
        Assert.assertEquals("13/08/2002", stays.get(1).getProperty("Date"));
    }

}
