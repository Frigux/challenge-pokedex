package com.modyo.test.pokedex.exceptions;

public class PokemonServerException extends RuntimeException{

    public PokemonServerException() {
        super();
    }

    public PokemonServerException(String message) {
        super(message);
    }
}
