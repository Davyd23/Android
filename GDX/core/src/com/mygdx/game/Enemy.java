package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by David on 2/11/2016.
 */
public class Enemy extends GameObjectAndAttributes {

    private Texture spritesheet;
    private Animation animation;
    private Animation standBy;
    private Item mItem;

    public Enemy(Texture res,int x,int y,int w,int h,int numFrames,int heightPosition,int widthPosition,int strength,int defense,int dexterity,int intelligence,int constitution,Item item){
        this.x=x;
        this.y=y;

        width=w;
        height=h;

        this.strength=strength;
        this.dexterity=dexterity;
        this.defense=defense;
        this.intelligence=intelligence;
        this.constitution=constitution;
        this.health=getMaxHealth();

        mItem=item;

        TextureRegion[] image=new TextureRegion[numFrames];
        animation=new Animation();
        spritesheet=res;

        for(int i=0;i<image.length;i++){
            image[i]=new TextureRegion(res,widthPosition+i * width, heightPosition, width, height);
        }
        animation.setFrames(image);
    }

    public void update(){
        animation.update();
    }

    public void setSize(int w,int h){
        width=w;
        height=h;
    }

    public Item getItem() {
        return mItem;
    }

    public void setFightingAnimation(Texture res,int w,int h,int numFrames,int heightPosition,int widthPosition){
        TextureRegion[] image=new TextureRegion[numFrames];
        standBy=new Animation();

        for(int i=0;i<image.length;i++){
            image[i]=new TextureRegion(res,widthPosition+i *w, heightPosition,w, h);
        }
        standBy.setFrames(image);
    }

    public void swampAnimation(){  // assignement because eighter player dies, eighter player wins and we don't see the enemy anymore
        if(standBy!=null)
        animation=standBy;
    }

    public void flip(boolean an,boolean sb){
        if(an) animation.flip();
        if(sb) standBy.flip();
    }
    public void draw(Batch batch){
        batch.draw(animation.getImage(),x,y,width,height);
    }
}
