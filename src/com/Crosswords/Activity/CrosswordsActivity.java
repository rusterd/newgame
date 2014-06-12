package com.Crosswords.Activity;

import org.json.JSONObject;

import com.Crosswords.Activity.MainView.Status;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class CrosswordsActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// MainView mv = (MainView) findViewById(R.id.linearLayout2);


		showDesignMenu();
		MainView mv = (MainView) findViewById(R.id.mainv);

		mv.setUpdateHandler(handler);

	}

	Handler handler = new Handler(){
    	@Override
    	 public void handleMessage(Message msg) {  
    		MainView mv = (MainView) findViewById(R.id.mainv);
            switch(msg.what){
            case Utils.CrossUpdate:
            	TextView  tx = (TextView) findViewById(R.id.cross);
            	tx.setText(mv.getCrossNumber());
            	break;
            case Utils.Success:
            	dealPassed();
            	break;
            case Utils.RemainBlock:
            	dealFailed("Remain Block");
            	break;
            case Utils.NotSeperated:
            	dealFailed("Not Seperated");
            	break;
            case Utils.DupEdge:
            	dealFailed("Duplicted Edge");
            	break;
            case Utils.InvalidCross:
            	dealFailed("Invalid Cross");
            	break;
            }
                }


    };
    
    
    
	private void dealPassed() {
	    AlertDialog.Builder builder = new Builder(this);
	     builder.setMessage("Success");
	  
	    builder.setTitle("Success");
	    builder.setPositiveButton("Cool", new DialogInterface.OnClickListener() {
	    

			

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();				
			}



	     });	
	    builder.show();
	}

	private void dealFailed(String string) {
		// TODO Auto-generated method stub
	    AlertDialog.Builder builder = new Builder(this);
	     builder.setMessage(string);
	  
	    builder.setTitle("Failed");
	    builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
	    

			

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();				
			}



	     });
	    builder.show();

		
	}  

	private void showDesignMenu() {
		toPlay();
		
        
	}

	private void toPlay() {
		MainView mv = (MainView) findViewById(R.id.mainv);

		mv.initState(0);
		
		findViewById(R.id.modedesign).setVisibility(View.VISIBLE);
		//Change the mode of the main

		Button totry = (Button) findViewById(R.id.totry);
		mv.changeStatus(Status.Play);
		totry.setText("Change");
		changetv(mv,"Play");
		totry.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				toDesign();


			}

		
			
		});
		
		//Erase all path
		Button erasePath = (Button) findViewById(R.id.cleanpath);
		erasePath.setVisibility(View.VISIBLE);;
		erasePath.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainView mv = (MainView) findViewById(R.id.mainv);
				
				mv.erasePath();
			}
		});

		
        Button shrink = (Button)findViewById(R.id.shrink);
        shrink.setVisibility(View.VISIBLE);;

        shrink.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainView mv = (MainView) findViewById(R.id.mainv);
				mv.shrink();
				
			}
        	
        });		
        Button type =  (Button)findViewById(R.id.type);
        type.setVisibility(View.GONE);
        mv.updateView();
	}

	private void changetv(MainView mv, String string) {
		TextView tv = (TextView) findViewById(R.id.mode);
		tv.setText(string);

	}

	private void toDesign() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		MainView mv = (MainView) findViewById(R.id.mainv);
		mv.changeStatus(Status.Design);
		changetv(mv,"Design");
		Button totry = (Button) findViewById(R.id.totry);
	//	mv.changeStatus(Status.Play);
		totry.setText("Change");
		totry.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				toPlay();


			}

		
			
		});
		Button erasePath = (Button) findViewById(R.id.cleanpath);
		erasePath.setVisibility(View.GONE);;
        Button shrink = (Button)findViewById(R.id.shrink);
        shrink.setVisibility(View.GONE);
        Button type =  (Button)findViewById(R.id.type);
        type.setVisibility(View.VISIBLE);
        type.setText(mv.getCurType());
        type.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainView mv = (MainView) findViewById(R.id.mainv);
				mv.changeDType();
		        Button type =  (Button)findViewById(R.id.type);

				type.setText(mv.getCurType());

			}
        	
        });
        mv.updateView();
	}
	
}