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
 * THIS FILE HAS BEEN MODIFIED BY UZIUKIUZI, 02/12/14
 */
package com.uziukiuzi.voronoiexample;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.uziukiuzi.voronoiexample.R;

public class ActivitySnowflakes extends Activity{

    private MyGLSurfaceView mGLView;
    private TextView mTextView;
	private MyGLRenderer mRenderer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        setContentView(R.layout.voronoi_snowflakes);
        
        
        mGLView = (MyGLSurfaceView) findViewById(R.id.surfaceView_snowflakes);
        mGLView.setEffect(VoronoiExampleActivity.VORONOI_SNOWFLAKES);
        
        mRenderer = new MyGLRenderer(this, mGLView, VoronoiExampleActivity.VORONOI_SNOWFLAKES);
        mGLView.setRenderer(mRenderer);
        mGLView.setMyGLRenderer(mRenderer);
        
        mTextView = (TextView) findViewById(R.id.tv_snowflakes);
        

       
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }


}