package fr.remiguitreau.aroeveren.lucine.api;


public interface LucineAPIDriver {
    
    byte[] retrieveStaysFileAsXLSFormat(
			AroevenStaysDescriptor aroevenStaysDescriptor);
}
