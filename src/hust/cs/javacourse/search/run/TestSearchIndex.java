package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.util.Config;

import java.io.*;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args) throws IOException {
        Sort freqSort = new SimpleSorter();
        String indexFile = Config.INDEX_DIR + "index.data";
        String resultFile = Config.INDEX_DIR + "search.txt";
        AbstractIndexSearcher searcher = new IndexSearcher();
        searcher.open(indexFile);

        /**
         * 命中结果写入文件
         */
        File filename = new File(Config.DOC_DIR+"/用于检索的测试词.txt"); // 要读取以上路径的input.txt文件
        InputStreamReader reader; // 建立一个输入流对象reader
        reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String line = "";
        line = br.readLine();
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(resultFile)));
        while (line != null) {
            line = br.readLine(); // 一次读入一行数据
            if(line == null) break;
            else {
                try {
                    AbstractHit[] hits = searcher.search(new Term(line), freqSort);
                    System.out.println("*********查询单词:"+line);
                    writer.write("*********查询单词:"+line);
                    for(AbstractHit hit : hits){
                        System.out.println(hit);
                        writer.write(hit.toString());
                    }

                } catch (IOException|NullPointerException e) {
                    e.printStackTrace();
                }}}
        writer.close();
    }
}
