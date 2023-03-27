package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class TermTupleScanner extends AbstractTermTupleScanner {
    public TermTupleScanner(BufferedReader reader) {
        super(reader);
    }
    Queue<AbstractTermTuple> q = new LinkedList<>();
    int pos = 0;
    @Override
    public AbstractTermTuple next() throws IOException {
        if(q.isEmpty()) {
            String line = input.readLine();
            if(line == null) {
                return null;
            }
            while(line.trim().length() == 0) {
                line = input.readLine();
                if(line == null) {
                    return null;
                }
            }
            StringSplitter splitter = new StringSplitter();
            splitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
            for(String word : splitter.splitByRegex(line)) {
                AbstractTermTuple tuple = new TermTuple();
                tuple.curPos = pos;
                pos++;
                if(Config.IGNORE_CASE) {
                    tuple.term = new Term(word.toLowerCase());
                } else {
                    tuple.term = new Term(word);
                }
                q.add(tuple);
            }
        }
        return q.poll();
    }
}
