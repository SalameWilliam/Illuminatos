package com.illuminati_zombies.illuminati_zombies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.provider.Settings;

/**
 * Created by Will on 16/03/2017.
 */

public class GameUIButton extends GameObject{

    public static void sUpdate(UIDessinator dessinator){

    }
    public static final int JOYSTICk = 0;
    public static final int ABUTTON = 1;
    public static final float []ABUTTONPUSHEDUV= {
            0.500f, 0f,
            0.5f, 0.125f,
            0.625f, 0.125f,
            0.625f, 0f
    };
    public static final float []ABUTTONNOTPUSHEDUV={
        0.625f, 0f,
        0.625f, 0.125f,
        0.750f, 0.125f,
        0.750f, 0f
        };
    public static final float []JOYSTICKNOMOVEUV={
            0.4375f, 0.0625f,
            0.4375f, 0.125f,
            0.5f, 0.125f,
            0.5f, 0.0625f
    };
    public static final float []JOYSTICKMOVEUV={
            0.375f, 0.0625f,
            0.375f, 0.125f,
            0.4375f, 0.125f,
            0.4375f, 0.0625f
    };

    private boolean pushed = false;

    private int type;


    private int leftPadding;
    private int upPadding;
    private int padding = 80;

    private Player player;

    GameUIButton (RectF rect, int type, Player player){
        super(rect);
        this.type = type;
        this.player = player;
        if(type == JOYSTICk){
            uvs = JOYSTICKNOMOVEUV;
        }
        else if(type == ABUTTON){
            uvs = ABUTTONNOTPUSHEDUV;
        }

    }
    public void update(UIDessinator dessinator){
        if(type == ABUTTON){
            if(player.usedTouchIDS[1] < 0){
                uvs = ABUTTONNOTPUSHEDUV;
            }
            else{
                uvs = ABUTTONPUSHEDUV;
            }
            dessinator.update(this);
        }
        if(type == JOYSTICk) {
            float angleVectorX = player.getDx();
            float angleVectorY = player.getDy();       //L'ordonnée est inversée par rapport à repère normal


            abcdxy[0] = rect.right;
            abcdxy[1] = rect.top;

            abcdxy[2] = rect.right;
            abcdxy[3] = rect.bottom;

            abcdxy[4] = rect.left;
            abcdxy[5] = rect.bottom;

            abcdxy[6] = rect.left;
            abcdxy[7] = rect.top;
            if ((int) player.getDx() != 0 || (int) player.getDy() != 0) {
                float angle = (float) Math.atan(angleVectorX / angleVectorY);          //On calcule l'angle en fonction du vecteur du point touché
                angle = (float) (angle * 180 / (Math.PI));

                //On remet l'angle de 0 à 360 degrés
                if (angleVectorY < 0)
                    angle += 180;

                if (angleVectorY > 0 && angleVectorX < 0)
                    angle += 360;
                this.degree_from_origin = angle;

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
                uvs = JOYSTICKMOVEUV;
                dessinator.update(this);
            }
            else{
                uvs = JOYSTICKNOMOVEUV;
                dessinator.update(this);
            }
        }
    }


}
