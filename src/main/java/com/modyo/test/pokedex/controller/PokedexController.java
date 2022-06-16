package com.modyo.test.pokedex.controller;

import com.modyo.test.pokedex.exceptions.PokemonNotFoundException;
import com.modyo.test.pokedex.exceptions.PokemonServerException;
import com.modyo.test.pokedex.model.Pokemon;
import com.modyo.test.pokedex.service.PokedexService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase controlador que expone los servicios API REST de la aplicacion
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pokemon")
public class PokedexController {

    private static final Logger LOG = LoggerFactory.getLogger(PokedexController.class);

    private final PokedexService pokedexService;

    /**
     * Servicio REST de tipo GET que permite obtener informacion basica
     * de un pokemon segun el nombre enviado en el path
     *
     * @param nombre: Nombre del pokemon a buscar
     * @return Pokemon con su informacion basica
     */
    @GetMapping("/{nombre}")
    public ResponseEntity<Pokemon> getPokemon(@PathVariable("nombre") String nombre){
        LOG.info(String.format("Calling getPokemon(%s)", nombre));
        return new ResponseEntity<>(pokedexService.getPokemonInformation(nombre), HttpStatus.OK);
    }

    /**
     * Controlador de excepcion en caso de que no se encuentre el pokemon buscado
     *
     * @param pokemonNotFoundException:  Excepcion personalizada
     * @return Mensaje de excepcion
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public String handlePokemonNotFound(PokemonNotFoundException pokemonNotFoundException) {
        return pokemonNotFoundException.getMessage();
    }

    /**
     * Controlador de excepcion en caso de que ocurra alguna excepcion de tipo 5xx
     *
     * @param pokemonServerException Excepcion personalizada
     * @return Mensaje de excepcion
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public String handlePokemonServerError(PokemonServerException pokemonServerException) {
        return pokemonServerException.getMessage();
    }

}
