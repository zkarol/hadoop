package pl.com.sages.hbase.api;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import pl.com.sages.hbase.api.util.HBaseUtil;

import java.io.File;
import java.io.FileInputStream;

import static pl.com.sages.hbase.api.util.HbaseConfigurationFactory.getConfiguration;

/**
 * Pobranie lektur:
 * scp -r login@hdp1:/tmp/lektury /home/users/login/bigdata
 * <p>
 * Usunięcie wpisu w known hosts (problem dostępu)
 * ssh-keygen -R hdp1
 */
public class RadekBooksLoader {

    private static final String BOOKS = "/home/users/login/bigdata/lektury/lektury-all";
//    public static final String BOOKS = "/home/radek/Sages/dane/lektury/lektury-all";

    private static final TableName TABLE_NAME = HBaseUtil.getUserTableName("books");
    private static final String FAMILY_NAME = "cf";


    // psvm
    public static void main(String[] args) throws Exception {
        // wczytywanie lektur do bazy (lektury-all)
        // tabelka (name, content) z zbioru lektury-all
        Configuration configuration = getConfiguration();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Admin admin = connection.getAdmin();

        if (admin.tableExists(TABLE_NAME)) {
            admin.disableTable(TABLE_NAME);
            admin.deleteTable(TABLE_NAME);
        }
        admin.createTable(new HTableDescriptor(TABLE_NAME).addFamily(new HColumnDescriptor(FAMILY_NAME)));

        File[] files = new File(BOOKS).listFiles();
        if (files != null) {
            for (File file : files) {
                String content = IOUtils.toString(new FileInputStream(file));
                System.out.println(content);
            }
        }
    }

}
