package fr.remiguitreau.aroeven.lucine.api;

public interface LucineAPIConnector {

	byte[] retrieveStaysFileAsXLSFormat(
			AroevenStaysDescriptor aroevenStaysDescriptor);
}
