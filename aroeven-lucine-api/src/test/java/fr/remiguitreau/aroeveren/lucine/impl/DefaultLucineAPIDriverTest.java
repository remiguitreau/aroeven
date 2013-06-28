package fr.remiguitreau.aroeveren.lucine.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.remiguitreau.aroeveren.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeveren.lucine.api.LucineAPIConnector;

public class DefaultLucineAPIDriverTest {

    private DefaultLucineAPIDriver lucineAPIDriver;

    @Mock
    private LucineAPIConnector lucineAPIConnectorMock;
    
    @Mock
    private AroevenStaysDescriptor aroevenStaysDescriptorMock;

    @Before
    public void init_test() {
        MockitoAnnotations.initMocks(this);
        lucineAPIDriver = new DefaultLucineAPIDriver(lucineAPIConnectorMock);
    }
   
    @Test
    public void test_retrieve_stays_as_xls_format() {
        Mockito.when(lucineAPIConnectorMock.retrieveStaysFileAsXLSFormat(aroevenStaysDescriptorMock)).thenReturn(
                new byte[]{0,1,2,3,4,5});

        Assert.assertArrayEquals(new byte[]{0,1,2,3,4,5}, lucineAPIDriver.retrieveStaysFileAsXLSFormat(aroevenStaysDescriptorMock));
    }
}
