package com.illuminati_zombies.illuminati_zombies;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by eizanprime on 13/04/17.
 */

public class NewRenderer implements Renderer {



    //PARTIE GAMEPLAY
    private Player player1 = null;
    private Player player2 = null;
    private static long frameCounter = 0;
    public static long getFrameCounter(){
        return frameCounter;
    }

    Random rnd = new Random();

    //DESSINATORS

    private Dessinator playerDessinator;
    private Dessinator ennemiDessinator;
    private Dessinator projectileDessinator;
    private Dessinator wallDessinator;
    private Dessinator fondDessinator;

    private UIDessinator uidessinator;

    //FIN PARTIE GAMEPLAY

    private long time;
    private long elapsedtime;

    public final static BitmapFactory.Options options = new BitmapFactory.Options();


    public final static String vertexShaderCode = "uniform 	mat4 		uMVPMatrix;" +
            "attribute 	vec4 		vPosition;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    public final static String fragmentShaderCode = "precision mediump float;" +
            "void main() {" +
            "  gl_FragColor = vec4(0.5,0,0,1);" +
            "}";

    public final static String vextexShaderTextures =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";

    public final static String fragmentShaderTextures =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
                    "}";
    public static int shaderProgram;

    public static int shaderProgramTextures;

    private float[] mtrxProjection = new float[16];
    private float[] mtrxView = new float[16];
    private float[] mtrxProjectionAndView = new float[16];

    public float[] getMtrxProjection(){
        return mtrxProjection;
    }
    public float[] getMtrxView(){
        return mtrxView;
    }
    public float[] getMtrxProjectionAndView(){
        return mtrxProjectionAndView;
    }

    // Our screenresolution
    public float screenWidth = 800;
    public float screenHeight = 1300;
    float ratio = 1;
    float virtualsize = 1000;

    private int[] textures;

    private Context context;

    public NewRenderer(Context context){
        super();
        this.context = context;
        options.inScaled = false;   // No pre-scaling


        //GAMEPLAY PART
        GameObject.wallPipeline = new ArrayList<GameObject>();
        GameObject.ennemyPipeline = new ArrayList<GameObject>();
        GameObject.playerPipeline = new ArrayList<GameObject>();
        GameObject.projectilePipeline = new ArrayList<GameObject>();
        GameObject.uiPipeline = new ArrayList<GameObject>();
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    private Triangle triangle;
    private Triangle triangle2;
    private Triangle square;
    private TexSquare textTest;
    private double i = 0;

    @Override
    public void onDrawFrame(GL10 gl){
        frameCounter++;
        time = System.currentTimeMillis();
        //ALL UPDATES
        Player.sUpdate(playerDessinator);
        Zombie.sUpdate(ennemiDessinator);
        Projectile.sUpdate(projectileDessinator);


        GLES20.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, player1.getRectangle().centerX()-ratio*virtualsize/2, player1.getRectangle().centerY()-virtualsize/2, 1f, player1.getRectangle().centerX()-ratio*virtualsize/2, player1.getRectangle().centerY()-virtualsize/2, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
        //triangle.draw();
        /*
        triangle2.draw();
        square.draw();

        triangle2.color[2] = (float)Math.sin(i);*/
        //textTest.draw();

        i += 0.01;
        if(GameObject.ennemyPipeline.size() > 0) {
            ennemiDessinator.draw();
        }
        if(GameObject.playerPipeline.size() > 0){

            playerDessinator.draw();
        }
        if(GameObject.projectilePipeline.size() > 0) {

            projectileDessinator.draw();
        }
        uidessinator.draw();




        // drawing code goes here

        // get the time taken to render the frame
        elapsedtime = System.currentTimeMillis() - time;
        if(elapsedtime < 17){
            try {
            Thread.sleep(17 - elapsedtime);


            } catch (InterruptedException e) {
                // Thread error
                e.printStackTrace();
            }
        }

    }
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        frameCounter = 0;
        // We need to know the current width and height.
        screenWidth = width;
        screenHeight = height;
        ratio = screenWidth/screenHeight;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int) screenWidth, (int) screenHeight);

        // Clear our matrices
        for (int i = 0; i < 16; i++) {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, (ratio)*virtualsize, 0.0f, virtualsize, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
        //GLES20.glViewport(width / 2, 0, width / 2, height / 2);
        int size = GameObject.playerPipeline.size();
            if (size > 1){

                    player1.joystickLocation[0] *= screenWidth;
                    player1.joystickLocation[1] = 1 -  player1.joystickLocation[1];
                    player1.joystickLocation[1] *= screenHeight;
                    player1.joystickLocation[2] *= screenHeight;

                    player1.shootButtonLocation[0] *= screenWidth;
                    player1.shootButtonLocation[1] =1 - player1.shootButtonLocation[1];
                    player1.shootButtonLocation[1] *= screenHeight;
                    player1.shootButtonLocation[2] *= screenHeight;

                player2.joystickLocation[0] = 1 - player2.joystickLocation[0];
                player2.joystickLocation[1] = player2.joystickLocation[1];
                player2.joystickLocation[2] = 1 - player2.joystickLocation[2];
                player2.shootButtonLocation[0] = 1 - player2.shootButtonLocation[0];
                player2.shootButtonLocation[1] = player2.shootButtonLocation[1];
                player2.shootButtonLocation[2] = 1 - player2.shootButtonLocation[2];

                player2.joystickLocation[0] *= screenWidth;
                player2.joystickLocation[1] *= screenHeight;
                player2.joystickLocation[2] *= screenHeight;

                player2.shootButtonLocation[0] *= screenWidth;
                player2.shootButtonLocation[1] *= screenHeight;
                player2.shootButtonLocation[2] *= screenHeight;

            }
            if(size == 1){
                player1.joystickLocation[0] *= screenWidth;
                player1.joystickLocation[1] = 1 - player1.joystickLocation[1];
                player1.joystickLocation[1] *= screenHeight;
                player1.joystickLocation[2] *= screenHeight;

                player1.shootButtonLocation[0] *= screenWidth;
                player1.shootButtonLocation[1] = 1 - player1.shootButtonLocation[1];
                player1.shootButtonLocation[1] *= screenHeight;
                player1.shootButtonLocation[2] *= screenHeight;
            }
            player1.buttons.get(0).setRect(

                    new RectF(player1.joystickLocation[0] - player1.joystickLocation[2],
                            (screenHeight - player1.joystickLocation[1]) + player1.joystickLocation[2],
                    player1.joystickLocation[0] + player1.joystickLocation[2],
                            (screenHeight - player1.joystickLocation[1])  - player1.joystickLocation[2]));


            player1.buttons.get(1).setRect(

                    new RectF(player1.shootButtonLocation[0] - player1.shootButtonLocation[2],
                            (screenHeight - player1.shootButtonLocation[1]) + player1.shootButtonLocation[2],
                        player1.shootButtonLocation[0] + player1.shootButtonLocation[2],
                            (screenHeight - player1.shootButtonLocation[1]) - player1.shootButtonLocation[2]));
            uidessinator.transformArray();
            uidessinator.remakeMatrix();

    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
        textures = new int[10];

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);//colors

        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram); //put shader on graphic card

        //on load les shaders pour les textures

        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vextexShaderTextures);
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderTextures);//colors

        shaderProgramTextures = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgramTextures, vertexShader);
        GLES20.glAttachShader(shaderProgramTextures, fragmentShader);
        GLES20.glLinkProgram(shaderProgramTextures); //put shader on graphic card

        //loadTexture(BitmapFactory.decodeResource(context.getResources(), R.drawable.facebook, options), 0); //TODO DEBUG THIS

        //   /!\  //TODO make this modular

        int[] texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);

        // Retrieve our image from resources.
        int id = context.getResources().getIdentifier("drawable/textures", null, context.getPackageName());

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);
        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();


        //ACTIVATE GAMEPLAY ELEMENTS

        player1 = new Player(0, 0);
        GameObject.playerPipeline.add((GameObject)player1);
        GameObject.ennemyPipeline.add((GameObject)(new Zombie(rnd.nextFloat()*1000f, rnd.nextFloat()*1000f, 30, 1)));

        int i;
        int j = 0;
        /*for(i = 0; i <= 20*400; i += 200) {
            GameObject.wallPipeline.add((GameObject) (new MapWall( 400, 400, i, j, 1, (float) 0.5)));
        }
        for(j = 0; j <= 20*400; j += 200 ){
            GameObject.wallPipeline.add((GameObject) (new MapWall( 400, 400, i, j, 1, (float) 0.5)));
        }
        for(i = 21*400; i >= 0; i -= 200) {
            GameObject.wallPipeline.add((GameObject) (new MapWall( 400, 400, i, j, 1, (float) 0.5)));
        }
        for(j = 21*400; j >= 0; j -= 200 ){
            GameObject.wallPipeline.add((GameObject) (new MapWall( 400, 400, i, j, 1, (float) 0.5)));
        }
        */

        //Setup UI //TODO
        //gameUIButton = new GameUIButton(getWidth()/5 * 4,getHeight()/4*3,getWidth()/4,getHeight()/4*3,0.65f,1,player);

        //we can start the game loop
        /*
        lock = new ReentrantLock();


        triangle = new Triangle(this);
        /*
        triangle2 = new Triangle(new float[]{
                0.0f, 0.3f, 0.0f,
                -0.7f, -0.7f, 0.0f,
                0.3f, -0.7f, 0.0f
        },new short[] {0, 1, 2}, new float[] {1.0f, 0.0f, 0.0f, 1.0f});
        square = new Triangle(new float[]{
                0.0f, 0.4f, 0.0f,
                -0.6f, -0.6f, 0.0f,
                0.4f, -0.6f, 0.0f,
                0.4f, 0.4f, 0.0f
        },new short[] {0, 1, 2, 0, 2, 3}, new float[] {0.0f, 1.0f, 0.0f, 1.0f});*/

        //textTest = new TexSquare(this, 0);

        playerDessinator = new Dessinator(this, 0, GameObject.playerPipeline);
        playerDessinator.transformArray();
        ennemiDessinator = new Dessinator(this, 0, GameObject.ennemyPipeline);
        ennemiDessinator.transformArray();
        projectileDessinator = new Dessinator(this, 0, GameObject.projectilePipeline);

        uidessinator = new UIDessinator(this, 0, GameObject.uiPipeline);
        player1.buttons.add(new GameUIButton(new RectF(), GameUIButton.JOYSTICk,player1));
        player1.buttons.add(new GameUIButton(new RectF(), GameUIButton.ABUTTON,player1));
        GameObject.uiPipeline.add((GameObject)player1.buttons.get(0));
        GameObject.uiPipeline.add((GameObject)player1.buttons.get(1));

    }

    //use this to load textures my little friend, make sure you have enough space in the texture table.
    private void loadTexture(Bitmap bitmap, int textureIndex) {
        GLES20.glGenTextures ( 1, textures, textureIndex );

        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textures[textureIndex] );

        // Scale up if the texture if smaller.
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);


        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        if (textures[textureIndex] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

    }

    public boolean handleEvent(MotionEvent event){ // /!\ ATTTTTEEEEENNNNTIIIONNN  opengl c'est y vers le haut, android c'est y vers le bas !
        // get pointer index from the event object
        int i = -1;
        int j = -1;
        float eventX = event.getX();
        float eventY = event.getY();
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        boolean usedQ = false;
        for(i = 0; i < GameObject.playerPipeline.size(); i++){
            for(j = 0; j < 2; j++){
                if(pointerId == ((Player)GameObject.playerPipeline.get(i)).usedTouchIDS[j]){
                    usedQ = true;
                    break;
                }
            }
            if(usedQ){
                break;
            }
        }

        int maskedAction = event.getActionMasked();
        if(usedQ && j == 0){
            Player player = ((Player)GameObject.playerPipeline.get(i));
            if(maskedAction == MotionEvent.ACTION_MOVE){
                //System.out.println("ACTION MOOOOOOOOOOOOOVE");
                eventX = eventX - player.joystickLocation[0];
                eventY = eventY - player.joystickLocation[1];
                float hyp = eventX*eventX + eventY*eventY;
                if(i == 1){
                    eventX = -eventX;
                    eventY = -eventY;
                }
                if(hyp > player.getSpeed()*player.getSpeed()){
                    player.setDX((eventX/(float)Math.sqrt(hyp))*player.getSpeed());
                    player.setDY(-(eventY/(float)Math.sqrt(hyp))*player.getSpeed());
                }
                else{
                    player.setDX(eventX);
                    player.setDY(-eventY);
                }
            }
        }
        if(usedQ){
            if(maskedAction == MotionEvent.ACTION_UP || maskedAction == MotionEvent.ACTION_POINTER_UP || maskedAction == MotionEvent.ACTION_CANCEL){
                ((Player)GameObject.playerPipeline.get(i)).usedTouchIDS[j] = -1;
                System.out.println("LACHE ! ");
                if(j == 0){
                    System.out.println("Joystick LACHE");
                    ((Player)GameObject.playerPipeline.get(i)).setDX(0);
                    ((Player)GameObject.playerPipeline.get(i)).setDY(0);
                }
            }

        }
        else{
            boolean inTheZone = false;
            float [][]circle = new float[2][];
            for(i = 0; i < GameObject.playerPipeline.size(); i++){
                circle[0] = ((Player)GameObject.playerPipeline.get(i)).joystickLocation;
                circle[1] = ((Player)GameObject.playerPipeline.get(i)).shootButtonLocation;
                for(j = 0; j < 2; j++){
                    if((circle[j][0] - eventX)*(circle[j][0] - eventX) + (circle[j][1] - eventY)*(circle[j][1] - eventY) < circle[j][2]*circle[j][2]){
                        inTheZone = true;
                        break;
                    }
                }
                if(inTheZone){
                    break;
                }
            }
            if(inTheZone){
                if(maskedAction == MotionEvent.ACTION_DOWN || maskedAction == MotionEvent.ACTION_POINTER_DOWN){
                    System.out.println("ACTION DOOOWN");
                    ((Player)GameObject.playerPipeline.get(i)).usedTouchIDS[j] = pointerId;
                    if(j == 1){
                        ((Player)GameObject.playerPipeline.get(i)).shoot();

                        projectileDessinator.transformArray();
                    }
                }
            }
        }
        if(i != GameObject.playerPipeline.size()){
            ((Player)GameObject.playerPipeline.get(i)).buttons.get(j).update(uidessinator);
        }
        return true;
    }

}
