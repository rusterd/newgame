package com.Crosswords.Activity;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Crosswords.Activity.MainView.Status;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.style.LineBackgroundSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.widget.TextView;

public class MainView extends View {
    PlayStruct pStruct = null;
    DesignStruct pDesign = null;

    //ArrayList<MyPoint> pList = new ArrayList<MyPoint>();
	static float distanceToDecide ;
	static final float TouchErrorDivider = (float) 20.0; 
	static final float StrokeWidth = (float) 5;
	static final float EndDiametre = (float) 7.0;
	static final float RadiusOfBlock = (float)20.0;
	static final float RadiusOfTar = (float)16.0;
	public static final float LimitPath=(float)100.0;

	static final int DashNumber = 10;
	static final String[] colorList = { "#070707", "#4cb848", "#fcd209", "#3e83c5",
		"#d71345", "#ef4136", "#eb6876", "#adb5da", "#7060ab", "#234b90",
		"#905b3c", "#88843b", "#e4bf05", "#bfd743", "#eef893" };
	public static final String ColorTrace = "#CDCDCD";
	public static final String ColorBlock = "#4cb848";
	public static final String ColorLine = "#707070";
	public static final String ColorDupLine = "#d71345";
	public static final String ColorLineEnd = "#070707";
	public static final String ColorEp = "#070707";
	public static final String ColorBg = "#F0F0F0";
	public static final String ColorTar = "#234b90";
	public static final String ColorInner = "#404040";
	public static final String ColorRaster = "#EAEAEA";

	public static final float TraceWidth = (float) 13.0;
	public static final float LineWidth = (float) 7.0;
	ArrayList<MyPoint> blocks = new ArrayList<MyPoint>();
	
	void initState(int l){

	}


	
	public void changeDType(){
		pDesign.changeDType();
		
	}
	public String getCurType(){
		return pDesign.getCurType();
		
	}
	
	// 画笔不能重用
	public MainView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		distanceToDecide = ((float)w)/TouchErrorDivider;
		Log.i(Utils.MOD,this.getWidth()+"Distance"+distanceToDecide);
	
		pDesign = DesignStruct.getInstance(this);
        pStruct = PlayStruct.getInstance(this, pDesign);
		pDesign.setUpdateCross(update);
		pStruct.setUpdateCross(update);
		
	}




	

	void updateView(){

		invalidate();
	}
	

	static double min(int x, int y) {
		if (x > y)
			return y;
		else
			return x;
	}

	// 画笔事件
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(this.status == Status.Play)
		pStruct.onDrawPlay( canvas,getWidth(), getHeight(),pDesign);
		if(this.status == Status.Design)
		pDesign.onDrawDesign( canvas,getWidth(), getHeight());	

	}

	boolean lock = false;
	
	enum Status {
		Play,
		Design,
	}
	Status status = Status.Play;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (status){
		case Play:
			return playOntouch(event);
		case Design:
			return designOnTouch(event);
		}
		return true;

		
	}
	private boolean designOnTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		pDesign.onTouch(event);
	
		updateView();

	    return true;
	}



	private boolean  playOntouch(MotionEvent event) {


	
        pStruct.ontouch(event,pDesign);
		updateView();

		return true;		
	}








	public void erasePath() {
		// TODO Auto-generated method stub
		if (this.status==Status.Play)
			pStruct.clear();
		if (this.status==Status.Design)
			pDesign.clear();
		//this.oriPList.clear();
		this.lock = false;
		this.updateView();
		
	}

	public void eraseBlock() {
		// TODO Auto-generated method stub
		this.blocks.clear();
		this.updateView();

	}
	
    public void shrink() {

    	pStruct.shrink(pDesign);


    }


	public void changeStatus(Status theStatus) {
		// TODO Auto-generated method stub
		if (theStatus==Status.Design){
			Log.i(Utils.MOD, "Status Design");

			
		}else
		if (theStatus==Status.Play){
			Log.i(Utils.MOD, "Status Play");
		}

		status =theStatus;
	}

    Handler update ;
	public void setUpdateHandler(Handler handler) {
		update  = handler;

	}



	public String  getCrossNumber() {
		return pDesign.getCross();
	}
}
