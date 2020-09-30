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
        char keypr;
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
                        //else if(( key == GLFW_KEY_W && action == GLFW_RELEASE && keypr != 'D')){
                            //keypr = 'U';
                        //} else if(( key == GLFW_KEY_A && action == GLFW_RELEASE && keypr != 'R')){
                            //keypr = 'L';
                        //} else if(( key == GLFW_KEY_S && action == GLFW_RELEASE && keypr != 'U')){
                            //keypr = 'D';
                        //} else if(( key == GLFW_KEY_D && action == GLFW_RELEASE && keypr != 'L')){
                           //keypr = 'R';
                        //}
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
                        }
                        
			glfwSwapBuffers(window); // swap the color buffers
                        
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
                        glClear(GL_COLOR_BUFFER_BIT); 
                        Thread.sleep(100);
                        
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
    Rocket rocket = new Rocket(0,0);
    Game() {
        
    }
    public int loop(char keypr) {
        rocket.draw();
        return 0;
        
        
    }
}
class Rocket{
    int x;
    int y;
    double vect1[] = {0,0.5};
    double vect2[] = {0.25, -0.25};
    double vect3[] = {-0.25, -0.25};
    
    Rocket(int x, int y){
        this.x = x;
        this.y = y;
    }
    void draw(){  
        
        glBegin(GL_TRIANGLES);           
           glColor3f(0.0f, 1.0f, 0.0f); // Red
           glVertex2f((float)(this.x  + 0),(float)(this.y + 0.5)); 
           glVertex2f((float)(this.x - 0.25),(float)(this.y - 0.25)); 
           glVertex2f((float)(this.x +0.25),(float)(this.y - 0.25));          
        glEnd();
        glFlush();       
    }
    void rotate(double a[], double degree){
        return;
    }
    void move(){
        
    }
    
}