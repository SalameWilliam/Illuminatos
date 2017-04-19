package com.illuminati_zombies.illuminati_zombies;

import android.graphics.Rect;
import android.graphics.RectF;


/**
 * Created by eizanprime on 26/02/17.
 */

public class Projectile extends GameObject {

    public final static int FIREBALL = 5, FIREBALL_RANGE = 50, FIREBALL_FRAMES = 10;
    public final static float FIREBALL_SPEED = 20,  FIREBALL_SIZE=100, FIREBALL_DAMAGE = 10, FIREBALL_UV_SIZE = 0.48828125f;
    public final static float []FIREBALL_UV = {
            0f, 0f,
            0f, 0.048828125f,
            0.048828125f, 0.048828125f,
            0.048828125f, 0f
    };
    protected float dmg;
    //protected Animation animation;
    protected int range;
    protected int speed = 5;
    int type;
    int frameCounter = 0;
    protected GameObject owner = null;

    public static void sUpdate(Dessinator dessinator){
        for(int i = 0; i < GameObject.projectilePipeline.size(); i++){
            GameObject.projectilePipeline.get(i).update(dessinator);
        }
    }
    public static void sDraw(){
        for(int i = 0; i < GameObject.projectilePipeline.size(); i++){

        }
    }
    public static float getSize(int type){
        if(type == FIREBALL){
            return FIREBALL_SIZE;
        }
        return 0;
    }
    public static float getSpeed(int type){
        if(type == FIREBALL){
            return FIREBALL_SPEED;
        }
        return 0;
    }
    public static int getRange(int type){
        if(type == FIREBALL){
            return FIREBALL_RANGE;
        }
        return 0;
    }
    public static float[] getUv(int type){
        if(type == FIREBALL){
            return FIREBALL_UV;
        }
        return null;
    }
    public static float getDmg(int type){
        if(type == FIREBALL){
            return FIREBALL_DAMAGE;
        }
        return 0;
    }
    public Projectile(float dx, float dy, GameObject owner, int type){

            super(new RectF(owner.getX() - getSize(type), owner.getY() -getSize(type), owner.getX() + getSize(type), owner.getY() + getSize(type)));
        System.out.println("BUILD");
        float c = (float)Math.sqrt(dx * dx + dy * dy);
        dx = dx / c;
        dy = dy / c;
        this.dx = dx * getSpeed(type);
        this.dy = dy * getSpeed(type);

        this.owner = owner;
        this.uvs = getUv(type).clone();
        this.range = getRange(type);
        this.dmg = getDmg(type);

    }
    public void update(Dessinator dessinator){
        super.update(dessinator);
        frameCounter++;

        /*this.uvs = getUv(type).clone();

        for(int i = 0; i < 8; i += 2) {
            uvs[i] +=  FIREBALL_UV_SIZE;
        }
        if(frameCounter % 10) == 0){

        }*/
        if(dx != 0) {
            RectF rect;
            GameObject obj;
            for(int i = 0; i < GameObject.ennemyPipeline.size(); i++) {
                rect = GameObject.ennemyPipeline.get(i).getRectangle();
                if (RectF.intersects(getRectangle(), rect)){

                    GameObject.ennemyPipeline.get(i).recieveDmg(dmg);
                    GameObject.projectilePipeline.remove(this);
                    dessinator.transformArray();
                    return;

                }
            }
            for(int i = 0; i < GameObject.playerPipeline.size(); i++) {
                obj = GameObject.playerPipeline.get(i);
                if (RectF.intersects(getRectangle(), obj.getRectangle()) && (obj != owner)){

                    GameObject.playerPipeline.get(i).recieveDmg(dmg);
                    GameObject.projectilePipeline.remove(this);
                    dessinator.transformArray();

                    return;

                }
            }
        }
        range--;
        if(range < 0){
            GameObject.projectilePipeline.remove(this);
            dessinator.transformArray();
            return;

        }
    }
    public void draw(){
    }
}
