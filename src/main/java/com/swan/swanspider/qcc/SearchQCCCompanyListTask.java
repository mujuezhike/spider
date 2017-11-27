package com.swan.swanspider.qcc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.swan.swanspider.ipcontrol.DynamicIpContainer;

public class SearchQCCCompanyListTask implements Runnable{
	
	private final static String saveBasePath = "C:\\getpage\\list\\";

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
					System.out.println("changeip::}}");
					eer++;
					if(eer >= DynamicIpContainer.dynamicips.size()){
						eer = 0;
					}
				}
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				System.out.println("changeip::}}");
				eer++;
				if(eer >= DynamicIpContainer.dynamicips.size()){
					eer = 0;
				}
			}
		}
		
		/** 鍒囨崲鏂版枃浠�     **/
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
		
		Cookie cookie2 = new Cookie("g.alicdn.com", "_uab_collina", "151140856485176110429407");
		Cookie cookie3 = new Cookie("www.qichacha.com", "_uab_collina", "151142069835265994204304");
//		Cookie cookie4 = new Cookie("g.alicdn.com", "_umdata", "A502B1276E6D5FEF3001584AF99821BE2459495D775ECD5EE6C9D54B9D75731688B240F034C62EA1CD43AD3E795C914C2053C5B223499DBB86DB1D634ACDF7BC");
//		Cookie cookie5 = new Cookie("www.qichacha.com", "_umdata", "ED82BDCEC1AA6EB9579BA636C6A89D2ECE6D565E7920CF2FD3724230DF3DB73C452052A5B795CBB1CD43AD3E795C914CEC86D9ACAB3B7341FDBC81787707D5A5");
		ck.addCookie(cookie);
		ck.addCookie(cookie2);
		ck.addCookie(cookie3);
//		ck.addCookie(cookie4);
//		ck.addCookie(cookie5);
		webClient.setCookieManager(ck);
		
		webClient.getOptions().setCssEnabled(false);//origin true
		webClient.getOptions().setJavaScriptEnabled(false);//origin true
		webClient.getOptions().setTimeout(8000);
		webClient.setJavaScriptTimeout(8000);
		webClient.getJavaScriptEngine().setJavaScriptTimeout(8000);

		String codenum = url.substring(url.lastIndexOf("/")+1);
		codenum = codenum.substring(codenum.lastIndexOf("=")+1);
		HtmlPage page = webClient.getPage(url);
		try {
			Thread.sleep(200);
			System.out.println("qcc:"+url);
			String s = page.asXml();
			if(s.contains("font-15 text-dark pull-left m-l") || s.contains("ma_h1")){
				 Pattern pattern = Pattern.compile("<a href=\"(.+?)\" target=\"_blank\" class=\"ma_h1\">"); 
				 Matcher matcher = pattern.matcher(s);   
				String num = "";
				String[] srr = new String[5];
				int i = 0;
	        		while(matcher.find()){
	        			String mm = matcher.group(1);
		                System.out.println(matcher.group(1));
		                String dd = matcher.group(1);
		                num += matcher.group(1);
		                num += "\r\n";
		                System.out.println(num);
		                srr[i] = "http://www.qichacha.com"+mm;
		                i++;
	        		}
				getFileFromBytes(s,saveBasePath+codenum+".html");
				webClient.close();
				
				SearchQCCCompanyBeanTask task = new SearchQCCCompanyBeanTask();
				
				Random rd = new Random();
				int ioe = rd.nextInt(5);
				if(srr!=null && srr.length>0){
					task.setStrs(srr);
					task.setEer(ioe);
					ScheduleController.fixedThreadPool.execute(task);
				}
				return 1l;
				
			}else{
				Random rd = new Random();
				int em = rd.nextInt(10);
				getFileFromBytes(s,saveBasePath+"eerr"+em+".html");
			}
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
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
