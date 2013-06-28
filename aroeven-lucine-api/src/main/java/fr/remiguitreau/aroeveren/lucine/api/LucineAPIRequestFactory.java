package fr.remiguitreau.aroeveren.lucine.api;

import org.apache.commons.httpclient.HttpMethod;

public interface LucineAPIRequestFactory {

	HttpMethod buildRetrieveStaysMethod(
			LucineAccessDescriptor lucineAccessDescriptor,
			AroevenStaysDescriptor aroevenStaysDescriptor);

}
