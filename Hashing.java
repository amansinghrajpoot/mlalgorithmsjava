package supportvectormachine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Hashing {

	public static void hash(ArrayList<String[]> arrlist ) {
		

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
		String[] wordlist = null;
		String[] trigram = null;
		
		Double[] vectors = null;
		
		int len = 592;
		vectors = new Double[len];
		
		 int index = 0;
	     long h = 0;
		while(it.hasNext()) {
			
			 

			
			for(int i = 0; i < len; i ++) {
				vectors[i] = 0.0;
			}
			
			 data = it.next();
			
			 tweet = data[0];
			 wordlist = tweet.split(" ");
			 
			 if (wordlist.length > 2) {
			 trigram = new String[wordlist.length - 2];
			 
			        for (int l =0; l < wordlist.length -2; l ++) {
				    trigram[l] = wordlist[l]+wordlist[l+1]+wordlist[l+2];
			         }
			 }
			 else {
				 trigram = new String[wordlist.length];
				 for (int l =0; l < wordlist.length; l ++) {
					    trigram[l] = wordlist[l];
				         }
				 
			 }
			 
		     try {
		    	
		    	 label = Double.parseDouble(data[1]);
		    	
		      }catch (Exception e) {
		    	
		    	label = 0.0;
			  }
		     if (label > 0.5)label = 1;
		     else label = 0.0;
		     
		     vectors[len -1] = label;
		     
		    
		     for (int i =0; i < trigram.length; i ++) {
		    	 
		    	 h = Hashing.hash64(trigram[i]);
		    	 index = Math.floorMod(h, len-1);
		    	 vectors[index] = vectors[index] +1 ;
		    	 h = 0;
		    	 index = 0;
		    	 
		      }
		     
		       
		     for (int j = 0; j < vectors.length-1; j ++) {
		    	 
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
	
	
	private static long hash64( final String text) {
		final byte[] bytes = text.getBytes(); 
		return hash64( bytes, bytes.length);
	}
	
	private static long hash64( final byte[] data, int length) {
		return hash64( data, length, 0xe17a1465);
	}
	
	private static long hash64( final byte[] data, int length, int seed) {
		final long m = 0xc6a4a7935bd1e995L;
		final int r = 47;

		long h = (seed&0xffffffffl)^(length*m);

		int length8 = length/8;

		for (int i=0; i<length8; i++) {
			final int i8 = i*8;
			long k =  ((long)data[i8+0]&0xff)      +(((long)data[i8+1]&0xff)<<8)
					+(((long)data[i8+2]&0xff)<<16) +(((long)data[i8+3]&0xff)<<24)
					+(((long)data[i8+4]&0xff)<<32) +(((long)data[i8+5]&0xff)<<40)
					+(((long)data[i8+6]&0xff)<<48) +(((long)data[i8+7]&0xff)<<56);
			
			k *= m;
			k ^= k >>> r;
			k *= m;
			
			h ^= k;
			h *= m; 
		}
		
		switch (length%8) {
		case 7: h ^= (long)(data[(length&~7)+6]&0xff) << 48;
		case 6: h ^= (long)(data[(length&~7)+5]&0xff) << 40;
		case 5: h ^= (long)(data[(length&~7)+4]&0xff) << 32;
		case 4: h ^= (long)(data[(length&~7)+3]&0xff) << 24;
		case 3: h ^= (long)(data[(length&~7)+2]&0xff) << 16;
		case 2: h ^= (long)(data[(length&~7)+1]&0xff) << 8;
		case 1: h ^= (long)(data[length&~7]&0xff);
		        h *= m;
		};
	 
		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		return h;
	}
	
}
	
	

