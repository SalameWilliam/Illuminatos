package com.illuminati_zombies.illuminati_zombies;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by eizanprime on 18/02/17.
 */

public class Zombie extends GameObject{
    public final static float HALFSIZE = 100f;
    public final static float []ZOMBIEUV = {
            0.875f, 0f,
            0.875f, 0.125f,
            1f, 0.125f,
            1f, 0f
    };
    private int score;
    private boolean up;
    //private Animation animation;
    private long startTime;
    Matrix matrix;
    float degree_from_origin;

    private float speed;
    private float hp;
    private float damage = 5;

    public static void sUpdate(Dessinator dessinator){
        for(int i = 0; i < GameObject.ennemyPipeline.size(); i++){
            GameObject.ennemyPipeline.get(i).update(dessinator);
        }
    }
    public static void sDraw(){
        for(int i = 0; i < GameObject.ennemyPipeline.size(); i++){
        }
    }

    public Zombie(float x, float y, float hp, float speed){
        super(new RectF(x - HALFSIZE, y - HALFSIZE, x + HALFSIZE, y + HALFSIZE));
        super.uvs = ZOMBIEUV;
        dy = 0;
        dx = 0;
        this.speed = speed;
        super.hp = hp;


        startTime = System.nanoTime();
    }

    public void setUp(boolean b){up = b;}

    public void recieveDmg(int dmg){
        super.hp -= dmg;
        if(super.hp < 0){
            GameObject.ennemyPipeline.remove(this);
        }
    }
    public void update(Dessinator dessinator){
        super.update(dessinator);
        if(super.hp < 0){
            GameObject.ennemyPipeline.remove(this);
            dessinator.transformArray();
        }
        dx = speed*(Integer.signum((int)(GameObject.playerPipeline.get(0).getX() - rect.centerX())));
        dy = speed*(Integer.signum((int)(GameObject.playerPipeline.get(0).getY() - rect.centerY())));
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
                if((Zombie)(GameObject.ennemyPipeline.get(i)) != this) {
                    rect = GameObject.ennemyPipeline.get(i).getRectangle();

                    if (RectF.intersects(getRectangle(), rect)) {
                        System.out.println("Collision detected");
                        this.rect.offset(-dx, -dy);

                        //System.out.println("player = " + getRectangle().left + " " + getRectangle().top + " " + getRectangle().right + " " + getRectangle().bottom + " ");
                        //System.out.println("zombie = " + rect.left + " " + rect.top + " " + rect.right + " " + rect.bottom + " ");
                        //this.rect.offset(-dx, -dy);
                        //recieveDmg(((Zombie) GameObject.ennemyPipeline.get(i)).getDamage());


                        return;
                    }
                }
            }
            for (int i = 0; i < GameObject.playerPipeline.size(); i++) {     //TODO: GROUP THE TWO LOOPS
                rect = GameObject.playerPipeline.get(i).getRectangle();
                if (RectF.intersects(getRectangle(), rect)) {
                    System.out.println("Collision detected");
                    this.rect.offset(-dx, -dy);

                    //System.out.println("player = " + getRectangle().left + " " + getRectangle().top + " " + getRectangle().right + " " + getRectangle().bottom + " ");
                    //System.out.println("zombie = " + rect.left + " " + rect.top + " " + rect.right + " " + rect.bottom + " ");
                    //this.rect.offset(-dx, -dy);
                    ((Player)GameObject.playerPipeline.get(i)).recieveDmg(((Zombie) GameObject.ennemyPipeline.get(i)).getDamage());


                    return;
                }
            }

        }





    }

    public void draw(){
    }

    public int getScore(){
        return score;
    }
    public void resetScore(){
        score = 0;
    }
    public float getDamage() {return damage;}

    /*private float rotate(){
        Player player = (Player)GameObject.playerPipeline.get(0);

        double xDiff = player.getX() - x;
        double yDiff = player.getY() - y;

        double angleVectorX = xDiff;
        double angleVectorY = -yDiff;       //L'ordonnée est inversée par rapport à repère normal

        float angle = (float)Math.atan(angleVectorX/angleVectorY);          //On calcule l'angle en fonction du vecteur du point touché
        angle = (float)(angle*180/(Math.PI));

        //On remet l'angle de 0 à 360 degrés
        if(angleVectorY < 0)
            angle += 180;

        if(angleVectorY > 0 && angleVectorX < 0)
            angle += 360;

        return angle;
    }*/


}
