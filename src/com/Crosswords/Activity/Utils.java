package com.Crosswords.Activity;

import java.util.ArrayList;

import com.Crosswords.Activity.PlayStruct.Edge;

import android.util.Log;

public class Utils {
	public static final int CrossUpdate = 1;
		public static final int Success = 2;
	public static final int RemainBlock = 3;
	public static final int NotSeperated = 4;
	public static final int DupEdge=5;
	public static final int InvalidCross=6;
    static final int Invalid = -1;

	static final float Max = 100000;
	static double sign(MyPoint p1, MyPoint p2, MyPoint p3)
	{
	  return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}

	static final String MOD="TestNG";


	public static boolean inTriangle(MyPoint pt, MyPoint v1, MyPoint v2,
			MyPoint v3) {
		boolean b1, b2, b3;
          
		  b1 = sign(pt, v1, v2) < 0.0f;
		  b2 = sign(pt, v2, v3) < 0.0f;
		  b3 = sign(pt, v3, v1) < 0.0f;
         if(sign(pt, v3, v1)==0||sign(pt, v2, v3)==0||sign(pt, v1, v2)==0)
        	 return false;
		  
		  return ((b1 == b2) && (b2 == b3));

	}

	public static double distance(MyPoint pos, MyPoint curp) {
		// TODO Auto-generated method stub
      //  Log.i(MOD, "Dis:" +" "+((pos.x - curp.x) * (pos.x - curp.x) + (pos.y - curp.y)
		//		* (pos.y - curp.y)));
		return Math.sqrt((pos.x - curp.x) * (pos.x - curp.x) + (pos.y - curp.y)
				* (pos.y - curp.y));
	}




	public static double angle(double sx, double sy,double ex, double ey) {
		// TODO Auto-generated method stub
		double dtheta,theta1,theta2;

		   theta1 = Math.atan2(sy,sx);
		   theta2 = Math.atan2(ey,ex);
		   dtheta = theta2 - theta1;
		   while (dtheta > Math.PI)
		      dtheta -= 2*Math.PI;
		   while (dtheta < -Math.PI)
		      dtheta += 2*Math.PI;

		   return(dtheta);
	}



	public static void logTemp(ArrayList<MyPoint> temp) {
		// TODO Auto-generated method stub
		Log.i(MOD,"Temp begin");
		for (MyPoint m:temp){
			Log.i(MOD,"Point: "+ m);

		}
		Log.i(MOD,"Temp end");
	}
    public static  float[] exportPts(ArrayList<MyPoint> pList2) {
		// TODO Auto-generated method stub
    	if (pList2.size()==0)
    		return new float[0];
    	
    	float[] res = new float[(pList2.size()-1)*4];
    	for (int i = 0; i<pList2.size()-1; i++){
    		res[i*4] = (float)pList2.get(i).x;
    		res[i*4+1] = (float)pList2.get(i).y;
    		res[i*4+2] = (float)pList2.get(i+1).x;
    		res[i*4+3] = (float)pList2.get(i+1).y;
    	}
		return res;
	}
    public static  float[] exportEdges(ArrayList<Edge> pList2) {
		// TODO Auto-generated method stub
    	if (pList2.size()==0)
    		return new float[0];
    	
    	float[] res = new float[(pList2.size())*4];
    	for (int i = 0; i<pList2.size(); i++){
    		res[i*4] = (float)pList2.get(i).pS.x;
    		res[i*4+1] = (float)pList2.get(i).pS.y;
    		res[i*4+2] = (float)pList2.get(i).pE.x;
    		res[i*4+3] = (float)pList2.get(i).pE.y;
    	}
		return res;
	}

	public static double distance(MyPoint tar,ArrayList<MyPoint> pList2) {
		double res = Max;
		if (pList2.size()==0)
			return 0;
		if (pList2.size()==1){
			return distance(tar, pList2.get(0));
		}
		for (int i = 0; i<pList2.size()-1; i++){
			double temp =distancePLine(tar, pList2.get(i), pList2.get(i+1));
			if (temp<res){
				res =temp; 
				
			}
			
		}
		return res;
	}
	public static double distancePLine(MyPoint p, MyPoint pStart, MyPoint pEnd)   
	{   
	  
		double a,b,c;   
	 a=(double) distance(p,pStart);   
	 if(a<=0.00001)   
	  return 0.0f;   
	 b=(double)distance(p,pEnd);   
	 if(b<=0.00001)   
	  return 0.0f;   
	 c=(double)distance(pStart,pEnd);   
	 if(c<=0.00001)   
	  return a;//如果PA和PB坐标相同，则退出函数，并返回距离   
	//------------------------------   
	  
	    
	 if(a*a>=b*b+c*c)//--------图3--------   
	  return b;   
	 if(b*b>=a*a+c*c)//--------图4-------   
	  return a;    

	//图1   
	 double l=(a+b+c)/2;     //周长的一半   
	 double s= Math.sqrt(l*(l-a)*(l-b)*(l-c));  //海伦公式求面积   
	 return 2*s/c;   
	} 
	public static double getWholeLength(ArrayList<MyPoint>pList){
		if (pList.size()<2)
			return 0;
		double res= 0;
		for (int i = 0; i< pList.size()-1; i++){
			res = res+ Utils.distance(pList.get(i), pList.get(i+1));
		}
		return res;
	}
}
