package fr.remiguitreau.aroeveren.lucine.api;

public interface LucineAPIConnector {

	byte[] retrieveStaysFileAsXLSFormat(
			AroevenStaysDescriptor aroevenStaysDescriptor);
}
