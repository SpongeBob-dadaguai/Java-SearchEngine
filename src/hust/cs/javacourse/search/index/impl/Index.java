package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * AbstractIndex的具体实现类
 * @author lxiny
 */
public class Index extends AbstractIndex {
    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        Set<AbstractTerm> keys = this.termToPostingListMapping.keySet();
        StringBuilder string = new StringBuilder();
        for(AbstractTerm term : keys) {
            AbstractPostingList postingList = this.termToPostingListMapping.get(term);
            string.append("term: ").append(term.getContent()).append("\n");
            string.append(postingList.toString());
        }
        return string.toString();
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        //获取index中word的集合
        Set<AbstractTerm> keys = this.getDictionary();
        //获取document中所有三元组
        List<AbstractTermTuple> tupleList = document.getTuples();
        for(AbstractTermTuple tuple: tupleList) {
            //集合中已经存在word
            if(keys.contains(tuple.term)) {
                int docId = document.getDocId();
                AbstractPostingList postingList = this.termToPostingListMapping.get(tuple.term);
                int index = postingList.indexOf(docId);
                //index中不存在这个文档对应的posting
                if(index == -1) {
                    List<Integer> positions = new ArrayList<>();
                    positions.add(tuple.curPos);
                    AbstractPosting posting = new Posting(docId, tuple.freq, positions);
                    postingList.add(posting);
                }
                //index中存在这个文档对应的posting
                else {
                    AbstractPosting posting = postingList.get(postingList.indexOf(docId));
                    posting.getPositions().add(tuple.curPos);
                    posting.setFreq(posting.getFreq() + tuple.freq);
                }
            }
            //集合中不存在这个word
            else {
                AbstractPostingList postingList = new PostingList();
                List positions = new ArrayList<>();
                positions.add(tuple.curPos);
                AbstractPosting posting = new Posting(document.getDocId(), tuple.freq, positions);
                postingList.add(posting);
                this.termToPostingListMapping.put(tuple.term, postingList);
                keys.add(tuple.term);
            }
        }
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        if(file == null) return;
        try {
            readObject(new ObjectInputStream(Files.newInputStream(file.toPath())));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        try {
            writeObject(new ObjectOutputStream(Files.newOutputStream(file.toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        return this.termToPostingListMapping.get(term);
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        Set<AbstractTerm> keySet = this.termToPostingListMapping.keySet();
        return new HashSet<>(keySet);
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        Collection<AbstractPostingList> lists = this.termToPostingListMapping.values();
        for(AbstractPostingList postingList : lists) {
            for(int i = 0; i < postingList.size(); i++) {
                postingList.get(i).sort();
            }
            postingList.sort();
        }
    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return this.docIdToDocPathMapping.get(docId);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            Set<Integer> docIds = this.docIdToDocPathMapping.keySet();
            out.writeObject(docIds.size());
            for(Integer docId : docIds) {
                out.writeObject(docId);
                out.writeObject(this.getDocName(docId));
            }
            Set<AbstractTerm> terms = this.getDictionary();
            out.writeObject(terms.size());
            for(AbstractTerm term : terms) {
                out.writeObject(term);
                out.writeObject(this.search(term));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            int size = (Integer)in.readObject();
            for(int i = 0; i < size; i++) {
                int docId = (Integer)in.readObject();
                String path = (String)in.readObject();
                this.docIdToDocPathMapping.put(docId, path);
            }
            size = (Integer)in.readObject();
            for(int i = 0;i <size; i++) {
                AbstractTerm term = (AbstractTerm) in.readObject();
                AbstractPostingList postingList = (AbstractPostingList) in.readObject();
                this.termToPostingListMapping.put(term, postingList);
            }
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
