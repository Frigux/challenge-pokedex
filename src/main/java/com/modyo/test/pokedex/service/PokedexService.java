package com.modyo.test.pokedex.service;

import com.modyo.test.pokedex.exceptions.PokemonNotFoundException;
import com.modyo.test.pokedex.exceptions.PokemonServerException;
import com.modyo.test.pokedex.model.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Clase con servicios que interactuan con PokeApi
 */
@Service
public class PokedexService {

    private static final Logger LOG = LoggerFactory.getLogger(PokedexService.class);
    private final WebClient webClient;

    /**
     * Constructor del servicio que se encarga de inicializar el objeto webClient
     * ocupado para ejecutar la interaccion con la PokeApi
     *
     * @param baseUrlPokeApi: Url base de la PokeAPI
     */
    public PokedexService(@Value("${pokeapi.get.uri}") String baseUrlPokeApi) {
        final int size = 16 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        this.webClient = WebClient
                            .builder()
                            .exchangeStrategies(strategies)
                            .baseUrl(baseUrlPokeApi)
                            .build();
    }

    /**
     * Servicio que se comunica con PokeApi para obtener informacion
     * basica de un pokemon según su nombre
     *
     * @param nombre: nombre del pokemon a buscar
     * @return Pokemon con su informacion basica
     */
    @Cacheable(cacheNames="pokeCache", key="#nombre", unless="#result==null")
    public Pokemon getPokemonInformation(String nombre){
        LOG.info(String.format("Calling getPokemonInformation(%s)", nombre));

        Mono<Pokemon> pokemonMono = this.webClient
                .get()
                .uri("pokemon/" + nombre)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handlePokemonNotFoundError)
                .onStatus(HttpStatus::is5xxServerError, this::handlePokemonServerError)
                .bodyToMono(Pokemon.class);

        return pokemonMono.block();
    }

    private Mono<Throwable> handlePokemonNotFoundError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class).flatMap(body -> {
            LOG.error("Pokemon no encontrado");
            return Mono.error(new PokemonNotFoundException("Pokemon no encontrado"));
        });
    }

    private Mono<Throwable> handlePokemonServerError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class).flatMap(body -> {
            LOG.error("Error en el proceso del servicio");
            return Mono.error(new PokemonServerException("Error al procesar servicio. Intentarlo mas tarde"));
        });
    }

}
