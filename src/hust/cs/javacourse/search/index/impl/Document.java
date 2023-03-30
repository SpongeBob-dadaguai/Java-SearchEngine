package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractTermTuple;

import java.util.List;

/**
 * 对 AbstractDocument类的具体实现类
 */
public class Document extends AbstractDocument {
    /**
     *
     */
    public Document() {

    }
    public Document(int docId, String docPath) {
        super(docId, docPath);
    }

    public Document(int docId, String docPath, List<AbstractTermTuple> tuples) {
        super(docId, docPath, tuples);
    }

    /**
     * 获得文档id
     * @return ：文档id
     */
    @Override
    public int getDocId() {
        return this.docId;
    }

    /**
     * 设置文档id
     * @param docId：文档id
     */
    @Override
    public void setDocId(int docId) {
        this.docId = docId;
    }

    /**
     * 获得文档绝对路径
     * @return 文档绝对路径
     */
    @Override
    public String getDocPath() {
        return this.docPath;
    }

    /**
     * 设置文档绝对路径
     * @param docPath：文档绝对路径
     */
    @Override
    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    /**
     * 获得文档中包含的三元组列表
     * @return 文档中包含的三元组列表
     */
    @Override
    public List<AbstractTermTuple> getTuples() {
        return this.tuples;
    }

    /**
     * 向文档中添加三元组，要求不能有重复的三元组
     * @param tuple ：要添加的三元组
     */
    @Override
    public void addTuple(AbstractTermTuple tuple) {
        if(this.contains(tuple)) {
            System.out.println("列表中已存在该三元组，不能重复添加");
            return;
        }
        (this.tuples).add(tuple);
    }

    /**
     * 判断是否包含指定的三元组
     * @param tuple ： 指定的三元组
     * @return 如果包含指定的三元组，返回true;否则返回false
     */
    @Override
    public boolean contains(AbstractTermTuple tuple) {
        for(AbstractTermTuple tp: this.tuples) {
            if(tp.equals(tuple))
                return true;
        }
        return false;
    }

    /**
     * 获取指定下标位置的三元组
     * @param index：指定下标位置
     * @return 三元组
     */
    @Override
    public AbstractTermTuple getTuple(int index) {
        if(index < 0 || index >= this.getTupleSize()) {
            System.out.println("下标越界");
            return null;
        }
        return (this.tuples).get(index);
    }

    /**
     * 返回文档对象包含的三元组的个数
     * @return 文档对象包含的三元组的个数
     */
    @Override
    public int getTupleSize() {
        return this.tuples.size();
    }

    /**
     * 获得Document的字符串表示
     * @return Document的字符串表示
     */
    @Override
    public String toString() {
        return null;
    }
}
