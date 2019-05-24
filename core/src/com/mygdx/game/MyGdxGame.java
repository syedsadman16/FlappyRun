package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	int GameState;
    Random random;
	//For the dragon character
	Texture[] dragon;
	int dragonSpeed;
	int dragonState; //This will change the images
	int dragY;
	//Enviroment physics
    double gravity = 0.7;
    float velocity = 0;
    //Projectiles
	Texture[] gem;
	int gemCount;
	ArrayList<Integer> gemX = new ArrayList<Integer>();
	ArrayList<Integer> gemY = new ArrayList<Integer>();
	Texture fireball;
	int fireballCount;
	ArrayList<Integer> fireballX = new ArrayList<Integer>();
	ArrayList<Integer> fireballY = new ArrayList<Integer>();
	Texture tree;
	int treeCount;
    ArrayList<Integer> treeX = new ArrayList<Integer>();
    ArrayList<Integer> treeY = new ArrayList<Integer>();



	//This is where the setup goes
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("Full-Background.png");

		//Setup all the frames for the dragon
		dragon = new Texture[4];
		dragon[0] = new Texture("1.png");
        dragon[1] = new Texture("2.png");
        dragon[2] = new Texture("3.png");
        dragon[3] = new Texture("4.png");

        //height of the dragon will change as it goes up and down
        dragY = Gdx.graphics.getHeight()/2;

		random = new Random();

		//Setup textures
		fireball = new Texture("fireball.png");
		tree = new Texture("dead_tree-001.png");
		gem = new Texture[3];
		gem[0] = new Texture("diamond.png");
		gem[1] = new Texture("diamond blue.png");
		gem[2] = new Texture("diamond red.png");

	}

	//Need a method that generates random values for Y coordinate of projectiles
	//Create gems
	public void createGem() {
		//generate random heights on grid for gems to be placed at
		float projectileHeight = random.nextFloat() * Gdx.graphics.getHeight();
		gemY.add((int)projectileHeight);
        //Create a x coordinate corresponding with y
		gemX.add(Gdx.graphics.getWidth());
	}

	//Creates fireball
	public void createFireball() {
		float projectileHeight = random.nextFloat() * Gdx.graphics.getHeight();
		fireballY.add((int)projectileHeight);
		fireballX.add(Gdx.graphics.getWidth());
	}

	//Create trees
	//Height of tree image will be random
	public void createTree(){
		float treeHeight = random.nextFloat() * (Gdx.graphics.getHeight() - 300);
		treeY.add((int)treeHeight);
		//track the y coordinates so it can shift left
		treeX.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		//tap to fly
		if(Gdx.input.justTouched()) {
			velocity = -15;
		}

		//Create the fireballs
		if (fireballCount < 200) {
			fireballCount++;
		} else {
			fireballCount = 0;
			createFireball();
		}
        //Add the fireballs to the screen
		for (int i=0; i < fireballX.size(); i++) {
			batch.draw(fireball, fireballX.get(i), fireballY.get(i),150,150);
			fireballX.set(i, fireballX.get(i) - 8);
		}

		//Create gems and add them to screen
		if (gemCount < 100) {
			gemCount++;
		} else {
			gemCount = 0;
			createGem();
		}
		for (int i=0;i < gemX.size(); i++) {
			batch.draw(gem[1], gemX.get(i), gemY.get(i), 100, 100);
			gemX.set(i, gemX.get(i) - 4);
		}

        //Create the trees
		if (treeCount < 300){
			treeCount++;
		} else {
			treeCount = 0;
			createTree();
		}
        //Add trees to screen
		for (int i=0; i < treeX.size(); i++){
			//Will have a constant height but X value will change as it goes across screen
			batch.draw(tree, treeX.get(i), 200,150,treeY.get(i));
			treeX.set(i, treeX.get(i) - 4);
		}

		//Speed of the dragon animations every 8th loop; delay
		if(dragonSpeed < 6){
		    dragonSpeed++;
        } else {
		    //Once 8th loop runs, reset the loop
		    dragonSpeed = 0;
		    //this is the last state of the dragon
		    if(dragonState < 3){
		        //while the state is not last, keep changing
		        dragonState++;
            } else {
		        //Once state has reached the last one, reset it back to 0
		        dragonState = 0;
            }
        }




		//Speed at which objects fall will increment by 0.2 gravity
		velocity += gravity;
		//This will make it constantly fall down
		dragY-=velocity;

		//Height limitations
		if(dragY < 200){
			dragY = 200;
			dragonState = 3;
		} else if(dragY > Gdx.graphics.getHeight()){
			dragY = Gdx.graphics.getHeight()-200;
		}


		//draw the dragon onto the left of screen
		batch.draw(dragon[dragonState],Gdx.graphics.getWidth() - dragon[dragonState].getWidth() - 100, dragY,
				200,200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
