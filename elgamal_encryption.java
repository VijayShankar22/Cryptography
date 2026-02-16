import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class elgamal_encryption {

    private static BigInteger p;
    private static BigInteger g;
    private static BigInteger x;
    private static BigInteger y;

    private static SecureRandom random = new SecureRandom();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== ELGAMAL ENCRYPTION =====");
            System.out.println("1. Generate Keys");
            System.out.println("2. Encrypt Message");
            System.out.println("3. Decrypt Message");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    generateKeys();
                    break;

                case 2:
                    System.out.print("Enter message to encrypt: ");
                    String message = sc.nextLine();
                    encrypt(message);
                    break;

                case 3:
                    System.out.print("Enter encrypted 'a': ");
                    BigInteger a = new BigInteger(sc.nextLine());
                    System.out.print("Enter encrypted 'b': ");
                    BigInteger b = new BigInteger(sc.nextLine());
                    decrypt(a, b);
                    break;

                case 4:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
 
    private static void generateKeys() {
        p = BigInteger.probablePrime(512, random);
        g = new BigInteger("2");

        x = new BigInteger(512, random).mod(p);
        y = g.modPow(x, p);

        System.out.println("\nKeys Generated!");
        System.out.println("Public Key (p, g, y): ");
        System.out.println("p = " + p);
        System.out.println("g = " + g);
        System.out.println("y = " + y);
        System.out.println("\nPrivate Key x = " + x);
    }
    private static void encrypt(String message) {
        if (p == null) {
            System.out.println("Generate keys first.");
            return;
        }

        BigInteger m = new BigInteger(message.getBytes());

        BigInteger k = new BigInteger(512, random).mod(p);
        BigInteger a = g.modPow(k, p);
        BigInteger b = m.multiply(y.modPow(k, p)).mod(p);

        System.out.println("\nEncryption Output:");
        System.out.println("a = " + a);
        System.out.println("b = " + b);
    }

    private static void decrypt(BigInteger a, BigInteger b) {
        if (x == null) {
            System.out.println("Generate keys first.");
            return;
        }

        BigInteger s = a.modPow(x, p);
        BigInteger sInv = s.modInverse(p);
        BigInteger m = b.multiply(sInv).mod(p);

        String message = new String(m.toByteArray());

        System.out.println("\nDecrypted Message: " + message);
    }
}
