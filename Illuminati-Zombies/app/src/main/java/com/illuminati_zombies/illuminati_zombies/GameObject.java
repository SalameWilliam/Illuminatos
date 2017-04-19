package com.illuminati_zombies.illuminati_zombies;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by eizanprime on 18/02/17.
 */

public abstract class GameObject {
    //NECESSAIRE FOR RENDU
    protected float[] vertex;
    protected float[] uvs;
    protected int placeInList;

    //END


    public static ArrayList<GameObject> wallPipeline;
    public static ArrayList<GameObject> ennemyPipeline;
    public static ArrayList<GameObject> playerPipeline;
    public static ArrayList<GameObject> projectilePipeline;
    public static ArrayList<GameObject> uiPipeline;

    Matrix matrix;

    //public static Context context;

    //protected double x;
    //protected double y;
    protected float []abcdxy = new float[8];

    protected float dx;
    protected float dy;
    protected float hp;
    protected float odx;
    protected float ody;
    protected int width, height, rw, rh, numFrames;
    protected RectF rect;
    protected float scalefactor;
    protected float degree_from_origin = 0;

    public GameObject(RectF rect){
        this.rect = rect;
        dx = 0;
        dy = 0;
        matrix = new Matrix();
        vertex = new float[12];


        abcdxy[0] = rect.right;
        abcdxy[1] = rect.top;

        abcdxy[2] = rect.right;
        abcdxy[3] = rect.bottom;

        abcdxy[4] = rect.left;
        abcdxy[5] = rect.bottom;

        abcdxy[6] = rect.left;
        abcdxy[7] = rect.top;

        vertex[0] = abcdxy[0];
        vertex[1] = abcdxy[1];
        vertex[2] = 0;
        vertex[3] = abcdxy[2];
        vertex[4] = abcdxy[3];
        vertex[5] = 0;
        vertex[6] = abcdxy[4];
        vertex[7] = abcdxy[5];
        vertex[8] = 0;
        vertex[9] = abcdxy[6];
        vertex[10] = abcdxy[7];
        vertex[11] = 0;
    }
    /*public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }*/
    public void setDX(float dx){
        if(dx != 0){
            odx = dx;
            if(dy == 0){
                ody = 0;
            }
        }

        this.dx = dx;
    }
    public void setDY(float dy){
        if(dy != 0){
            ody = dy;
            if(dx == 0){
                odx = 0;
            }
        }

        this.dy = dy;
    }
    /*public void setWidth(int width){
        this.width = width;
    }
    public void setHeight(int height){
        this.height = height;
    }
    */
    public RectF getRectangle(){
        return rect;
    }
    public void setRect(RectF rect){
        this.rect =  rect;
        abcdxy[0] = rect.right;
        abcdxy[1] = rect.top;

        abcdxy[2] = rect.right;
        abcdxy[3] = rect.bottom;

        abcdxy[4] = rect.left;
        abcdxy[5] = rect.bottom;

        abcdxy[6] = rect.left;
        abcdxy[7] = rect.top;

        vertex[0] = abcdxy[0];
        vertex[1] = abcdxy[1];
        vertex[2] = 0;
        vertex[3] = abcdxy[2];
        vertex[4] = abcdxy[3];
        vertex[5] = 0;
        vertex[6] = abcdxy[4];
        vertex[7] = abcdxy[5];
        vertex[8] = 0;
        vertex[9] = abcdxy[6];
        vertex[10] = abcdxy[7];
        vertex[11] = 0;

    }
    public void setScaleFactor(float sf){
        this.scalefactor = sf;
    }
    public float getDx(){ return dx; }
    public float getDy(){ return dy; }
    public float getX(){return rect.centerX();}
    public float getY(){return rect.centerY();}
    public double getHeight(){return height;}
    public double getWidth(){return width;}
    public void recieveDmg(float dmg){this.hp -= dmg;}
    public void update(Dessinator dessinator){
        setDX(dx);
        setDY(dy);
        rect.offset(dx, dy);
        float angleVectorX = dx;
        float angleVectorY = dy;       //L'ordonnée est inversée par rapport à repère normal


        abcdxy[0] = rect.right;
        abcdxy[1] = rect.bottom;

        abcdxy[2] = rect.right;
        abcdxy[3] = rect.top;

        abcdxy[4] = rect.left;
        abcdxy[5] = rect.top;

        abcdxy[6] = rect.left;
        abcdxy[7] = rect.bottom;
        if((int)dx !=0 || (int)dy !=0) {
            float angle = (float) Math.atan(angleVectorX / angleVectorY);          //On calcule l'angle en fonction du vecteur du point touché
            angle = (float) (angle * 180 / (Math.PI));

            //On remet l'angle de 0 à 360 degrés
            if (angleVectorY < 0)
                angle += 180;

            if (angleVectorY > 0 && angleVectorX < 0)
                angle += 360;
            this.degree_from_origin = angle;


        }
        matrix.setRotate(-degree_from_origin, rect.centerX(), rect.centerY());
        matrix.mapPoints(abcdxy);


        vertex[0] = abcdxy[0];
        vertex[1] = abcdxy[1];
        vertex[2] = 0;
        vertex[3] = abcdxy[2];
        vertex[4] = abcdxy[3];
        vertex[5] = 0;
        vertex[6] = abcdxy[4];
        vertex[7] = abcdxy[5];
        vertex[8] = 0;
        vertex[9] = abcdxy[6];
        vertex[10] = abcdxy[7];
        vertex[11] = 0;
        dessinator.update(this);

    }
    public float[] getVertex(){
        return vertex;
    }
    public float[] getUvs(){
        return uvs;
    }
    //public void draw(Canvas canvas){}
    public float getOrientationX(){
        if(dy == 0 && dx == 0){
            return odx;
        }
        return dx;
    }
    public float getOrientationY(){
        if(dy == 0 && dx == 0){
            return ody;
        }
        return dy;
    }
}
