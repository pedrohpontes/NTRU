package ntru;

public class EncDecTest {
	
public static void main(String[] args) {
		
		int N = 503;
		int p = 3;
		int q = 191;
		int d = 11;
		
		NTRUkeygen keygen = new NTRUkeygen(N, p, q, d);
		
		ConvPoly privKey = keygen.genPrivKey();
		ConvPoly pubKey = keygen.genPubKey();
		
		System.out.println("Keys:");
		System.out.println(privKey);
		System.out.println(pubKey);
		
		int[] array = {1, -1, 1, 1, 0, -1};
		
		ConvPoly plaintext = new ConvPoly(array, N);
		
		System.out.println("Plaintext:");
		System.out.println(plaintext);
		
		NTRUencrypt enc = new NTRUencrypt(p, q, d, pubKey);
		
		ConvPoly ciphertext = enc.encrypt(plaintext); 
		
		System.out.println("Ciphertext:");
		System.out.println(ciphertext);
		
		NTRUdecrypt dec = new NTRUdecrypt(p, q, privKey);
		
		ConvPoly message = dec.decrypt(ciphertext);
		
		System.out.println("Message:");
		System.out.println(message);
		
	}

}

