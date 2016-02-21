package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by David on 2/11/2016.
 */
public class Icons {
    private TextureRegion backpack;// icon for inventory
    private Texture joystick; // for movement

    public static final int backpackW=50,backpackH=50;
    public Icons(){
        backpack=new TextureRegion(new Texture("backpack.png"),150,0,150,150);
        joystick=new Texture("Stick.png");
    }

    public void render(Batch batch){

        batch.draw(backpack,0,Game.HEIGHT-backpackH,backpackW,backpackH);


        //draw the joystick(foreground)(alfa 0.3)
        Color c=batch.getColor();
        batch.setColor(c.r, c.g, c.b, 0.3f);
        batch.draw(joystick, 0, 0, Game.joystickW, Game.joystickH);

    }
}
