package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

import sun.rmi.runtime.Log;
import sun.security.krb5.internal.EncAPRepPart;

import static com.badlogic.gdx.math.Intersector.nearestSegmentPoint;
import static com.badlogic.gdx.math.Intersector.overlaps;

public class Game extends ApplicationAdapter  implements InputProcessor{
	private SpriteBatch batch;
	private BitmapFont mFont;

	public static final int joystickW=200,joystickH=200;

    private Player player,mMoving,mFighting;
    private boolean mDead=false;
	private ArrayList<Enemy> mEnemy;

    private Icons mIcons;

	private boolean Left=true;

    private ArrayList<Item> mItems;
    private Inventory mInventory;
    private boolean inventory=false;
    private boolean mClick=false; // for inventory, so when you hold on the corner so it acts like a button

    private boolean showItemStats=false;
    private int itemSelected;// for stats showing

	public static int WIDTH;
	public static int HEIGHT;

	class Touch{
		int x;
		int y;
		boolean hold=false;
	}
    Touch mTouch;

	@Override
	public void dispose() {
		batch.dispose();
		mFont.dispose();
	}

	@Override
	public void create () {
		WIDTH=Gdx.graphics.getWidth();
		HEIGHT=Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        mFont=new BitmapFont();
        mFont.setColor(Color.BLACK);

        reset();

		Gdx.input.setInputProcessor(this);

		mTouch=new Touch();
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        if(!mDead) {
            if (!inventory) inventoryFalse();
            else inventoryTrue();
        }else{
            mFont.getData().setScale(3,3); // 15 because default is arial 15
            mFont.draw(batch, "You have died", WIDTH / 2 - 7 * 10, HEIGHT / 2 + 30);

            checkReset();
        }
        batch.end();
	}

    public void inventoryFalse(){
        update();

        //draw background(alfa 1)
        Color c=batch.getColor();
        batch.setColor(c.r, c.g, c.b, 1f);

        player.draw(batch);
        for(Enemy e:mEnemy){
            e.draw(batch);
        }

        for(Item i:mItems){
            i.draw(batch);
        }

        mIcons.render(batch);

    }
    public void inventoryTrue(){

        //draw backpack in inventory
        TextureRegion backpack=new TextureRegion(new Texture("backpack.png"),150,0,150,150);
        batch.draw(backpack,0,Game.HEIGHT-Icons.backpackH,Icons.backpackW,Icons.backpackH);


        int row=1; // after 5 items go on next row
        int itemsContor=0;
        //draw background(alfa 1)
        Color c=batch.getColor();
        batch.setColor(c.r, c.g, c.b, 1f);

        ArrayList<Item> items=mInventory.getItems();
        for(int i=0;i<items.size();i++){
            Item item=items.get(i);

            item.setWidth(100);
            item.setHeight(100);
            item.setX(Icons.backpackW + itemsContor * item.getWidth());
            item.setY(HEIGHT-row*item.getHeight());

            itemsContor++;
            if(itemsContor==5){
                row++;
                itemsContor=0;
            }
            item.drawInventory(batch);
        }
        inventory();
        putItemsOn();
        setShowItemStats();
    }

	public void update(){

		fighting();
        items();
		movement();
        inventory();

	}

	public void fighting(){

		//object interaction
		//bool pt modificare sprite
		boolean interaction=false;
		for(int i=0;i<mEnemy.size();i++) {
            Enemy e=mEnemy.get(i);
			if (overlaps(player.getRectangle(), e.getRectangle()) && player != mFighting) {
				mFighting = new Player(new Texture("player1.png"), 80, 80, 2, 80, 3 * 80, mMoving.strength, mMoving.defense, mMoving.dexterity, mMoving.intelligence, mMoving.constitution);
				mFighting.x = mMoving.x;
				mFighting.y = mMoving.y;
				player = mFighting;
				mTouch.hold = false;
				interaction=true;

				e.health-=player.getDamage()-e.getResist();

                e.swampAnimation();

			} else if (overlaps(player.getRectangle(), e.getRectangle())) {
				player.update(0, 0);
				mMoving.x = mFighting.x;
				mMoving.y = mFighting.y;
				interaction = true;

				e.health-=player.getDamage()-e.getResist();

                e.update();
                player.health-=e.getDamage()-player.getResist();
			}

			if(e.health<=0){
                if(e.getItem()!=null) mItems.add(e.getItem());
				mEnemy.remove(e);

			}

            if(player.health<=0){
                mDead=true;
            }

		}
		if(!interaction){
			player = mMoving;
		}
	}
    public void items(){
        for(int i=0;i<mItems.size();i++){
            Item item=mItems.get(i);
            if(overlaps(player.getRectangle(),item.getRectangle())){
                mInventory.addItem(item);
                mItems.remove(item);
            }
        }
    }
	public void movement(){
		//moving controls
		if(mTouch.hold  && player==mMoving){
			if(mTouch.x>=(2*joystickW/3) && mTouch.x<=joystickW && mTouch.y>HEIGHT-joystickH){
				if(!Left){
					Left=!Left;
					player.flip();
				}
				player.update(5,0);
			}
			if(mTouch.x<=(joystickW/3) && mTouch.y>HEIGHT-joystickH){
				if(Left){
					Left=!Left;
					player.flip();
				}
				player.update(-5,0);
			}
			if(mTouch.y>HEIGHT-joystickH/3 && mTouch.x<=joystickW){
				player.update(0,-5);
			}
			if(mTouch.y>(HEIGHT-joystickH) && mTouch.y<HEIGHT-2*joystickH/3 && mTouch.x<=joystickW){
				player.update(0,5);
			}
		}
	}
    public void inventory(){
        if(mTouch.hold && mTouch.x<=Icons.backpackW && mTouch.y<=Icons.backpackH && mClick) {
            inventory=!inventory;
            mClick=false;
            showItemStats=false;
        }
    }
    public void putItemsOn(){

        if(mTouch.hold && mTouch.x<=5*200+Icons.backpackW && mTouch.x>=Icons.backpackW){  // the second check is to not press outside of bonds and get an item

            int row=mTouch.y/200; // numbers of rows which we must multiply by 5
            int col=(mTouch.x-Icons.backpackW)/200+1; // numbers of colons we must add to see if we have enough elements



            ArrayList<Item> itemsFromInventory=mInventory.getItems();
            if(itemsFromInventory.size()>=row*5+col){ // 5 items per row
                showItemStats=true;
                itemSelected=row*5+col;
            }
        }
    }
    public void setShowItemStats(){
        if(showItemStats) {
            ArrayList<Item> itemsFromInventory=mInventory.getItems();
            Item selectedItem=itemsFromInventory.get(itemSelected - 1);

            mFont.getData().setScale(1, 1);
            mFont.draw(batch,selectedItem.getName()+":",WIDTH-120,HEIGHT);
            mFont.draw(batch, "Strength: " + selectedItem.strength, WIDTH - 120, HEIGHT-20);
            mFont.draw(batch, "Defense: " + selectedItem.defense, WIDTH - 120, HEIGHT - 40);
            mFont.draw(batch, "Dexterity: " + selectedItem.dexterity, WIDTH - 120, HEIGHT - 60);
            mFont.draw(batch, "Intelligence: " + selectedItem.intelligence, WIDTH - 120, HEIGHT - 80);
            mFont.draw(batch, "Constitution: " + selectedItem.constitution, WIDTH - 120, HEIGHT - 100);

            mFont.getData().setScale(1.1f, 1.1f);

            if(selectedItem.isEquipped()) {
                mFont.setColor(Color.GREEN);
                mFont.draw(batch,"Equipped",WIDTH-100,HEIGHT-150);
            }
            else {
                mFont.setColor(Color.BLUE);
                mFont.draw(batch,"Equip",WIDTH-100,HEIGHT-150);
            }
            mFont.setColor(Color.BLACK);
            checkEquip(selectedItem);
        }
    }

    public void checkEquip(Item item){
        if(mTouch.hold && mTouch.x>WIDTH-100 && mTouch.x<WIDTH-50 &&  mTouch.y<160 && mTouch.y>130){
            item.setEquipped(true);

            ArrayList<Item> fromInventory=mInventory.getItems();
            player.addInventory(fromInventory.get(itemSelected-1));

            for(Item i:fromInventory){
                if(i.getType().equals(item.getType()) && i!=item){
                    i.setEquipped(false);
                }
            }
        }
    }

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		mTouch.x=screenX;
		mTouch.y=screenY;
		mTouch.hold=true;

        mClick=true;

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mTouch.hold=false;

        mClick=false;

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mTouch.x=screenX;
		mTouch.y=screenY;

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	enum Keys{
        LEFT,RIGHT,JUMP,DOWN;
    }

    public void checkReset(){
        if(mTouch.hold){
            mDead=false;
            reset();
        }
    }

    public void reset(){

        player=new Player(new Texture("player1.png"),55,75,4,0,0,20,20,20,20,2000);
        mMoving=player;

        mEnemy=new ArrayList<Enemy>();

        Enemy enemy=new Enemy(new Texture("enemy_mexican.png"), Gdx.graphics.getWidth() - 160, 5, 48, 48, 1, 0, 0, 20, 20, 20, 20, 2000,null);
        enemy.setFightingAnimation(new Texture("enemy_mexican.png"),48,48,3, 0, 4 * 48);
        mEnemy.add(enemy);


        Item item=new Item(new Texture("item1.png"),"Sword of truth","weapon",WIDTH-130,HEIGHT/2-10,60,60,60,60,60);
        enemy=new Enemy(new Texture("FF2_Maki.png"),WIDTH-160,HEIGHT/2-10,45,100,1,0,0,15,15,15,15,600,item);
        enemy.setFightingAnimation(new Texture("FF2_Maki.png"),63,110,6,100, 0);
        enemy.flip(true, true);
        mEnemy.add(enemy);

        enemy=new Enemy(new Texture("enemy.png"),WIDTH-160,HEIGHT-75,40,50,1,40,0,45,45,45,45,4500,null);
        enemy.setFightingAnimation(new Texture("enemy.png"),50,50,2,4*48,7 * 48);
        enemy.flip(true,true);
        mEnemy.add(enemy);

        for(Enemy e:mEnemy){
            e.setSize(player.width,player.height);
        }

        mItems=new ArrayList<Item>();
        //mItems.add();

        mInventory=new Inventory();

        mIcons=new Icons();
    }
}
