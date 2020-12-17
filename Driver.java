package supportvectormachine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

import supportvectormachine.Logistic.Instance;

public class Driver {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
      
		File file = new File(System.getProperty("user.dir")+"//"+"TFIDFvectorsTraining");
		
		if( !file.exists()) {
		
		ArrayList<String[]> arrlist = TFIDF.getData();
		
		String[] wordlist = TFIDF.getWords(arrlist);
		
		//BOW.generate(arrlist, wordlist);
		
		//TFIDF.generateTFIDF(arrlist, wordlist);
		
		Hashing.hash(arrlist);
		}
		
		String str = System.getProperty("user.dir")+"//"+"TFIDFvectorsTraining";
		
		List<Instance> arr = Logistic.readDataSet(str);
		
		Logistic log = new Logistic(591);
		log.train(arr);
		
		/*******************************************************************************************/
		
//		ArrayList<double[][]> vectors = SVM.readData();
//		
//		SVM.prepare_and_fit(vectors);
//		
//		System.out.println(SVM.getB());
//		
//		RealMatrix w = SVM.getW();
//		
//        for(int i = 0 ; i < w.getRowDimension(); i ++) {
//        	  
//        	for( int j = 0; j < w.getColumnDimension(); j ++) {
//        		
//        		System.out.println(w.getEntry(i, j));
//        	}
//        }
//        
        
     //   System.out.println(SVM.classify(data));
		
	}

}
