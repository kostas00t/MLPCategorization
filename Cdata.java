/*
 * KONSTANTINOS KIKIDIS (4387) 
 * CHRISTOS KROKIDAS (4399) 
 * KONSTANTINOS TSAMPIRAS (4508)
 */

public class Cdata {

	private double x1;
	private double x2;
	private String C;
	

	public Cdata(double x1, double x2, String C) {
		this.x1 = x1;
		this.x2 = x2;
		this.C = C;
	}
	

	public double getX1() {
		return x1;
	}

	public double getX2() {
		return x2;
	}

	public String getC() {
		return C;
	}


	public void setX1(double x1) {
		this.x1 = x1;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public void setC(String c) {
		C = c;
	}


	public double[] toVector() {
		double[] vec = {1, x1, x2};
		return vec;
	}
	
	public double[] toVectorNoBias(){
		double[] vec = {x1, x2};
		return vec;
	}
	

	public String toString() {
		return (x1+","+x2+","+C);
	}
	
}
