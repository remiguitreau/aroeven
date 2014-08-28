package fr.remiguitreau.aroeven.lucine.api;

import java.io.InputStream;
import java.util.List;

public interface StaysExtractor {

    List<AroevenStay> extractStays(final InputStream inputStream);

}
