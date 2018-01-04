package com.sam.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

/**
 * @Author: SamZhao
 * @Description: 搜索查询
 * @Date: 2018/01/04 14:57
 */
public class IndexSearchTest {

    /**
     * 搜索
     *
     * @throws Exception
     */
    @Test
    public void testIndexSearch() throws Exception {
        //创建分词器
        //创建索引和搜索时所用的分词器必须一致
        Analyzer analyzer = new IKAnalyzer();

        //指定索引和文档的目录
        Directory directory = FSDirectory.open(new File("E:\\Code\\study\\lucene\\lucene20180104\\directory"));

        //索引和文档的读取对象
        IndexReader indexReader = DirectoryReader.open(directory);

        //创建索引的搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //创建查询对象
        //1.String f    默认搜索域
        //2.Analyzer a  分词器
        //默认搜索域作用： 如搜索语法中指定域名，则从指定域中搜索  如果搜索只写了查询关键字，则从默认搜索域中搜索
        QueryParser queryParser = new QueryParser("fileContext", analyzer);
        //查询语法 域名：搜索关键字
        Query query = queryParser.parse("fileContext:java");

        //1.Query query 查询语句对象
        //2.int n       指定显示多少条
        TopDocs topDocs = indexSearcher.search(query, 10);

        //一共搜索到多少条记录
        System.out.println("=========count=========="+topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs){
            //获取doc id （自动生成的）
            int docID = scoreDoc.doc;
            System.out.println();
            System.out.println("=========docID=========="+docID);
            //通过文档id 从硬盘中读取出对应文档
            Document document = indexReader.document(docID);
            //get 域名 可以取值
            System.out.println("fileName: "+document.get("fileName"));
            System.out.println("fileSize: "+document.get("fileSize"));
            System.out.println();
        }

        indexReader.close();

    }
}
