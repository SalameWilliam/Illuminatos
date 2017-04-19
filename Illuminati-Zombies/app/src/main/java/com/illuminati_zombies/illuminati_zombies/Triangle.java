package com.illuminati_zombies.illuminati_zombies;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by eizanprime on 12/04/17.
 */

public class Triangle {

    private NewRenderer renderer;
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private float vertices[] = {
        200f, 400f, 0.0f,
                200f, 200f, 0.0f,
                400f, 200f, 0.0f

    };
    private short []indices = new short[] {0, 1, 2};
    public float color[] = new float[] {0.0f, 0.6f, 1.0f, 1.0f};

    /* //Transféré vers NewRenderer
    private final String vertexShaderCode = "attribute vec4 vPosition;" + "void main() {" + "  gl_Position = vPosition;" + "}";

    private final String fragmentShaderCode = "precision mediump float;" + "uniform vec4 vColor;" + "void main() {"
            + "  gl_FragColor = vColor;" + "}";
    private int shaderProgram;

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
    */

    public Triangle(NewRenderer renderer) {
        this.renderer = renderer;
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);//size of float
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2); // (# of coordinate values * 2 bytes per short)
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

        /* //transfer vers Renderer
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);//colors

        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram); //put shader on graphic card
        */
    }
    public Triangle(float[] xyz, short[] indices, float[] rgba){
        vertices = xyz;
        color = rgba;
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
        /* //went to NewRenderer
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);//colors

        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram); //put shader on graphic card
        */
    }

    public void draw() {
        GLES20.glUseProgram(NewRenderer.shaderProgram);

        int positionAttrib = GLES20.glGetAttribLocation(NewRenderer.shaderProgram, "vPosition"); //sort of a pointer
        GLES20.glEnableVertexAttribArray(positionAttrib);  //enable pointers
        GLES20.glVertexAttribPointer(positionAttrib, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer); //make it
        int colorUniform = GLES20.glGetUniformLocation(NewRenderer.shaderProgram, "vColor");


        int mtrxhandle = GLES20.glGetUniformLocation(NewRenderer.shaderProgram, "uMVPMatrix");


        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, renderer.getMtrxProjectionAndView(), 0);

        GLES20.glUniform4fv(colorUniform, 1, color, 0);


        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length /3); //make it work by triangle; //we decided to go for the drawElement strat
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(positionAttrib); //so we don't modify anymore
    }


}
