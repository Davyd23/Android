package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by David on 2/6/2016.
 */
public abstract class GameObject {

    protected int x,y,width,height;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rectangle getRectangle(){
        return new Rectangle(x,y,width,height);
    }
}
