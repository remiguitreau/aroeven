package fr.remiguitreau.aroeven.lucine.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.remiguitreau.aroeven.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeven.lucine.api.LucineAPIConnector;
import fr.remiguitreau.aroeven.lucine.api.StaysExtractor;

public class DefaultLucineAPIDriverTest {

    private DefaultLucineAPIDriver lucineAPIDriver;

    @Mock
    private LucineAPIConnector lucineAPIConnectorMock;

    @Mock
    private AroevenStaysDescriptor aroevenStaysDescriptorMock;

    @Mock
    private StaysExtractor staysExtractorMock;

    @Before
    public void init_test() {
        MockitoAnnotations.initMocks(this);
        lucineAPIDriver = new DefaultLucineAPIDriver(lucineAPIConnectorMock, staysExtractorMock);
    }

    @Test
    public void test_retrieve_stays_as_xls_format() {
        Mockito.when(lucineAPIConnectorMock.retrieveStaysFileAsXLSFormat(aroevenStaysDescriptorMock)).thenReturn(
                new byte[] { 0, 1, 2, 3, 4, 5 });

        Assert.assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5 },
                lucineAPIDriver.retrieveStaysFileAsXLSFormat(aroevenStaysDescriptorMock));
    }
}
