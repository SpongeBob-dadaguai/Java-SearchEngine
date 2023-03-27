package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.util.FileUtil;

import java.util.Map;

public class Hit extends AbstractHit {
    /**
     * 默认构造函数
     */
    public Hit(){

    }

    /**
     * 构造函数
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     */
    public Hit(int docId, String docPath){
        super(docId, docPath);
    }

    /**
     * 构造函数
     * @param docId              ：文档id
     * @param docPath            ：文档绝对路径
     * @param termPostingMapping ：命中的三元组列表
     */
    public Hit(int docId, String docPath, Map<AbstractTerm, AbstractPosting> termPostingMapping){
        super(docId, docPath, termPostingMapping);
    }
    @Override
    public int getDocId() {
        return this.docId;
    }

    @Override
    public String getDocPath() {
        return this.docPath;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public double getScore() {
        return this.score;
    }

    @Override
    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public Map<AbstractTerm, AbstractPosting> getTermPostingMapping() {
        return this.termPostingMapping;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("docId: ").append(this.docId).append('\n');
        ret.append("docPath: ").append(this.docPath).append('\n');
        ret.append("score: ").append(this.score).append('\n');
        ret.append("content").append(this.content).append('\n').append("Map: ");
        for(AbstractTerm term : this.termPostingMapping.keySet()) {
            ret.append("term-Posting").append(term.getContent()).append(this.termPostingMapping.get(term).toString());
        }
        return ret.toString();
    }

    @Override
    public int compareTo(AbstractHit o) {
        return (int) (this.score - o.getScore());
    }
}
