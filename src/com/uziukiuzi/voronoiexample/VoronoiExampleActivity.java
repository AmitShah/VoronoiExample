/*
 * Copyright (C) 2014 uziukiuzi
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
 */
package com.uziukiuzi.voronoiexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.uziukiuzi.voronoiexample.R;


public class VoronoiExampleActivity extends Activity implements OnClickListener{

private Button buttonClassic;
private Button buttonSpheres;
private Button buttonSnowflakes;

public static final int VORONOI_CLASSIC = 1;
public static final int VORONOI_SPHERES = 2;
public static final int VORONOI_SNOWFLAKES = 3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        buttonClassic = (Button) findViewById(R.id.button_classic);
        buttonSpheres = (Button) findViewById(R.id.button_spheres);
        buttonSnowflakes = (Button) findViewById(R.id.button_snowflakes);

        buttonClassic.setOnClickListener(this);
        buttonSpheres.setOnClickListener(this);
        buttonSnowflakes.setOnClickListener(this);
       
    }


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.button_classic:
			startActivity(new Intent(this, ActivityClassic.class));
			break;
		case R.id.button_spheres:
			startActivity(new Intent(this, ActivitySpheres.class));
			break;
		case R.id.button_snowflakes:
			startActivity(new Intent(this, ActivitySnowflakes.class));
			break;
		}
	}




}