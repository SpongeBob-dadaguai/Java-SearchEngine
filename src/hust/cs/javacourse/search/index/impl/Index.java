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
    public Index() {}
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
        docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
        for (AbstractTermTuple termTuple : document.getTuples()) {
            if (!termToPostingListMapping.containsKey(termTuple.term)) {//如果原来没有这个term
                Posting posting = new Posting();
                posting.setDocId(document.getDocId());
                posting.setFreq(termTuple.freq);
                List<Integer> positions = new ArrayList<>();
                positions.add(termTuple.curPos);
                posting.setPositions(positions);
                termToPostingListMapping.put(termTuple.term, new PostingList());
                termToPostingListMapping.get(termTuple.term).add(posting);
            } else {
                boolean flag = false;
                //包含这个term
                // 先获得已经存储的index,再将目前的curPos加入
                //System.out.println(termToPostingListMapping.get(termTuple.term));
                for (int i = 0; i < termToPostingListMapping.get(termTuple.term).size(); i++) {
                    if (termToPostingListMapping.get(termTuple.term).get(i).getDocId() == document.getDocId()) {
                        termToPostingListMapping.get(termTuple.term).get(i).getPositions().add(termTuple.curPos);
                        termToPostingListMapping.get(termTuple.term).get(i).setFreq(termToPostingListMapping.get(termTuple.term).get(i).getFreq() + 1);
                        flag = true;
                    }
                }
                if (!flag) {
                    Posting posting = new Posting();
                    posting.setDocId(document.getDocId());
                    posting.setFreq(termTuple.freq);
                    List<Integer> positions = new ArrayList<>();
                    positions.add(termTuple.curPos);
                    posting.setPositions(positions);
                    termToPostingListMapping.get(termTuple.term).add(posting);
                }
            }

        }
        optimize();
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
            readObject(new ObjectInputStream(new FileInputStream(file)));
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
            writeObject(new ObjectOutputStream(new FileOutputStream(file)));
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
        return this.termToPostingListMapping.keySet();
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

    public void writePlainText(File file) {
        try {
            new BufferedWriter((new FileWriter(file))).write(toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
