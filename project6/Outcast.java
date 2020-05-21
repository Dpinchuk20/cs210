package edu.umb.cs210.p6;

import stdlib.In;
import stdlib.StdOut;

// An immutable data type for outcast detection.
public class Outcast {
    WordNet wordnet;

    // Construct an Outcast object given a WordNet object.
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // The outcast noun from nouns.
    public String outcast(String[] nouns) {
        int maxD = 0;
        String answer = nouns[0];
        for (int i = 0; i < nouns.length; i++)
        {
            int dis = 0;
            for (int j = 0; j < nouns.length; j++)
            {
                dis += wordnet.distance(nouns[i], nouns[j]);
            }
            if (dis > maxD)
            {
                maxD = dis;
                answer = nouns[i];
            }
        }
        return answer;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println("outcast(" + args[t] + ") = "
                           + outcast.outcast(nouns));
        }
    }
}
