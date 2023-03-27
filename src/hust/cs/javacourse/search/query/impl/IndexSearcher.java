package hust.cs.javacourse.search.query.impl;
import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.util.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IndexSearcher extends AbstractIndexSearcher {
    @Override
    public void open(String indexFile) {
        this.index = new Index();
        index.load(new File(indexFile));
    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        // 忽略大小写
        if (Config.IGNORE_CASE) {
            queryTerm.setContent(queryTerm.getContent().toLowerCase());
        }
        AbstractPostingList postingList = index.search(queryTerm);
        if(postingList == null) {
            return new AbstractHit[0];
        }
        List<AbstractHit> ret = new ArrayList<>();
        for(int i = 0;i < postingList.size(); i++) {
            AbstractPosting posting = postingList.get(i);
            AbstractHit hit = new Hit(posting.getDocId(), index.getDocName(posting.getDocId()));
            hit.getTermPostingMapping().put(queryTerm, posting);
            hit.setScore(sorter.score(hit));
            ret.add(hit);
        }
        sorter.sort(ret);
        AbstractHit[] returnResult = new AbstractHit[ret.size()];
        return ret.toArray(returnResult);
    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        // 忽略大小写
        if (Config.IGNORE_CASE) {
            queryTerm1.setContent(queryTerm1.getContent().toLowerCase());
            queryTerm2.setContent(queryTerm2.getContent().toLowerCase());
        }
        AbstractPostingList postingList1 = index.search(queryTerm1);
        AbstractPostingList postingList2 = index.search(queryTerm2);
        if(postingList1 == null && postingList2 == null) {
            return new AbstractHit[0];
        }

        List<AbstractHit> ret = new ArrayList<>();
        if(combine == LogicalCombination.AND) {
            if(postingList2 == null || postingList1 == null) {
                return new AbstractHit[0];
            }
            for(int i = 0; i < postingList1.size(); i++) {
                int docId = postingList1.get(i).getDocId();
                int sub_index = postingList1.indexOf(docId);
                if (docId != -1) {
                    AbstractHit hit = new Hit(docId, index.getDocName(docId));
                    hit.getTermPostingMapping().put(queryTerm1, postingList1.get(i));
                    hit.getTermPostingMapping().put(queryTerm2, postingList2.get(sub_index));
                    hit.setScore(sorter.score(hit));
                    ret.add(hit);
                }
            }
        }
        if(combine == LogicalCombination.OR) {
            if(postingList1 == null) {
                return search(queryTerm2, sorter);
            }
            if(postingList2 == null) {
                return search(queryTerm1, sorter);
            }
            for (int i = 0; i < postingList1.size(); i++) {
                // 首先添加
                int docId = postingList1.get(i).getDocId();
                int sub_index = postingList2.indexOf(docId);
                if (sub_index == -1) {
                    // 如果在另外一个词语中没有,那就正常添加
                    AbstractHit hit = new Hit(docId, index.getDocName(docId));
                    hit.getTermPostingMapping().put(queryTerm1, postingList1.get(i));
                    hit.setScore(sorter.score(hit));
                    ret.add(hit);
                } else {
                    // 如果在另外一个中有, 那就要做一些修改
                    AbstractHit hit = new Hit(docId, index.getDocName(docId));
                    hit.getTermPostingMapping().put(queryTerm1, postingList1.get(i));
                    hit.getTermPostingMapping().put(queryTerm2, postingList2.get(sub_index));
                    hit.setScore(sorter.score(hit));
                    ret.add(hit);
                }
            }
            for (int i = 0; i < postingList2.size(); i++) {
                int docId = postingList2.get(i).getDocId();
                int sub_index = postingList1.indexOf(docId);
                if (sub_index == -1) {
                    // 只有当1中不存在的时候才添加
                    AbstractHit hit = new Hit(docId, index.getDocName(docId));
                    hit.getTermPostingMapping().put(queryTerm2, postingList2.get(i));
                    hit.setScore(sorter.score(hit));
                    ret.add(hit);
                }
            }
        }
        sorter.sort(ret);
        AbstractHit[] returnResult = new AbstractHit[ret.size()];
        return ret.toArray(returnResult);
    }
}
