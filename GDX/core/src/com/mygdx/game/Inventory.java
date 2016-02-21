package com.mygdx.game;

import java.util.ArrayList;

/**
 * Created by David on 2/13/2016.
 */
public class Inventory {
    private ArrayList<Item> inventory;

    public Inventory(){
        inventory=new ArrayList<Item>();
    }

    public void addItem(Item item){
        inventory.add(item);
    }

    public ArrayList<Item> getItems(){
        return inventory;
    }

    public void removeItem(Item item){
        for(Item i:inventory){
            if(i.equals(item)){
                inventory.remove(i);
                break;
            }
        }
    }
}
