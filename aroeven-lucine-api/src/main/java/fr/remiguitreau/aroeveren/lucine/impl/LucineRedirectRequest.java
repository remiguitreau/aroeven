package fr.remiguitreau.aroeveren.lucine.impl;

public class LucineRedirectRequest extends RuntimeException {

    public final String urlToRedirect;

    public LucineRedirectRequest(final String urlToRedirect) {
        this.urlToRedirect = urlToRedirect;
    }

}
