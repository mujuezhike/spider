package com.swan.swanspider.ipcontrol;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局IP 获取储存器
 * @author mujuezhike
 */
public class DynamicIpContainer {
	
	/** ip列表  **/
	public static List<String> dynamicips = new ArrayList<String>();
	
	/** 最大ip数量  **/
	public static Integer maxLiveIp = 20;

	/** 下个等待获取需要变换的ip位置   **/
	private static Integer nextchangeIpCount = 0;
	
	public static void main(String args[]){
		
		DynamicIpContainer.init();
		
	}
	
	public static void init(){
		 
		new Thread(new IpChanger()).start();
		
		//调试使用
//		for(;;){
//		
//		try {
//			Thread.sleep(10000);
//			for(int i=0;i<dynamicips.size();i++){
//				
//				System.out.print(dynamicips.get(i) + "  ");
//				
//			}
//			System.out.print(nextchangeIpCount);
//			System.out.println();
//			
//		} catch (Exception e) {
//			
//			
//			
//			e.printStackTrace();
//		}
//		}
		
	}
	
	public static void setDIp(String sip){
		
		sip = sip.trim();
		if(nextchangeIpCount < dynamicips.size()){
			
			dynamicips.set(nextchangeIpCount, sip);
			nextchangeIpCount++;
			
		}else{
			
			dynamicips.add(sip);
			nextchangeIpCount++;
		}
		
		if(nextchangeIpCount >= maxLiveIp){
			nextchangeIpCount = 0;
		}
		
	}
}
