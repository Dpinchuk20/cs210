package edu.umb.cs210.p6;

import dsa.DiGraph;
import dsa.RedBlackBinarySearchTreeST;
import dsa.Set;
import stdlib.In;
import stdlib.StdOut;

// An immutable WordNet data type.
public class WordNet {
    private RedBlackBinarySearchTreeST<String, Set<Integer>> st; // ID index
    private RedBlackBinarySearchTreeST<Integer, String> rst; // string index
    private ShortestCommonAncestor sca; // common ancester
    private RedBlackBinarySearchTreeST<String, Integer> tree; // track size

    // Construct a WordNet object given the names of the input (synset and
    // hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null)
        {
            throw new NullPointerException("synsets is null");
        }
        if (hypernyms == null)
        {
            throw new NullPointerException("hypernyms is null");
        }
        st = new RedBlackBinarySearchTreeST<String, Set<Integer>>();
        rst = new RedBlackBinarySearchTreeST<Integer, String>();
        tree = new RedBlackBinarySearchTreeST<String, Integer>();
        In in = new In(synsets);
        int t = 0;
        while (!in.isEmpty()) {
            String[] token = in.readLine().split(",");
            int id = Integer.parseInt(token[0]);
            String[] word = token[1].split(" ");
            for (String w : word) {
                if (in == null) {
                    break;
                }
                if (!st.contains(w)) {
                    st.put(w, new Set<Integer>());
                }
                st.get(w).add(id);
            }
            rst.put(id, token[1]);
            t = id;
        }
        DiGraph G = new DiGraph(t + 1);
        In hyper = new In(hypernyms);
        while (!hyper.isEmpty())
        {
            String[] token = hyper.readLine().split(",");
            int x = Integer.parseInt(token[0]);
            int[] line = new int[token.length];
            for (int i = 1; i < token.length; i++)
            {
                line[i] = Integer.parseInt(token[i]);
                G.addEdge(x, line[i]);
            }
        }
        sca = new ShortestCommonAncestor(G);
    }

    // All WordNet nouns.
    public Iterable<String> nouns() {
        return st.keys();
    }

    // Is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
        {
            throw new NullPointerException("word is null");
        }
        return st.contains(word);
    }

    // A synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
        if (noun1 == null)
        {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null)
        {
            throw new NullPointerException("noun2 is null");
        }
        if (!isNoun(noun1))
        {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!isNoun(noun2))
        {
            throw new IllegalArgumentException("noun2 is not a noun");
        }
        Set<Integer> x = st.get(noun1);
        Set<Integer> y = st.get(noun2);
        int token = sca.ancestor(x, y);
        return rst.get(token);
    }

    // Distance between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        if (noun1 == null)
        {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null)
        {
            throw new NullPointerException("noun2 is null");
        }
        if (!isNoun(noun1))
        {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!isNoun(noun2))
        {
            throw new IllegalArgumentException("noun2 is not a noun");
        }
        Set<Integer> x = st.get(noun1);
        Set<Integer> y = st.get(noun2);
        if (x.size() == 1 && y.size() == 1) {
            return sca.length(x.max(), y.max());
        }
        else {
            return sca.length(x, y);
        }
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.println("# of nouns = " + nouns);
        StdOut.println("isNoun(" + word1 + ") = " + wordnet.isNoun(word1));
        StdOut.println("isNoun(" + word2 + ") = " + wordnet.isNoun(word2));
        StdOut.println("isNoun(" + (word1 + " " + word2) + ") = "
                + wordnet.isNoun(word1 + " " + word2));
        StdOut.println("sca(" + word1 + ", " + word2 + ") = "
                + wordnet.sca(word1, word2));
        StdOut.println("distance(" + word1 + ", " + word2 + ") = "
                + wordnet.distance(word1, word2));
    }
}