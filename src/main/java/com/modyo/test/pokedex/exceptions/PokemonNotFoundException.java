package com.modyo.test.pokedex.exceptions;

public class PokemonNotFoundException extends RuntimeException{

    public PokemonNotFoundException() {
        super();
    }

    public PokemonNotFoundException(String message) {
        super(message);
    }
}
