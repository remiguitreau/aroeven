package fr.remiguitreau.aroeveren.lucine.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang3.Validate;

import fr.remiguitreau.aroeveren.lucine.api.AroevenSite;
import fr.remiguitreau.aroeveren.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeveren.lucine.api.LucineAPIConnector;
import fr.remiguitreau.aroeveren.lucine.api.LucineAPIDriver;
import fr.remiguitreau.aroeveren.lucine.api.LucineAccessDescriptor;
import fr.remiguitreau.aroeveren.lucine.api.StaySeason;
import fr.remiguitreau.aroeveren.lucine.api.dsl.AroevenStaysDescriptorBuilder;

public class DefaultLucineAPIDriver implements LucineAPIDriver {

	private final LucineAPIConnector lucineAPIConnector;

	public DefaultLucineAPIDriver(final LucineAPIConnector lucineAPIConnector) {
		Validate.notNull(lucineAPIConnector);
		this.lucineAPIConnector = lucineAPIConnector;
	}

	@Override
	public byte[] retrieveStaysFileAsXLSFormat(
			final AroevenStaysDescriptor aroevenStaysDescriptor) {
		return lucineAPIConnector
				.retrieveStaysFileAsXLSFormat(aroevenStaysDescriptor);
	}

	
}
