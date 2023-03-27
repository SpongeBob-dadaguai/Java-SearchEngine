package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

import java.io.IOException;

public class LengthTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 构造函数
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public LengthTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    @Override
    public AbstractTermTuple next() throws IOException {
        AbstractTermTuple tuple = input.next();
        if(tuple == null) {
            return null;
        }
        while (tuple.term.getContent().length() > Config.TERM_FILTER_MAXLENGTH ||
                tuple.term.getContent().length() < Config.TERM_FILTER_MINLENGTH)  {
            tuple = input.next();
            if(tuple == null) {
                return null;
            }
        }
        return tuple;
    }
}
