package fr.remiguitreau.aroeven.lucine.impl;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import fr.remiguitreau.aroeven.lucine.api.AroevenStay;
import fr.remiguitreau.aroeven.lucine.api.StaysExtractor;

@Slf4j
public class StaysExtractorFromCSVFormat implements StaysExtractor {

    @Override
    public List<AroevenStay> extractStays(final InputStream inputStream) {
        try {
            final List<AroevenStay> stays = new LinkedList<AroevenStay>();
            final List<String> lines = IOUtils.readLines(inputStream, "ISO-8859-1");
            final String[] headers = extractHeaders(lines);
            while (!lines.isEmpty() && !lines.get(0).isEmpty()) {
                stays.add(extractNextStayAndClearLines(headers, lines));
            }
            return stays;
        } catch (final Exception ex) {
            throw new StayExtractionException(ex);
        }
    }

    // -------------------------------------------------------------------------

    private AroevenStay extractNextStayAndClearLines(final String[] headers, final List<String> lines) {
        log.info("Nb headers: {}", headers.length);
        final AroevenStay stay = new AroevenStay();
        int i = 0;
        boolean processString = false;
        StringBuilder buf = new StringBuilder();
        do {
            final String[] splittedProps = lines.get(0).split("\t");
            for (int j = 0; j < splittedProps.length; j++) {
                if (i >= headers.length) {
                    return stay;
                }
                final String splittedProp = splittedProps[j];
                if (splittedProp.startsWith("\"") && j == splittedProps.length - 1) {
                    log.debug("Start string...");
                    processString = true;
                }
                if (processString) {
                    log.debug("append '" + splittedProp + "' to '" + buf.toString() + "'");
                    buf.append(" ").append(splittedProp.replaceAll("\"", ""));
                    if (splittedProp.trim().equals("\"")) {
                        log.debug("... end string = " + buf.toString());
                        processString = false;
                        log.info("    - " + i + " -> " + headers[i] + " = " + buf.toString());
                        stay.addProperty(headers[i], buf.toString().trim());
                        buf = new StringBuilder();
                        i++;
                    }
                } else {
                    log.info("    - " + i + " -> " + headers[i] + " = " + splittedProp);
                    stay.addProperty(headers[i], formatProperty(splittedProp));
                    i++;
                }
            }
            lines.remove(0);
        } while (i < headers.length && !lines.isEmpty());
        log.info("Stay extracted = ", stay.getProperties());
        return stay;
    }

    private String formatProperty(final String prop) {
        return prop.replaceAll("\"", "").trim();
    }

    private String[] extractHeaders(final List<String> lines) {
        final List<String> headers = new ArrayList<String>();
        for (final String header : lines.get(0).split("\t")) {
            if (!formatProperty(header).trim().isEmpty()) {
                headers.add(formatProperty(header).trim());
            }
        }
        lines.remove(0);
        return headers.toArray(new String[headers.size()]);
    }
}
