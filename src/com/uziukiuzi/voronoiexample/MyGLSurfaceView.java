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

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private MyGLRenderer mRenderer;
    private Context mContext;

    public MyGLSurfaceView(Context context, AttributeSet aSet) {
        super(context, aSet);

        mContext = context;
        init(mContext);

    }

	private int mEffect;
    
    

    

    private void init(Context context) {
		// TODO Auto-generated method stub
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

	}



	
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    
	public Renderer getRenderer(){
		return mRenderer;
	}
	
	public void setEffect(int effect){
		mEffect = effect;
	}
	
	public void setMyGLRenderer(MyGLRenderer renderer){
		mRenderer = renderer;
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
}
