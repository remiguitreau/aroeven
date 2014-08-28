package fr.remiguitreau.aroeven.lucine.impl;

import org.apache.commons.lang3.Validate;

import java.io.ByteArrayInputStream;
import java.util.List;

import fr.remiguitreau.aroeven.lucine.api.AroevenStay;
import fr.remiguitreau.aroeven.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeven.lucine.api.LucineAPIConnector;
import fr.remiguitreau.aroeven.lucine.api.LucineAPIDriver;
import fr.remiguitreau.aroeven.lucine.api.StaysExtractor;

public class DefaultLucineAPIDriver implements LucineAPIDriver {

    private final LucineAPIConnector lucineAPIConnector;

    private final StaysExtractor staysExtractor;

    public DefaultLucineAPIDriver(final LucineAPIConnector lucineAPIConnector,
            final StaysExtractor staysExtractor) {
        Validate.notNull(staysExtractor);
        this.staysExtractor = staysExtractor;
        Validate.notNull(lucineAPIConnector);
        this.lucineAPIConnector = lucineAPIConnector;
    }

    @Override
    public byte[] retrieveStaysFileAsXLSFormat(final AroevenStaysDescriptor aroevenStaysDescriptor) {
        return lucineAPIConnector.retrieveStaysFileAsXLSFormat(aroevenStaysDescriptor);
    }

    @Override
    public List<AroevenStay> retrieveStays(final AroevenStaysDescriptor aroevenStaysDescriptor) {
        return staysExtractor.extractStays(new ByteArrayInputStream(
                retrieveStaysFileAsXLSFormat(aroevenStaysDescriptor)));
    }

}
