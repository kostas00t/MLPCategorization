/*
 * KONSTANTINOS KIKIDIS (4387) 
 * CHRISTOS KROKIDAS (4399) 
 * KONSTANTINOS TSAMPIRAS (4508)
 */

import java.util.Random;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.Math;

public class DataGen {
	public Cdata[] educationSet;
	public Cdata[] testSet;
	private Random randomgen;

	public DataGen(Boolean benchmarkMode) {
		
		educationSet = new Cdata[4000];    
		testSet = new Cdata[4000];
		randomgen  = new Random();
		
		createEducationSet(benchmarkMode);
		createTestSet();

	}
	
	private void createEducationSet(Boolean benchmarkMode) {
		
		double r1, r2;
		int C1counter = 0;
		int C2counter = 0;
		int C3counter = 0;
		
		for (int i = 0; i < 4000; i++) {
			r1 = 2*randomgen.nextDouble() - 1;
			r2 = 2*randomgen.nextDouble() - 1;
			if (inC1(r1,r2)) {
				educationSet[i] = new Cdata(r1,r2,"C1");
				C1counter ++;
			} else if (inC2(r1,r2)) {
				educationSet[i] = new Cdata(r1,r2,"C2");
				C2counter ++;
			} else {
				educationSet[i] = new Cdata(r1,r2,"C3");
				C3counter ++;
			}
		}
		if (!benchmarkMode) {
			System.out.println("In C1:" + C1counter);
			System.out.println("In C2:" + C2counter);
			System.out.println("In C3:" + C3counter);
		}
	}
	
	private void createTestSet() {
		
		double r1, r2;
		
		for(int i = 0; i < 4000; i++) {
			
			r1 = 2*randomgen.nextDouble() - 1;
			r2 = 2*randomgen.nextDouble() - 1;
            if(inC1(r1,r2)) {
				testSet[i] = new Cdata(r1,r2,"C1");
			}
			else if(inC2(r1,r2)) {
				testSet[i] = new Cdata(r1,r2,"C2");
			}
			else {
				testSet[i] = new Cdata(r1,r2,"C3");
			}
		}
	}
	
    private boolean inC1(double x1, double x2) {
		if((Math.pow((x1-0.5),2) + Math.pow((x2-0.5),2) < 0.2) && x2 > 0.5) {
			return true;
		}
		if((Math.pow((x1+0.5),2) + Math.pow((x2+0.5),2) < 0.2) && x2 > -0.5) {
			return true;
		}
		if((Math.pow((x1-0.5),2) + Math.pow((x2+0.5),2) < 0.2) && x2 > -0.5) {
			return true;
		}
		if((Math.pow((x1+0.5),2) + Math.pow((x2-0.5),2) < 0.2) && x2 > 0.5) {
			return true;
		}
		return false;
	}

	private boolean inC2(double x1, double x2) {
		if((Math.pow((x1-0.5),2) + Math.pow((x2-0.5),2) < 0.2) && x2 < 0.5) {
			return true;
		}
		if((Math.pow((x1+0.5),2) + Math.pow((x2+0.5),2) < 0.2) && x2 < -0.5) {
			return true;
		}
		if((Math.pow((x1-0.5),2) + Math.pow((x2+0.5),2) < 0.2) && x2 < -0.5) {
			return true;
		}
        if((Math.pow((x1+0.5),2) + Math.pow((x2-0.5),2) < 0.2) && x2 < 0.5) {
			return true;
		}
		
		return false;
	}
	
	public void writeToFile() {
		PrintWriter out = null;
		try
		{
		out = new PrintWriter(new FileOutputStream("Cdata.txt"));
		}
		catch(FileNotFoundException e)
		{
		System.out.println("Error opening the file Cdata.txt");
		System.exit(0);
		}
		for(int i = 0; i < 4000; i++) {
			out.println(educationSet[i]);
		}
		out.println("TEST");
		for(int i = 0; i < 4000; i++) {
			out.println(testSet[i]);
		}
		out.close();
	}
}
