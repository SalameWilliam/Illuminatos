package com.illuminati_zombies.illuminati_zombies;


import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by eizanprime on 18/02/17.
 */

public class Player extends GameObject{
    //private Bitmap spritesheet; Tout est maintenant sur un seul truc de textures

    public int[] usedTouchIDS = {-1, -1, -1, -1, -1};

    public ArrayList<GameUIButton> buttons;
    public static final float[] UVPLAYER={
            0.750f, 0f,
            0.750f, 0.125f,
            0.875f, 0.125f,
            0.875f, 0f
    };
    public static final float[] PASDUV={
            0.750f, 0.125f,
            0.750f, 0.250f,
            0.875f, 0.250f,
            0.875f, 0.125f
    };

    public final static float HALFSIZE = 50f;
    private int score;
    private boolean up;
    private boolean playing;
    //private Animation animation;
    private long startTime;
    private long timeInvulnerable = 0;
    int orientation;
    private float speed = 15;
    private int weapon = Projectile.FIREBALL;
    ReentrantLock lock = new ReentrantLock();

    public float []joystickLocation = {0.2f, 0.2f, 0.2f}; // x, y puis le rayon, seras ensuite transformé en coordonnées pixel
    public float []shootButtonLocation = {0.8f, 0.2f, 0.2f};
    //ect

    public int life = 100;
    private boolean vulnerable;
    private float degree_from_origin = 0;


    public static void sUpdate(Dessinator dessinator){
        for(int i = 0; i < GameObject.playerPipeline.size(); i++){
            GameObject.playerPipeline.get(i).update(dessinator);
        }
    }
    /*public static void sDraw(){
        for(int i = 0; i < GameObject.playerPipeline.size(); i++){
        }
    }*/

    public Player(float x, float y){
        super(new RectF(x - HALFSIZE, y - HALFSIZE, x + HALFSIZE, y + HALFSIZE)); //!!!
        super.uvs = UVPLAYER;
        dy = 0;
        dx = 0;
        score = 0;
        vulnerable = true;
        orientation = 0;

        buttons = new ArrayList<GameUIButton>();
        startTime = System.nanoTime();
    }

    public void setUp(boolean b){up = b;}


    public void update(Dessinator dessinator) {
        super.update(dessinator);
        if(!vulnerable){
            if(NewRenderer.getFrameCounter() - timeInvulnerable > 50){
                vulnerable = true;
                uvs = UVPLAYER;
            }
            else{
                if ((NewRenderer.getFrameCounter() - timeInvulnerable)%20 <=10){
                    uvs = PASDUV;
                }
                else {
                    uvs = UVPLAYER;
                }
            }
        }

        if (dx != 0 || dy != 0) {
            RectF rect;
            for (int i = 0; i < GameObject.wallPipeline.size(); i++) {     //TODO: GROUP THE TWO LOOPS
                rect = GameObject.wallPipeline.get(i).getRectangle();
                if (RectF.intersects(getRectangle(), rect)) {
                    System.out.println("Collision detected");
                    this.rect.offset(-dx, -dy);


                    return;
                }
            }

            for (int i = 0; i < GameObject.ennemyPipeline.size(); i++) {     //TODO: GROUP THE TWO LOOPS
                rect = GameObject.ennemyPipeline.get(i).getRectangle();
                if (RectF.intersects(getRectangle(), rect)) {
                    System.out.println("Collision detected");

                    System.out.println("player = " + getRectangle().left + " " + getRectangle().top + " " + getRectangle().right + " " + getRectangle().bottom + " ");
                    System.out.println("zombie = " + rect.left + " " + rect.top + " " + rect.right + " " + rect.bottom + " ");
                    recieveDmg(((Zombie) GameObject.ennemyPipeline.get(i)).getDamage());


                    return;
                }
            }

        }
    }


    public void draw(){
        lock.lock();
        try {
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public void recieveDmg(float dmg) {
        if(vulnerable) {
            System.out.println("Zombie");
            life -= dmg;
            vulnerable = !vulnerable;
            timeInvulnerable = NewRenderer.getFrameCounter();


        }
    }

    /*public void rotate(float degree){
        lock.lock();
        try {
            matrix.setRotate(-degree_from_origin,(float)width/2,(float)height/2);
            this.degree_from_origin = degree;
            matrix.setRotate(degree_from_origin,(float)width/2,(float)height/2);
            matrix.postTranslate(canv_width/2 - width/2, canv_height/2 - height/2);
        }
        finally {
            lock.unlock();
        }
    }*/

    public int getScore(){
        return score;
    }
    public boolean getPlaying(){
        return playing;
    }
    public void setPlaying(boolean b){
        playing = b;
    }
    public void resetScore(){
        score = 0;
    }
    public float getSpeed() { return speed; }
    public int getWeapon(){
        return weapon;
    }
    public int getLife() { return life; }
    public void shoot(){
        if(weapon == Projectile.FIREBALL){
            GameObject.projectilePipeline.add(new Projectile(getOrientationX(), getOrientationY(), this, Projectile.FIREBALL));
        }
    }

}
