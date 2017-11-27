package com.swan.swanspider.qcc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.swan.swanspider.ipcontrol.DynamicIpContainer;

public class SearchQCCCompanyBeanTask implements Runnable{
	
	private final static String saveBasePath = "C:\\getpage\\bean\\";

	private String[] strs = null;
	
	private int eer = 0;
	
	public String[] getStrs() {
		return strs;
	}

	public void setStrs(String[] strs) {
		this.strs = strs;
	}

	public int getEer() {
		return eer;
	}

	public void setEer(int eer) {
		this.eer = eer;
	}
	
	@Override
	public void run() {
		
		System.out.println("run");
		
		for (int s = 0; s < strs.length;) {
			
			try {
				
				Long emunu = findName(strs[s]);
				if(emunu!=null && emunu>0){
					
					s++;
					
				}else{
					System.out.println("changeip::}3}");
					eer++;
					if(eer >= DynamicIpContainer.dynamicips.size()){
						eer = 0;
					}
				}
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				System.out.println("changeip::}4}");
				eer++;
				if(eer >= DynamicIpContainer.dynamicips.size()){
					eer = 0;
				}
			}
		}
		
		//ScheduleController.turnNextFileName();
		
	}
	
	public Long findName(String url) throws IOException, InterruptedException {
		
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		
		webClient.setHTMLParserListener(null);

		// ProxyConfig proxyConfig = new ProxyConfig();
		// proxyConfig.setProxyHost("140.255.236.138");
		// proxyConfig.setProxyPort(8118);
		// webClient.getOptions().setProxyConfig(proxyConfig);
		String ipport = DynamicIpContainer.dynamicips.get(eer);
		if (ipport != null) {
			ProxyConfig proxyConfig = new ProxyConfig(ipport.split(":")[0], Integer.parseInt(ipport.split(":")[1]));
			webClient.getOptions().setProxyConfig(proxyConfig);
		}else {
			System.out.print(" ");
			webClient.close();
			return null;
		}

		CookieManager ck = new CookieManager();
		Cookie cookie = new Cookie("www.qichacha.com", "hasShow", "1");
		Cookie cookie2 = new Cookie("www.qichacha.com", "acw_tc", "AQAAACUUhXFYMgsAabld34bT2498dGZe");
		Cookie cookie3 = new Cookie("www.qichacha.com", "_uab_collina", "151174962106136758594117");
		Cookie cookie4 = new Cookie("www.qichacha.com", "zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f", "%7B%22sid%22%3A%201511749613740%2C%22updated%22%3A%201511751148718%2C%22info%22%3A%201511749613744%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22www.baidu.com%22%7D");
		Cookie cookie5 = new Cookie("www.qichacha.com", "zg_did", "%7B%22did%22%3A%20%2215ffb4c78571ce-060d3ebbbffa58-5e183017-100200-15ffb4c7858d7%22%7D");
		ck.addCookie(cookie);
		ck.addCookie(cookie2);
		ck.addCookie(cookie3);
		ck.addCookie(cookie4);
		ck.addCookie(cookie5);
		webClient.setCookieManager(ck);
		
		webClient.getOptions().setCssEnabled(false);//origin true
		webClient.getOptions().setJavaScriptEnabled(false);//origin true
		webClient.getOptions().setTimeout(5000);
		webClient.setJavaScriptTimeout(5000);
		webClient.getJavaScriptEngine().setJavaScriptTimeout(5000);

		String codenum = url.substring(url.lastIndexOf("/")+1);
		codenum = codenum.substring(codenum.lastIndexOf("=")+1);
		HtmlPage page = webClient.getPage(url);
		try {
			Thread.sleep(200);
			System.out.println("qcc:"+url);
			String s = page.asXml();
			//getFileFromBytes(s,saveBasePath+codenum+".html");
			if(s.contains("ma_line1")){
				 
				getFileFromBytes(s,saveBasePath+codenum+".html");
				webClient.close();
				return 1l;
				
			}else{
				Random rd = new Random();
				int em = rd.nextInt(10);
				getFileFromBytes(s,saveBasePath+"eerr"+em+".html");
			}
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webClient.close();
		//Thread.sleep(2000);
		return 0l;
		
	}
	
	 /**  
	  * 灏哠tring鏁版嵁瀛樹负鏂囦欢  
	  */  
	 public static File getFileFromBytes(String str,String path) {  
	     
		 byte[] b=str.getBytes();  
	     
	     BufferedOutputStream stream = null;  
	     File file = null;  
	     try {  
	         file = new File(path);  
	         //20170705
	         if(file.exists()){
	        	 return file;
	         }
	         FileOutputStream fstream = new FileOutputStream(file);  
	         stream = new BufferedOutputStream(fstream);  
	         stream.write(b);  
	     } catch (Exception e) {  
	         e.printStackTrace();  
	     } finally {  
	         if (stream != null) {  
	             try {  
	                 stream.close();  
	             } catch (IOException e1) {  
	                 e1.printStackTrace();  
	             }  
	         }  
	     }  
	     return file;  
	     
	 } 

}
