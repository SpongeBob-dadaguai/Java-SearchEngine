package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.PatternTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;

import java.io.*;
import java.nio.file.Files;

/**
 * AbstractDocumentBuilder的实现
 * @author lxiny
 */
public class DocumentBuilder extends AbstractDocumentBuilder {
    /**
     * 由解析文本文档得到的TermTupleStream,构造Document对象.
     * @param docId             : 文档id
     * @param docPath           : 文档绝对路径
     * @param termTupleStream   : 文档对应的TermTupleStream
     * @return Document对象
     */
    @Override
    public AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream) throws IOException {
        AbstractDocument document = new Document(docId, docPath);
        AbstractTermTuple tuple = termTupleStream.next();
        while(tuple != null) {
            document.addTuple(tuple);
            tuple = termTupleStream.next();
        }
        termTupleStream.close();
        return document;
    }

    /**
     * 由给定的File,构造Document对象.
     *  该方法利用输入参数file构造出AbstractTermTupleStream子类对象后,内部调用
     *       AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream)
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     * @param file      : 文档对应File对象
     * @return 根据上述参数构造的Document
     */
    @Override
    public AbstractDocument build(int docId, String docPath, File file) throws IOException {
        AbstractDocument document = null;
        AbstractTermTupleStream ts = null;
        try {
            ts = new TermTupleScanner(new BufferedReader(new InputStreamReader(new
                    FileInputStream(file))));
            ts = new StopWordTermTupleFilter(ts); //再加上停用词过滤器
            ts = new PatternTermTupleFilter(ts); //再加上正则表达式过滤器
            ts = new LengthTermTupleFilter(ts); //再加上单词长度过滤器
            document = build(docId, docPath, ts);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ts.close();
        }
        return document;
    }
}
