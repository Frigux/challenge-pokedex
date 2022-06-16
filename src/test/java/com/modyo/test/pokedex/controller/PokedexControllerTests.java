package com.modyo.test.pokedex.controller;

import com.modyo.test.pokedex.exceptions.PokemonNotFoundException;
import com.modyo.test.pokedex.exceptions.PokemonServerException;
import com.modyo.test.pokedex.model.*;
import com.modyo.test.pokedex.service.PokedexService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(PokedexController.class)
@ExtendWith(MockitoExtension.class)
class PokedexControllerTests {
	@MockBean
	private PokedexService pokedexService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getPokemonSuccesful() throws Exception {
		Pokemon pokemon = new Pokemon();
		pokemon.setName("ditto");
		pokemon.setWeight(40);
		Ability ability0 = new Ability();
		ability0.setName("limber");
		Ability ability1 = new Ability();
		ability1.setName("imposter");
		PokemonAbility pokemonAbility0 = new PokemonAbility();
		pokemonAbility0.setAbility(ability0);
		PokemonAbility pokemonAbility1 = new PokemonAbility();
		pokemonAbility1.setAbility(ability1);
		List<PokemonAbility> pokemonAbilityList =  new ArrayList<>();
		pokemonAbilityList.add(pokemonAbility0);
		pokemonAbilityList.add(pokemonAbility1);
		Type type = new Type();
		type.setName("normal");
		PokemonType pokemonType = new PokemonType();
		pokemonType.setType(type);
		List<PokemonType> pokemonTypeList =  new ArrayList<>();
		pokemonTypeList.add(pokemonType);
		Sprite sprite = new Sprite();
		sprite.setFront_default("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/132.png");
		pokemon.setAbilities(pokemonAbilityList);
		pokemon.setTypes(pokemonTypeList);
		pokemon.setSprites(sprite);

		when(pokedexService.getPokemonInformation("ditto")).thenReturn(pokemon);

		mockMvc.perform(get("/pokemon/{nombre}", "ditto"))
						.andExpect(jsonPath("$.name").value("ditto"))
						.andExpect(jsonPath("$.weight").value(40))
						.andExpect(jsonPath("$.types").isNotEmpty())
						.andExpect(jsonPath("$.abilities").isNotEmpty())
						.andExpect(jsonPath("$.sprites.front_default").isNotEmpty())
						.andExpect(status().is2xxSuccessful());
	}

	@Test
	void getPokemonFailsWhenPokemonNameIsNotSent() throws Exception {
		mockMvc.perform(get("/pokemon/{nombre}", ""))
				.andExpect(status().is4xxClientError());
	}

	@Test
	void getPokemonNotFound() throws Exception {
		when(pokedexService.getPokemonInformation("asd")).thenThrow(PokemonNotFoundException.class);
		mockMvc.perform(get("/pokemon/{nombre}", "asd"))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	void getPokemonServerError() throws Exception {
		when(pokedexService.getPokemonInformation("newtwo")).thenThrow(PokemonServerException.class);
		mockMvc.perform(get("/pokemon/{nombre}", "newtwo"))
				.andExpect(status().isServiceUnavailable());
	}
}
