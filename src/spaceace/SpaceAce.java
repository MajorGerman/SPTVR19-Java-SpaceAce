package spaceace;


import java.awt.Font;
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
    public static final int KEY_V_PR = 107;
    public static final int KEY_CTRL_PR = 108;
    public static final int KEY_SPACE_PR = 109;
    public static final int KEY_W_RL = 201; 
    public static final int KEY_A_RL = 202;
    public static final int KEY_D_RL = 203;
    public static final int KEY_UP_RL = 204;
    public static final int KEY_LEFT_RL = 205;
    public static final int KEY_RIGHT_RL = 206;
    public static final int KEY_V_RL = 207;
    public static final int KEY_CTRL_RL = 208;
    public static final int KEY_SPACE_RL = 209;
    public static final int NO_ONE = 0;
}

public class SpaceAce {
        String keypr = "nothing";
	private long window;
        int keys[] = new int[20];
        int num = 0;
        int display = 1;
        int[] mouse = new int[2];

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
                        
                         if(( key == GLFW_KEY_V && action == GLFW_PRESS )){
                           keys[num] = Consts.KEY_V_PR;
                            num++;
                        } else if (( key == GLFW_KEY_V && action == GLFW_RELEASE )) {
                            keys[num] = Consts.KEY_V_RL;
                            num++; 
                        }      
                         
                         if(( key == GLFW_KEY_RIGHT_CONTROL&& action == GLFW_PRESS )){
                           keys[num] = Consts.KEY_CTRL_PR;
                            num++;
                        } else if (( key == GLFW_KEY_RIGHT_CONTROL && action == GLFW_RELEASE )) {
                            keys[num] = Consts.KEY_CTRL_RL;
                            num++; 
                        }  
                         
                         if(( key == GLFW_KEY_SPACE && action == GLFW_PRESS )){
                           keys[num] = Consts.KEY_SPACE_PR;
                            num++;
                        } else if (( key == GLFW_KEY_SPACE && action == GLFW_RELEASE )) {
                            keys[num] = Consts.KEY_SPACE_RL;
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
                Menu menu = new Menu();
                Game game = new Game();           
                Thread.sleep(500);
                
		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                        if (display == 2) {
                            int exitCode = game.loop(keys);
                            switch (exitCode) {
                                case 1:
                                    display = 1;
                                    System.out.println("Draw");
                                    break;
                                case 2:
                                    display = 1;
                                    System.out.println("Red win");
                                    break;
                                case 3:
                                    display = 1;
                                    System.out.println("Green win");
                                    break;
                                default:
                                    break;
                            }
                        } else if (display == 1) {
                            int exitCode = menu.loop(keys);
                            switch (exitCode) {
                                case 1:
                                    display = 2;
                                    game = new Game(); 
                                    System.out.println("Game started");
                                    break;
                                default:
                                    break;
                            }                            
                        }
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

class Menu {

    Menu() {
        
    }
    
    int loop(int[] keypr) {
        triangle();
        if (isValueInArray(keypr,Consts.KEY_SPACE_PR)) {
            return 1;
        }
        return 0;
    }
    
    void triangle() {
        float vect1[] = {0.3f, 0f};
        float vect2[] = {-0.15f, 0.26f};
        float vect3[] = {-0.15f, -0.26f};
        
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        
        glPushMatrix();
             
        glBegin(GL_TRIANGLES);           
           glColor3f(0, 1, 0); // Red
           glVertex2f((float)(vect1[0]),(float)(vect1[1])); 
           glVertex2f((float)(vect2[0]),(float)(vect2[1])); 
           glVertex2f((float)(vect3[0]),(float)(vect3[1]));           
        glEnd();
        glFlush(); 
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
    
    Bullet bullets[] = new Bullet[100];
    
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
    
    boolean collide_bullets(Rocket a, Bullet[] b) {
        for (int i = 0; i < b.length; i++) {
            if (b[i] != null) {
                float firsthitbox[] = a.getHitBox();
                float secondhitbox[] = b[i].getHitBox();
                if ((firsthitbox[0] < secondhitbox[0] + secondhitbox[2] &&
                        firsthitbox[0] + firsthitbox[2] > secondhitbox[0] &&
                        firsthitbox[1] < secondhitbox[1] + secondhitbox[3] &&
                        firsthitbox[1] + firsthitbox[3] > secondhitbox [1])) {
                    return true;
                }
            }
        }
        return false;
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
        
        for (int i = 0; i < bullets.length; i++) {
            if (bullets[i] != null) {
                bullets[i].draw(1,0,1);
            }
        }
        
        draw();
        this.rocket.draw(0,1,0);
        this.rocket2.draw(1,0,0);
        
        if (isValueInArray(keypr,Consts.KEY_V_PR)) {           
            for (int i = 0; i < bullets.length; i++) {
                if (bullets[i] == null) {
                    bullets[i] = rocket.createBullet();
                    break;
                }
            }         
        }
        
        if (isValueInArray(keypr,Consts.KEY_CTRL_PR)) {           
            for (int i = 0; i < bullets.length; i++) {
                if (bullets[i] == null) {
                    bullets[i] = rocket2.createBullet();
                    break;
                }
            }         
        }
        
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
        
        
        for (int i = 0; i < bullets.length; i++) {
                if (bullets[i] != null) {
                    if (bullets[i].move()) {
                        bullets[i] = null;
                    }                    
                }
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

            return 1;
        }
        
        if (this.collide_bullets(rocket,bullets)){

            return 2;
        }       
        
        if (this.collide_bullets(rocket2,bullets)){
            return 3;
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
        this.ds[0] = (float)(Math.atan(realds[0])/25);       
        this.ds[1] = (float)(Math.atan(realds[1])/25);
        
    }
    public Bullet createBullet() {
        float a[] = new float[2];
        a[0] = this.coord[0] + this.vect1[0];
        a[1] = this.coord[1] + this.vect1[1];
        float speed[] = vect1;
        Bullet bul = new Bullet(speed,a,this.ds);
        return bul;
    }
    
}

class Bullet {
    float speed[] = new float[2];
    float vect[] = {0.005f, 0.005f};
    float hitbox[] = {-0.005f, 0.005f, 0.01f, 0.01f};
    float hitboxstart[] = {-0.005f, -0.005f};
    float coords[] = new float[2];
    Bullet(float[] a, float[] coords, float[] speed) {
        this.speed = a.clone();
        this.speed[0] = this.speed[0] + speed[0];
        this.speed[1] = this.speed[1] + speed[1];
        this.coords = coords.clone();
        this.coords[0]+=speed[0]*0.3f;
        this.coords[1]+=speed[0]*0.3f;
    }
    public boolean move() {
        coords[0] = coords[0] + speed[0]*0.6f;
        coords[1] = coords[1] + speed[1]*0.6f;
        if (coords[0] < -1 || coords[0] > 1 || coords[1] < -1 || coords[1] > 1) {
            return true;
        }
        return false;
    }
    float[] getHitBox() {  
        this.hitbox[0] = this.hitboxstart[0] + this.coords[0];
        this.hitbox[1] = this.hitboxstart[1] + this.coords[1];
        return this.hitbox;
    }
    public void draw(float r, float g, float b) {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        
        glPushMatrix();
             
        glBegin(GL_QUADS);           
           glColor3f(r, g, b); // Red
           glVertex2f((float)(this.coords[0]  + vect[0]),(float)(this.coords[1] + vect[1])); 
           glVertex2f((float)(this.coords[0] + vect[0]),(float)(this.coords[1] - vect[1])); 
           glVertex2f((float)(this.coords[0] - vect[0]),(float)(this.coords[1] - vect[1]));          
           glVertex2f((float)(this.coords[0] - vect[0]),(float)(this.coords[1] + vect[1])); 
        glEnd();
        glFlush();            
    }

}