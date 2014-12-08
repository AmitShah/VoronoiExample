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

public class VoronoiSnowflakes {
	
    public static final String FRAGMENT_SHADER = 
    		"precision mediump float;" +
    		" varying highp vec2 textureCoordinate;\n" + 

			"uniform vec4 vColor;" +
    		" uniform sampler2D lookupTexture;\n" + 
    		"uniform int time;" +
    		
    		

   "vec3 voronoi(float scaleFactor){" +
    		
   
			"float minDist = 0.5;" +
    		
    		"vec4 textureColor = texture2D(lookupTexture, textureCoordinate);" +
    		"vec2 scaledXY = scaleFactor*textureCoordinate;" +
    		"vec2 evalSquare = vec2(floor(scaledXY.x), floor(scaledXY.y));" +
    		
    		"vec2 featurePoint1;" +
    		"vec2 featurePoint2;" +
    		"float minDistPre;" +
    		"float rand;" +
    		"vec2 currentSquare;" +
    		"vec2 currentCoords = textureCoordinate;" +
    		"vec2 elem1;" +
    		"vec2 elem2;" +
    		"float prevMinDist;" +
    		"float dist1, dist2;" +
    		"vec2 controlCoords = (evalSquare + vec2(0.5, 0.5))/scaleFactor;" +
    		"float controlDist = distance(textureCoordinate, controlCoords);" +
    		"vec3 color = vec3(0.);" +
    		"float jitterSpeed = 0.01;" +
    		"float timeF = float(time);" +

    		

    		
    			
    		"for(int x = -1; x < 2; x++){" +
    			"for(int y = -1; y < 2; y++){" +
    		
    	"if(mod(evalSquare.x, 2.) == 1. && mod(evalSquare.y, 2.) == 1.){minDist = min(minDist, 0.5);}" +
    			
    			"currentSquare = evalSquare + vec2(float(x), float(y));" +
    			
    			"elem1 = texture2D(lookupTexture, (currentSquare)/scaleFactor).xy;" +
    			"elem2 = texture2D(lookupTexture, (currentSquare)/scaleFactor).zw;" +
    		
    		
    	
    		"featurePoint1 = (currentSquare + elem1)/scaleFactor + vec2(sin(jitterSpeed*elem1.x*timeF), sin(jitterSpeed*elem1.y*timeF))/(5.*scaleFactor*elem1);" +
    		"featurePoint2 = (currentSquare + elem2)/scaleFactor + vec2(sin(jitterSpeed*elem2.x*timeF), sin(jitterSpeed*elem2.y*timeF))/(5.*scaleFactor*elem2);" +
    		
    		"dist1 = distance(textureCoordinate, featurePoint1);" +
    		"dist2 = distance(textureCoordinate, featurePoint2);" +
    		
    		"minDistPre = min(dist1, dist2);" +
    		
    		"prevMinDist = minDist;" +
   		"minDist = min(minDist, minDistPre);" +
    		
    		"if(minDist < prevMinDist){" +
    		"	if(minDistPre == dist1){" +
    		"		currentCoords = featurePoint1;" +
    		"	} else{" +
    		"		currentCoords = featurePoint2;" +
    		"	}" +
    		"}" +
    		
    				
    			"}" +
    		"}" +
    			
    		

    		"color = vec3(0.6, 0.7, 1.);" +
    		
    		
"float theta = atan(distance(textureCoordinate.y, currentCoords.y), distance(textureCoordinate.x, currentCoords.x));" +
    	
    	"if(minDist*scaleFactor<0.75*cos(6.*theta)){return 1.8*color*(1.-minDist*scaleFactor);}" +
    	"else{return vec3(0.);}" +
    	
    		"}" +

    		
    		" void main() {" + 
    
"gl_FragColor = vec4(voronoi(25.), 1.);" +


  " }";
    

    
	public static final float DEFAULT_SCALE = 25;
	public static final float MIN_SCALE = 5;
	public static final float MAX_SCALE = 45;
    
    

}
