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
	Random random;
	int GameState;
	//For the dragon character
	Texture[] dragon;
	int dragonSpeed;
	int dragonState; //This will change the images
	int dragY;
	//Enviroment physics
    double gravity = 0.7;
    float velocity = 0;
    //Projectiles
	Texture gems[];
	ArrayList<Integer> gemX = new ArrayList<Integer>();
	ArrayList<Integer> gemY = new ArrayList<Integer>();
	int gemRate; //rate at which gems are produced
	Texture tree;
	ArrayList<Integer> treeX = new ArrayList<Integer>();
	ArrayList<Integer> treeY = new ArrayList<Integer>();
	int treeRate;
	Texture fireball;
	ArrayList<Integer> fireballX = new ArrayList<Integer>();
	ArrayList<Integer> fireballY = new ArrayList<Integer>();
	int fireballRate;




	//This is where the setup goes
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("Full-Background.png");
		background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

		//Setup all the frames for the dragon
		dragon = new Texture[4];
		dragon[0] = new Texture("1.png");
        dragon[1] = new Texture("2.png");
        dragon[2] = new Texture("3.png");
        dragon[3] = new Texture("4.png");

        //height of the dragon will change as it goes up and down
        dragY = Gdx.graphics.getHeight()/2;

        //Setup textures
		random = new Random();
		fireball = new Texture("fireball.png");
		tree = new Texture("dead_tree-001.png");
		gems = new Texture[3];
		gems[0] = new Texture("diamond.png");
		gems[1] = new Texture("diamond blue.png");
		gems[2] = new Texture("diamond red.png");
	}

	//Need a method that generates random values for X coordinate of projectiles
	public void createFireball(){
		float projectileHeight = random.nextFloat() * Gdx.graphics.getHeight();
		fireballY.add((int)projectileHeight);
		//When method called, will record width at given height
		fireballX.add(Gdx.graphics.getWidth());
	}

	//Create gems
    public void createGem(){
        float projectileHeight = random.nextFloat() * Gdx.graphics.getHeight();
        gemY.add((int)projectileHeight);
        //When method called, will record width at given height
        gemX.add(Gdx.graphics.getWidth());
    }

    //Create trees
    //This time, the X value will be different
	public void createTrees(){
		float projectileHeight = random.nextFloat() * Gdx.graphics.getHeight();
		treeX.add((int)projectileHeight);
		treeY.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		//flying
		if(Gdx.input.justTouched()) {
			GameState = 1;
			velocity = -15;
		}

		//Create the fireballs
		if(fireballRate < 200){
			fireballRate++;
		} else {
			fireballRate = 0;
			createFireball();
		}
		//Add the fireballs to the screen
		for(int i=0; i<fireballX.size(); i++){
			batch.draw(fireball, fireballX.get(i), fireballY.get(i),150,150);
			fireballX.set(i, fireballX.get(i) - 8);
		}

        //Create the fireballs
        if(gemRate < 100){
            gemRate++;
        } else {
            gemRate = 0;
            createGem();
        }
        //Add the fireballs to the screen
        for(int i=0; i<gemX.size(); i++){
            batch.draw(gems[1], gemX.get(i), gemX.get(i),150,150);
            gemX.set(i, gemX.get(i) - 8);
        }

		//Create the trees
		if(treeRate < 100){
			treeRate++;
		} else {
			treeRate = 0;
			createTrees();
		}
		//Add the fireballs to the screen
		for(int i=0; i<treeX.size(); i++){
			batch.draw(tree, treeX.get(i), treeX.get(i));
			treeX.set(i, treeX.get(i) - 8);
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
