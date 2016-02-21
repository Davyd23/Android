package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by David on 2/13/2016.
 */
public class Item extends GameObjectAndAttributes {
    private String mName;
    private Texture spritesheet;
    private String mType;  //types: helm, armor, shoulders, ring, greaves, weapon
    private boolean mEquipped;

    public Item(Texture res,String name,String type,int x,int y,int strength,int defense,int dexterity,int intelligence,int constitution){
        spritesheet=res;
        mName=name;
        mType=type;
        this.x=x;
        this.y=y;
        this.strength=strength;
        this.defense=defense;
        this.dexterity=dexterity;
        this.intelligence=intelligence;
        this.constitution=constitution;

        width=res.getWidth();
        height=res.getHeight();

        mEquipped=false;

    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public boolean isEquipped() {
        return mEquipped;
    }

    public void setEquipped(boolean mEquipped) {
        this.mEquipped = mEquipped;
    }


    public void draw(Batch batch){ // draw on map, when picked uo wont be anymore
        batch.draw(spritesheet,x,y);
    }

    public void drawInventory(Batch batch){// draw in inventory, when inventory showed up set x,y,width, height
        batch.draw(spritesheet,x,y,width,height);
    }
}
