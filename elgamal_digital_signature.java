import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class elgamal_digital_signature {

    private static final SecureRandom random = new SecureRandom();
    private static BigInteger p, g, x, y;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== ELGAMAL DIGITAL SIGNATURE =====");
            System.out.println("1. Generate Keys");
            System.out.println("2. Sign Message");
            System.out.println("3. Verify Signature");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    generateKeys();
                    break;

                case 2:
                    if (p == null) {
                        System.out.println("Generate keys first!");
                        break;
                    }
                    System.out.print("Enter message to sign: ");
                    String message = sc.nextLine();
                    sign(message);
                    break;

                case 3:
                    if (y == null) {
                        System.out.println("Generate keys first!");
                        break;
                    }
                    System.out.print("Enter original message: ");
                    String msg = sc.nextLine();

                    System.out.print("Enter r: ");
                    BigInteger r = new BigInteger(sc.nextLine().trim());

                    System.out.print("Enter s: ");
                    BigInteger s = new BigInteger(sc.nextLine().trim());

                    verify(msg, r, s);
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
        System.out.println("\nGenerating keys...");

        p = BigInteger.probablePrime(512, random);
        g = new BigInteger("2");
        x = new BigInteger(511, random).mod(p.subtract(BigInteger.ONE));
        if (x.equals(BigInteger.ZERO)) x = BigInteger.ONE;

        y = g.modPow(x, p);

        System.out.println("\nElGamal Keys Generated!");
        System.out.println("p = " + p);
        System.out.println("g = " + g);
        System.out.println("Private key x = " + x);
        System.out.println("Public key y = " + y);
    }

    private static void sign(String message) {
        System.out.println("\n--- Signing Message ---");

        BigInteger H = new BigInteger(message.getBytes()).mod(p);

        BigInteger k;
        do {
            k = new BigInteger(p.bitLength() - 1, random);
        } while (!k.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE));

        BigInteger r = g.modPow(k, p);

        BigInteger kInv = k.modInverse(p.subtract(BigInteger.ONE));

        BigInteger s = (H.subtract(x.multiply(r)))
                .multiply(kInv)
                .mod(p.subtract(BigInteger.ONE));

        System.out.println("Message Hash H = " + H);
        System.out.println("Random k = " + k);
        System.out.println("\nSignature:");
        System.out.println("r = " + r);
        System.out.println("s = " + s);
    }
    private static void verify(String message, BigInteger r, BigInteger s) {

        BigInteger H = new BigInteger(message.getBytes()).mod(p);

        if (r.compareTo(BigInteger.ZERO) <= 0 || 
            r.compareTo(p) >= 0 ||
            s.compareTo(BigInteger.ZERO) < 0 ||
            s.compareTo(p.subtract(BigInteger.ONE)) >= 0) {

            System.out.println("Signature INVALID (values out of range)");
            return;
        }
        BigInteger left = g.modPow(H, p);

        BigInteger right = (y.modPow(r, p).multiply(r.modPow(s, p))).mod(p);

        System.out.println("\nLEFT  = g^H mod p = " + left);
        System.out.println("RIGHT = y^r * r^s mod p = " + right);

        if (left.equals(right)) {
            System.out.println("\nSignature is VALID");
        } else {
            System.out.println("\nSignature is INVALID");
        }
    }
}
