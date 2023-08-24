/*
 * KONSTANTINOS KIKIDIS (4387) 
 * CHRISTOS KROKIDAS (4399) 
 * KONSTANTINOS TSAMPIRAS (4508)
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MLP {

	public static Boolean benchmarkMode = false;
	
	public static final int d = 2; 
	public static final int K = 3;
	public static int H1;
	public static int H2;
	public static int H3;
	public static double EDU_RATE;
    public static String typeOfFunctionInH1;
	public static String typeOfFunctionInH2;
	public static String typeOfFunctionInH3;
	public static String typeOfFunctionInOutput;
	public static int B;
	private Cdata[] educationSet;
	private Cdata[] testSet;
	private Layer[] neuronsH1;
	private Layer[] neuronsH2;
	private Layer[] neuronsH3;
	private Layer[] neuronsOut;
	
	// sum of errors of each education example in one epoch
	private double squaredErrorOfEpoch; 
	private double lastEpochError;
	
	
	//for testing the network
	private int correctExamples = 0;
	private int wrongExamples = 0;
	
	private ArrayList<Cdata> correctList = new ArrayList<Cdata>(); 
	private ArrayList<Cdata> wrongList = new ArrayList<Cdata>();
	
	private int numOfExamplesC1 = 0;
	private int numOfExamplesC2 = 0;
	private int numOfExamplesC3 = 0;
	private int numOfExamplesNOCATEGORY =0;
	
	// d = number of inputs (in H1)
	// K = num of category
	// H1 = num of neurons in H1
	// H2 = num of neurons in H2
	// H3 = num of neurons in H3
    // String typeOfFunction in H1
	// String typeOfFunction in H2
	// String typeOfFunction in H3
	// EDU_RATE = education rate//printErrorOfEpoch();

	// B = size of batch
	
	/*
	 * Custom MLP
	 */
	public MLP(String[] args) {
		H1 = Integer.parseInt(args[0]);
		H2 = Integer.parseInt(args[1]);
		H3 = Integer.parseInt(args[2]);
		EDU_RATE = Double.parseDouble(args[3]);
		typeOfFunctionInH1 = args[4];
		typeOfFunctionInH2 = args[5];
		typeOfFunctionInH3 = args[6];
		typeOfFunctionInOutput = args[7];
		B = Integer.parseInt(args[8]);

		educationSet = new Cdata[4000];
		testSet = new Cdata[4000];
		
		this.squaredErrorOfEpoch = 0;
		this.lastEpochError = 0;
		setUpNetwork();
	}

	/*
	 * Default MLP
	 */
	public MLP() {
		H1 = 10;
		H2 = 9;
		H3 = 8;
		EDU_RATE = 0.01;
		typeOfFunctionInH1 = "sigma";
		typeOfFunctionInH2 = "tanh";
		typeOfFunctionInH3 = "tanh";
		typeOfFunctionInOutput = "sigma";
		B = 400;

		educationSet = new Cdata[4000];
		testSet = new Cdata[4000];
		
		this.squaredErrorOfEpoch = 0;
		this.lastEpochError = 0;
		setUpNetwork();
	}
	
	/*
	 * Initialize MLP
	 */
	public void setUpNetwork() {
		if (!MLP.benchmarkMode) {
			System.out.println("---Initializing MLP Network---");
		}
		initHiddenLayer1(H1,d);
		initHiddenLayer2(H2,H1);
		initHiddenLayer3(H3,H2);
		initOutputLayer(K,H3);
	}
	
	/*
	 * Initialize 1st Hidden Layer
	 */
	private void initHiddenLayer1(int numOfH1Neurons, int numOfInputs) {
		
		neuronsH1 = new Layer[numOfH1Neurons];
		for (int i = 0; i < numOfH1Neurons; i++) {
			neuronsH1[i] = new Layer(numOfInputs, 1, typeOfFunctionInH1);
		}
		if (!MLP.benchmarkMode) {
			System.out.println("Hidden Layer H1: " + numOfH1Neurons + " neurons, " + numOfInputs + " inputs.");
		}
	}
	
	/*
	 * Initialize 2nd Hidden Layer
	 */
	private void initHiddenLayer2(int numOfH2Neurons, int numOfInputs) {
		neuronsH2 = new Layer[numOfH2Neurons];
		for (int i = 0; i < numOfH2Neurons; i++) {
			neuronsH2[i] = new Layer(numOfInputs, 2, typeOfFunctionInH2);
		}
		if (!MLP.benchmarkMode) {
			System.out.println("Hidden Layer H2: " + numOfH2Neurons + " neurons, " + numOfInputs + " inputs.");
		}
	}
	
	/*
	 * Initialize 3rd Hidden Layer
	 */
	private void initHiddenLayer3(int numOfH3Neurons, int numOfInputs) {
		neuronsH3 = new Layer[numOfH3Neurons];
		for (int i = 0; i < numOfH3Neurons; i++) {
			neuronsH3[i] = new Layer(numOfInputs, 3, typeOfFunctionInH3);
		}
		if (!MLP.benchmarkMode) {
			System.out.println("Hidden Layer H3: " + numOfH3Neurons + " neurons, " + numOfInputs + " inputs.");
		}
	}
	
	/*
	 * Initialize Output Layer
	 */
	private void initOutputLayer(int numOfNeurons, int numOfInputs) {
		neuronsOut = new Layer[numOfNeurons];
		for (int i = 0; i < numOfNeurons; i++) {
			neuronsOut[i] = new Layer(numOfInputs, 4, typeOfFunctionInOutput, true);
		}
		if (!MLP.benchmarkMode) {
			System.out.println("Final Layer: " + numOfNeurons + " neurons, " + numOfInputs + " inputs.");
		}
	}
	
	/*
	 * Loads the data generated from DataGen
	 */
	public void loadCdataFromFile(String filename) {
		Scanner inputReader = null;
		try
		{
			inputReader = new Scanner(new FileInputStream(filename));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File "+ filename + " was not found");
			System.out.println("or could not be opened.");
			System.exit(0);
		}
		
		int i = 0;
		while (inputReader.hasNextLine()) {
			String line = inputReader.nextLine();
			if(line.equals("TEST")) {
				break;
			}
			String[] lineData = line.split(",");	
			educationSet[i] = new Cdata(Double.parseDouble(lineData[0]), Double.parseDouble(lineData[1]), lineData[2]);
			i++;
		}
		i = 0;
		while (inputReader.hasNextLine()) {
			String line = inputReader.nextLine();
			String[] lineData = line.split(",");
			Cdata data = new Cdata(Double.parseDouble(lineData[0]), Double.parseDouble(lineData[1]), lineData[2]);
			testSet[i] = data;
			i++;
		}
		
		inputReader.close();
	}
	
	/*
	 * Forward Pass, get the output of each layer
	 */
	public double[] forwardPass(double[] input) {
		
		double[] outputH1 = new double[H1];
		double[] outputH2 = new double[H2];
		double[] outputH3 = new double[H3];
		double[] networkOutput = new double[K];
		
		for (int i = 0; i < H1; i++) {
			neuronsH1[i].setInput(input);
			outputH1[i] = neuronsH1[i].getOutput();
		}
		
		for (int i = 0; i < H2; i++) {
			neuronsH2[i].setInput(outputH1);
			outputH2[i] = neuronsH2[i].getOutput();
		}

		for (int i = 0; i < H3; i++) {
			neuronsH3[i].setInput(outputH2);
			outputH3[i] = neuronsH3[i].getOutput();
		}
		
		for (int i = 0; i < K; i++) {
			neuronsOut[i].setInput(outputH3);
			networkOutput[i] = neuronsOut[i].getOutput();
		}
		
		return networkOutput;
	}	
	
	/*
	 * Backpropagation, adjust weights and biases
	 */
	public void backprop(Cdata example) {
		
		double[] input  = example.toVectorNoBias();
		double[] networkOut = forwardPass(input);
		double[] difference = calculateDiffWithExpected(networkOut, example.getC());

		this.squaredErrorOfEpoch += calculateSquaredErrorOfExample(networkOut, example.getC());
		
		for(int i = 0; i < K; i++) {
			neuronsOut[i].setError(difference[i] * neuronsOut[i].derivative());
			neuronsOut[i].calcDerivativeOfWeights();
		}

		for (int i = 0; i < H3; i++) {
			double dotProdWeightsError = 0;
			for (int j = 0; j < K; j++) {
				double weightji = neuronsOut[j].getWeight(i);
				double errorj = neuronsOut[j].getError();
				dotProdWeightsError += weightji * errorj;
			}
			neuronsH3[i].setError(dotProdWeightsError * neuronsH3[i].derivative()); 
			neuronsH3[i].calcDerivativeOfWeights();
		}
			
		for (int i = 0; i < H2; i++) {
			double dotProdWeightsError = 0;
			for (int j = 0; j < H3; j++) {
				double weightji = neuronsH3[j].getWeight(i);
				double errorj = neuronsH3[j].getError();
				dotProdWeightsError += weightji * errorj;
			}
			neuronsH2[i].setError(dotProdWeightsError * neuronsH2[i].derivative()); 
			neuronsH2[i].calcDerivativeOfWeights();
		}

		for (int i = 0; i < H1; i++) {
			double dotProdWeightsError = 0;
			for (int j = 0; j < H2; j++) {
				double weightji = neuronsH2[j].getWeight(i);
				double errorj = neuronsH2[j].getError();
				dotProdWeightsError += weightji * errorj;
			}
			neuronsH1[i].setError(dotProdWeightsError * neuronsH1[i].derivative());
			neuronsH1[i].calcDerivativeOfWeights();
		}	
	}
	
	/*
	 * Calculate the difference from the expected output - category - 
	 * and the output of forward pass from the network
	 */
	public double[] calculateDiffWithExpected(double[] networkOutput, String exampleCategory) {
		
		double[] expectedOut = new double[K];
		switch(exampleCategory) { 
			case "C1": 
				expectedOut = new double[] {1,0,0};
				break; 
			case "C2": 
				expectedOut = new double[] {0,1,0};
				break; 
			case "C3": 
				expectedOut = new double[] {0,0,1};
				break; 
		}
		double[] diff = new double[K];
		
		for(int i = 0; i < K; i++) {
			diff[i] =  networkOutput[i] - expectedOut[i];
		}
	
		return diff;
	}

	/*
	 * Calculate the error from ONLY one example, call it after forward pass, 
	 * so that we dont pass the network one extra time. 
	 */
	public double calculateSquaredErrorOfExample(double[] networkOutput, String exampleCategory) {
		
		double[] expectedOut = new double[K];
		switch(exampleCategory) { 
			case "C1": 
				expectedOut = new double[] {1,0,0};
				break; 
			case "C2": 
				expectedOut = new double[] {0,1,0};
				break; 
			case "C3": 
				expectedOut = new double[] {0,0,1};
				break; 
		}
		double sum = 0;
		for(int i = 0; i < K; i++) {
			sum +=  Math.pow(Math.abs((networkOutput[i] - expectedOut[i])), 2);
		}
		double error = 0.5 * sum;
		
		return error;
	}

	/*
	 * Set squaredErrorOfEpoch to 0
	 */
	private void clearErrorOfEpoch() {
		this.squaredErrorOfEpoch = 0;
	}
	
	/*
	 * Print squaredErrorOfEpoch
	 */
	public void printErrorOfEpoch() {
		System.out.println("Squared Error: " + this.squaredErrorOfEpoch);
	}
	
	/*
	 * Calculate error between 2 epochs
	 */
	public double calcErrorBetween2Epochs() {
		double epochDiff = this.lastEpochError - this.squaredErrorOfEpoch;
		this.lastEpochError = this.squaredErrorOfEpoch;
		return epochDiff;
	}
	
	/*
	 * Update all weights of the network
	 */
	public void updateAllWeights() {
		for(int i = 0; i < H1; i++) {
			neuronsH1[i].updateWeights(EDU_RATE);
		}
		for(int i = 0; i < H2; i++) {
			neuronsH2[i].updateWeights(EDU_RATE);
		}
		for(int i = 0; i < H3; i++) {
			neuronsH3[i].updateWeights(EDU_RATE);
		}
		for(int i = 0; i < K; i++) {
			neuronsOut[i].updateWeights(EDU_RATE);
		}
	}
	
	/*
	 * Set derivatives vectors to 0
	 */
	public void clearAllDerivatives() {
		for(int i = 0; i < H1; i++) {
			neuronsH1[i].clearVectorOfDerivatives();
		}
		for(int i = 0; i < H2; i++) {
			neuronsH2[i].clearVectorOfDerivatives();
		}
		for(int i = 0; i < H3; i++) {
			neuronsH3[i].clearVectorOfDerivatives();
		}
		for(int i = 0; i < K; i++) {
			neuronsOut[i].clearVectorOfDerivatives();
		}
	}
	
	/*
	 * Network Training, in B sized batches
	 * runs for at least 700 epochs 
	 */
	public void educate() {

		// Could be used for plotting how the sqerror falls with each epoch
		//ArrayList<Integer> x_epoch = new ArrayList<Integer>();
		//ArrayList<Double> y_sqError = new ArrayList<Double>();
		
		int epoch = 0;
		do {
			if (!MLP.benchmarkMode) {
				System.out.print("---Epoch: " + epoch + "        ");	
			}		
			clearErrorOfEpoch();
			int counterOfBatches = 0;
			for(int i = 0; i < educationSet.length; i+=B) {
				counterOfBatches ++;
				clearAllDerivatives();
				for (int j = i; j < counterOfBatches*B; j++) {
					backprop(educationSet[j]);
				}
				updateAllWeights();
			}

			if (!MLP.benchmarkMode) {
				printErrorOfEpoch();
			}

			//x_epoch.add(epoch);
			//y_sqError.add(this.squaredErrorOfEpoch);

			epoch ++;
		} while ((epoch < 700 || Math.abs(calcErrorBetween2Epochs()) > 0.01) && epoch < 10000);		
	}
	
	/*
	 * Test network performance after training
	 */
	public void testNetwork() {
		
		for (int i = 0; i < testSet.length; i++) {
			double[] input = testSet[i].toVectorNoBias();
			double[] netOut = forwardPass(input);
			compareOutandCorrect(netOut,testSet[i]);
		}

		if (!MLP.benchmarkMode) {
			Plot p = new Plot(correctList,wrongList);
			p.setVisible(true);
		}
		printTestStatistics();
		
	}
	
	/*
	 * Compare actual position and network output
	 */
	private void compareOutandCorrect(double[] netOut, Cdata example) {
		
		// Find maximum of the networks output 
		double max = -100000000.000; // Smallest value
		int maxIndex = 0;
		for(int i = 0; i < K; i++) {
			if(netOut[i] > max) {
				max = netOut[i];
				maxIndex = i;
			}
		}
		String category = example.getC();
		//Add it to category & compare if its actually correct
		switch(maxIndex) {
			case 0: // {1,0,0} C1
				this.numOfExamplesC1 ++;
				if(category.equals("C1")) {
					this.correctExamples ++;
					this.correctList.add(example);
				} else {
					this.wrongExamples ++;
					this.wrongList.add(example);
				}
				break;
				
			case 1: // {0,1,0} C2
				this.numOfExamplesC2 ++;
				if(category.equals("C2")) {
					this.correctExamples ++;
					this.correctList.add(example);
				} else {
					this.wrongExamples ++;
					this.wrongList.add(example);
				}
				break;
				
			case 2: // {0,0,1} C3
				this.numOfExamplesC3 ++;
				if(category.equals("C3")) {
					this.correctExamples ++;
					this.correctList.add(example);
				} else {
					this.wrongExamples ++;
					this.wrongList.add(example);
				}
				break;
		}
	}
	
	/*
	 * Print statistics after run
	 */
	private void printTestStatistics() {
		
		if (!MLP.benchmarkMode) {
			System.out.println("---After Test Set---");
			System.out.println("Test Set size: "+ testSet.length);
			System.out.println("Correctly Categorized: " + this.correctExamples);
			System.out.println("Wrongly Categorized: " + this.wrongExamples);
			
			double percentage = (this.correctExamples / (double)testSet.length) * 100;
			System.out.print("Percentage: " + String.format("%.5g", percentage) + "% correct -- " + String.format("%.5g",(100-percentage)) + "% wrong\n");
			
			System.out.println("--------------------");
			System.out.println("In C1: " + this.numOfExamplesC1);
			System.out.println("In C2: " + this.numOfExamplesC2);
			System.out.println("In C3: " + this.numOfExamplesC3);
			System.out.println("In NOCategory: " + this.numOfExamplesNOCATEGORY);
			System.out.println("---Actual---");
			int numOfActualC1 = 0;
			int numOfActualC2 = 0;
			int numOfActualC3 = 0;
			for (int i = 0; i < testSet.length; i++) {
				switch(testSet[i].getC()) { 
					case "C1": 
						numOfActualC1 ++;
						break; 
					case "C2": 
						numOfActualC2 ++;
						break; 
					case "C3": 
						numOfActualC3 ++;
						break; 
				}
			}
			System.out.println("In C1: " + numOfActualC1);
			System.out.println("In C2: " + numOfActualC2);
			System.out.println("In C3: " + numOfActualC3);
		
		} else { 
			double percentage = (this.correctExamples / (double)testSet.length) * 100;
			System.out.print("Percentage: " + String.format("%.5g", percentage) + "% correct -- " + String.format("%.5g",(100-percentage)) + "% wrong");
		}
	}
	
	/*
	 * For batch testing with different parameters
	 */
	public static void benchmark(MLP network) {

		String[][] benchmark = {{"10","9","8","0.01","sigma","tanh","tanh","sigma","400"},
								{"10","9","8","0.01","sigma","tanh","tanh","sigma","1"},
								{"10","9","8","0.01","tanh","sigma","sigma","linear","4000"},
								{"10","9","8","0.01","tanh","sigma","tanh","sigma","40"},
								{"10","9","8","0.01","sigma","relu","tanh","sigma","400"},
								{"6","5","3","0.01","sigma","tanh","tanh","sigma","400"},
								{"6","5","3","0.01","tanh","tanh","sigma","sigma","400"},
								{"6","5","3","0.01","tanh","sigma","tanh","sigma","400"},
								{"6","5","3","0.01","sigma","tanh","sigma","linear","400"},
								{"7","6","5","0.01","tanh","relu","relu","sigma","400"},
								{"7","6","5","0.01","tanh","sigma","tanh","linear","400"},
								{"7","6","5","0.01","sigma","sigma","tanh","linear","400"},
								{"8","8","8","0.01","tanh","tanh","sigma","sigma","400"},
								{"8","8","8","0.01","sigma","tanh","tanh","sigma","400"},
								{"8","7","6","0.01","tanh","sigma","tanh","sigma","400"},
								{"8","7","6","0.01","sigma","tanh","tanh","sigma","400"},
								{"8","8","8","0.01","tanh","tanh","sigma","sigma","40"},
								{"10","9","8","0.01","sigma","relu","tanh","sigma","40"},
								{"6","5","3","0.01","sigma","tanh","tanh","sigma","40"},
								{"6","5","3","0.01","tanh","tanh","sigma","sigma","40"},
								{"6","5","3","0.01","tanh","sigma","tanh","sigma","40"},
								{"6","5","3","0.01","sigma","tanh","sigma","linear","40"},
								{"7","6","5","0.01","tanh","relu","relu","sigma","40"},
								{"7","6","5","0.01","tanh","sigma","tanh","linear","40"},
								{"7","6","5","0.01","sigma","sigma","tanh","linear","40"},
								{"8","8","8","0.01","tanh","tanh","sigma","sigma","40"},
								{"8","8","8","0.01","sigma","tanh","tanh","sigma","40"},
								{"8","7","6","0.01","tanh","sigma","tanh","sigma","40"},
								{"8","7","6","0.01","sigma","tanh","tanh","sigma","40"},
								{"10","10","10","0.01","sigma","tanh","tanh","sigma","400"}
							};

		int idx = 1;
		for (String[] benchmarkArgs : benchmark) {
			System.out.print(idx + ")\t");
			network = new MLP(benchmarkArgs);
			network.loadCdataFromFile("Cdata.txt");
			network.educate();
			network.testNetwork();
			System.out.print("\tBenchmark Set (");
			for (String x: benchmarkArgs) {
				System.out.print(x + " ");
			}
			System.out.println("\b)");
			idx++;
		}
	}

	/*
	 * Start the MLP 
	 */
	public static void startMLP(MLP network, String executionType, String[] args) {
		if (executionType.equals("Default")) {
			network = new MLP();
			network.loadCdataFromFile("Cdata.txt");
			network.educate();
			network.testNetwork();
			System.out.print("Defaults (10 9 8 0.01 sigma tanh tanh sigma 400)");
		} else if (executionType.equals("Custom")) {	
			network = new MLP(args);
			network.loadCdataFromFile("Cdata.txt");
			network.educate();
			network.testNetwork();
			System.out.print("Custom (");
			for (String i: args) {
				System.out.print(i + " ");
			}
			System.out.print("\b)");
		} else {
			benchmark(network);
		}

	}


	public static void main(String[] args) {
		
		benchmarkMode = false;   // for print/plot suppresion on benchmark mode

		if (args.length == 1 && args[0].equals("benchmark")) {
			benchmarkMode = true;
		}

		DataGen generator = new DataGen(benchmarkMode);
		generator.writeToFile();
		
		/*
		 * Run with default parameters: 	java MLP
		 * Run with custom parameters: 		java MLP 3 4 5 0.01 sigma tanh tanh sigma 400
		 * Run benchmark: 					java MLP benchmark
		 */
	
		MLP network = null;
		if (args.length == 0) {	
			System.out.println("Default Mode");
			MLP.startMLP(network, "Default", args);
		} else if (args[0].equals("benchmark")) {
			System.out.println("Benchmark Mode");
			MLP.startMLP(network, "Benchmark", args);
		} else {
			System.out.println("Custom Mode");
			MLP.startMLP(network, "Custom", args);
		}
	}
}