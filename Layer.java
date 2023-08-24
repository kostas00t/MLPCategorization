/*
 * KONSTANTINOS KIKIDIS (4387) 
 * CHRISTOS KROKIDAS (4399) 
 * KONSTANTINOS TSAMPIRAS (4508)
 */

 import java.util.Random;


public class Layer {

    private int level = 0;
    private String typeOfFunction;
    private Boolean outputLayer = false;

	private double[] weights;
	private double bias;
	private double[] input;
	private int numberOfInputs;
	private Random randomGenerator;
	private double error;	
	private double[] derivativeOfWeights;
	private double derivativeOfBias;
	
	
	public Layer(int numberOfInputs, int level, String typeOfFunction){

        this.level = level;
        this.typeOfFunction = typeOfFunction;
		
		this.numberOfInputs = numberOfInputs;
		this.weights = new double[numberOfInputs];
		this.bias = 0; // will be randomized later
		this.input = new double[numberOfInputs];
		
		this.derivativeOfWeights = new double[numberOfInputs];
		this.derivativeOfBias = 0;
		
		this.error = 0;
		
		this.randomGenerator = new Random();

		randomizeWeights();
		
	}

    public Layer(int numberOfInputs, int level, String typeOfFunction, Boolean outputLayer){

        this.level = level;
        this.typeOfFunction = typeOfFunction;
        this.outputLayer = outputLayer;
		
		this.numberOfInputs = numberOfInputs;
		this.weights = new double[numberOfInputs];
		this.bias = 0; // will be randomized later
		this.input = new double[numberOfInputs];
		
		this.derivativeOfWeights = new double[numberOfInputs];
		this.derivativeOfBias = 0;
		
		this.error = 0;
		
		this.randomGenerator = new Random();

		randomizeWeights();
		
	}
	
	public String getderivativeOfWeights() {
		
		String str = "";
		for(int i =0 ;i< derivativeOfWeights.length; i++) {
			str += derivativeOfWeights[i] + " ";
		}
		str += "\n";
		return str;
	}
	
	// we randomize in range [-1,1]
	private void randomizeWeights(){

		for (int i = 0; i < numberOfInputs ; i ++){
			weights[i] = 2 * randomGenerator.nextDouble() - 1;
		}
		bias = 2 * randomGenerator.nextDouble() - 1;
		
	}
	
	public void updateWeights(double eduRate) {
		
		for (int i = 0; i < numberOfInputs; i++) {
			weights[i] = weights[i] - eduRate * this.derivativeOfWeights[i];
		}
		this.bias = this.bias - eduRate * this.derivativeOfBias;
	}
	
	public void setInput(double[] input){
		
		this.input = input;
	}

    private double function(double x) {
		
		if(typeOfFunction.equals("relu")) {
			
			return Math.max(0,x);
			
		} else if (typeOfFunction.equals("tanh")){
			
			return ( Math.exp(x) - Math.exp(-x) ) / ( Math.exp(x) + Math.exp(-x) );

		} else if (typeOfFunction.equals("sigma")){

            return 1 / ( 1 + Math.exp(-x) ); 

        } else { //linear

            return x;
        }
	}
	
	public double derivative() {
		
		if(typeOfFunction.equals("relu")) {
			double dotProd = dotProduct();
			double out = function(dotProd);
			double tetr = Math.pow(out,2);
			double retur = 1 - tetr;
			return retur;
		
        } else if (typeOfFunction.equals("tanh")){
            double dotProd = dotProduct();
			double out = function(dotProd);
			double tetr = Math.pow(out,2);
			double retur = 1 - tetr;
			return retur;

        } else if (typeOfFunction.equals("sigma")){ 
        
            return function(dotProduct()) * (1 - function(dotProduct()));

        } else { //linear
			
			return 1;
		}
	}
	
	public double dotProduct(){
		double sum = 0;
		for(int i=0; i< numberOfInputs; i++){
			sum += weights[i] * input[i];
		}
		sum += bias;
		return sum;
	}

	public double getWeight(int i) {
		
		return weights[i];
	}
	
	public double getOutput(){
		
		return function(dotProduct());
	}
	
	public double getError() {

		return this.error;
	}
	
	public void setError(double error) {
		
		this.error = error;
	}
	
	public void calcDerivativeOfWeights() {
		
		for(int j=0; j< numberOfInputs; j++){
			this.derivativeOfWeights[j] += error * input[j];
		}
		calcDerivativeOfBias();
	}
	
	private void calcDerivativeOfBias() {
		
		this.derivativeOfBias += this.error;
	}
	
	public void clearVectorOfDerivatives() {

		for ( int i = 0; i < numberOfInputs; i++) {
			this.derivativeOfWeights[i] = 0;
		}
		this.derivativeOfBias = 0;
	}
	
	public String toString() {

		String returnstr = "";
        if (outputLayer) {
            returnstr += "Neuron of Final Layer: \n";
        } else {
            returnstr += "Neuron of Layer "+ level +": \n";
        }
		for(int i = 0; i < numberOfInputs; i++) {
			returnstr += weights[i]+" ";
		}
		returnstr += "bias: " + this.bias;
		returnstr += "\n";
		return returnstr;
	}
}
