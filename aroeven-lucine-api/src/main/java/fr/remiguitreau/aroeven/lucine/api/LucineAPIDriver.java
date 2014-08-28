package fr.remiguitreau.aroeven.lucine.api;

import java.util.List;

public interface LucineAPIDriver {

    byte[] retrieveStaysFileAsXLSFormat(final AroevenStaysDescriptor aroevenStaysDescriptor);

    List<AroevenStay> retrieveStays(final AroevenStaysDescriptor aroevenStaysDescriptor);
}
