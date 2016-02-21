package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by David on 2/7/2016.
 */
public class Animation {

    private TextureRegion[] frames;
    private int currentFrame;

    public void setFrames(TextureRegion[] frames){
        this.frames=frames;
        currentFrame=0;
    }

    public void flip(){
        for(int i=0;i<frames.length;i++){
            frames[i].flip(true,false);
        }
    }

    public void update(){
        currentFrame++;
        if(currentFrame==frames.length){
            currentFrame=0;
        }
    }

    public TextureRegion getImage(){
        return frames[currentFrame];
    }
}
