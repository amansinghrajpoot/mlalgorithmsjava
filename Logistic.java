package supportvectormachine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Performs simple logistic regression.
 * User: tpeng
 * Date: 6/22/12
 * Time: 11:01 PM
 * 
 * @author tpeng
 * @author Matthieu Labas
 */
public class Logistic {

	/** the learning rate */
	private double rate;

	/** the weight to learn */
	private double[] weights;

	/** the number of iterations */
	private int ITERATIONS = 100000;

	public Logistic(int n) {
		this.rate = 0.0001;
		weights = new double[n];
	}

	private static double sigmoid(double z) {
		return 1.0 / (1.0 + Math.exp(-z));
	}

	public void train(List<Instance> instances) {
		for (int n=0; n<ITERATIONS; n++) {
			double lik = 0.0;
			for (int i=0; i<instances.size(); i++) {
				double[] x = instances.get(i).x;
				double predicted = classify(x);
				double label = instances.get(i).label;
				for (int j=0; j<weights.length; j++) {
					weights[j] = weights[j] + rate * (label - predicted) * x[j];
				}
				// not necessary for learning
				lik += label * Math.log(classify(x)) + (1-label) * Math.log(1- classify(x));
			}
			//System.out.println("iteration: " + n + " " + Arrays.toString(weights) + " mle: " + lik);
		}
		
		for(int i = 0 ; i < weights.length; i ++) {
			System.out.println(weights[i]);
		}
	}

	private double classify(double[] x) {
		double logit = .0;
		for (int i=0; i<weights.length;i++)  {
			logit += weights[i] * x[i];
		}
		return sigmoid(logit);
	}

	public static class Instance {
		public double label;
		public double[] x;

		public Instance(double label, double[] x) {
			this.label = label;
			this.x = x;
		}
	}

	public static List<Instance> readDataSet(String file) throws FileNotFoundException {
		List<Instance> dataset = new ArrayList<Instance>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(file));
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.startsWith("#")) {
					continue;
				}
				String[] columns = line.split(",");

				// skip first column and last column is the label
				int i = 0;
				double[] data = new double[columns.length-1];
				for (i= 0; i < columns.length-1; i++) {
					data[i] = Double.parseDouble(columns[i]);
				}
				double label = Double.parseDouble(columns[i]);
				Instance instance = new Instance(label, data);
				dataset.add(instance);
			}
		} finally {
			if (scanner != null)
				scanner.close();
		}
		return dataset;
	}
}
