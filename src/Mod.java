package ntru;

public class Mod {
	
	
	public static int inverse(int a, int mod) {
		
		//just to keep the mod positive in the recursive calls
		int sign = 1;
		
		if(a < 0) {
			sign = -1;
			a = -a;
		}
		
		int b = a%mod;
		
		if(b == 0)
			throw new RuntimeException("Arithmetical error: cannot calculate inverse in mod.");
		
		if(b == 1)
			return 1*sign;
		
		int c = inverse(mod, b);
		
		int result = (1-c*mod)/b;
		
		result = sign*result;
		
		result = result%mod;
		result = result + mod;
		result = result%mod;
		
		return result;
	}
	
	
	public static void main(String[] args) {
		
		System.out.println(inverse(-1,3));
		
	}

}
