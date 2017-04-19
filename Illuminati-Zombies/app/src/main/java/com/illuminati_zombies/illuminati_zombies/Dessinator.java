package com.illuminati_zombies.illuminati_zombies;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by eizanprime on 17/04/17.
 */

public class Dessinator extends TexSquare{ //Dessine tout avec openHELL


    protected float vertices[] = {
            100f, 300f, 0.0f,
            100f, 100f, 0.0f,
            300f, 100f, 0.0f,
            300f, 300f, 0.0f

    };

    protected short []indices = new short[] {0, 1, 2, 0, 2, 3};
    //public float color[] = new float[] {0.0f, 0.6f, 1.0f, 1.0f};
    float []uv = new float[]{0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f};



    ArrayList<GameObject> targetPipeline;
    int texture;
    int index;
    protected NewRenderer renderer;

    protected FloatBuffer vertexBuffer;
    protected ShortBuffer drawListBuffer;
    protected FloatBuffer uvBuffer;
    protected int bufferLength;

    public Dessinator(NewRenderer renderer, int texture, ArrayList<GameObject> targetPipeline){
        this.targetPipeline = targetPipeline;

        this.texture = texture;
        this.renderer = renderer;


    }
    public void update(GameObject target){
        index = targetPipeline.indexOf(target);
        vertexBuffer.position(index*12);
        vertexBuffer.put(target.getVertex());
        vertexBuffer.position(0);

        uvBuffer.position(index*8);
        uvBuffer.put(target.getUvs());
        uvBuffer.position(0);
    }

    public void transformArray(){
        if(targetPipeline.size() * 1.5 > bufferLength) {
            ByteBuffer bb = ByteBuffer.allocateDirect(targetPipeline.size() * 12 * 4);//size of float
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();

            ByteBuffer dlb = ByteBuffer.allocateDirect(targetPipeline.size() * 6 * 2); // (# of coordinate values * 2 bytes per short)
            dlb.order(ByteOrder.nativeOrder());
            drawListBuffer = dlb.asShortBuffer();

            //initialize the uvvvvvvv hahahahaha
            ByteBuffer uvb = ByteBuffer.allocateDirect(uv.length * 8 * 4); // (# of coordinate values * 4 bytes per float)
            uvb.order(ByteOrder.nativeOrder());
            uvBuffer = uvb.asFloatBuffer();
        }
        for(int i = 0; i < targetPipeline.size(); i++) {
            GameObject target = targetPipeline.get(i);
            vertices = target.getVertex();
            vertexBuffer.position(i*12);
            vertexBuffer.put(vertices);
            vertexBuffer.position(0);


            indices[0] = (short)(4 * i);
            indices[1] = (short)(4 * i + 1) ;
            indices[2] = (short)(4 * i + 2);
            indices[3] = (short)(4 * i);
            indices[4] = (short)(4 * i + 2);
            indices[5] = (short)(4 * i + 3);

            drawListBuffer.position(6*i);
            drawListBuffer.put(indices);
            drawListBuffer.position(0);



            uv = target.getUvs();
            uvBuffer.position(8*i);
            uvBuffer.put(uv);
            uvBuffer.position(0);
        }

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
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, renderer.getMtrxProjectionAndView(), 0);

        // Get handle to textures locations //j'y pige rien
        int mSamplerLoc = GLES20.glGetUniformLocation (NewRenderer.shaderProgramTextures,
                "s_texture" );



        GLES20.glUniform1i (mSamplerLoc, 0); //TODO a rendre modulable pour textures
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, targetPipeline.size()*6, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(positionAttrib); //so we don't modify anymore
        GLES20.glDisableVertexAttribArray(textAttrib); //same for texture
    }

}
