/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * THIS FILE HAS BEEN MODIFIED BY UZIUKIUZI, 02/12/14
 */
package com.uziukiuzi.voronoiexample;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.util.Log;
import com.uziukiuzi.voronoiexample.R;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Square {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec4 lookupTextureCoordinate;" +
            "varying vec2 textureCoordinate;" +
            "void main() {" +
            
            "textureCoordinate = lookupTextureCoordinate.xy;" +
            
            // The matrix must be included as a modifier of gl_Position.
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "varying vec2 textureCoordinate;" +
            "uniform sampler2D lookupTexture;" +
            "void main() {" +
            "  gl_FragColor = texture2D(lookupTexture, textureCoordinate);" +
            "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private int mProgram;
    private int mPositionHandle;
    private Context mContext;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private MyGLSurfaceView mGLView;
    

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -1.0f,  -1.0f, 0.0f,   // top left
            1.0f, -1.0f, 0.0f,   // bottom left
             -1.0f, 1.0f, 0.0f,   // bottom right
             1.0f,  1.0f, 0.0f }; // top right
    
    private float texture[] = {
    		        // Mapping coordinates for the vertices
    		        0.0f, 1.0f,     // top left     (V2)
    		        1.0f, 1.0f,     // bottom left  (V1)
    		        0.0f, 0.0f,     // top right    (V4)
    		        1.0f, 0.0f      // bottom right (V3)
    		};


    private final short drawOrder[] = { 2, 0, 1, 2, 1, 3 }; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

	private int mTextureDataHandle;
	private int mTextureUniformHandle;

	private FloatBuffer textureBuffer;

	private int mTextureCoordinateHandle;

	private int mTimeUniformHandle;


	private int mEffect;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Square(Context context, MyGLSurfaceView glView, int effect) {
    	mContext = context;
    	mGLView = glView;
    	mEffect = effect;
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        
        
    	ByteBuffer byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
    	byteBuffer.order(ByteOrder.nativeOrder());
    	textureBuffer = byteBuffer.asFloatBuffer();
    	textureBuffer.put(texture);
    	textureBuffer.position(0);
        

        // prepare shaders and OpenGL program
    	mProgram = GLES20.glCreateProgram(); 

        prepareProgram(mProgram, getFragmentShader(mEffect), vertexShaderCode);
        
        
        
        // Load the texture
        mTextureDataHandle = loadTexture(mContext, R.drawable.ic_launcher);
        

        
    }



	private static void prepareProgram(int program, String fragmentShader, String vertexShader) {
		// TODO Auto-generated method stub
		
        int vertexShaderHandle = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShader);
        int fragmentShaderHandle = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShader);
		
        
        GLES20.glAttachShader(program, vertexShaderHandle);   // add the vertex shader to program
        GLES20.glAttachShader(program, fragmentShaderHandle); // add the fragment shader to program
        GLES20.glLinkProgram(program);                  // create OpenGL program executables
	}



	private static String getFragmentShader(int effect) {
		// TODO Auto-generated method stub
		String shader = null;
		switch(effect){
		case 1:
			shader = VoronoiClassic.FRAGMENT_SHADER;
			break;
		case 2:
			shader = VoronoiSpheres.FRAGMENT_SHADER;
			break;
		case 3:
			shader = VoronoiSnowflakes.FRAGMENT_SHADER;
			break;
		}
		return shader;
	}



	/**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "lookupTextureCoordinate");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        
        
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0,
                textureBuffer);
        
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "lookupTexture");
        mTimeUniformHandle = GLES20.glGetUniformLocation(mProgram, "time");
     
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
     
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
     
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        
        
		long time = SystemClock.uptimeMillis() % 30000L;
		GLES20.glUniform1i(mTimeUniformHandle, (int) time);
		mGLView.requestRender();
        

        

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        

        
        
        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        
        GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
    }
    
    
    
    
    
    
    public static int loadTexture(final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];
     
        GLES20.glGenTextures(1, textureHandle, 0);
     
        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
          //  options.inScaled = false;   // No pre-scaling
     
            // Read in the resource
           // final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
     
            
            Bitmap lookupBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            
            
           float[] rands = new float[4];
            
            for(int x = 0; x < lookupBitmap.getWidth(); x++){
            	for(int y = 0; y < lookupBitmap.getHeight(); y++){
            		
                    
            		for(int count = 0; count < rands.length; count++){
            		rands[count] = (float) Math.random();
            		}
            		lookupBitmap.setPixel(x, y, Color.argb((int) (rands[3]*255), (int) (rands[0]*255), (int) (rands[1]*255), (int) (rands[2]*255)));
                    

                    
            	}
            }
            
            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
     
            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
     
            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, lookupBitmap, 0);
     
            // Recycle the bitmap, since its data has been loaded into OpenGL.
            lookupBitmap.recycle();
        }
     
        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }
     
        return textureHandle[0];
    }



    

}