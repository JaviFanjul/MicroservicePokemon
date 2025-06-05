package main.model;

public class Pokemon {

    // Internal atributes
    private int id;

    private int numberPokedex;

    private String name;

    private int speed;

    private int[] attack;

    private int[] defense;

    private String[] types;

    private String[] weakAgainst;

    public Pokemon() {
        // Default constructor
    }//Pokemon default constructor

    public Pokemon(int id, String name, int numberPokedex, String[] types, String[] weakAgainst, int speed, int[] attack, int[] defense) {
        this.id = id;
        this.name = name;
        this.numberPokedex = numberPokedex;
        this.types = types;
        this.weakAgainst = weakAgainst;
        this.speed = speed;
        this.attack = attack;
        this.defense = defense;
    } //Pokemon constructor

}//Pokemon class
