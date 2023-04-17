package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import jdk.internal.org.objectweb.asm.tree.InnerClassNode;

import java.awt.print.PrinterGraphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;

public class Posting extends AbstractPosting {
    public Posting() {}

    public Posting(int docId, int freq, List<Integer> positions) {
        super(docId, freq, positions);
    }

    /**
     * 判断两个posting内容是否相同
     * @param obj ：要比较的另外一个Posting
     * @return 如果内容相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Posting post = (Posting) obj;
        return post.docId == docId && post.freq == freq && this.positions.size() == post.positions.size()
                && this.positions.containsAll(post.positions)
                && post.positions.containsAll(this.positions);
    }

    /**
     * 返回posting的字符串表示
     * @return 字符串
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("docId: ").append(getDocId()).append(",");
        ret.append("freq: ").append(getFreq()).append(",");
        ret.append("positions: ");
//        System.out.println(positions);
        for (int pos: positions) ret.append(pos).append(" ");
        return ret.toString();
    }

    /**
     * 返回包含单词的文档id
     * @return 文档id
     */
    @Override
    public int getDocId() {
        return this.docId;
    }

    /**
     * 设置包含单词的文档id
     * @param docId：包含单词的文档id
     */
    @Override
    public void setDocId(int docId) {
        this.docId = docId;
    }

    /**
     * 返回单词在文档中出现的频率
     * @return 出现次数
     */
    @Override
    public int getFreq() {
        return this.freq;
    }

    /**
     * 设置单词在文档中的频率
     * @param freq:单词在文档里出现的次数
     */
    @Override
    public void setFreq(int freq) {
        this.freq = freq;
    }

    /**
     * 返回单词在文档中出现的位置列表
     * @return 位置列表
     */
    @Override
    public List<Integer> getPositions() {
        return this.positions;
    }

    /**
     * 设置单词在文档中出现的位置列表
     * @param positions：单词在文档里出现的位置列表
     */
    @Override
    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    /**
     * 比较两个Posting对象的大小（根据docId）
     * @param o the object to be compared.
     * @return 二个Posting对象的docId的差值
     */
    @Override
    public int compareTo(AbstractPosting o) {
        return this.docId - o.getDocId();
    }

    /**
     * 对内部positions从小到大排序
     */
    @Override
    public void sort() {
        Collections.sort(this.positions);  //默认升序排序
    }

    /**
     * 写入文件
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this.docId);
            out.writeObject(this.freq);
            out.writeObject(this.positions.size());
            for (Integer pos : positions) {
                out.writeObject(pos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件读取
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docId = (Integer)in.readObject();
            this.freq = (Integer)in.readObject();
            int size = (Integer)in.readObject();
            for(int i = 0; i < size; i++) {
                this.positions.add((Integer) in.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
