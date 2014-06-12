package com.Crosswords.Activity;

public class InnerBlock {
	private MyPoint center;
    public static final float WidthDivideMax = 20;

    int level=0;
    public static final int LevelLimit = 1;
    public static final float IncreaseRate = (float) 0.4;
    public MyPoint getCenter(){
    	return this.center;
    }
    
    public InnerBlock(MyPoint p){
    	level = 0;
    	center = p;
    	
    }
    public float getRadius(){
    	return WidthDivideMax*(1+level*IncreaseRate);
    }
    
    public boolean changeLvl(){
    	level = (level+1)%LevelLimit;
    	return level==0;
    }
    

}
