package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;


/**
 * Created by David on 2/6/2016.
 */
public class Player  extends GameObjectAndAttributes{

    private Texture spritesheet;
    private Animation animation;
    private ArrayList<Item> items;

    public Player(Texture res,int w,int h,int numFrames,int heightPosition, int widthPosition,int strength,int defense,int dexterity,int intelligence,int constitution){
        x=Game.WIDTH/2;
        y=Game.HEIGHT/2;

        width=w;
        height=h;

        this.strength=strength;
        this.dexterity=dexterity;
        this.defense=defense;
        this.intelligence=intelligence;
        this.constitution=constitution;
        health=getMaxHealth();

        TextureRegion[] image=new TextureRegion[numFrames];
        animation=new Animation();
        spritesheet=res;

        for(int i=0;i<image.length;i++){
            image[i]=new TextureRegion(res,widthPosition+i * width, heightPosition, width, height);
        }
        animation.setFrames(image);

        items=new ArrayList<Item>();
    }
    public void flip(){
        animation.flip();
    }
    public void update(int dx,int dy){
        x+=dx;
        y+=dy;
        animation.update();
    }

    public void addInventory(Item item) {
        for(Item i:items){
            if(i.getType().equals(item.getType())){

                this.strength-=i.getStrength();
                this.defense-=i.getDefense();
                this.dexterity-=i.getDexterity();
                this.intelligence-=i.getIntelligence();
                this.constitution-=i.getConstitution();

                items.remove(i);
                break;
            }
        }

        this.strength+=item.getStrength();
        this.defense+=item.getDefense();
        this.dexterity+=item.getDexterity();
        this.intelligence+=item.getIntelligence();
        this.constitution+=item.getConstitution();

        items.add(item);
    }

    public void draw(Batch batch){
        batch.draw(animation.getImage(),x,y);
    }

}
