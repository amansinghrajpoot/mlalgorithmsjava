package supportvectormachine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TFIDF {

	
	public static void generateTFIDF(ArrayList<String[]> arrlist, String[] wordlist) {
		
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
		double tf = 0.0;
		double idf = 0.0;
		
		Double[] vectors = null;
	//	ArrayList<Double[]> vectorslist = new ArrayList<Double[]>();
		
		
		vectors = new Double[wordlist.length + 2];
		
		while(it.hasNext()) {
			
			 
			 
			 data = it.next();
			
			 tweet = data[0];
		     try {
		    	
		    	 label = Double.parseDouble(data[1]);
		    	
		      }catch (Exception e) {
		    	
		    	label = 0.0;
			  }
		     
		      vectors[wordlist.length +1] = label;
		      
		       
		         for (int i = 0; i < wordlist.length ; i ++) {
		        	 
		        	 tf = TFIDF.calculateTF(tweet, wordlist[i]);
		        	 idf = TFIDF.calculateIDF(wordlist[i], arrlist);
		        	 
		        	 vectors[i] = tf ;// idf;
		        	 try {
						bw.append(vectors[i]+",");
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
	
    public static String[] getWords(ArrayList<String[]> arrlist) {
		
		File dictionary = new File (System.getProperty("user.dir")+"//"+ "dictionary");
//		FileWriter fw = null;
//		BufferedWriter bw = null;
		
		FileReader fr = null;
		BufferedReader br = null;
		String[] wordsetarray = null;
		
		try {
			fr = new FileReader(dictionary);
			br = new BufferedReader(fr);
			
		    wordsetarray = br.readLine().split(" ");
		} catch ( IOException e) {
			// TODO Auto-generated catch block
              System.out.println(e.getLocalizedMessage());
		}
		
		
		
		
//    	HashSet <String> wordset = new HashSet<String>();
//    	String[] strarr = null;
//    	String[] wordlist = null;
//    	String strbd = null;
//        
//    	Iterator<String[]> it = arrlist.iterator();
//    	
//    	while ( it.hasNext()) {
//    		
//    		strarr = (String[]) it.next();
//    		
//    		strbd = strarr[0];	
//    		
//    		wordlist = strbd.split(" ");
//    		    for( int i = 0; i < wordlist.length; i ++) {
//    		    	
//    		    	wordset.add(wordlist[i].toLowerCase());
//    		    }
//    		
//    	}
//    	
//    	strarr = null;
//    	wordlist= null;
//    	System.gc();
    	
//    	String[] wordsetarray = new String[wordset.size()];
//    	Iterator<String> wsit = wordset.iterator();
//    
//    	for ( int i = 0; i < wordset.size(); i ++) {
//    		
//    		wordsetarray[i] = wsit.next();
//    	}
    	
    	
//    	try {
//			fw = new FileWriter(dictionary);
//			bw = new BufferedWriter(fw);
//			for(int i = 0; i < wordsetarray.length; i ++) {
//				
//				bw.write(wordsetarray[i]);
//				if(i != wordsetarray.length) {
//					bw.newLine();
//				}
//				
//			}
//			System.out.println("Dictionary created");
//			bw.flush();
//	        fw.close();
//	        bw.close();
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			System.out.println(e.getLocalizedMessage());
//  		}
    	
    	return wordsetarray;
    }
	
	public static ArrayList<String[]> getData(){
		
		File trainingdata = new File(System.getProperty("user.dir")+"//" +"testdata" );
		ArrayList<String[]> al = new ArrayList<>();		
		try {
			FileReader fr = new FileReader(trainingdata);
			BufferedReader br = new BufferedReader(fr);
			ArrayList<String> allwords = null;
			String[] stopwords = {"a", "about","above", "therefore","midnight", "after", "again", "against", "ain", "all", "am", "an", "and", "any", "are", "aren", "arent", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can", "couldn", "couldnt", "d", "did", "didn", "didnt", "do", "does", "doesn", "doesnt", "doing", "don", "dont", "down", "during", "each", "few", "for", "from", "further", "had", "hadn", "hadnt", "has", "hasn", "hasnt", "have", "haven", "havent", "having", "he", "her", "here", "hers", "herself", "him", "himself", "his", "how", "i", "if", "in", "into", "is", "isn", "isnt", "it", "its", "its", "itself", "just", "ll", "m", "ma", "me", "mightn", "mightnt", "more", "most", "mustn", "mustnt", "my", "myself", "needn", "neednt", "no", "nor", "not", "now", "o", "of", "off", "on", "once", "only", "or", "other", "our", "ours", "ourselves", "out", "over", "own", "re", "s", "same", "shan", "shant", "she", "shes", "should", "shouldve", "shouldn", "shouldnt", "so", "some", "such", "t", "than", "that", "thatll", "the", "their", "theirs", "them", "themselves", "then", "there", "these", "they", "this", "those", "through", "to", "too", "under", "until", "up", "ve", "very", "was", "wasn", "wasnt", "we", "were", "weren", "werent", "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with", "won", "wont", "wouldn", "wouldnt", "y", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "could", "hed", "hell", "hes", "heres", "hows", "id", "ill", "im", "ive", "lets", "ought", "shed", "shell", "thats", "theres", "theyd", "theyll", "theyre", "theyve", "wed", "well", "were", "weve", "whats", "whens", "wheres", "whos", "whys", "would"};
			
			ArrayList<String> stoplist = new ArrayList<String>();
			for(int i = 0; i < stopwords.length; i ++) {
				stoplist.add(stopwords[i]);
			}
			
			String line = null;
			
			String[] dataandlabel = null;
	    	
			
			while ( (line = br.readLine()) != null) {
				
				dataandlabel = line.split(";");

				dataandlabel[0] = dataandlabel[0].replaceAll( "[^a-zA-Z ]", "").toLowerCase();
				dataandlabel[1] = dataandlabel[1].replaceAll( "[^0-9]", "");
				allwords =  Stream.of(dataandlabel[0].split(" ")).collect(Collectors.toCollection( ArrayList<String>::new) );
				allwords.removeAll(stoplist);
				dataandlabel[0] = allwords.stream().collect(Collectors.joining(" "));				
				al.add(dataandlabel);
				
			}
					
			br.close();
			fr.close();
			System.gc();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			System.out.println(e.getLocalizedMessage());
		}
		return al;		
	}
	
	private static double calculateTF(String tweet, String word) {
		
		double tf = 0.0;
		double wordcount = 0.0;
		double tfcount = 0.0;
		
		String[] tweetwords = tweet.split(" ");
		
		wordcount = (double) tweetwords.length;
		
		for(int i = 0; i < tweetwords.length ; i ++) {
			
			if( tweetwords[i].equals(word)) {
				tfcount = tfcount + 1.0;
			}
		 }
		
		tf = tfcount; ///wordcount;
		
		return tf;
	}
	
	private static double calculateIDF(String word, ArrayList<String[]> arrlist) {
		
		double idf = 0.0;
		double ND = 1.0;
		double NF = 1.0;
		
		Iterator<String[]> it = arrlist.iterator();
		String tweet = null;
		String[] tweetwords = null;

		ND = arrlist.size();
		
		while (it.hasNext()) {
			
			tweet = it.next()[0];
			
			tweetwords = tweet.split(" ");
			   for ( int i = 0; i < tweetwords.length; i ++ ) {
				   
				   if( tweetwords[i].equals(word)) {
					   
					   NF = NF + 1.0;
					   break;
				     }
			   }
			
			}
		
		idf = Math.log(1 +(ND/NF));
		
		return idf;
	}
}
