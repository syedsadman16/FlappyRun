package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	BitmapFont font;
    Random random;
	int GameState = 1; //Check start, end and active game
    int score = 0;
	//For the dragon character
	Texture[] dragon;
	int dragonSpeed;
	int dragonState; //This will change the images
	int dragY; //We will be changing dragY a lot
    Rectangle dragRectangle; //Used for collision detection
	//Enviroment physics
    double gravity = 0.7;
    float velocity = 0;
    //Projectiles
	Texture[] gem;
	int gemCount;
	ArrayList<Integer> gemX = new ArrayList<Integer>();
	ArrayList<Integer> gemY = new ArrayList<Integer>();
	//Collision detection array
	ArrayList<Rectangle> gemRectangle = new ArrayList<Rectangle>();
	Texture fireball;
	int fireballCount;
	ArrayList<Integer> fireballX = new ArrayList<Integer>();
	ArrayList<Integer> fireballY = new ArrayList<Integer>();
	ArrayList<Rectangle> fireballRectangle = new ArrayList<Rectangle>();
	Texture tree;
	int treeCount;
    ArrayList<Integer> treeX = new ArrayList<Integer>();
    ArrayList<Integer> treeY = new ArrayList<Integer>();
	ArrayList<Rectangle> treeRectangle = new ArrayList<Rectangle>();
	Texture rocket;
	int rocketCount;
	ArrayList<Integer> rocketX = new ArrayList<Integer>();
	ArrayList<Integer> rocketY = new ArrayList<Integer>();
	ArrayList<Rectangle> rocketRectangle = new ArrayList<Rectangle>();



	//This is where the setup goes
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("Full-Background.png");

		//Setup all the frames for the dragon
		dragon = new Texture[5];
		dragon[0] = new Texture("1.png");
        dragon[1] = new Texture("2.png");
        dragon[2] = new Texture("3.png");
        dragon[3] = new Texture("4.png");
        dragon[4] = new Texture("explosion.png");

        //height of the dragon will change as it goes up and down
        dragY = Gdx.graphics.getHeight()/2;

		random = new Random();
		font = new BitmapFont();
		font.getData().setScale(10);

		//Setup textures
		fireball = new Texture("fireball.png");
		tree = new Texture("dead_tree-001.png");
		rocket = new Texture("Red Roket.png");
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


	public void createRocket() {
		float projectileHeight = random.nextFloat() * Gdx.graphics.getHeight();
		rocketY.add((int)projectileHeight);
		rocketX.add(Gdx.graphics.getWidth());
	}


	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		//Game is running
		if(GameState == 2){

			//tap to fly
			if(Gdx.input.justTouched()) {
				velocity = -15;
			}

			//Create the fireballs

			if (fireballCount < 40) {
				fireballCount++;
			} else {
				fireballCount = 0;
				createFireball();
			}
			//Add the fireballs to the screen
			//Only need to get the current rectangle so clear everytime
			fireballRectangle.clear();
			for (int i=0; i < fireballX.size(); i++) {
				batch.draw(fireball, fireballX.get(i), fireballY.get(i),150,150);
				if(score < 20) {
					fireballX.set(i, fireballX.get(i) - 8);
				} else {
					fireballX.set(i, fireballX.get(i) - 16);
				}
				//Each time fireball is created, add rectangle around it
				fireballRectangle.add(new Rectangle(fireballX.get(i), fireballY.get(i),fireball.getWidth(),
						fireball.getHeight()));
			}

			//Create gems and add them to screen
			if (gemCount < 70) {
				gemCount++;
			} else {
				gemCount = 0;
				createGem();
			}
			gemRectangle.clear();
			for (int i=0;i < gemX.size(); i++) {
				batch.draw(gem[1], gemX.get(i), gemY.get(i), 100, 100);
				gemX.set(i, gemX.get(i) - 4);
				gemRectangle.add(new Rectangle(gemX.get(i), gemY.get(i),gem[1].getWidth(),gem[1].getHeight()));
			}

			//Create the trees
			if (treeCount < 150){
				treeCount++;
			} else {
				treeCount = 0;
				createTree();
			}
			//Add trees to screen
			treeRectangle.clear();
			for (int i=0; i < treeX.size(); i++){
				//Will have a constant height but X value will change as it goes across screen
				batch.draw(tree, treeX.get(i), 200,150,treeY.get(i));
				treeX.set(i, treeX.get(i) - 4);
				treeRectangle.add(new Rectangle(treeX.get(i), 200,tree.getWidth(),tree.getHeight()));
			}


			//NEW ROCKETS
			if(score > 10) {
				if (rocketCount < 100) {
					rocketCount++;
				} else {
					rocketCount = 0;
					createRocket();
				}
				//Add the fireballs to the screen
				//Only need to get the current rectangle so clear everytime
				rocketRectangle.clear();
				for (int i = 0; i < rocketX.size(); i++) {
					batch.draw(rocket, rocketX.get(i), rocketY.get(i), 150, 150);
					if(score < 30) {
						rocketX.set(i, rocketX.get(i) - 16);
					} else {
						rocketX.set(i, rocketX.get(i) - 20);
					}
					//Each time fireball is created, add rectangle around it
					rocketRectangle.add(new Rectangle(rocketX.get(i), rocketY.get(i), rocket.getWidth(),
							rocket.getHeight()));
				}
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


		}
		//Game start
		else if(GameState == 1) {
			//In this state, wait until screen touched to start the game
			if(Gdx.input.justTouched()) {
				GameState = 2;
			}
		}

		//End - reset everything
		else if(GameState == 0) {

			 if(Gdx.input.justTouched()) {
				 GameState = 2;
				 dragY = Gdx.graphics.getHeight() / 2;
				 score = 0;
				 velocity = 0;
				 gemY.clear();
				 gemX.clear();
				 gemRectangle.clear();
				 gemCount = 0;
				 fireballY.clear();
				 fireballX.clear();
				 fireballRectangle.clear();
				 fireballCount = 0;
				 treeX.clear();
				 treeY.clear();
				 treeRectangle.clear();
				 treeCount = 0;
			 }
		}




		//draw the dragon onto the left of screen and give it a rectangle
		batch.draw(dragon[dragonState], Gdx.graphics.getWidth() - dragon[dragonState].getWidth() - 100, dragY,
					200, 200);

		dragRectangle = new Rectangle(Gdx.graphics.getWidth() - dragon[dragonState].getWidth() - 100, dragY,
				100,100);

		//Set up collision detections
		//Need a for loop to find gem that was collided with
		for(int j=0; j < gemRectangle.size(); j++){
			if(Intersector.overlaps(dragRectangle, gemRectangle.get(j))) {
				//Once it collides, remove the gem (not clear)
				score++;
				gemRectangle.remove(j);
				gemX.remove(j);
				gemY.remove(j);
				//Break out of loop once done removing that gem or else it will keep going for j and app will crash!
				break;
			}
		}

		//If tree or fireball is hit, then the game has ended
		for(int j=0; j < fireballRectangle.size(); j++){
			if(Intersector.overlaps(dragRectangle, fireballRectangle.get(j))) {
				GameState = 0;
                font.setColor(Color.RED);
                font.draw(batch, "Game Over",Gdx.graphics.getWidth() / 5,Gdx.graphics.getHeight() / 2);
				batch.draw(dragon[4], Gdx.graphics.getWidth() - dragon[dragonState].getWidth() - 100, dragY,
						200, 200);
				break;
			}
		}

		//COLLISION FOR ROCKETS
		for(int j=0; j < rocketRectangle.size(); j++){
			if(Intersector.overlaps(dragRectangle, rocketRectangle.get(j))) {
				GameState = 0;
				font.setColor(Color.RED);
				font.draw(batch, "Game Over",Gdx.graphics.getWidth() / 5,Gdx.graphics.getHeight() / 2);
				batch.draw(dragon[4], Gdx.graphics.getWidth() - dragon[dragonState].getWidth() - 100, dragY,
						200, 200);
				break;
			}
		}

		for(int j=0; j < treeRectangle.size(); j++){
			if(Intersector.overlaps(dragRectangle, treeRectangle.get(j))) {
				GameState = 0;
                font.setColor(Color.RED);
                font.draw(batch, "Game Over",Gdx.graphics.getWidth() / 5 ,Gdx.graphics.getHeight() / 2);
				batch.draw(dragon[4], Gdx.graphics.getWidth() - dragon[dragonState].getWidth() - 100, dragY,
						200, 200);
				break;
			}
		}
		//After we get data, display it
        font.setColor(Color.WHITE);
        font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth() - 150,200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
