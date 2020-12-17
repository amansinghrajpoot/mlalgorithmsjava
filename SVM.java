package supportvectormachine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public final class SVM {
	
	static int max_no_of_iterations = 5;
	static double b = 0.0; 
	static double epsilon = 0.001;
	static double c = 0.8;
	static RealMatrix alpha;
	static double min_alpha_optization = 0.00001;
	static RealMatrix w;
	
	public static ArrayList<double[][]> readData() {
		
		File file = new File(System.getProperty("user.dir")+"//"+"TFIDFvectorsTraining" );
		ArrayList<double[][]> xlist = new ArrayList<>();
		
		double[][] xarray ;
		double[][] yarray ;
		
		int row = 0;
		
		FileReader fr= null;
		FileReader fr1= null;
		BufferedReader br = null;
		BufferedReader br1 = null;
		try {
			 fr = new FileReader(file);
			 fr1 = new FileReader(file);
			 br = new BufferedReader(fr);
			 br1 = new BufferedReader(fr1);
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getLocalizedMessage());
		}
		String line = null;
		String[] arr = null;
		double target = 0.0;
		
		int no_of_observations = 0;
		int features = 0;
		
		
		try {
			line = br1.readLine();
			features = line.split(",").length -1;
			no_of_observations += 1;

			while ((line = br1.readLine()) != null && line != "" ) {
				no_of_observations += 1;

			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		line = null;
		
		
		xarray = new double[no_of_observations][features];
		yarray = new double[no_of_observations][1];
		   
		
		try {
			while( (line = br.readLine()) != null && line != "" ) {
				
				arr = line.split(",");
				
				
				
				for( int i = 0; i < arr.length - 1 ; i ++) {
					
					xarray[row][i] = Double.parseDouble(arr[i]);
				}
				
				
				target = Math.round(Double.parseDouble(arr[arr.length -1]));
				if (target == 0) {
					target = -1;
				}
								
				yarray[row][0] = target;
				
				
				row++;
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getLocalizedMessage());
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xlist.add(xarray);
		xlist.add(yarray);		
		
		return xlist;
	}
	
	public static void prepare_and_fit(ArrayList<double[][]> vectors) {
		
		double[][] xarray = vectors.get(0);
		double[][] yarray = vectors.get(1);
		
		RealMatrix x = MatrixUtils.createRealMatrix(xarray);
		RealMatrix y = MatrixUtils.createRealMatrix(yarray);
			
		try {
			fit(x, y);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void fit(RealMatrix x, RealMatrix y) throws IOException {
		
		
		File alphafile = new File(System.getProperty("user.dir")+"//"+"alpha");
		
		BufferedWriter br = new BufferedWriter(new FileWriter(alphafile));
		
		double[] alphaArray = new double [x.getData().length];
        
		IntStream.range(0, alphaArray.length).forEach(i -> alphaArray[i] = 0);
		
		alpha = MatrixUtils.createColumnRealMatrix(alphaArray);
		
		
		int i = 0;
		int smo = 0;
		 
		while ( i < max_no_of_iterations) {
			
			smo = performSMO(x, y);
	         
			if (smo == 0) {
				i += 1;
				
				
			}
			else {
		        	i = 0;
			}
			
			for( int k =0 ; k < alpha.getRowDimension(); k++) {
				
				br.write( alpha.getEntry(k, 0)+" ");
			}
			br.write( smo +" ");
			br.newLine();
			br.flush();
		}
		br.close();
		w = calculateW(x, y);
	}
	
	
	public static int classify(RealMatrix data) {
		
		int c = -1;
		
		if(   Math.signum(data.multiply(w).getEntry(0, 0) + b ) == 1) c = 1;
		
		return c;
		
	}
	
	
	
	/*******************************************************************/
	
	public static RealMatrix calculateW(RealMatrix x, RealMatrix y) {
		
		double[][] Warray = new double[x.getData()[0].length][1];
		
		IntStream.range(0, Warray.length).forEach(i -> Warray[i][0] = 0.0);
		
		RealMatrix w = MatrixUtils.createRealMatrix(Warray);
		
		for (int i = 0; i < x.getData().length; i ++) {
			
			w = w.add(x.getRowMatrix(i).transpose().scalarMultiply(y.getRowMatrix(i).multiply(alpha.getRowMatrix(i)).getEntry(0, 0)));
		}
		return w;
	}
	
	private static int performSMO(RealMatrix x, RealMatrix y) {
		
		int no_of_alpha_pairs_optimized = 0;
		
		
		for(int i = 0 ; i < x.getData().length; i ++) {
			
			RealMatrix Ei = multiply(y, alpha).transpose()
					                            .multiply(x.multiply(x.getRowMatrix(i).transpose() ))
											   .scalarAdd(b)
											   .subtract( y.getRowMatrix(i));
			
			if (checkIfAlphaViolatesKKT(alpha.getEntry(i, 0), Ei.getEntry(0, 0), epsilon, c)) {
				
				int j = selectIdxOf2ndAlphaToOpt(i, x.getData().length);
				
				RealMatrix Ej = multiply(y, alpha)
                        .transpose()
                        .multiply(x.multiply(x.getRowMatrix(j).transpose()))
						   .scalarAdd(b)
						   .subtract(y.getRowMatrix(j));
				
				double alphaIold = alpha.getRowMatrix(i).getEntry(0, 0);
				double alphaJold = alpha.getRowMatrix(j).getEntry(0, 0);
				
				double[] bounds = boundAlpha(alpha.getEntry(i, 0), alpha.getEntry(j, 0), y.getEntry(i, 0), y.getEntry(j, 0));
				
				double ETA = x.getRowMatrix(i).multiply(x.getRowMatrix(j).transpose()).scalarMultiply(2.0).getEntry(0, 0) 
						- x.getRowMatrix(i).multiply(x.getRowMatrix(i).transpose()).getEntry(0, 0)
						- x.getRowMatrix(j).multiply(x.getRowMatrix(j).transpose()).getEntry(0, 0);
				
				if( bounds[0] != bounds[1] && ETA < 0) {
					
					if (optimizeAlphaPair(i, j, Ei.getEntry(0, 0), Ej.getEntry(0, 0), ETA, bounds, alphaIold, alphaJold, y)) {
						
						optimizeB( Ei.getEntry(0, 0), Ej.getEntry(0, 0), alphaIold, alphaJold, i, j, x, y);
						no_of_alpha_pairs_optimized += 1;
					}
					
				}
				
				
			  }
			
		 }
		
		
		return no_of_alpha_pairs_optimized;
	}
	
	
	public static boolean optimizeAlphaPair(int i, int j, double Ei, double Ej, double ETA, double bounds[], double alphaIold, double alphaJold, RealMatrix y) {
		
		boolean flag = false;
		
		alpha.setEntry(j, 0, alpha.getEntry(j, 0) - y.getEntry(j, 0) * (Ei -Ej)/ETA);
		clipAlphaJ(j, bounds[1], bounds[0]);
		
		if (Math.abs(alpha.getEntry(j, 0) - alphaJold)  >= min_alpha_optization) {
			
			optimizeAlphaISameAlphaJOppositeDirection(i, j, alphaJold, y);
			
			flag = true;
		}
		
		return flag;		
		
	}
	
	public static void optimizeB(double Ei, double Ej, double alphaIold, double alphaJold, int i, int j, RealMatrix x, RealMatrix y) {
				
		
       double b1 = b - Ei - multiply(y.getRowMatrix(i), alpha.getRowMatrix(i).scalarAdd(- alphaIold)).
    		                multiply(x.getRowMatrix(i).multiply(x.getRowMatrix(i).transpose())).getEntry(0, 0)
    		              - multiply(y.getRowMatrix(j), alpha.getRowMatrix(j).scalarAdd(- alphaJold)).
    		                multiply(x.getRowMatrix(i).multiply(x.getRowMatrix(j).transpose())).getEntry(0, 0);
       
       double b2 = b - Ej - multiply(y.getRowMatrix(i), alpha.getRowMatrix(i).scalarAdd(- alphaIold)).
    		                multiply(x.getRowMatrix(i).multiply(x.getRowMatrix(j).transpose())).getEntry(0, 0)
    		              - multiply(y.getRowMatrix(j), alpha.getRowMatrix(j).scalarAdd(- alphaJold)).
    		                multiply(x.getRowMatrix(j).multiply(x.getRowMatrix(j).transpose())).getEntry(0, 0);
       
       if ( 0 < alpha.getRowMatrix(i).getEntry(0, 0) && c > alpha.getRowMatrix(i).getEntry(0, 0) ) b = b1;
       else if ( 0 < alpha.getRowMatrix(j).getEntry(0, 0) && c > alpha.getRowMatrix(j).getEntry(0, 0)) b = b2;
       else b =  (b1 + b2)/ 2.0;
		
	}
	
	public static void optimizeAlphaISameAlphaJOppositeDirection(int i, int j, double alphaJold, RealMatrix y) {
		
		alpha.setEntry(i, 0, alpha.getEntry(i, 0) + y.getEntry(j, 0) * y.getEntry(i, 0) * (alphaJold - alpha.getEntry(j, 0)));
		
	}
	
	
	public static void clipAlphaJ(int j, double highBound, double lowBound) {
		
		if(alpha.getEntry(j, 0)  < lowBound) alpha.setEntry(j, 0, lowBound);
		if(alpha.getEntry(j, 0)  > highBound) alpha.setEntry(j, 0, highBound);		
	}
	/******************************************************************************************/
	
	public static boolean checkIfAlphaViolatesKKT(double alpha, double e, double epsilon, double c) {
		
		return (alpha > 0 && Math.abs(e) < epsilon )|| (alpha < c  && Math.abs(e) > epsilon);
	}
	
	/******************************************************************************************/
	
	public static double[] boundAlpha(double alphaI, double alphaJ, double yI, double yJ) {
		
		double bounds[]  = new double [2];
		
		if(yI == yJ) {
			bounds[0] = Math.max(0, alphaJ + alphaI -c );
			bounds[1] = Math.min(c,  alphaJ + alphaI);
		}
		else {
			bounds[0] = Math.max(0, alphaJ - alphaI -c );
			bounds[1] = Math.min(c,  alphaJ - alphaI + c);
		}
		
		return bounds;
	}
	
	public static int selectIdxOf2ndAlphaToOpt(int idx1stalpha,int noOfRows ) {
		
		int idx2ndalpha = idx1stalpha;
		
		while (idx1stalpha == idx2ndalpha)  idx2ndalpha = ThreadLocalRandom.current().nextInt(0, noOfRows -1);
		
		return idx2ndalpha;
	}
	
	
	public static RealMatrix multiply(RealMatrix m1, RealMatrix m2) {
		
		double[][] returndata = new double[m1.getData().length][m1.getData()[0].length];
		
		IntStream.range(0, m1.getData().length).forEach(r -> 
				 IntStream.range(0, m1.getData()[0].length).forEach(c -> 
				  returndata[r][c] = m1.getEntry(r, c) * m2.getEntry(r, c)));
		
		return MatrixUtils.createRealMatrix(returndata);
	}
	

	public static double getB() {
		return b;
	}

	
	public static RealMatrix getAlpha() {
		return alpha;
	}

	
	public static RealMatrix getW() {
		return w;
	}
	

}
