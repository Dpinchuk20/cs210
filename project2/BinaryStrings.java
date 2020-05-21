package edu.umb.cs210.p2;

import stdlib.StdOut;

import java.util.Iterator;

// An immutable data type to systematically iterate over binary
// strings of length n.
public class BinaryStrings implements Iterable<String> {
    private final int n; // need all binary strings of length n
    // Construct an iterable BinaryStrings object given the length
    // of binary strings needed.
    BinaryStrings(int n) {
        // assigns the value for n
        this.n = n;
    }

    // A BinaryStringsIterator object.
    public Iterator<String> iterator() {
        // returns an instance of BinaryStringsIterator
        return new BinaryStringsIterator();
    }
    
    // Binary strings iterator.
    private class BinaryStringsIterator implements Iterator<String> {
        private int count; // number of binary strings returned
        private int p; // current number

        // Construct a BinaryStringsIterator object.
        BinaryStringsIterator() {
            // if the length of binary string is n, then it can represent...
            // 2^n numbers starting from 0
            count = (int) Math.pow(2, n);
            p = 0;
        }

        // Are there anymore binary strings left to be iterated?
        public boolean hasNext() { 
            // returns true if p < count, else false
            return p < count;
        }

        // The next binary string.
        public String next() {
            // finding binary representation of p
            String bin = binary(p);
            // incrementing p
            p++;
            // return bin
            return bin;
        }
        
        // Remove is not supported.
        public void remove() {
            // nothing to do
        }

        // The n-bit representation of x.
        private String binary(int x) {
            String s = Integer.toBinaryString(x);
            int padding = n - s.length();
            for (int i = 1; i <= padding; i++) {
                s = "0" + s;
            }
            return s;
        }
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        for (String s : new BinaryStrings(n)) {
            StdOut.println(s);
        }
    }
}
