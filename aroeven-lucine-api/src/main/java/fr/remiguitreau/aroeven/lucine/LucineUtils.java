package fr.remiguitreau.aroeven.lucine;

import fr.remiguitreau.aroeven.lucine.api.LucineAccessDescriptor;

public class LucineUtils {

    public static String buildRedirectUrl(final LucineAccessDescriptor lucineAccessDescriptor,
            final String basePath, final String redirectUrl) {
        return lucineAccessDescriptor.url + extractBasePathRoot(basePath) + redirectUrl;
    }

    private static String extractBasePathRoot(final String basePath) {
        return basePath.substring(0, basePath.lastIndexOf('/') + 1);
    }

    // -------------------------------------------------------------------------

    private LucineUtils() {

    }
}
