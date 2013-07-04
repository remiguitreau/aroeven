package fr.remiguitreau.aroeven.lucine.api;


public interface LucineAPIDriver {
    
    byte[] retrieveStaysFileAsXLSFormat(
			AroevenStaysDescriptor aroevenStaysDescriptor);
}
