package com.illuminati_zombies.illuminati_zombies;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

/**
 * Created by eizanprime on 13/04/17.
 */

public class NewGLSurf extends GLSurfaceView {
    private NewRenderer renderer;
    public NewGLSurf(Context context, NewRenderer renderer){
        super(context);
        this.renderer = renderer;
        //TODO
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return renderer.handleEvent(event);


    }

}
