package com.Crosswords.Activity;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class DesignStruct {
	private static DesignStruct instance;
	private MainView pContext=null;
    DesignType curType = DesignType.StartPoint;
    ArrayList<MyPoint> blocks = new ArrayList<MyPoint>();
    ArrayList<InnerBlock> inners = new ArrayList<InnerBlock>();
    ArrayList<MyPoint> tars = new ArrayList<MyPoint>();

	MyPoint startPoint = null;
	int cross = 0;
	public static final int CrossLimit = 10;
	
	public enum DesignType{
		StartPoint,
		Block,
		InnerPoint,
		Tar,
		Cross
	}
	
	public void changeDType(){
		switch(curType){
		case StartPoint:
	
			curType = DesignType.Block;
		break;
		case Block:
			curType = DesignType.InnerPoint;
		break;
		case InnerPoint:
			curType = DesignType.Tar;
		break;
		case Tar:
			curType = DesignType.Cross;
			break;
			
		case Cross:
			curType = DesignType.StartPoint;
		break;
		}
	}
	public String getCurType(){
		switch(curType){
		case StartPoint:
			return "StartP";
		

		case Block:
			return "Block";
		case InnerPoint:
			return "InnerP";
		
		case Tar:
			return "Tar";
		case Cross:
			return "Cross";
			
			default:
				return "Tar";
		}
		
	}
	ArrayList<MyPoint> theAvailPoints = new ArrayList<MyPoint>();
	public static final float baseCol = 12; 
	float base = 0;
	
	private DesignStruct(MainView mainView) {
		pContext = mainView;
		base = mainView.getWidth()/baseCol;
		genAvailPoints(mainView);
		genInnerAvail(mainView);
	}
	ArrayList<MyPoint> thInnerAvail = new ArrayList<MyPoint>();

	boolean checkTarIndex(int index){
		 Log.i(Utils.MOD, "Cur Index "+ index+" Size: "+this.getTars().size());
		 if (index==Utils.Invalid||index>=this.getTars().size())
			 return false;
		 else
			 return true;
	}
	private void genInnerAvail(MainView mainView) {
		thInnerAvail.clear();
		for (float i=base; i<mainView.getWidth();i+=base/2){
			for (float j = base; j<mainView.getHeight();j+=base/2){
				thInnerAvail.add(new MyPoint(i,j));
			}
		}
	}
	private void genAvailPoints(MainView mainView) {
		theAvailPoints.clear();
		for (float i=base/2; i<mainView.getWidth();i+=base){
			for (float j = base/2; j<mainView.getHeight();j+=base){
				theAvailPoints.add(new MyPoint(i,j));
			}
		}
	}
	
	public static DesignStruct getInstance(MainView mainView) {
		// TODO Auto-generated method stub
		if (instance == null)
			instance = new DesignStruct(mainView);
		return instance;
	}
	public MyPoint getStart(){
		return startPoint;
	}

	public ArrayList<MyPoint> getBlocks(){
		return this.blocks;
	}
	public ArrayList<InnerBlock> getInners(){
		return this.inners;
	}
	public ArrayList<MyPoint> getTars(){
		return this.tars;
	}
	public void onDrawDesign(Canvas canvas, int width, int height) {
		 ArrayList<MyPoint> pBlocks = getBlocks();
		 
		 
		 paintBg(canvas, width, height);
		 paintRaster(canvas);
		 
		 paintBlocks(canvas, pBlocks);
		 
		 paintTars(canvas, getTars());
		 paintInners(canvas, getInners());
		
		 paintStart(canvas, startPoint);
		
		
	

	}
	
    public void paintInners(Canvas canvas, ArrayList<InnerBlock> inners2) {
		Paint linepaint1 = new Paint();
        
		for (InnerBlock p: getInners()){
			float dashLength = (float) ((float)p.getRadius()*Math.PI/(MainView.DashNumber));
	    	PathEffect effects = new DashPathEffect(new float[] {dashLength, dashLength}, 0);
	    	linepaint1.setStyle(Paint.Style.STROKE);
			linepaint1.setColor(Color.parseColor(MainView.ColorInner));
			linepaint1.setStrokeWidth(MainView.StrokeWidth);
			linepaint1.setPathEffect(effects);
		canvas.drawCircle((float) p.getCenter().x, (float)p. getCenter().y, (float)p.getRadius(),
				linepaint1);
		}
	}
    public void paintTar(Canvas canvas,int tarIndex)
    {
    	if (tarIndex == Utils.Invalid)
    		return;
    	if (tarIndex< this.tars.size()){
    		paintTar(canvas, tars.get(tarIndex));
    	}
    }
    	private void paintTar(Canvas canvas, MyPoint tar){
    
		Paint linepaint1 = new Paint();

    	linepaint1.setStyle(Style.STROKE);
		linepaint1.setColor(Color.parseColor(MainView.ColorTar));
		linepaint1.setStrokeWidth(MainView.LineWidth);
		canvas.drawLine((float) tar.x-MainView.RadiusOfTar, (float) tar.y-MainView.RadiusOfTar, 
				(float) tar.x+MainView.RadiusOfTar, (float) tar.y+MainView.RadiusOfTar, linepaint1);

		canvas.drawLine((float) tar.x-MainView.RadiusOfTar, (float) tar.y+MainView.RadiusOfTar, 
				(float) tar.x+MainView.RadiusOfTar, (float) tar.y-MainView.RadiusOfTar, linepaint1);
    	
    }
	public void paintTars(Canvas canvas, ArrayList<MyPoint> tars2) {
		Paint linepaint1 = new Paint();

		for (MyPoint p: getTars()){
			paintTar(canvas, p);
		}		
	}

	public void paintRaster(Canvas canvas) {
		// TODO Auto-generated method stub
    	Paint linepaint1 = new Paint();
    	linepaint1.setStyle(Paint.Style.FILL);
		linepaint1.setColor(Color.parseColor(MainView.ColorRaster));
		linepaint1.setStrokeWidth(MainView.StrokeWidth);
    	for (float i=base/2; i<canvas.getWidth();i+=base){
    		canvas.drawLine(i, 0, i, canvas.getHeight(), linepaint1);
		
				
		}
    	for (float j = base/2; j<canvas.getHeight();j+=base){

    		canvas.drawLine( 0,j,canvas.getWidth(), j,  linepaint1);
		}
	}
	/*private void paintEnd(Canvas canvas, MyPoint endPoint2) {
    	Paint linepaint1 = new Paint();
    			if (endPoint!=null){
    				linepaint1.setStyle(Paint.Style.STROKE);
    				linepaint1.setColor(Color.parseColor(MainView.ColorEp));
    				linepaint1.setStrokeWidth(MainView.StrokeWidth);
        			float dashLength = (float) ((float)MainView.distanceToDecide*Math.PI/(MainView.DashNumber));
        	    	PathEffect effects = new DashPathEffect(new float[] {dashLength, dashLength}, 0);
        	    	linepaint1.setPathEffect(effects);
    				canvas.drawCircle((float) endPoint.x, (float) endPoint.y, (float) MainView.distanceToDecide,
    						linepaint1);
    				
    			}		
	}*/
	private void paintStart(Canvas canvas, MyPoint startPoint2) {
    	Paint linepaint1 = new Paint();

    			if (startPoint!=null){
    			linepaint1.setStyle(Paint.Style.STROKE);
    			linepaint1.setColor(Color.parseColor(MainView.ColorEp));
    			linepaint1.setStrokeWidth(MainView.StrokeWidth);
    			float dashLength = (float) ((float)MainView.distanceToDecide*Math.PI/(MainView.DashNumber));
    	    	PathEffect effects = new DashPathEffect(new float[] {dashLength, dashLength}, 0);
    	    	linepaint1.setPathEffect(effects);
    			canvas.drawCircle((float) startPoint.x, (float) startPoint.y, (float) MainView.distanceToDecide,
    					linepaint1);
    			linepaint1.setStyle(Style.FILL);
    			linepaint1.setColor(Color.parseColor(MainView.ColorEp));
    			canvas.drawCircle((float) startPoint.x, (float) startPoint.y, (float) MainView.RadiusOfBlock,
    					linepaint1);
    			}		
	}
	private void paintBlocks(Canvas canvas, ArrayList<MyPoint> pBlocks) {
    	Paint linepaint1 = new Paint();

		for (MyPoint p: getBlocks()){
			linepaint1.setStyle(Style.FILL);
			linepaint1.setColor(Color.parseColor(MainView.ColorBlock));
			canvas.drawCircle((float) p.x, (float) p.y, (float) MainView.RadiusOfBlock,
					linepaint1);
		}
	}
 void paintBg(Canvas canvas, int width, int height) {
		// TODO Auto-generated method stub
		Paint backgroundpaint = new Paint();

    	backgroundpaint.setColor(Color.parseColor(MainView.ColorBg));
		canvas.drawRect(0, 0, width, height, backgroundpaint);
	}
	MyPoint lastP=null;

	public boolean onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN){
		    lastP = new MyPoint(event.getX(),event.getY());
		    lastP.isHangPoint = true;
			return true;
		}
	    if(event.getAction() ==MotionEvent.ACTION_UP){
	    	MyPoint now = new MyPoint (event.getX(),event.getY());
	    	if (isClick(now, lastP)){
	    		switch(this.curType){
	    		case StartPoint:
	    			this.startPoint = getAvail(now,this.theAvailPoints);
	    			break;
	    		
	    		case Block:
	    			dealBlock(now);
	    			
	    			break;
	    		case InnerPoint:
	    			dealInnerPoint(now);
	    			break;
	    		case Tar:
	    			dealTar(now);
	    			break;
	    		case Cross:
	    			dealCross(now);
		    	
	    		}
	    	}
	    	lastP = null;
	    	
	    }
		return true;		
	    
	}
	private void dealCross(MyPoint now) {
		cross = (cross+1)%CrossLimit;
		updateCross.sendEmptyMessage(Utils.CrossUpdate);
		
	}
	public String getCross(){
		return "  "+cross +" X";
	}
	private void dealTar(MyPoint now) {
		MyPoint theP = getAvail(now,this.theAvailPoints);
		for (MyPoint p: this.tars)
			if (p.equals(  theP)){
				tars.remove(p);
		    	lastP = null;

				return;
			}
	
		tars.add(theP);
    	//Log.i(Utils.MOD,"Added Tar"+ theP);		
	}
	private void dealInnerPoint(MyPoint now) {
		MyPoint theP = getAvail(now,this.thInnerAvail);
		for (InnerBlock p: this.inners)
			if (p.getCenter().equals(  theP)){
				if (p.changeLvl())
					inners.remove(p);
		    	lastP = null;
		    	

				return;
				
			}
		inners.add(new InnerBlock(theP));
    	//Log.i(Utils.MOD,"Inner "+ theP);		

	}
	
	private void dealBlock(MyPoint now) {
		MyPoint theP = getAvail(now,this.theAvailPoints);
		for (MyPoint p: blocks)
			if (p.equals(  theP)){
				blocks.remove(p);
		    	lastP = null;

				return;
			}
	
		blocks.add(theP);
    	//Log.i(Utils.MOD,"Added "+ theP);		
	}
	private MyPoint getAvail(MyPoint now,ArrayList<MyPoint> avail) {
		// TODO Auto-generated method stub
		float minDist = base;
		MyPoint res  = now ;
		
		for (MyPoint p: avail){
			if (Utils.distance(now, p)<minDist){
				res = p;
				minDist = (float) Utils.distance(now, p);
			}
		}
		return new MyPoint(res);
	}
	private boolean isClick(MyPoint now, MyPoint lastP2) {
		// TODO Auto-generated method stub
		if (now==null|| lastP2==null)
		return false;
		if (Utils.distance(now,lastP2)<pContext.distanceToDecide)
			return true;
		else
			return false;
	}
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	Handler updateCross;
	public void setUpdateCross(Handler handler) {
		// TODO Auto-generated method stub
		updateCross = handler;
	}


}
