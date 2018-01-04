package com.sam.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SamZhao
 * @Description: 管理 增删改
 * @Date: 2018/01/04 14:56
 */
public class IndexManagerTest {

    /**
     * 采集文件系统中的文档数据，放入lucene中
     *
     * @throws Exception
     */
    @Test
    public void testIndexCreate() throws Exception {
        //创建文档列表 保存多个document
        List<Document> documentList = new ArrayList<Document>();

        //指定文件所在目录
        File dir = new File("E:\\Code\\study\\lucene\\lucene20180104\\searchsource");
        //循环文件夹 取出文件
        for (File file : dir.listFiles()) {
            //文件名
            String fileName = file.getName();
            //文件内容
            String fileContext = FileUtils.readFileToString(file);
            //文件大小
            Long fileSize = FileUtils.sizeOf(file);
            System.out.println(fileName + " ; " + fileSize);

            //文档对象 文件系统中的一个文件就是一个document对象
            Document document = new Document();

            //第一个参数:域名
            //第二个参数:域值
            //第三个参数:是否存储,是为yes,不存储为no
			/*TextField nameFiled = new TextField("fileName", fileName, Store.YES);
			TextField contextFiled = new TextField("fileContext", fileContext, Store.YES);
			TextField sizeFiled = new TextField("fileSize", fileSize.toString(), Store.YES);*/

            //是否分词:要,因为它要索引,并且它不是一个整体,分词有意义
            //是否索引:要,因为要通过它来进行搜索
            //是否存储:要,因为要直接在页面上显示
            TextField nameFiled = new TextField("fileName", fileName, Field.Store.YES);

            //是否分词: 要,因为要根据内容进行搜索,并且它分词有意义
            //是否索引: 要,因为要根据它进行搜索
            //是否存储: 可以要也可以不要,不存储搜索完内容就提取不出来
            TextField contextFiled = new TextField("fileContext", fileContext, Field.Store.NO);

            //是否分词: 要, 因为数字要对比,搜索文档的时候可以搜大小, lunene内部对数字进行了分词算法
            //是否索引: 要, 因为要根据大小进行搜索
            //是否存储: 要, 因为要显示文档大小
            LongField sizeFiled = new LongField("fileSize", fileSize, Field.Store.YES);

            //将所有的域存入文档
            document.add(nameFiled);
            document.add(contextFiled);
            document.add(sizeFiled);

            //将所有文档存入集合
            documentList.add(document);

        }

        //创建分词器 StandardAnalyzer是标准分词器 对英文效果很好，对中文是单字分词
//        Analyzer analyzer = new IKAnalyzer();
        Analyzer analyzer = new IKAnalyzer();

        //指定索引和文档的存储目录
        Directory directory = FSDirectory.open(new File("E:\\Code\\study\\lucene\\lucene20180104\\directory"));

        //创建写对象的初始化对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);

        //创建索引的写入对象
        //1.Directory
        //2.IndexWriterConfig
        IndexWriter indexWriter = new IndexWriter(directory, config);

        for (Document document : documentList) {//将文档加入到索引和文档的写对象中
            indexWriter.addDocument(document);
        }
        //提交
        indexWriter.commit();
        //关闭
        indexWriter.close();

    }
}
