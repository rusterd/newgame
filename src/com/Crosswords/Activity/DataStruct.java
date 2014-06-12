package com.Crosswords.Activity;

import java.util.ArrayList;

public class DataStruct {
	ArrayList<MyPoint>  theOriList;
	ArrayList<MyPoint>  theSrcList;
	ArrayList<MyPoint> theTarList;
	ArrayList<MyPoint> theBlocks;
	
	
	/**
	 * @param ori 画出的点列
	 * @param src  传入空点列，结果会填满变换开始点列
	 * @param tar  传入空点列，结果会填满变换结束的点列
	 * @param pDesign 关卡中的点
	 */
	public DataStruct(ArrayList<MyPoint>ori,
			DesignStruct pDesign, ArrayList<MyPoint>src,ArrayList<MyPoint>tar){
		src.clear();
		tar.clear();
		theOriList = ori;
		this.theSrcList = src;
		this.theTarList = tar;
		this.theBlocks = pDesign.getBlocks();
		
	}

	/**
	 * 计算出之前和之后两个点列
	 */
	public void processTransfer(int j) {
		// TODO Auto-generated method stub
		//int i = 0;
		ArrayList<MyPoint> temp = new ArrayList<MyPoint>();

		temp.add( this.theOriList.get(0));

		if (j<2){
			theTarList.clear();
			theTarList.addAll( temp);
			theSrcList.clear();
			theSrcList.addAll(theOriList);
			temp.add(theOriList.get(theOriList.size()-1));
			return;
		}
		int lastSrc=0;
		for (int i = 1; i< j; i++){
			MyPoint p = this.theOriList.get(i-1);
			MyPoint nextP = this.theOriList.get(i);
			recursiveDeal(temp,p,nextP,theOriList,theBlocks,i);    
		
		}
		temp.add(theOriList.get(j-1));
		theTarList.clear();
		
		theTarList.addAll( temp);
		//theTarList.add(theOriList.get(theOriList.size()-1));
		theSrcList.clear();
		theSrcList.addAll(theOriList);
		
		
	}
	public void processTransfer() {
		processTransfer(this.theOriList.size());
		
	}
	

	private void recursiveDeal(ArrayList<MyPoint> temp, MyPoint point,
			MyPoint next, ArrayList<MyPoint> srcList, ArrayList<MyPoint> blocks, int lim) {
		// TODO Auto-generated method stub
		MyPoint lastP = temp.get(temp.size()-1);
		MyPoint needAdd = getInnerPoint(lastP , point,
				next,blocks);
		if (needAdd!=null){
           // needAdd.calNearest(srcList,temp.get(temp.size()-1).hangCorrespond,lim);

			//temp.get(temp.size()-1).changeAngle(Utils.angle(lastP, point, needAdd));
			temp.add(needAdd);
			//Utils.logTemp(temp);
			recursiveDeal(temp,point,next,srcList,blocks,lim);

		}else{
			temp.get(temp.size()-1).changeAngle(Utils.angle(point.x-lastP.x, point.y-lastP.y, next.x-lastP.x, next.y-lastP.y));
		}
		reGen(temp,next,srcList,blocks);//To make all wrong temp right;
	}

	

	/**
	 * @param temp
	 * @param next 
	 * @param blocks 
	 * @param srcList 
	 */
	private void reGen(ArrayList<MyPoint> temp, MyPoint next, ArrayList<MyPoint> srcList, ArrayList<MyPoint> blocks) {
		// TODO Auto-generated method stub
		MyPoint need = null;
		int needint= 0;
		for (int i =1;i<temp.size();i++){
		    if (temp.get(i).failed){
		    	need = temp.get(i);
		    	needint = i;
		    }
		}
		if (need==null)
			return;
		else{
			ArrayList<MyPoint> allTemp =new ArrayList<MyPoint>();
			allTemp.addAll(temp);
			allTemp.add(next);
			ArrayList<MyPoint> tfront= new ArrayList<MyPoint>();
			tfront.addAll(allTemp.subList(0, needint));
			ArrayList<MyPoint> tNext= new ArrayList<MyPoint>();
			tNext.addAll(allTemp.subList(needint+1, allTemp.size()));
			recursiveDeal(tfront,need,tNext.get(0),srcList,blocks,need.hangCorrespond);
			MyPoint pA = tNext.get(0);
			MyPoint ori =need;
			MyPoint tar = tfront.get(tfront.size()-1);
			tNext.get(0).changeAngle(Utils.angle(pA.x-tar.x,
					pA.y-tar.y, pA.x-ori.x,
					pA.y-ori.y ));
			temp.clear();
			temp.addAll(tfront);
			temp.addAll(tNext);
			temp.remove(temp.size()-1);
			reGen(temp,next,srcList,blocks);
			
		}
		
	}

	private MyPoint getInnerPoint(MyPoint lastP, MyPoint point, MyPoint next,
			ArrayList<MyPoint> blocks) {
		// TODO Auto-generated method stub
		MyPoint res = null;
		double minAngle = 2*Math.PI;
		for (MyPoint p: blocks)
		{
			if (Utils.inTriangle(p, lastP, point, next)){
				if(Math.abs(Utils.angle(point.x-lastP.x, point.y-lastP.y, p.x-lastP.x, p.y-lastP.y))<minAngle){
					minAngle = (Math.abs(Utils.angle(point.x-lastP.x, point.y-lastP.y, p.x-lastP.x, p.y-lastP.y)));
					MyPoint pf = new MyPoint(p);
					pf.isHangPoint = true;
					double ang = Utils.angle(p.x-lastP.x, p.y-lastP.y, next.x-p.x, next.y-p.y);
					pf.hangAngle =ang;
					res = pf;
				}
			}
		}
		if (res ==null)
			return null;
			else
				return res;
		//return null;
	}


	
	

}
