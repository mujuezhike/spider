package com.swan.swanspider.qcc;

import java.net.URLEncoder;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.swan.swanspider.ipcontrol.DynamicIpContainer;
import com.swan.swanspider.word.WordStore;

public class ScheduleController {
	
	final private static int TASK_MAX_THREAD = 15;
	
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(TASK_MAX_THREAD);  
	
	public static void init(){
		
		
		
	}

	public static void main(String[] args) throws InterruptedException{
		// TODO Auto-generated method stub
		
		//Ip切换器  :启动
		DynamicIpContainer.init();
		
		//单词库  :启动
		WordStore.init();
		
		Thread.sleep(35000);
		//ExecutorService fixedThreadPool = Executors.newFixedThreadPool(TASK_MAX_THREAD);  
		  
			for (;;) {  
				//logger.info("boforestart:"+index);
				int threadCount = ((ThreadPoolExecutor)fixedThreadPool).getActiveCount();
				
				Thread.sleep(1000);
				//System.out.println(threadCount);
				
				if(threadCount < TASK_MAX_THREAD){
					SearchQCCCompanyListTask task = new SearchQCCCompanyListTask();
					String[] srr = getNextURLsFromFileName();
					Random rd = new Random();
					int ioe = rd.nextInt(5);
					if(srr!=null && srr.length>0){
						task.setStrs(srr);
						task.setEer(ioe);
						fixedThreadPool.execute(task);
					}
				}
				
		    }  

	}
	
	public static String[] getNextURLsFromFileName(){
		
		String[] slist = new String[10];
		
		for(int i=0;i<10;i++) {
		
			String ors = WordStore.getNextWord();
			String ls = "http://www.qichacha.com/search?key="+ors;
			System.out.println(ls);
			slist[i] = ls;
			
		}
		
		return slist;
		
	}
	
}
