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

public class SpaceAce {
        String keypr = "nothing";
	private long window;

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
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true);
                        else if(( key == GLFW_KEY_W && action == GLFW_PRESS )){
                            keypr = "1uppress";
                        } else if(( key == GLFW_KEY_A && action == GLFW_PRESS )){
                            keypr = "1leftpress";
                        } else if(( key == GLFW_KEY_D && action == GLFW_PRESS )){
                           keypr = "1rightpress";
                        } else if(( key == GLFW_KEY_W && action == GLFW_RELEASE )){
                            keypr = "1uprelease";
                        } else if (( key == GLFW_KEY_A && action == GLFW_RELEASE )) {
                            keypr = "1leftrelease";
                        } else if (( key == GLFW_KEY_D && action == GLFW_RELEASE )) {
                            keypr = "1rightrelease";
                            
                        } else if(( key == GLFW_KEY_UP && action == GLFW_PRESS )){
                            keypr = "2uppress";
                        } else if(( key == GLFW_KEY_LEFT && action == GLFW_PRESS )){
                            keypr = "2leftpress";
                        } else if(( key == GLFW_KEY_RIGHT && action == GLFW_PRESS )){
                           keypr = "2rightpress";
                        } else if(( key == GLFW_KEY_UP && action == GLFW_RELEASE )){
                            keypr = "2uprelease";
                        } else if (( key == GLFW_KEY_LEFT && action == GLFW_RELEASE )) {
                            keypr = "2leftrelease";
                        } else if (( key == GLFW_KEY_RIGHT && action == GLFW_RELEASE )) {
                            keypr = "2rightrelease"; 
                            
                        } else {
                            keypr = "LOL";
                        }
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
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
                Game game = new Game();
                
                Thread.sleep(500);
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                        if (game.loop(keypr) == 1){
                            glfwSetWindowShouldClose(window, true);
                        };
      
			glfwSwapBuffers(window); // swap the color buffers
                        
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
                        glClear(GL_COLOR_BUFFER_BIT); 
                        Thread.sleep(20);
                        
		}
                
	}       

	public static void main(String[] args) throws InterruptedException {
		new SpaceAce().run();              
	}
        
        public void render() {
            //
        }
        
        void display(float red,float green,float blue) {
           glClear(GL_COLOR_BUFFER_BIT);         // Clear the color buffer (background)

           // Draw a Red 1x1 Square centered at origin
           //glBegin(GL_QUADS);              // Each set of 4 vertices form a quad
              //glColor3f(red, green, blue); // Red
              //glVertex2f(-0.5f, -0.5f);    // x, y
              //glVertex2f( 0.5f, -0.5f);
              //glVertex2f( 0.5f,  0.5f);
              //glVertex2f(-0.5f,  0.5f);
           //glEnd();

           //glFlush();  // Render now
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
        return firsthitbox[0] < secondhitbox[0] + secondhitbox[2] &&
                firsthitbox[0] + firsthitbox[2] > secondhitbox[0] &&
                firsthitbox[1] < secondhitbox[1] + secondhitbox[3] &&
                firsthitbox[1] + firsthitbox[3] > secondhitbox [1];
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
    public int loop(String keypr) {
        
        draw();
        this.rocket.draw(0,1,0);
        this.rocket2.draw(1,0,0);
        
        if (keypr.equals("1leftpress")) {
            oleftpress = true;
        } else if (keypr.equals("1rightpress")){
            orightpress = true;
        } else if (keypr.equals("1leftrelease")) {
            oleftpress = false;
        } else if (keypr.equals("1rightrelease")) {
            orightpress = false;
        }
        if (keypr.equals("1uppress")) {
            ouppress = true;
        } else if (keypr.equals("1uprelease")) {
            ouppress = false;
        }

        if (keypr.equals("2leftpress")) {
            sleftpress = true;
        } else if (keypr.equals("2rightpress")){
            srightpress = true;
        } else if (keypr.equals("2leftrelease")) {
            sleftpress = false;
        } else if (keypr.equals("2rightrelease")) {
            srightpress = false;
        }
        if (keypr.equals("2uppress")) {
            suppress = true;
        } else if (keypr.equals("2uprelease")) {
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
         
        return 0;
        
        
    }
}
class Rocket{
    float dx = 0;
    float dy = 0;
    float realdx = 0;
    float realdy = 0;
    
    float hitbox[] = {-0.3f, -0.3f, 0.6f, 0.6f};
    
    float x;
    float y;
    
    float vect1[] = {0f,0.06f};
    float vect2[] = {0.03f, -0.03f};
    float vect3[] = {-0.03f, -0.03f};
    
    Rocket(float x, float y){
        this.x = x;
        this.y = y;
    }
    
    float[] getHitBox() {  
        this.hitbox[0] = this.hitbox[0] + this.x;
        this.hitbox[1] = this.hitbox[1] + this.y;
        return this.hitbox;
    }
    
    void draw(float r, float g, float b){    
        
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        
        glPushMatrix();
             
        glBegin(GL_TRIANGLES);           
           glColor3f(r, g, b); // Red
           glVertex2f((float)(this.x  + vect1[0]),(float)(this.y + vect1[1])); 
           glVertex2f((float)(this.x + vect2[0]),(float)(this.y + vect2[1])); 
           glVertex2f((float)(this.x + vect3[0]),(float)(this.y + vect3[1]));          
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
        //this.dx = this.dx - 0.01f;
        //this.dy = this.dy - 0.01f;
        this.x = this.x + this.dx;
        this.y = this.y + this.dy;
        
        if (this.x > 1.01){
            this.x = -1.01f;
        } else if (this.x < -1.01){
            this.x = 1.01f;
        }
        if (this.y > 1.01){
            this.y = -1.01f;
        } else if (this.y < -1.01){
            this.y = 1.01f;
        }
    }
    void changespeed() {
        this.realdx = this.realdx + this.vect1[0]/12;
        System.out.println(this.realdx);
        this.realdy = this.realdy + this.vect1[1]/12;
        this.dx = (float)(Math.atan(realdx)/10);
        System.out.println(this.dx);       
        this.dy = (float)(Math.atan(realdy)/10);
        
    }
    
}