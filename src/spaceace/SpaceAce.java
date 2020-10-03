package spaceace;


import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

class Consts{ //коды нажатия и отпускания клавиш
    public static final int KEY_W_PR = 101; 
    public static final int KEY_A_PR = 102;
    public static final int KEY_D_PR = 103;
    public static final int KEY_UP_PR = 104;
    public static final int KEY_LEFT_PR = 105;
    public static final int KEY_RIGHT_PR = 106;
    public static final int KEY_W_RL = 107; 
    public static final int KEY_A_RL = 108;
    public static final int KEY_D_RL = 109;
    public static final int KEY_UP_RL = 110;
    public static final int KEY_LEFT_RL = 111;
    public static final int KEY_RIGHT_RL = 112;
    public static final int NO_ONE = 0;
}

public class SpaceAce {
        String keypr = "nothing";
	private long window;
        int keys[] = new int[20];
        int num = 0;

	public void run() throws InterruptedException {       
                
		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
         
                
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
                
		// Create the window
		window = glfwCreateWindow(1000, 1000, "SpaceAce", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
                
                
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
                for (int i = num;i < keys.length;i++){
                            keys[i] = Consts.NO_ONE;
                        }
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true);
                        
                        if(( key == GLFW_KEY_W && action == GLFW_PRESS )){
                            keys[num] = Consts.KEY_W_PR;
                            num++;
                        } else if (( key == GLFW_KEY_W && action == GLFW_RELEASE )){
                            keys[num] = Consts.KEY_W_RL;
                            num++;
                        } 
                        
                        if(( key == GLFW_KEY_A && action == GLFW_PRESS )){
                            keys[num] = Consts.KEY_A_PR;
                            num++;
                        } else if (( key == GLFW_KEY_A && action == GLFW_RELEASE )) {
                            keys[num] = Consts.KEY_A_RL;
                            num++;
                        } 
                        
                        if(( key == GLFW_KEY_D && action == GLFW_PRESS )){
                           keys[num] = Consts.KEY_D_PR;
                            num++;
                        } else if (( key == GLFW_KEY_D && action == GLFW_RELEASE )) {
                            keys[num] = Consts.KEY_D_RL;
                            num++;   
                        } 
                        
                        if(( key == GLFW_KEY_UP && action == GLFW_PRESS )){
                            keys[num] = Consts.KEY_UP_PR;
                            num++;
                        } else if(( key == GLFW_KEY_UP && action == GLFW_RELEASE )){
                            keys[num] = Consts.KEY_UP_RL;
                            num++;
                        }
                        
                        if(( key == GLFW_KEY_LEFT && action == GLFW_PRESS )){
                            keys[num] = Consts.KEY_LEFT_PR;
                            num++;
                        } else if (( key == GLFW_KEY_LEFT && action == GLFW_RELEASE )) {
                            keys[num] = Consts.KEY_LEFT_RL;
                            num++;
                        }
                        
                        if(( key == GLFW_KEY_RIGHT && action == GLFW_PRESS )){
                           keys[num] = Consts.KEY_RIGHT_PR;
                            num++;
                        } else if (( key == GLFW_KEY_RIGHT && action == GLFW_RELEASE )) {
                            keys[num] = Consts.KEY_RIGHT_RL;
                            num++; 
                        }
                        
                        //делаем так, чтобы массив заполнялся с нуля в следующей итерации
                        num = 0;
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);
		// Make the window visible
		glfwShowWindow(window);
	}

	private void loop() throws InterruptedException {
		GL.createCapabilities();
                Game game = new Game();
                
                Thread.sleep(500);
                
		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                        if (game.loop(keys) == 1){
                            glfwSetWindowShouldClose(window, true);
                        };
                        //Очищаем массив от зажатых клавиш
                        for (int i = num;i < keys.length;i++){
                            keys[i] = Consts.NO_ONE;
                        }
      
			glfwSwapBuffers(window); // swap the color buffers
                        
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
                        glClear(GL_COLOR_BUFFER_BIT); 
                        Thread.sleep(30);
                        
		}
                
	}       

	public static void main(String[] args) throws InterruptedException {
		new SpaceAce().run();              
	}
}

class Game {
    float stars[][] = new float[2][];
    private final Random rand = new Random();
    
    boolean oleftpress = false;
    boolean orightpress = false;
    boolean ouppress = false;

    boolean sleftpress = false;
    boolean srightpress = false;
    boolean suppress = false;
    
    Rocket rocket = new Rocket(-0.5f,0);
    Rocket rocket2 = new Rocket(0.5f,0);
    
    Game() {
        float a[] = new float[100];
        float b[] = new float[100];
        
        for (int i = 0; i < a.length; i++) {
            a[i] = (float)(Math.random() * 2 - 1);
        }
        
        for (int i = 0; i < b.length; i++) {
            b[i] = (float)(Math.random() * 2 - 1);
        }      
        
        this.stars[0] = a;
        this.stars[1] = b;
        
    }
    
    boolean collide(Rocket a,Rocket b) {
        float firsthitbox[] = a.getHitBox();
        float secondhitbox[] = b.getHitBox();
        return (firsthitbox[0] < secondhitbox[0] + secondhitbox[2] &&
                firsthitbox[0] + firsthitbox[2] > secondhitbox[0] &&
                firsthitbox[1] < secondhitbox[1] + secondhitbox[3] &&
                firsthitbox[1] + firsthitbox[3] > secondhitbox [1]);
    }
    
    void draw() {
        for (int i = 0; i < this.stars[0].length; i++) {
            glBegin(GL_POINTS);
            glColor3f(1.0f, 1.0f, 1.0f); // Red
            glVertex2f(this.stars[0][i],this.stars[1][i]); 
            glEnd();
            glFlush(); 
        }
    }
    public int loop(int[] keypr) {
        
        draw();
        this.rocket.draw(0,1,0);
        this.rocket2.draw(1,0,0);
        
        if (isValueInArray(keypr,Consts.KEY_A_PR)) {
            oleftpress = true;
        } else  if (isValueInArray(keypr,Consts.KEY_A_RL)) {
            oleftpress = false;
        }
        if (isValueInArray(keypr,Consts.KEY_D_PR)) {
            orightpress = true;
        }else if (isValueInArray(keypr,Consts.KEY_D_RL)){
            orightpress = false;
        }
            
        if (isValueInArray(keypr,Consts.KEY_W_PR)) {
            ouppress = true;
        } else if (isValueInArray(keypr,Consts.KEY_W_RL)) {
            ouppress = false;
        }

        if (isValueInArray(keypr,Consts.KEY_LEFT_PR)) {
            sleftpress = true;
        } else if (isValueInArray(keypr,Consts.KEY_LEFT_RL)) {
            sleftpress = false;
        } 
        
        if (isValueInArray(keypr,Consts.KEY_RIGHT_PR)){
            srightpress = true;
        } else if (isValueInArray(keypr,Consts.KEY_RIGHT_RL)) {
            srightpress = false;
        }
            
            
        if (isValueInArray(keypr,Consts.KEY_UP_PR)) {
            suppress = true;
        } else if (isValueInArray(keypr,Consts.KEY_UP_RL)) {
            suppress = false;
        }
        
        if (oleftpress) {
            rocket.rotate((float)(Math.toRadians(10)));
        } else if (orightpress) {
            rocket.rotate((float)(Math.toRadians(-10)));
        } 
        
        if (sleftpress) {
            rocket2.rotate((float)(Math.toRadians(10)));
        } else if (srightpress) {
            rocket2.rotate((float)(Math.toRadians(-10)));
        }
        
        rocket.move();
        rocket2.move();
        
        if (ouppress) {
            rocket.changespeed();
        }
 
         if (suppress) {
            rocket2.changespeed();
        }
        if (this.collide(rocket,rocket2)){
            System.out.println("gotcha");
            return 1;
        }
         
        return 0;
        
        
    }
    private boolean isValueInArray(int[] array, int value){
        for (int i = 0; i< array.length;i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }
}
class Rocket{
    float ds[] = {0f,0f};
    float realds[] = {0f,0f};
    
    float hitbox[] = {-0.03f, -0.03f, 0.06f, 0.06f};
    float hitboxstart[] = {-0.03f, -0.03f};
    
    float coord[] = new float[2];
    
    float vect1[] = {0f,0.06f};
    float vect2[] = {0.03f, -0.03f};
    float vect3[] = {-0.03f, -0.03f};
    
    Rocket(float x, float y){
        this.coord[0] = x;
        this.coord[1] = y;
    }
    
    float[] getHitBox() {  
        this.hitbox[0] = this.hitboxstart[0] + this.coord[0];
        this.hitbox[1] = this.hitboxstart[1] + this.coord[1];
        return this.hitbox;
    }
    
    void draw(float r, float g, float b){    
        
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        
        glPushMatrix();
             
        glBegin(GL_TRIANGLES);           
           glColor3f(r, g, b); // Red
           glVertex2f((float)(this.coord[0]  + vect1[0]),(float)(this.coord[1] + vect1[1])); 
           glVertex2f((float)(this.coord[0] + vect2[0]),(float)(this.coord[1] + vect2[1])); 
           glVertex2f((float)(this.coord[0] + vect3[0]),(float)(this.coord[1] + vect3[1]));          
        glEnd();
        glFlush();       
    }
    void rotate(float degree){
        float a = (float)(this.vect1[0]* Math.cos(degree)+ this.vect1[1]*-Math.sin(degree));
        float b = (float)(this.vect1[0]* Math.sin(degree)+ this.vect1[1]* Math.cos(degree));    
        this.vect1[0] = a;
        this.vect1[1] = b;
        
        a = (float)(this.vect2[0]* Math.cos(degree)+ this.vect2[1]* -Math.sin(degree));
        b = (float)(this.vect2[0]* Math.sin(degree)+ this.vect2[1]* Math.cos(degree));
        this.vect2[0] = a;
        this.vect2[1] = b;
        
        a = (float)(this.vect3[0]* Math.cos(degree)+ this.vect3[1]* -Math.sin(degree));
        b = (float)(this.vect3[0]* Math.sin(degree)+ this.vect3[1]* Math.cos(degree));
        this.vect3[0] = a;
        this.vect3[1] = b;       
    }
    void move(){ 
        this.coord[0] += this.ds[0];
        this.coord[1] += this.ds[1];
        
        if (this.coord[0] > 1.01){
            this.coord[0] = -1.01f;
        } else if (this.coord[0] < -1.01){
            this.coord[0] = 1.01f;
        }
        if (this.coord[1] > 1.01){
            this.coord[1] = -1.01f;
        } else if (this.coord[1] < -1.01){
            this.coord[1] = 1.01f;
        }
    }
    void changespeed() {
        this.realds[0] += this.vect1[0]/12;
        this.realds[1] += this.vect1[1]/12;
        this.ds[0] = (float)(Math.atan(realds[0])/10);       
        this.ds[1] = (float)(Math.atan(realds[1])/10);
        
    }
    
}