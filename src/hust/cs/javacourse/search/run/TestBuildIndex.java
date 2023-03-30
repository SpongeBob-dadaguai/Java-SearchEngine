package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.util.Config;

import java.io.File;
import java.io.IOException;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args) throws IOException {
        AbstractDocumentBuilder documentBuilder = new DocumentBuilder();
        AbstractIndexBuilder indexBuilder = new IndexBuilder(documentBuilder);
        String rootDir = Config.DOC_DIR;
//        System.out.println("rootDir: " + rootDir);
        System.out.println("Start to build index ...");
        AbstractIndex index = indexBuilder.buildIndex(rootDir);
        index.optimize();
        System.out.println(index);
        //控制台打印 index 的内容
        //测试保存到文件
        String indexFile = Config.INDEX_DIR + "index.data";
        index.save(new File(indexFile));
        //((Index) index).writePlainText(new File(Config.INDEX_DIR + "index.txt"));
        //索引保存到文件
        //测试从文件读取
        AbstractIndex index2 = new Index(); //创建一个空的 index
        index2.load(new File(indexFile));
        //从文件加载对象的内容
        System.out.println("\n-------------------\n");
        System.out.println(index2);
    }
}
