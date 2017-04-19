package com.illuminati_zombies.illuminati_zombies;


import android.graphics.RectF;

/**
 * Created by eizanprime on 26/02/17.
 */

public class MapWall extends GameObject {
    //protected Bitmap[] images;
    protected int dmg;
    public static void sDraw(){
    }
    public MapWall(RectF rect) {
        super(rect);
        this.numFrames = numFrames;
        this.scalefactor = scalefactor;
    }
}
