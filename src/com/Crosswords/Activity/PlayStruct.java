package com.Crosswords.Activity;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PlayStruct {
     ArrayList<MyPoint> oriPList = new ArrayList<MyPoint>(); 
	ArrayList<MyPoint>  theSrcList;
    static final long ShrinkTime=5000;
	ArrayList<MyPoint> theTarList= new ArrayList<MyPoint>();
    ArrayList<MyPoint> pTrace = new ArrayList<MyPoint>();
    MyPoint lastP=null;
	boolean lock=false;
	MainView pContext;
	int tarIndex = 0;
	static final long EndLevelDelay = 200; 

	int judge(DesignStruct theDesign , ArrayList<MyPoint> tarList){

		if (judgeIsDuplicate(tarList)){
			return Utils.DupEdge;
			
		}
		if(judgeAnyPointMissed(theDesign.getBlocks(),tarList)){
			return Utils.RemainBlock;
		}
		if (judgeIsInvalidCross(tarList)){
			return Utils.InvalidCross;
		}
		if (judgeIsInnerFine(theDesign.getInners(),tarList)){
			return Utils.NotSeperated;
		}

		return Utils.Success;	
		
	}
    private boolean judgeIsInvalidCross(ArrayList<MyPoint> tarList) {
		// TODO Auto-generated method stub
		return false;
	}
	private boolean judgeIsInnerFine(ArrayList<InnerBlock> inners,
			ArrayList<MyPoint> tarList) {
		// TODO Auto-generated method stub
		return false;
	}
	private boolean judgeAnyPointMissed(ArrayList<MyPoint> blocks,
			ArrayList<MyPoint> tarList) {
		for (MyPoint p:blocks){
			boolean checked =false;
			for(MyPoint pTar:tarList){
				if (p.equals(pTar))
					checked = true;
			}
			if (checked ==false)
				return true;
		}
		return false;
	}
	private boolean judgeIsDuplicate(ArrayList<MyPoint> tarList) {
		// TODO Auto-generated method stub
		if(tarList.size()<2)
			return false;
		else {
			for (int i = 0;i<tarList.size()-1; i++)
			for (int j=0; j<i; j++){if (tarList.get(i).equals(tarList.get(j))&&
					tarList.get(i+1).equals(tarList.get(j+1)))
				return true;
			if (tarList.get(i).equals(tarList.get(j+1))&&
					tarList.get(i+1).equals(tarList.get(j)))
				return true;
				
			}
		}
		return false;
	}
	private PlayStruct(MainView context, DesignStruct pDesign2) {
    	pContext =context;
    	pDesign = pDesign2;
		// TODO Auto-generated constructor stub
	}
	boolean  ontouch(MotionEvent event, DesignStruct pDesign) {
        if (pDesign.getStart()==null)
        	return true;
        if (pTrace.size()>0&&Utils.getWholeLength(pTrace)>MainView.LimitPath)
        if (pTrace.get(pTrace.size()-1).equals(pDesign.getStart()))
        	return true;
        if (pTrace.size()==0)
			if(Utils.distance(pDesign.getStart(), new MyPoint(event.getX(),event.getY()))
					>=MainView.distanceToDecide)
				return true;
		if (!lock&&Utils.getWholeLength(pTrace)>MainView.LimitPath)

        if(Utils.distance(pDesign.getStart(), new MyPoint(event.getX(),event.getY()))
				<MainView.distanceToDecide&&
				(!pDesign.checkTarIndex(tarIndex))){
			pTrace.add(new MyPoint(event.getX(),event.getY()));

			pTrace.add(new MyPoint(pDesign.getStart()));
			lock = true;
			return true;

        }
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			if (pTrace.size()==0){
				
	
				pTrace.add(new MyPoint(pDesign.startPoint));
				pTrace.add(new MyPoint(event.getX(),event.getY()));
				lock = false;

			}
			else
			if (isContinuous(event)){
				lock = false;
				pTrace.add(new MyPoint(event.getX(),event.getY()));

				Log.i(Utils.MOD,"Unlocked");
			}
			else{
				lock = true;
				Log.i(Utils.MOD,"Locked");

			}
			
		}
		if (event.getAction() == MotionEvent.ACTION_UP){
			if (!lock){

				pTrace.add(new MyPoint(event.getX(),event.getY()));
				
			}
			lock = true;

				//return true;
			
			}

		if (event.getAction() == MotionEvent.ACTION_MOVE){
			if (!lock)
				pTrace.add(new MyPoint(event.getX(),event.getY()));
				//return true;

			}
	
	
        if (reachedTar(pTrace)){
        	tarIndex++;
        	Log.i(Utils.MOD,"TarInd Change: "+tarIndex);
        }

		return true;		
	}
    private boolean reachedTar(ArrayList<MyPoint> pList2) {
    	if (tarIndex == Utils.Invalid)
    		return false;
    	if (tarIndex>=pDesign.getTars().size())
		return false;
    	if (pTrace.size()<2)
    		return false;
    	else{
    			if (Utils.distancePLine(pDesign.getTars().get(tarIndex), pList2.get(pList2.size()-2), pList2.get(pList2.size()-1))<MainView.distanceToDecide){
    				return true;
    		}
    	}
    	return false;
	}
	private boolean isContinuous(MotionEvent event) {
		// TODO Auto-generated method stub
		//Log.i(Utils.MOD,"Distance"+distanceToDecide);
		if (pTrace.size()==0)
		return true;
		if (Utils.distance(new MyPoint(event.getX(),event.getY()),pTrace.get(pTrace.size()-1))
				<MainView.distanceToDecide){
			return true;
		}
		else
			return false;
	}
    ArrayList<MyPoint> pBlocks= null;
    
void onDrawPlay(Canvas canvas,int w, int h, DesignStruct pDesign) {
    	Paint backgroundpaint = new Paint();
    	pBlocks = pDesign.getBlocks();
    	pDesign.paintBg(canvas,w,h);
		pDesign.paintRaster(canvas);
		pDesign.paintTar(canvas,tarIndex);

			
		
		//pDesign.paintTars(canvas, pDesign.getTars());
		pDesign.paintInners(canvas, pDesign.getInners());
		paintPlist(canvas,pTrace);
		paintTar(canvas,theTarList);
	
		paintBlocks(canvas, pDesign.getBlocks());
		
		if (pTrace.size() > 0) {
			paintEP(canvas,lock,pTrace);
		
		}

        if (pDesign.startPoint!=null){
        	paintSP(canvas,pTrace.size()==0,pDesign);

    		}
    		
    			
    		
	}
private void paintBlocks(Canvas canvas, ArrayList<MyPoint> blocks) {
	Paint linepaint1 = new Paint();

	for (MyPoint p: blocks){
		linepaint1.setStyle(Style.FILL);
		linepaint1.setColor(Color.parseColor(MainView.ColorBlock));
		//linepaint1.setStrokeWidth(5);
		canvas.drawCircle((float) p.x, (float) p.y, (float) MainView.RadiusOfBlock,
				linepaint1);
	}		
}
private void paintSP(Canvas canvas, boolean b, DesignStruct pDesign2) {
	Paint linepaint1 = new Paint();
    linepaint1.setStyle(Paint.Style.STROKE);
	linepaint1.setColor(Color.parseColor(MainView.ColorEp));
	linepaint1.setStrokeWidth(MainView.StrokeWidth);
	float dashLength = (float) ((float) MainView.distanceToDecide*Math.PI/(MainView.DashNumber));
	PathEffect effects = new DashPathEffect(new float[] {dashLength, dashLength}, 0);
	linepaint1.setPathEffect (effects);
	canvas.drawCircle((float) pDesign2.startPoint.x, (float) pDesign2.startPoint.y, (float) MainView.distanceToDecide,
			linepaint1);
	linepaint1.setStyle(Style.FILL);
	linepaint1.setColor(Color.parseColor(MainView.ColorEp));
	//linepaint1.setStrokeWidth(5);
	if (b)
	canvas.drawCircle((float) pDesign2.startPoint.x, (float) pDesign2.startPoint.y, (float) MainView.EndDiametre,
			linepaint1);
	//draw end
	
	/*linepaint1.setStyle(Paint.Style.STROKE);
	linepaint1.setColor(Color.parseColor(MainView.ColorEp));
	linepaint1.setStrokeWidth(MainView.StrokeWidth);
	 dashLength = (float) ((float) MainView.distanceToDecide*Math.PI/(MainView.DashNumber));
	 effects = new DashPathEffect(new float[] {dashLength, dashLength}, 0);
	linepaint1.setPathEffect (effects);
	canvas.drawCircle((float) pDesign2.endPoint.x, (float) pDesign2.endPoint.y, (float) MainView.distanceToDecide,
			linepaint1);	*/
}
private void paintEP(Canvas canvas, boolean lock2, ArrayList<MyPoint> pList2) {
	Paint linepaint1 = new Paint();

	linepaint1.setStyle(Style.FILL);
	linepaint1.setColor(Color.parseColor(MainView.ColorEp));
	linepaint1.setStrokeWidth(MainView.StrokeWidth);
	canvas.drawCircle((float) pList2.get(0).x, (float) pList2.get(0).y,
			(float) MainView.EndDiametre, linepaint1);

	linepaint1.setStyle(Style.FILL);
	linepaint1.setColor(Color.parseColor(MainView.ColorEp));
	linepaint1.setStrokeWidth(MainView.StrokeWidth);
	canvas.drawCircle((float) pList2.get(pList2.size() - 1).x,
			(float) pList2.get(pList2.size() - 1).y,
			(float) MainView.EndDiametre, linepaint1);

	if (lock2) {

		linepaint1.setStyle(Style.STROKE);
		linepaint1.setColor(Color.parseColor(MainView.ColorEp));
		linepaint1.setStrokeWidth(MainView.StrokeWidth);
		float dashLength = (float) ((float) MainView.distanceToDecide*Math.PI/(MainView.DashNumber));
    	PathEffect effects = new DashPathEffect(new float[] {dashLength, dashLength}, 0);
    	linepaint1.setPathEffect (effects);
		canvas.drawCircle((float) pList2.get(pList2.size() - 1).x,
				(float) pList2.get(pList2.size() - 1).y,
				(float) MainView.distanceToDecide, linepaint1);
	}	
}
private void paintPlist(Canvas canvas, ArrayList<MyPoint> pList2) {
	Paint linepaint1 = new Paint();
	linepaint1.setColor(Color.parseColor(MainView.ColorTrace));

	linepaint1.setAntiAlias(true);  //抗锯齿状
	linepaint1.setDither(true);
	linepaint1.setColor(Color.parseColor(MainView.ColorTrace));
	linepaint1.setStrokeWidth(MainView.TraceWidth);

	linepaint1.setStyle(Paint.Style.STROKE);
	linepaint1.setStrokeJoin(Paint.Join.ROUND);
	linepaint1.setStrokeCap(Paint.Cap.ROUND);
   
		float[] pts = Utils.exportPts(pList2);
		canvas.drawLines(pts, linepaint1);
	

}
class Edge {
	MyPoint pS;
	MyPoint pE;
	Edge(MyPoint s, MyPoint e){
		pS = s;
		pE = e;
	}
	boolean equals(Edge ed){
		if (pS.equals(ed.pS)&&pE.equals(ed.pE))
			return true;
		if (pS.equals(ed.pE)&&pE.equals(ed.pS))
			return true;
		return false;
		
	}
	}
private  ArrayList<Edge> getDupedPList(ArrayList<MyPoint> pList2){
	ArrayList<Edge> resTar = new ArrayList<Edge>();
	for (int i = 0;i<pList2.size()-1; i++)
		for (int j=0; j<i; j++){
			Edge iE = new Edge(pList2.get(i),pList2.get(i+1));
			Edge jE = new Edge(pList2.get(j),pList2.get(j+1));
			if (iE.equals(jE)&&!exist(resTar,iE)){
				resTar.add(jE);
			}
			
		}
	return resTar;
	
}
private boolean exist(ArrayList<Edge> resTar, Edge iE) {
	for (Edge e: resTar){
		if (iE.equals(e))
			return true;
	}
	return false;
}
private  ArrayList<Edge> getValidPList(ArrayList<MyPoint> pList2){
	ArrayList<Edge> dup = getDupedPList(pList2);
	ArrayList<Edge> resTar = new ArrayList<Edge>();

	for (int i = 0;i<pList2.size()-1; i++){
			Edge iE = new Edge(pList2.get(i),pList2.get(i+1));
			if (!exist(dup,iE)){
				resTar.add(iE);
			}
			
		}
	return resTar;
}
private void paintTar(Canvas canvas, ArrayList<MyPoint> tar) {
	Paint linepaint1 = new Paint();
	linepaint1.setColor(Color.parseColor(MainView.ColorLine));

	linepaint1.setAntiAlias(true);  //抗锯齿状
	linepaint1.setDither(true);
	linepaint1.setColor(Color.parseColor(MainView.ColorLine));
	linepaint1.setStrokeWidth(MainView.LineWidth);

	linepaint1.setStyle(Paint.Style.STROKE);
	linepaint1.setStrokeJoin(Paint.Join.ROUND);
	linepaint1.setStrokeCap(Paint.Cap.ROUND);

/*		float[] pts = Utils.exportPts(tar);
		canvas.drawLines(pts, linepaint1);	*/
	float[] pts = Utils.exportEdges(this.getValidPList(tar));
	canvas.drawLines(pts, linepaint1);
	linepaint1.setStrokeWidth(MainView.LineWidth*2);
	linepaint1.setColor(Color.parseColor(MainView.ColorDupLine));
	 pts = Utils.exportEdges(this.getDupedPList(tar));
		canvas.drawLines(pts, linepaint1);

	
		if(tar.size()>0){
			linepaint1.setStyle(Style.FILL);
			linepaint1.setColor(Color.parseColor(MainView.ColorLineEnd));
			linepaint1.setStrokeWidth(MainView.StrokeWidth);
			canvas.drawCircle((float) tar.get(tar.size() - 1).x,
					(float) tar.get(tar.size() - 1).y,
					(float) MainView.EndDiametre, linepaint1);
			
		}
}
long starttime = 0;
boolean animeLock = false;
private Handler handler = new Handler(){
	@Override
	 public void handleMessage(Message msg) {  
        if(msg.what==View.NO_ID){
        	pContext.updateView();
        }
     }  
};

public DesignStruct pDesign=null;

private Runnable runnable = new Runnable() 
{

    public void run() 
    {
         //
         // Do the stuff
         //
    	if (System.currentTimeMillis()-starttime>ShrinkTime){
          	DataStruct data = new DataStruct(oriPList, pDesign,theSrcList,theTarList);

    		data.processTransfer();
    		pTrace.clear();

        	Log.i(Utils.MOD, "End time:"+System.currentTimeMillis());
    		handler.removeCallbacks(this);
			handler.sendEmptyMessage(View.NO_ID);
			int judgeResult = judge(pDesign, theTarList);
			int message = judgeResult;
         
            Log.i(Utils.MOD, "Shrinked "+message);
            updateCross.sendEmptyMessageDelayed(message, EndLevelDelay);
    		animeLock = false;
    	return;
    	}
    	
    	int shrinknum = (int) ((System.currentTimeMillis()-starttime)*(oriPList.size())
    	/ShrinkTime);
      	DataStruct data = new DataStruct(oriPList, pDesign,theSrcList,theTarList);

		data.processTransfer(shrinknum); 
		pTrace.clear();
		if (shrinknum>0)
		for (int i = shrinknum-1;i<oriPList.size(); i++){
			pTrace.add(oriPList.get(i));
		}

		handler.sendEmptyMessage(View.NO_ID);

         handler.postDelayed(this, 30);
    	
         
    }
    

};


public void shrink(DesignStruct design) {
	
	animeLock = true;
	
	this.tarIndex = Utils.Invalid;
	if (pTrace.size()<2)
		return;
	if (!pTrace.get(pTrace.size()-1).equals(design.getStart()))
		return;
	starttime = System.currentTimeMillis();
	Log.i(Utils.MOD, "start time:"+starttime);
	theSrcList = new ArrayList<MyPoint>();
	theTarList = new ArrayList<MyPoint>();
	oriPList =  new ArrayList<MyPoint>();
	oriPList.addAll(pTrace);
	this.pDesign=design;

	handler.sendEmptyMessage(View.NO_ID);
	runnable.run();
}
public void clear() {
	// TODO Auto-generated method stub
	tarIndex = 0;
	pTrace.clear();
	theTarList.clear();
}
private  static PlayStruct instance = null;
public static PlayStruct getInstance(MainView mainView, DesignStruct pDesign2) {
	// TODO Auto-generated method stub
	if (instance == null)
		instance = new PlayStruct(mainView,pDesign2);
	return instance;
}
Handler updateCross;
public void setUpdateCross(Handler handler2) {
	// TODO Auto-generated method stub
	updateCross = handler2;
}

}
