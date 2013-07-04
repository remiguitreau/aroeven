package fr.remiguitreau.aroeven.lucine.impl;

import org.apache.commons.lang3.Validate;

import fr.remiguitreau.aroeven.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeven.lucine.api.LucineAPIConnector;
import fr.remiguitreau.aroeven.lucine.api.LucineAPIDriver;

public class DefaultLucineAPIDriver implements LucineAPIDriver {

    private final LucineAPIConnector lucineAPIConnector;

    public DefaultLucineAPIDriver(final LucineAPIConnector lucineAPIConnector) {
        Validate.notNull(lucineAPIConnector);
        this.lucineAPIConnector = lucineAPIConnector;
    }

    @Override
    public byte[] retrieveStaysFileAsXLSFormat(final AroevenStaysDescriptor aroevenStaysDescriptor) {
        return lucineAPIConnector.retrieveStaysFileAsXLSFormat(aroevenStaysDescriptor);
    }

}
