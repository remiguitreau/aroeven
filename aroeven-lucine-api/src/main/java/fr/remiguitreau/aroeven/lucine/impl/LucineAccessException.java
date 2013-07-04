package fr.remiguitreau.aroeven.lucine.impl;

public class LucineAccessException extends RuntimeException {
	
	public LucineAccessException(final String message) {
		super(message);
	}

	public LucineAccessException(final String message, final Exception cause) {
		super(message, cause);
	}

	public LucineAccessException(final Exception cause) {
		super(cause);
	}
}
