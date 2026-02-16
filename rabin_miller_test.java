import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class rabin_miller_test {

    private static final SecureRandom random = new SecureRandom();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== Miller-Rabin Primality Test =====");
        System.out.print("Enter number to test: ");
        BigInteger n = sc.nextBigInteger();

        System.out.print("Enter number of iterations (k): ");
        int k = sc.nextInt();

        if (isProbablePrime(n, k))
            System.out.println(n + " is PROBABLY PRIME.");
        else
            System.out.println(n + " is COMPOSITE.");
    }

    public static boolean isProbablePrime(BigInteger n, int k) {

        if (n.compareTo(BigInteger.ONE) == 0) return false;
        if (n.compareTo(BigInteger.valueOf(3)) <= 0) return true;
        if (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) return false;

        BigInteger d = n.subtract(BigInteger.ONE);
        int r = 0;

        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.TWO);
            r++;
        }
        for (int i = 0; i < k; i++) {
            BigInteger a = uniformRandom(BigInteger.TWO, n.subtract(BigInteger.TWO));
            BigInteger x = a.modPow(d, n);

            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE)))
                continue;

            boolean continueLoop = false;

            for (int j = 0; j < r - 1; j++) {
                x = x.modPow(BigInteger.TWO, n);

                if (x.equals(n.subtract(BigInteger.ONE))) {
                    continueLoop = true;
                    break;
                }
            }
            if (continueLoop)
                continue;

            return false;
        }
        return true;
    }
    private static BigInteger uniformRandom(BigInteger min, BigInteger max) {
        BigInteger range = max.subtract(min);
        int length = range.bitLength();
        BigInteger result;

        do {
            result = new BigInteger(length, random);
        } while (result.compareTo(range) > 0);

        return result.add(min);
    }
}
