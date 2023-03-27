package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.List;

public class PostingList extends AbstractPostingList {
    /**
     * 添加Posting,要求不能有内容重复的 posting
     * @param posting：Posting对象
     */
    @Override
    public void add(AbstractPosting posting) {
        if(list.contains(posting)) {
            System.out.println("列表中已存在该posting，不能重复添加");
        }
        list.add(posting); //需不需要再添加之后sort一下？
    }

    /**
     * 获得PosingList的字符串表示
     * @return PosingList的字符串表示
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for(AbstractPosting posting : list) {
            ret.append(posting.toString());
            ret.append("\n");
        }
        return ret.toString();
    }

    /**
     * 添加Posting列表,,要求不能有内容重复的posting
     * @param postings：Posting列表
     */
    @Override
    public void add(List<AbstractPosting> postings) {
        for (AbstractPosting posting : postings) {
            if(!list.contains(posting)) {
                list.add(posting);
            }
        }
    }

    /**
     * 返回指定下标位置的Posting
     * @param index ：下标
     * @return Posting
     */
    @Override
    public AbstractPosting get(int index) {
        return list.get(index);
    }

    /**
     * 返回指定Posting对象的下标
     * @param posting：指定的Posting对象
     * @return 如果找到返回对应下标；否则返回-1
     */
    @Override
    public int indexOf(AbstractPosting posting) {
        return list.indexOf(posting);
    }

    /**
     *
     * @param docId ：文档id
     * @return 如果找到返回对应下标；否则返回-1
     */
    @Override
    public int indexOf(int docId) {
        for (AbstractPosting posting : list) {
            if(posting.getDocId() == docId) {
                return list.indexOf(posting);
            }
        }
        return -1;
    }

    /**
     * 是否包含指定Posting对象
     * @param posting： 指定的Posting对象
     * @return 如果包含返回true，否则返回false
     */
    @Override
    public boolean contains(AbstractPosting posting) {
        for(AbstractPosting ps : list) {
            if(ps.equals(posting)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除指定下标的Posting对象
     * @param index：指定的下标
     */
    @Override
    public void remove(int index) {
        list.remove(index);
    }

    /**
     * 删除指定的Posting对象
     * @param posting ：定的Posting对象
     */
    @Override
    public void remove(AbstractPosting posting) {
        list.remove(posting);
    }

    /**
     * 返回PostingList的大小，即包含的Posting的个数
     * @return PostingList的大小
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * 清除PostingList
     */
    @Override
    public void clear() {
        list.clear();
    }

    /**
     * PostingList是否为空
     * @return 为空返回true;否则返回false
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * 根据文档id的大小对PostingList进行从小到大的排序
     */
    @Override
    public void sort() {
        list.sort(Comparator.comparingInt(AbstractPosting::getDocId));
    }

    @Override
    public void writeObject(ObjectOutputStream out) {

    }

    @Override
    public void readObject(ObjectInputStream in) {

    }
}
