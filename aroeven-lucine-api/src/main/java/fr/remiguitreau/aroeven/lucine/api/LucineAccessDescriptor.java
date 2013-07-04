package fr.remiguitreau.aroeven.lucine.api;

public class LucineAccessDescriptor {

	public final String user;
	public final String password;
	public final String url;

	public LucineAccessDescriptor(final String user, final String password,
			final String url) {
		this.user = user;
		this.password = password;
		this.url = url;
	}
}
