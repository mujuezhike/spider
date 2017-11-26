package com.swan.swanspider.word;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordStore {
	//swanspider\src\main\resources\word\word.txt
	private static List<String> wordlist;
	//swanspider\src\main\resources\word\area.txt
	private static List<String> arealist;
	
	private static int wordlistpointer;
	
	private static int arealistpointer;
	//swanspider\src\main\resources\word\ need change
	public static String dirPath = "C:\\zklist\\word\\";
	
	public static void init() {
		
		System.out.println("init ...");
		
		arealist = getListFromFile(new File(dirPath+"area.txt"));
		arealistpointer = 0;
		wordlist = getListFromFile(new File(dirPath+"word.txt"));
		wordlistpointer = 0;
		
		System.out.println("init complete");
		System.out.println("arealist:"+arealist.size());
		System.out.println("wordlist:"+wordlist.size());
		
	}

	private static String getCharset(File file) throws IOException{  
        
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));    
        int p = (bin.read() << 8) + bin.read();    
          
        String code = null;    
          
        switch (p) {    
            case 0xefbb:    
                code = "UTF-8";    
                break;    
            case 0xfffe:    
                code = "Unicode";    
                break;    
            case 0xfeff:    
                code = "UTF-16BE";    
                break;    
            default:    
                code = "GBK";    
        }    
        
        bin.close();
        return code;  
	}
	
	private static List<String> getListFromFile(File file){
		
		List<String> list = new ArrayList<String>();
		BufferedReader reader = null;
        try {
        	String charset = getCharset(file);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),charset));  
            String tempString = "";
            while ((tempString = reader.readLine()) != null) {
            	list.add(tempString);
            }
            reader.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                }
            }
        }
		return list;
		
	} 
	
	public static String getNextWord() {
		
		String returnString = wordlist.get(wordlistpointer) + " " + arealist.get(arealistpointer);
		
		if(wordlistpointer<wordlist.size()-1) {
			
			wordlistpointer ++;
			
		}else if(arealistpointer<arealist.size()-1){
			
			wordlistpointer = 0;
			arealistpointer ++;
			
		}else {
			
			wordlistpointer = 0;
			arealistpointer = 0;
			
		}
		
		return returnString;
		
	}
	
	public static void main(String[] args) {
		init();
	}
}