package com.Crosswords.Activity;

import java.util.ArrayList;

import android.util.Log;

public class MyPoint {
	double x;
	double y;
	
    public boolean isHangPoint = false;
    
    
    //MyPoint currentDual = null;
    int hangCorrespond = 0;
    double hangAngle = 0;
    
  long addedTime = 0;
	public MyPoint(float x2, float y2) {
		x = x2;
		y = y2;
		}


	public  MyPoint(double x1, double y1) {
		x = x1;
		y = y1;
	}
	public  MyPoint(MyPoint pt) {
		x =pt.x;
		y = pt.y;
	}
	
	//PointAttributes attr;
	public String toString(){
		return "Point: ( "+x+" , "+y+" )"+" HangPoint:"+this.isHangPoint+" Hangangle:"+this.hangAngle;
	}
	
	/**
	 * @return 是否是挂点
	 */
	public boolean isHangPoint(){
		return isHangPoint;
	}


	public MyPoint setCorPoint(int i) {
		// TODO Auto-generated method stub
		this.hangCorrespond = i;
		return this;
	}


 
    boolean failed = false;

	public void changeAngle(double angle) {
		if (!this.isHangPoint)
			return;
		//Log.i(Utils.MOD, this.toString());

		
		//Log.i(Utils.MOD, " changeAngle"+angle);

		if (Math.signum(this.hangAngle)!=Math.signum(this.hangAngle+angle)){
			failed = true;
			//Log.i(Utils.MOD, "FAILED");
		/*	Log.i(Utils.MOD, this.toString());

			Log.i(Utils.MOD, " changeAngle"+angle);*/
		}		
		this.hangAngle = this.hangAngle+angle;
	}

	public boolean equals(MyPoint p){
		
		return (p.x==this.x)&&(p.y==this.y);
		
	}
	


}
