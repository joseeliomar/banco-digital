package br.com.jbank.exception;

public class NoResponseException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoResponseException(String message) {
		super(message);
	}
}
