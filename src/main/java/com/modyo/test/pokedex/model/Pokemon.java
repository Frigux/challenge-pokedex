package com.modyo.test.pokedex.model;

import java.util.List;

/**
 * Clase POJO Pokemon
 */
public class Pokemon {
    private String name;
    private int weight;
    private Sprite sprite;
    private List<PokemonType> types;
    private List<PokemonAbility> abilities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Sprite getSprites() {
        return sprite;
    }

    public void setSprites(Sprite sprite) {
        this.sprite = sprite;
    }

    public List<PokemonType> getTypes() {
        return types;
    }

    public void setTypes(List<PokemonType> types) {
        this.types = types;
    }

    public List<PokemonAbility> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<PokemonAbility> abilities) {
        this.abilities = abilities;
    }
}
