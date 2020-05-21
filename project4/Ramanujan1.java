package edu.umb.cs210.p4;

import stdlib.StdOut;

public class Ramanujan1 {
    public static void main(String[] args) {
        // check cmd argument
        int n = Integer.parseInt(args[0]);

        // checking a^3 + b^3 = c^3 + d^3

        // outer loop
        for (int a = 1; (a*a*a) <= n; a++) {

            int a3 = a*a*a;

            // no solution is possible, since upcoming a values are more
            if (a3 > n)
                break;

            // avoid duplicate
            for (int b = a; (b * b * b) <= (n - a3); b++) {

                int b3 = b*b*b;

                if (a3 + b3 > n)
                    break;

                // avoid duplicates
                for (int c = a + 1; (c*c*c) <= n; c++) {

                    int c3 = c*c*c;

                    if (c3 > a3 + b3)
                        break;

                    // avoid duplicates
                    for (int d = c; (d * d * d) <= (n - c3); d++) {

                        int d3 = d*d*d;

                        if (c3 + d3 > a3 + b3)
                            break;

                        if (c3 + d3 == a3 + b3) {
                            StdOut.print((a3+b3) + " = ");
                            StdOut.print(a + "^3 + " + b + "^3 = ");
                            StdOut.print(c + "^3 + " + d + "^3");
                            StdOut.println();
                        }
                    }
                }
            }
        }
    }
}
