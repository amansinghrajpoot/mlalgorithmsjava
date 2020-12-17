package supportvectormachine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class BOW {

	public static void generate(ArrayList<String[]> arrlist, String[] wordlist) {
		
		File TFIDFvectors = new File(System.getProperty("user.dir")+"//"+ "TFIDFvectorsTraining");
		BufferedWriter bw = null;
		try {
		 bw = new BufferedWriter(new FileWriter(TFIDFvectors));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
            System.out.println(e1.getLocalizedMessage());
		}
		
		
		Iterator<String[]> it = arrlist.iterator();
		String data[] = null;
		String tweet = null;
		double label = 0.0;
		int len = wordlist.length +1;
		
		Double[] vectors = null;
		vectors = new Double[len];
		
		String[] words = null;
		
		while(it.hasNext()) {
			
			for(int i = 0; i <len; i ++) {
				vectors[i] = 0.0;
			}
			
			 data = it.next();
			 tweet = data[0];
			 
			 words = tweet.split(" ");
		     try {
		    	
		    	 label = Double.parseDouble(data[1]);
		    	
		      }catch (Exception e) {
		    	
		    	label = 0.0;
			  }
		     if (label > 0.5)label = 1;
		     else label = 0;
		     
		     vectors[len - 1] = label;
		     
		       
		     for (int j = 0; j < len -1; j ++) {
		    	 
		    	   for(int k = 0; k < words.length; k ++) {
		    		   
		    		   if(words[k].equals(wordlist[j]) ) {
		    			   vectors[j] = vectors[j] + 1;
		    		   }
		    		   
		    	   }
		    	   try {
						bw.append(vectors[j]+",");
						bw.flush();
					      } catch (IOException e) {
					    	// TODO Auto-generated catch block
					    	e.printStackTrace();
					      }
		     }
		     
		     
		     try {  
	        	 
					bw.append(label+"");
					bw.newLine();
					bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    }
		 }
	}
	
}


