import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class primitive_root {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a prime p (p > 2): ");
        long p = sc.nextLong();
        if (p <= 2 || !isPrime(p)) {
            System.out.println("Input must be a prime greater than 2.");
            sc.close();
            return;
        }
        long g = findAnyPrimitiveRoot(p);
        if (g == -1) {
            System.out.println("No primitive root found (unexpected for prime p).");
        } else {
            System.out.println("A primitive root modulo " + p + " is: " + g);
            System.out.print("List all primitive roots? (y/n): ");
            sc.nextLine();
            String ans = sc.nextLine().trim().toLowerCase();
            if (ans.equals("y") || ans.equals("yes")) {
                List<Long> all = findAllPrimitiveRoots(p);
                System.out.println("Total primitive roots: " + all.size());
                System.out.println(all);
            }
        }
        sc.close();
    }
    private static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        long r = (long) Math.sqrt(n);
        for (long i = 3; i <= r; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
    private static long findAnyPrimitiveRoot(long p) {
        long phi = p - 1;
        List<Long> factors = primeFactorsDistinct(phi);

        for (long g = 2; g <= p - 1; g++) {
            boolean ok = true;
            for (long q : factors) {
                if (modPow(g, phi / q, p) == 1) {
                    ok = false;
                    break;
                }
            }
            if (ok) return g;
        }
        return -1;
    }
    private static List<Long> findAllPrimitiveRoots(long p) {
        List<Long> roots = new ArrayList<>();
        long any = findAnyPrimitiveRoot(p);
        if (any == -1) return roots;

        long phi = p - 1;

        for (long k = 1; k <= phi; k++) {
            if (gcd(k, phi) == 1) {
                long val = modPow(any, k, p);
                if (!roots.contains(val)) roots.add(val);
            }
        }
        roots.sort(Long::compare);
        return roots;
    }
    private static List<Long> primeFactorsDistinct(long n) {
        List<Long> factors = new ArrayList<>();
        if (n % 2 == 0) {
            factors.add(2L);
            while (n % 2 == 0) n /= 2;
        }
        for (long i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                factors.add(i);
                while (n % i == 0) n /= i;
            }
        }
        if (n > 1) factors.add(n);
        return factors;
    }
    private static long modPow(long base, long exp, long mod) {
        BigInteger b = BigInteger.valueOf(base);
        BigInteger e = BigInteger.valueOf(exp);
        BigInteger m = BigInteger.valueOf(mod);
        return b.modPow(e, m).longValue();
    }
    private static long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
}