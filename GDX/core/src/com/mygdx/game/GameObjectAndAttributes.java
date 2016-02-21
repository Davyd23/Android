package com.mygdx.game;

/**
 * Created by David on 2/12/2016.
 */
public abstract class GameObjectAndAttributes extends GameObject{
    protected int strength,defense, dexterity, intelligence,constitution,health;

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if(health>getMaxHealth()){
            this.health=getMaxHealth();
        }
    }

    public int getDamage(){
        return 2*strength;
    }

    //how much dmg is blocked
    public int getResist(){
        return defense/2;
    }

    public int getMaxHealth() {
        return 2*constitution;
    }


}
