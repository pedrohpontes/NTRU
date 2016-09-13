package ntru;

public class NTRUencrypt {
	
	private final int p;
	private final int q;
	private final int d;
	private final ConvPoly pubKey;
	
	public NTRUencrypt(int p1, int q1, int d1, ConvPoly pubKey1) {
		p = p1;
		q = q1;
		d = d1;
		pubKey = pubKey1;
	}
	
	
	public ConvPoly encrypt(ConvPoly plaintext) {
		int N = plaintext.getN();
		
		return encrypt(plaintext, ConvPoly.randomT(N,d,d));
	}
	
	
	public ConvPoly encrypt(ConvPoly plaintext, ConvPoly ephKey) {
		return ((pubKey.multiply(ephKey).multiply(p)).add(plaintext)).mod(q);
	}
	

	public static void main(String[] args) {
		
		int N = 7;
		
		int[] pubKeyCoeffs = {30, 26, 8, 38, 2, 40, 20};
		
		ConvPoly pubKey = new ConvPoly(pubKeyCoeffs, N);
		
		NTRUencrypt sys = new NTRUencrypt(3,41,2, pubKey);
		
		int[] ephKeyCoeffs = {-1, 1, 0, 0, 0, -1, 1};
		int[] messCoeffs = {1, -1, 1, 1, 0, -1};
		
		ConvPoly plaintext = new ConvPoly(messCoeffs, N);
		ConvPoly ephKey = new ConvPoly(ephKeyCoeffs, N);
		
		System.out.println(sys.encrypt(plaintext, ephKey));
	}

}
