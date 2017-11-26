package com.swan.swanspider.ipcontrol;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * IP切换器
 * @author mujuezhike
 */
public class IpChanger implements Runnable{

	/** 订单号    **/
	/** @see http://www.data5u.com **/
	private String order = "7668920b2681fe8798755f745464782a";
	
	@Override
	public void run() {
		
		while(true){
			
			try {
				
				URL url = new URL("http://api.ip.data5u.com/dynamic/get.html?order="+order+"&random=true&sep=4");
		    	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		    	connection.setConnectTimeout(3000);
		    	connection = (HttpURLConnection)url.openConnection();
		    	
		        InputStream raw = connection.getInputStream();  
		        InputStream in = new BufferedInputStream(raw);  
		        byte[] data = new byte[in.available()];
		        int bytesRead = 0;  
		        int offset = 0;  
		        while(offset < data.length) {  
		            bytesRead = in.read(data, offset, data.length - offset);  
		            if(bytesRead == -1) {  
		                break;  
		            }  
		            offset += bytesRead;  
		        }  
		        in.close();  
		        raw.close();
				
		        String res = new String(data, "UTF-8");
			
				DynamicIpContainer.setDIp(res);
				
				Thread.sleep(5000);	
				
			} catch (Exception e) {
				
			}
			
		}
		
	}
	
	
	
}
