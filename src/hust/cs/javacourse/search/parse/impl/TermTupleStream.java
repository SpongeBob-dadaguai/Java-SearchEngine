package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import sun.net.www.ParseUtil;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author lxiny
 */
public class TermTupleStream extends AbstractTermTupleStream {
    private final BufferedReader reader;
    public TermTupleStream(BufferedReader br) {
        this.reader = br;
    }

    /**
     *
     * @return 下一个三元组
     */
    @Override
    public AbstractTermTuple next() {
        try {
            String line = reader.readLine();
            if(line != null) {
                //以空格等标点符号为区分
                String[] words = line.split("\\s+|(?=[,.?!;])|(?<=[,.?!;])");
                for(String word: words) {

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 关闭输入流
     */
    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
