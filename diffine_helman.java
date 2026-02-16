import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class diffine_helman {

    static SecureRandom random = new SecureRandom();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== Diffie-Hellman Key Exchange =====");

        BigInteger p = BigInteger.probablePrime(512, random);
        BigInteger g = new BigInteger("5");

        System.out.println("\nPublicly known values:");
        System.out.println("p = " + p);
        System.out.println("g = " + g);

        BigInteger a = new BigInteger(256, random);
        BigInteger A = g.modPow(a, p);

        BigInteger b = new BigInteger(256, random);
        BigInteger B = g.modPow(b, p);

        System.out.println("\nUser A Private Key (a): " + a);
        System.out.println("User A Public Key (A): " + A);

        System.out.println("\nUser B Private Key (b): " + b);
        System.out.println("User B Public Key (B): " + B);

        BigInteger sharedA = B.modPow(a, p);
        BigInteger sharedB = A.modPow(b, p);

        System.out.println("\nShared key calculated by A: " + sharedA);
        System.out.println("Shared key calculated by B: " + sharedB);

        if (sharedA.equals(sharedB)) {
            System.out.println("✓ Shared key successfully established!");
        } else {
            System.out.println("✗ Shared key mismatch! (Should not happen)");
            return;
        }
        BigInteger sharedKey = sharedA;

        System.out.print("\nEnter message to encrypt: ");
        String message = sc.nextLine();

        BigInteger m = new BigInteger(message.getBytes());
        BigInteger cipher = m.xor(sharedKey);

        System.out.println("\nEncrypted Message:");
        System.out.println(cipher);

        BigInteger decrypted = cipher.xor(sharedKey);
        String plainText = new String(decrypted.toByteArray());

        System.out.println("\nDecrypted Message:");
        System.out.println(plainText);
    }
}
