package supportvectormachine;

import java.io.*;
import java.util.ArrayList;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Test {
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
    File vfile = new File(System.getProperty("user.dir")+"//"+"TFIDFvectorsTraining");
    
    FileReader vfr = new FileReader(vfile);
    BufferedReader vbr = new BufferedReader(vfr);
      int obs = 24784;
      int wlen = 591;
	
	double[][] vector = new double[obs][wlen];
	
	String vtemp = null;
	String[] vecs = null;
	int r = 0;
	while((vtemp = vbr.readLine()) != null || (vtemp != " ")) {
		
		if(vtemp == null) break;
		vecs = vtemp.split(",");
		for(int i = 0; i < wlen ; i ++) {
			
			vector[r][i] = Double.parseDouble(vecs[i]);
		}
		r ++;
	}
	
	
	double b = 0.0;
	ArrayList<Double> arrl = new  ArrayList<>();
	double[] w = null;
	
	
	String temp;
	File file = new File(System.getProperty("user.dir")+"//"+"w");
	
	FileReader fr = null;
	BufferedReader br = null;
	
	try {
		fr = new FileReader(file);
		 br = new BufferedReader(fr);
	
		//b = Double.parseDouble(br.readLine());
		
		while((temp = br.readLine()) != null || (temp != " ")) {
			
			if(temp == null) break;
				
			arrl.add(Double.parseDouble(temp));
			
		}
		
		w = new double[arrl.size()];
		
		for(int j =0; j< arrl.size(); j++) {
			w[j] = arrl.get(j);
		}
		
		RealMatrix wa = MatrixUtils.createColumnRealMatrix(w);
		RealMatrix data = MatrixUtils.createRealMatrix(vector);
		
		RealMatrix output = null;
		
       double out= 0.0;
       double logit = 0.0;
       
       for(int i =0 ; i < obs ; i ++) {
    	 
    	    logit = 0.0;
   		    for (int j = 0; j < wlen; j++)  {
   		    	
   			logit += w[j] * data.getEntry(i, j);
   		   }
   		   if ((1.0 / (1.0 + Math.exp(-logit)) > 0.5)){
   			   System.out.println("1");
   		   }
   		   else
   		   {
   			   System.out.println("0");
   		   }
       }
	
	} catch ( NumberFormatException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	
	
	}
}
	
	

