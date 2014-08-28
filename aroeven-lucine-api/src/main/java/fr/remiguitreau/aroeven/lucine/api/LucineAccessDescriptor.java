package fr.remiguitreau.aroeven.lucine.api;

import org.apache.commons.lang3.Validate;

public class LucineAccessDescriptor {

    public final String user;

    public final String password;

    public final String url;

    public LucineAccessDescriptor(final String user, final String password, final String url) {
        Validate.notNull("Lucine login must not be null", user);
        Validate.notNull("Lucine password must not be null", password);
        Validate.notNull("Lucine url must not be null", url);
        this.user = user;
        this.password = password;
        this.url = url;
    }
}
