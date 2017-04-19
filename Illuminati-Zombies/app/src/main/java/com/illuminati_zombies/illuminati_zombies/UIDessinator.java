package com.illuminati_zombies.illuminati_zombies;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.util.ArrayList;

/**
 * Created by eizanprime on 18/04/17.
 */

public class UIDessinator extends Dessinator{

    private float[] mtrxProjection = new float[16];
    private float[] mtrxView = new float[16];
    private float[] mtrxProjectionAndView = new float[16];

    public UIDessinator(NewRenderer renderer, int texture, ArrayList<GameObject> targetPipeline){
        super(renderer, texture, targetPipeline);

        for (int i = 0; i < 16; i++) {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f,  renderer.screenWidth, 0.0f, renderer.screenWidth, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

    }
    public void remakeMatrix(){
        for (int i = 0; i < 16; i++) {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f,  renderer.screenWidth, 0.0f, renderer.screenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
    }
    public void draw(){
        GLES20.glUseProgram(NewRenderer.shaderProgramTextures);

        int positionAttrib = GLES20.glGetAttribLocation(NewRenderer.shaderProgramTextures, "vPosition"); //sort of a pointer
        GLES20.glVertexAttribPointer(positionAttrib, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer); //make it
        GLES20.glEnableVertexAttribArray(positionAttrib);  //enable pointers

        int textAttrib = GLES20.glGetAttribLocation(NewRenderer.shaderProgramTextures, "a_texCoord");

        /* //select the rigth texture before we draw otherwise were screwed
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture); */

        //do the same as for the position but for the texture
        GLES20.glVertexAttribPointer(textAttrib, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);
        GLES20.glEnableVertexAttribArray(textAttrib);

        int mtrxhandle = GLES20.glGetUniformLocation(NewRenderer.shaderProgramTextures, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mtrxProjectionAndView, 0);

        // Get handle to textures locations //j'y pige rien
        int mSamplerLoc = GLES20.glGetUniformLocation (NewRenderer.shaderProgramTextures,
                "s_texture" );



        GLES20.glUniform1i (mSamplerLoc, 0); //TODO a rendre modulable pour textures
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, targetPipeline.size()*6, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(positionAttrib); //so we don't modify anymore
        GLES20.glDisableVertexAttribArray(textAttrib); //same for texture
    }
}
