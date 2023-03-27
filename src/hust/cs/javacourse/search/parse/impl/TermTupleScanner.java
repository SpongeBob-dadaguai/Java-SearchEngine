package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;

import java.io.BufferedReader;

public class TermTupleScanner extends AbstractTermTupleScanner {
    public TermTupleScanner(BufferedReader reader) {
        this.input = reader;
    }
    @Override
    public AbstractTermTuple next() {
        return null;
    }
}
