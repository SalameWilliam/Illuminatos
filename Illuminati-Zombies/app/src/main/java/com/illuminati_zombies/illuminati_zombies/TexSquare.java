package com.illuminati_zombies.illuminati_zombies;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by eizanprime on 12/04/17.
 */

public class TexSquare{

    private double aVirer = 0;

    private int texture;
    private NewRenderer renderer;
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private FloatBuffer uvBuffer;
    private float vertices[] = {
            100f, 300f, 0.0f,
            100f, 100f, 0.0f,
            300f, 100f, 0.0f,
            300f, 300f, 0.0f

    };
    private float vertices2[] = {
            200f, 400f, 0.0f,
            200f, 200f, 0.0f,
            400f, 200f, 0.0f,
            400f, 400f, 0.0f

    };

    private short []indices = new short[] {0, 1, 2, 0, 2, 3};
    private short []indices2 = new short[] {4, 5, 6, 4, 6, 7};
    //public float color[] = new float[] {0.0f, 0.6f, 1.0f, 1.0f};
    float []uv = new float[]{0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f};


    public TexSquare(){}

    public TexSquare(NewRenderer renderer,int texture) {
        this.texture = texture;
        this.renderer = renderer;
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4 *2);//size of float //TODO MUST CHANGE
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.put(vertices2);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2 *2); // (# of coordinate values * 2 bytes per short)
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.put(indices2);
        drawListBuffer.position(0);

        //initialize the uvvvvvvv hahahahaha
        ByteBuffer uvb = ByteBuffer.allocateDirect(uv.length * 4 *2); // (# of coordinate values * 2 bytes per short)
        uvb.order(ByteOrder.nativeOrder());
        uvBuffer = uvb.asFloatBuffer();
        uvBuffer.put(uv);
        uvBuffer.put(uv);
        uvBuffer.position(0);



    }
    public TexSquare(int texture, float[] xyz, short[] indices, float[] uv){
        this.texture = texture;
        vertices = xyz;
        this.uv = uv;
        this.indices = indices;

        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);//size of float
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2); // (# of coordinate values * 2 bytes per short)
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

        //initialize the uvvvvvvv hahahahaha
        ByteBuffer uvb = ByteBuffer.allocateDirect(indices.length * 4); // (# of coordinate values * 2 bytes per short)
        dlb.order(ByteOrder.nativeOrder());
        uvBuffer = uvb.asFloatBuffer();
        uvBuffer.put(uv);
        uvBuffer.position(0);
    }

    public void draw() {
        aVirer += 0.1;
        vertices2[0] = 100*(float)Math.sin(aVirer);
        //vertexBuffer.put(12, 100*(float)Math.sin(aVirer));
        vertexBuffer.position(12);
        vertexBuffer.put(vertices2);
        vertexBuffer.position(0);
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
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 2*indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(positionAttrib); //so we don't modify anymore
        GLES20.glDisableVertexAttribArray(textAttrib); //same for texture
    }


}
