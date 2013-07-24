
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class KNearestNeighbour {

	//private int k = 1;		//Nearest Neighbour
	//private int k = 3;		//k-Nearest Neighbour Method at k=3
	private int k =9;		//k-Nearest Neighbour Method at k=5

	private List<Vector_Class> trainingResults;
	private List<Vector_Class> testCases;

	// Max and min for each field used for range for vectors
	private float min1=Float.MAX_VALUE, min2=Float.MAX_VALUE, min3=Float.MAX_VALUE, min4=Float.MAX_VALUE;
	private float max1=Float.MIN_VALUE, max2=Float.MIN_VALUE, max3=Float.MIN_VALUE, max4=Float.MIN_VALUE;

	/**
	 *
	 */
	private void train(File trainingFile) {
		trainingResults = new ArrayList<Vector_Class>();
		try {
			Scanner sc = new Scanner(trainingFile);
			String type = null;

			int i = 0;
			float[] vals = new float[4];
			Vector_Class vec;

			while (sc.hasNext()){
				if (sc.hasNextFloat()){
					float val = sc.nextFloat();
					limit(i, val);
					vals[i++] = val;
					i = i % 4;
				}

				if (i == 0) {
					type = sc.next();
					vec = new Vector_Class(type, vals);
					trainingResults.add(vec);
					vals = new float[4];
				}
			}
		} catch (FileNotFoundException e) { System.out.println("404: File not found"); System.exit(-1);	}
		/*
		for(Vector_Class vc: trainingResults)
			vc.printToScreen();
		/**/
	}

	private void loadTests(File testFile) {
		testCases = new ArrayList<Vector_Class>();
		try {
			Scanner sc = new Scanner(testFile);
			String type = null;

			int i = 0;
			float[] vals = new float[4];
			Vector_Class vec;

			while (sc.hasNext()){
				if(sc.hasNextFloat()){
					float val = sc.nextFloat();
					limit(i, val);
					vals[i++] = val;
					i = i % 4;
				}

				if(i == 0) {
					type = sc.next();
					vec = new Vector_Class(type, vals);
					testCases.add(vec);
					vals = new float[4];
				}
			}
		} catch (FileNotFoundException e) {	System.out.println("404: File not found"); System.exit(-1);	}
		test();
	}


	private void test(){
		Vector_Class[] close;
		float[] closeDis;
		int longD = 0;
		int i = 0;	// counter to k
		float d;	// distance between training case and test case

		float[] range = getRange();

		System.out.println(testCases.size());
		for(Vector_Class tc: testCases){
			// initilize variables for each test case
			close = new Vector_Class[k];
			closeDis = new float[k];
			longD = 0;
			i = 0;	// counter to k

			for(Vector_Class tr: trainingResults){
				d = calDistance(tc, tr, range);
				//System.out.println(d);
				if(i < k) {
					close[i] = tr;
					closeDis[i] = d;
					if(d > closeDis[longD]) longD = i;	// store the furtherest distance to test
				} else if (d < closeDis[longD]){	// if new closer value
					close[longD] = tr;				// store new training value
					closeDis[longD] = d;			// and distance
					// then find which training value distance is longest
					for (int j = 0; j < closeDis.length; j++)
						if (closeDis[j] > closeDis[longD]) longD = j;
				}
				i++;
			}

			/**/
			String guess = determineType(close);
			/**/
			String match;
			if(guess.equalsIgnoreCase(tc.getClassification())) match = "match";
			else match = "mismatch";
			System.out.println("guess: "+ guess+"\tAnswer: "+tc.getClassification()+"\t-\t"+match);
			/**/
		}
	}

	private String determineType(Vector_Class[] close) {
		int[] count = new int[3];
		String type = null;
		//System.out.print("Closest "+k+" casses:\t");
		for(int i = 0; i < close.length; i++){
			type = close[i].getClassification();
			//System.out.print(type+"\t");
			if(type.equalsIgnoreCase("Iris-setosa")) count[0]++;
			else if (type.equalsIgnoreCase("Iris-versicolor")) count[1]++;
			else if (type.equalsIgnoreCase("Iris-virginica")) count[2]++;
		}
		//System.out.println();
		int winnerIndex = 0;
		int winnerCount = 0;
		for(int i = 0; i < count.length; i++){
			if(count[i] > winnerCount) {
				winnerIndex = i;
				winnerCount = count[i];
			}
		}
		switch (winnerIndex) {
		case 0:
			return "Iris-setosa";
		case 1:
			return "Iris-versicolor";
		case 2:
			return "Iris-virginica";
		default:
			return null;
		}
	}

	private float calDistance(Vector_Class tc, Vector_Class tr, float[] range) {
		float[] vecTC = tc.getValues();
		float[] vecTR = tr.getValues();
		double ans = 0;
		float ele = 0;
		for(int i = 0; i < range.length; i++){
			ele = vecTC[i];
			ele -= vecTR[i];
			ele *= ele;
			ele /= range[i];
			ans += ele;
		}
		ans = Math.sqrt(ans);
		return (float) ans;
	}

	private void limit(int i, float val) {
		switch(i){
		case 0:
			if(val < min1) min1 = val;
			if(val > max1) max1 = val;
			return;
		case 1:
			if(val < min2) min2 = val;
			if(val > max2) max2 = val;
			return;
		case 2:
			if(val < min3) min3 = val;
			if(val > max3) max3 = val;
			return;
		case 3:
			if(val < min4) min4 = val;
			if(val > max4) max4 = val;
			return;
		default:
			return;
		}
	}

	private float[] getRange() {
		float[] r = new float[4];
		r[0] = max1 - min1;
		r[1] = max2 - min2;
		r[2] = max3 - min3;
		r[3] = max4 - min4;
		return r;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// test if the correct number of arguments have been given
		if(args.length != 2) {
			System.out.println("Incorrect number of files given.\nRequires two files:\n" +
					"First: training file\nSecond: test file");
			System.exit(-1);
		}
		// test that the filenames given are actually files
		File training = new File(args[0]);
		if(!training.exists()){
			System.out.println("Training filepath invalid");
			System.exit(-1);
		}
		File test = new File(args[1]);
		if(!test.exists()){
			System.out.println("Test filepath invalid");
			System.exit(-1);
		}

		KNearestNeighbour kNN = new KNearestNeighbour();
		kNN.train(training);
		kNN.loadTests(test);

	}

	//TODO print to file results method

}
