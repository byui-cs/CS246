/*	Note that we're not using wildcards in our import statements like:
   
	import java.util.* 

	Instead, we're being explicit about the classes we use.
	See the first answer here for a good discussion on why: 
	http://stackoverflow.com/questions/147454/why-is-using-a-wild-card-with-a-java-import-statement-bad
*/
import java.util.Random;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.util.Arrays;
import java.util.Base64;

/**
* The NSALoginController class handles password hashing and verification
* for the {@link User} class.
* <p>
* It contains only static methods, and therefore maintains no state.
* Another example of a class containing only static methods is 
* Java's built-in {@link java.lang.Math} class.
* <p>
* Notice the interesting formatting used in this comment block.
* This special formatting is called Javadocs and is used by Java IDEs
* to automatically generate documentation for classes. 
* <p>
* Many Java IDEs will help you automatically generate the formatting
* for the Javadoc comments. 
* <p>
* Javadocs are widely used in industry, and if you work for a company that
* uses Java code, you will almost certainly be expected to include Javadocs
* in your own code, so it's a good habit to develop.
* <p>
* You can use the <code>javadoc</code> tool to generate fancy HTML files. For example:
* <p>
* <code>javadoc *.java -d ./doc</code>
* <p>
* Then you can open the index.html file in the doc directory.
* <p>
* For more information about <em>how</em> to write javadocs see:
* <p>
* <a href="https://www.tutorialspoint.com/java/java_documentation.htm">Javadoc Tutorial</a>
* <p>
* For more information about <em>when</em> to write javadocs see these Stack Overflow posts:
* <ul>
* <li><a href="http://stackoverflow.com/questions/211041/do-you-use-javadoc-for-every-method-you-write">Do you use Javadoc for every method you write?</a></li>
* <li><a href="http://stackoverflow.com/questions/398546/technical-tips-for-writing-great-javadoc">Technical tips for writing great Javadoc</a></li>
* <li><a href="http://stackoverflow.com/questions/1028967/simple-getter-setter-comments">Simple Getter/Setter comments</a></li>
* <li><a href="http://stackoverflow.com/questions/3607641/javadoc-comments-vs-block-comments">Javadoc comments vs block comments?</a></li>
* <li><a href="http://stackoverflow.com/questions/21632086/javadoc-for-private-protected-methods">Javadoc for private/protected methods?</a></li>
* </ul>
* @author  Brother Falin
* @version 1.0
* @since   2016-12-08 
* 
* This code was adapted from <a href="http://stackoverflow.com/a/18143616/28106">this Stack Overflow post</a>.
*/
public final class NSALoginController {
	
	// The SecureRandom() class is a special subclass of Random() in 
	// order to make our "random" salt values less predictable.
	// For more information, see:
	// http://stackoverflow.com/questions/11051205/difference-between-java-util-random-and-java-security-securerandom
	private static final Random RANDOM = new SecureRandom();

	// The use of "static final" here is the equivalent of "const" in C++
	// These constant values are used by the hash algorithm.
	private static final int ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;

	// By using a private constructor, we prevent instances of this class from being created
	private NSALoginController() {

	}

	// This function generates a random, 16-byte 
	// salt value. You might be wondering if a longer salt
	// would result in a more secure hash:
	// http://stackoverflow.com/questions/184112/what-is-the-optimal-length-for-user-password-salt
	private static byte[] getNextSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}
	
	// This function uses the PBKDF2 algorithm for generating the hash. If you're 
	// interested in why this particular function was chosen, see:
	// http://security.stackexchange.com/questions/4781/do-any-security-experts-recommend-bcrypt-for-password-storage/6415#6415
	private static byte[] getHash(char[] password, byte[] salt) throws Exception {

		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		Arrays.fill(password, Character.MIN_VALUE);

		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
	
		return hash;
	}
	
	/**
	* This function takes the password from the {@link User} class and hashes it.
	* As a side-effect, the original password value is removed for security purposes.
	* @param user The user whose password needs to be hashed.
	* @exception Exception If there is a problem with the chosen hash function.
	*/
	public static void hashUserPassword(User user) throws Exception {
		
		// Get the next random salt value to use for this password
		byte[] salt = getNextSalt();
		char[] password = user.getPassword().toCharArray();

		// Once we've generated the hash, clear the old password
		// from memory for security purposes
		byte[] hash = getHash(password, salt);
		Arrays.fill(password, Character.MIN_VALUE);
		user.setPassword("");
		
		if(hash != null) {

			// By Base64-encoding the raw bytes, we can store them as strings.
			// This allows us to save the values to a file or database if needed.
			// For more information on Base64 encoding, see:
			// http://stackoverflow.com/questions/201479/what-is-base-64-encoding-used-for
			// https://en.wikipedia.org/wiki/Base64
			String hashString = Base64.getEncoder().encodeToString(hash);
			String saltString = Base64.getEncoder().encodeToString(salt);
			
			user.setHashedPassword(hashString);
			user.setSalt(saltString);
		}
		else
		{
			user.setHashedPassword(null);
			user.setSalt(null);
		}
	}
	
	/**
	* This function uses the password and salt in the {@link User} to generate a hash,
	* then compares that hash to the original hash value.
	* @param user The user whose password needs to be hashed.
	* @return Whether or not the password values match.
	* @exception Exception If there is a problem with the chosen hash function.
	*/
	public static Boolean verifyPassword(User user) throws Exception {
		
		// Have to get the raw data values to use on our hash function
		char[] password = user.getPassword().toCharArray();
		byte[] salt = Base64.getDecoder().decode(user.getSalt());
		
		// Generate the new hash, and retrieve the user's hash
		byte[] expectedHash = getHash(password, salt);
		byte[] userHash = Base64.getDecoder().decode(user.getHashedPassword());
		
		// If the new hash came out as null, or the lengths don't match,
		// we know that the original password is different
		if(expectedHash == null || expectedHash.length != userHash.length)
			return false;
		
		// Check each byte of the two hashes and as soon as we find one
		// that is different, we know that the passwords don't match
		for(int i = 0; i < expectedHash.length; i++) {
			if(expectedHash[i] != userHash[i])
				return false;
		}
		
		// If we got this far, it means the password hashes match, so we
		// can assume the passwords do as well.
		return true;
	}
}