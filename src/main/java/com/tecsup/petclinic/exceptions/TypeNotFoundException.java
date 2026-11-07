package com.tecsup.petclinic.exceptions;

/**
 * Se lanza cuando no existe un tipo con el id buscado.
 */
public class TypeNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public TypeNotFoundException(String message) {
		super(message);
	}

}
