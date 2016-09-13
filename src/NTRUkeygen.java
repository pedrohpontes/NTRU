package ntru;

public class NTRUkeygen {
	
	private final int N;
	private final int p;
	private final int q;
	private final int d;
	private ConvPoly privKey;
	
	public NTRUkeygen(int N1, int p1, int q1, int d1) {
		N = N1;
		p = p1;
		q = q1;
		d = d1;
		privKey = null;
	}
	
	public NTRUkeygen(int N1, int p1, int q1, int d1, ConvPoly privKey1) {
		N = N1;
		p = p1;
		q = q1;
		d = d1;
		privKey = privKey1;
	}
	
	public ConvPoly genPrivKey() {
		ConvPoly privKey1 = null;
		
		ConvPoly invModP = ConvPoly.zero(N);
		ConvPoly invModQ = ConvPoly.zero(N);
		
		while(invModP.isZero() && invModQ.isZero()) {
			privKey1 = ConvPoly.randomT(N, d+1, d);
			
			invModP = privKey1.inverse(p);
			invModQ = privKey1.inverse(q);
		}
		
		privKey = privKey1;
		
		return privKey;		
	}
	
	public ConvPoly genPubKey() {
		
		if(privKey == null)
			throw new RuntimeException("Error: private key not set to generate public key.");
		
		
		ConvPoly g = ConvPoly.randomT(N, d, d);
		
		ConvPoly Fq = privKey.inverse(q);
		
		return g.multiply(Fq);		
	}
	
	public ConvPoly genPubKey(ConvPoly privKey1) {
		privKey = privKey1;
		
		return genPubKey();
	}
	
	
	public static void main(String[] args) {
		
		int N = 7;
		int p = 3;
		int q = 41;
		int d = 2;
		
		NTRUkeygen sys = new NTRUkeygen(N, p, q, d);
		
		
		System.out.println(sys.genPrivKey());
		
	}

}
