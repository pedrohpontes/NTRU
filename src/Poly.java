package ntru;

public class Poly {
	
	private final int[] coeffs;
	private final int deg;
	
	
	public Poly(int[] coeffs1) {
		
		int len = coeffs1.length;
		
		int deg1 = len-1;
		
		while(deg1 > 0 && coeffs1[deg1] == 0)
			deg1--;
		
		deg = deg1;
		
		coeffs = new int[deg+1];
		
		for(int i = 0; i <= deg; i++)
			coeffs[i] = coeffs1[i];
	}
	
	
	public int getDeg() {
		return deg;
	}
	
	
	public int getCoeff(int i) {
		return coeffs[i];
	}
	
	
	public int[] getCoeffs() {
		int[] result = new int[deg+1];
		
		for(int i = 0; i <= deg; i++)
			result[i] = coeffs[i];
		
		return result;
	}
	
	
	public Poly add(Poly q) {
		
		int sumDeg = deg;
		int qDeg = q.getDeg();
		
		if(deg < qDeg)
			sumDeg = qDeg;
		
		int[] sumCoeffs = new int[sumDeg+1];
		
		for(int i = 0; i <= sumDeg; i++) {
			sumCoeffs[i] = 0;
			
			if(i <= deg)
				sumCoeffs[i] += coeffs[i];
			
			if(i <= qDeg)
				sumCoeffs[i] += q.getCoeff(i);
		}
		
		return new Poly(sumCoeffs);
	}
	
	
	public Poly negative() {
		int [] negCoeffs = new int[deg+1];
		
		for(int i = 0; i <= deg; i++)
			negCoeffs[i] = -coeffs[i];
		
		return new Poly(negCoeffs);
	}
	
	
	public Poly subtract(Poly q) {
		return this.add(q.negative());
	}
	
	
	public Poly multiply(Poly q) {
		
		int qDeg = q.getDeg();
		
		int newDeg = deg+qDeg;
		
		int[] multCoeffs = new int[newDeg+1];
		
		for(int k = 0; k <= newDeg; k++)
			multCoeffs[k] = 0;
		
		for(int i = 0; i <= deg; i++)
			for(int j = 0; j <= qDeg; j++)
				multCoeffs[i+j] += coeffs[i]*q.getCoeff(j);
		
		return new Poly(multCoeffs);
	}
	
	
	public static Poly monomial(int coef, int deg) {
		int[] coeff1 = new int[deg+1];
		
		for(int i = 0; i < deg; i++)
			coeff1[i] = 0;
		
		coeff1[deg] = coef;
		
		return new Poly(coeff1);
	}
	
	
	public Poly mod(int m) {
		int[] resCoeffs = new int[deg+1];
		
		for(int i = 0; i <= deg; i++)
			resCoeffs[i] = coeffs[i]%m;
		
		return new Poly(resCoeffs);
	}
	
	
	public Poly remainderMod(Poly divisor, int mod) {
		
		int divDeg = divisor.getDeg();
		
		if(deg < divDeg)
			return this;
		
		int k = deg - divDeg;
		int coef = (coeffs[deg]*Mod.inverse(divisor.getCoeff(divDeg), mod))%mod;
		
		Poly partQuot = monomial(coef, k);
		
		Poly newDividend = this.subtract( divisor.multiply(partQuot) );
		newDividend = newDividend.mod(mod);
		
		return newDividend.remainderMod(divisor, mod);
	}
	
	
	public static Poly zero() {
		int[] coef = {0};
		
		return new Poly(coef);
	}
	
	
	public static Poly one() {
		int[] coef = {1};
		
		return new Poly(coef);
	}
	
	
	public Poly[] divideMod(Poly divisor, int mod) {
		return this.divideMod(divisor, zero(), mod);
	}
	
	
	public Poly[] divideMod(Poly divisor, Poly currQuot, int mod) {
		
		int divDeg = divisor.getDeg();
		
		Poly[] result = new Poly[2];
		
		if(deg < divDeg) {
			 result[0] = this;
			 result[1] = currQuot;
			 
			 return result;
		}
		
		int k = deg - divDeg;
		int coef = (coeffs[deg] * Mod.inverse(divisor.getCoeff(divDeg), mod)) %mod;
		
		Poly partQuot = monomial(coef, k);
		
		Poly newDividend = this.subtract( divisor.multiply(partQuot) );
		newDividend = newDividend.mod(mod);
		
		currQuot = currQuot.add(partQuot);
		
		return newDividend.divideMod(divisor, currQuot, mod);
	}
	
	
	public boolean isZero() {
		if(deg == 0 && coeffs[0] == 0)
			return true;
		
		return false;
	}
	
	
	public boolean isOne() {
		if(deg == 0 && coeffs[0] == 1)
			return true;
		
		return false;
	}
	
	
	public Poly inverseModPoly(Poly pMod, int mod) {
		
		Poly remainder = this.remainderMod(pMod, mod);
		
		if(remainder.getDeg() == 0) {
			int[] inverse = new int[1];
			
			if( remainder.getCoeff(0)%mod != 0)
				inverse[0] = Mod.inverse(remainder.getCoeff(0), mod);
			else
				inverse[0] = 0;
			
			return new Poly(inverse);			 
		}
		
		
		Poly q = pMod.inverseModPoly(remainder, mod);
		
		Poly result = one().subtract(q.multiply(pMod));
		result = result.divideMod(remainder, mod)[1];
		
		return result;
	}
		
	
	public String toString() {
		
		String result = "" + coeffs[0];
		
		if(deg == 0)
			return result;
		
		result = result + " + ";
		
		for(int i = 1; i < deg; i++)
			result = result + String.format("%d*x^%d + ", coeffs[i], i);
		
		result = result + String.format("%d*x^%d", coeffs[deg], deg);
		
		return result;
	}
	
	
	public static Poly convertConvPoly(ConvPoly p) {
		return new Poly(p.getCoeffs());
	}
	

	
	public static void main(String[] args) {		
		
		int[] pCoeffs = {-1, 0, 1, -1, 0, 0, 1};
		int[] qCoeffs = {-1, 0, 0, 0, 0, 0, 0, 1};
		
		Poly p = new Poly(pCoeffs);
		Poly q = new Poly(qCoeffs);
		
		System.out.println(p);
		System.out.println(q);
		
		
		System.out.println(p.inverseModPoly(q, 41));
	}

}
