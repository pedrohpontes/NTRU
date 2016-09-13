package ntru;

import java.util.Random;

public class ConvPoly {
	
	private final int[] coeffs;
	private final int len; //so degree = len-1; or len = degree+1
	private final int N; //ring of polynomials modulo x^N-1
	
	public ConvPoly(int[] array, int degMod) {
		coeffs = reduceArrayMod(array, degMod);
		len = coeffs.length;
		
		N = degMod;		
	}
	
	
	private static int[] reduceArrayMod(int[] array, int mod) {
		
		int arrayLen = array.length;
		
		int[] newCoeffs = new int[arrayLen];
		
		for(int i = 0; i < arrayLen; i++) {
			newCoeffs[i] = 0;
			newCoeffs[i%mod] += array[i];
		}
		
		while(arrayLen > 1 && newCoeffs[arrayLen-1] == 0)
			arrayLen--;
		
		int[] result = new int[arrayLen];
		
		for(int i = 0; i < arrayLen; i++)
			result[i] = newCoeffs[i];
		
		return result;
	}
	
	
	public int getLen() {
		return len;
	}
	
	public int getN() {
		return N;
	}
	
	
	public int getCoeff(int i) {
		return coeffs[i];
	}
	
	
	public int[] getCoeffs() {
		int[] result = new int[len];
		
		for(int i = 0; i < len; i++)
			result[i] = coeffs[i];
		
		return result;
	}
	
	
	public ConvPoly add(ConvPoly q) {
		
		if(q.getN() != N)
			throw new RuntimeException("Error: trying to add polynomials in different convolution rings!");
		
		
		int sumLen = len;
		int qLen = q.getLen();
		
		if(len < qLen)
			sumLen = qLen;
		
		int[] sumCoeffs = new int[sumLen];
		
		for(int i = 0; i < sumLen; i++) {
			sumCoeffs[i] = 0;
			
			if(i < len)
				sumCoeffs[i] += coeffs[i];
			
			if(i < qLen)
				sumCoeffs[i] += q.getCoeff(i);
		}
		
		return new ConvPoly(sumCoeffs, N);
	}
	
	
	public ConvPoly multiply(ConvPoly q) {
		
		if(q.getN() != N)
			throw new RuntimeException("Error: trying to multiply polynomials in different convolution rings!");
		
		int qLen = q.getLen();
		
		int[] multCoeffs = new int[N];
		
		for(int k = 0; k < N; k++)
			multCoeffs[k] = 0;
		
		for(int i = 0; i < len; i++)
			for(int j = 0; j < qLen; j++)
				multCoeffs[(i+j)%N] += coeffs[i]*q.getCoeff(j);
		
		return new ConvPoly(multCoeffs, N);
	}
	
	
	public ConvPoly multiply(int n) {
		int[] multCoeffs = new int[len];
		
		for(int i = 0; i < len; i++)
			multCoeffs[i] = n*coeffs[i];
		
		return new ConvPoly(multCoeffs, N);
	}
			
	
	public ConvPoly mod(int m) {
		int[] resCoeffs = new int[len];
		
		for(int i = 0; i < len; i++)
			resCoeffs[i] = coeffs[i]%m;
		
		return new ConvPoly(resCoeffs, N);
	}
	
	
	public ConvPoly centerLift(int mod) {
		
		int[] liftCoeffs = new int[len];
		
		double halfMod = ((double) mod)/2;
		
		for(int i = 0; i < len; i++) {
			liftCoeffs[i] = coeffs[i]%mod;
			
			if(liftCoeffs[i] > halfMod)
				liftCoeffs[i] -= mod;
			
			if(liftCoeffs[i] <= -halfMod)
				liftCoeffs[i] += mod;
		}
		
		return new ConvPoly(liftCoeffs, N);		
	}
	
	
	public static ConvPoly randomT(int N, int d1, int d2) {
		
		if(d1 + d2 > N)
			throw new RuntimeException("Error: cannot produce random polynomial with so many coefficients in this ring.");
		
		int[] randCoeffs = new int[N];
		
		Random rand = new Random();
		
		int counter = 0;
		
		while(counter < d1) {
			int k = rand.nextInt(N);
			
			if(randCoeffs[k] == 0) {
				randCoeffs[k] = 1;
				counter++;
			}			
		}
		
		counter = 0;
		
		while(counter < d2) {
			int k = rand.nextInt(N);
			
			if(randCoeffs[k] == 0) {
				randCoeffs[k] = -1;
				counter++;
			}			
		}
				
		
		return new ConvPoly(randCoeffs, N);
	}
	
	
	public static ConvPoly convertPoly(Poly p, int N) {
		return new ConvPoly(p.getCoeffs(), N);
	}
	
	
	public ConvPoly inverse(int mod) {
		Poly poly = Poly.convertConvPoly(this);
		
		Poly pMod = (Poly.monomial(1, N)).subtract(Poly.one());
		
		Poly inverse = poly.inverseModPoly(pMod, mod);
		
		return convertPoly(inverse, N);
	}
	
	
	public static ConvPoly zero(int N) {
		int[] array = {0};
		
		return new ConvPoly(array, N);
	}
	
	
	public boolean isZero() {
		if(len == 1 && coeffs[0] == 0)
			return true;
		
		return false;
	}
	
	
	public String toString() {
				
		String result = "" + coeffs[0];
		
		if(len == 1)
			return result;
		
		result = result + " + ";
		
		for(int i = 1; i < len-1; i++)
			result = result + String.format("%d*x^%d + ", coeffs[i], i);
		
		result = result + String.format("%d*x^%d", coeffs[len-1], len-1);
		
		return result;
	}
	
	
	public static void main(String[] args) {
		
		int[] array = {-1, 0, 1, -1, 0, 0, 0, 0, 1, 0, 1};
		
		ConvPoly f = new ConvPoly(array, 11);
		
		System.out.println(f.inverse(73));
	}

}
