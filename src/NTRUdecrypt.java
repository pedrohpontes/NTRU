package ntru;

public class NTRUdecrypt {
	
	private final int p;
	private final int q;
	private final ConvPoly privKey;
	private ConvPoly privKeyInv;
	
	
	public NTRUdecrypt(int p1, int q1, ConvPoly privKey1) {
		p = p1;
		q = q1;
		privKey = privKey1;
		privKeyInv = privKey1.inverse(p);
	}
	
	public NTRUdecrypt(int p1, int q1, ConvPoly privKey1, ConvPoly privKeyInv1) {
		p = p1;
		q = q1;
		privKey = privKey1;
		privKeyInv = privKeyInv1;
	}
	
	
	public ConvPoly decrypt(ConvPoly ciphertext) {
		
		if(privKeyInv == null)
			throw new RuntimeException("Error: inverse of private key not computed.");
		
		ConvPoly result = (privKey.multiply(ciphertext)).mod(q);
		result = result.centerLift(q);
		
		result = (privKeyInv.multiply(result)).mod(p);
		
		return result.centerLift(p);
	}	
	
	
	public static void main(String[] args) {
		
		int N = 7;
		int p = 3;
		int q = 41;
		int d=  2;
		
		int[] pubKeyCoeffs = {30, 26, 8, 38, 2, 40, 20};
		
		ConvPoly pubKey = new ConvPoly(pubKeyCoeffs, N);
		
		NTRUencrypt enc = new NTRUencrypt(p, q, d, pubKey);
		
		int[] messCoeffs = {1, -1, 1, 1, 0, -1};
		ConvPoly plaintext = new ConvPoly(messCoeffs, N);
		
		System.out.println(plaintext);

		int[] ephKeyCoeffs = {-1, 1, 0, 0, 0, -1, 1};
		ConvPoly ephKey = new ConvPoly(ephKeyCoeffs, N);
		
		ConvPoly ciphertext = enc.encrypt(plaintext, ephKey);
		
		System.out.println(ciphertext);
		
		
		int[] privKeyCoeffs = {-1, 0, 1, 1, -1, 0, 1};
		ConvPoly privKey = new ConvPoly(privKeyCoeffs, N);
		
		
		int[] privKey2Coeffs = {1, 1, 1, 1, 0, 2, 1};
		ConvPoly privKey2 = new ConvPoly(privKey2Coeffs, N);
		
		
		NTRUdecrypt dec = new NTRUdecrypt(p, q, privKey, privKey2);		
		ConvPoly decrypted = dec.decrypt(ciphertext);
		
		System.out.println(decrypted);
	}

}
