import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.http.util.Asserts;

import java.io.IOException;
import java.util.NavigableMap;

public class Task1 {

    /**
     * 1. 列出 HBase 所有表的相关信息
     *
     * @param admin org.apache.hadoop.hbase.client.Admin
     */
    public static void showTables(Admin admin) throws IOException {
        System.out.println("=============== Tables ===============");
        for (TableDescriptor td : admin.listTableDescriptors()) {
            System.out.println("表名: " + td.getTableName().getNameAsString());
            System.out.print("列族: ");
            for (ColumnFamilyDescriptor cfd : td.getColumnFamilies()) {
                System.out.print(cfd.getNameAsString() + " ");
            }
            System.out.println("\n======================================");
        }
    }

    /**
     * 2. 在终端输出指定表的所有记录数据
     *
     * @param tableName 表名
     * @param admin     org.apache.hadoop.hbase.client.Admin
     */
    public static void showRecord(String tableName, Admin admin) throws IOException {
        TableName tname = TableName.valueOf(tableName);
        Asserts.check(admin.tableExists(tname), tableName + "does not exist");
        Table table = admin.getConnection().getTable(tname);
        ResultScanner scanner = table.getScanner(new Scan());
        System.out.println("=================== " + tableName + " ===================");
        for (Result res : scanner) {
            // 行号
            String rowKey = new String(res.getRow());
            NavigableMap<byte[], NavigableMap<byte[], byte[]>> familyMap = res.getNoVersionMap();
            for (byte[] fByte : familyMap.keySet()) {
                // 列族
                String fName = new String(fByte);
                NavigableMap<byte[], byte[]> qualifierMap = familyMap.get(fByte);
                for (byte[] qByte : qualifierMap.keySet()) {
                    // 限定符
                    String qName = new String(qByte);
                    // 值
                    String value = new String(qualifierMap.get(qByte));
                    System.out.printf("row_key: %s, column family: %s, qualifier: %s, value: %s\n",
                            rowKey, fName, qName, value);
                }
            }
        }
        System.out.println(StringUtils.repeat('=', 40 + tableName.length()));
    }

    /**
     * 3.1 向已经创建好的表添加指定的列族或列
     *
     * @param tableName    表名
     * @param columnFamily 列族名
     * @param admin        org.apache.hadoop.hbase.client.Admin
     */
    public static void addColumnFamily(String tableName, String columnFamily,
                                       Admin admin) throws IOException {
        System.out.print("ADD " + tableName + "{" + columnFamily + "}" + " >> ");
        TableName tname = TableName.valueOf(tableName);
        Asserts.check(admin.tableExists(tname), tableName + "does not exist");
        ColumnFamilyDescriptor desc = ColumnFamilyDescriptorBuilder.of(columnFamily);
        admin.addColumnFamily(tname, desc);
        System.out.println("FINISHED");
    }

    /**
     * 3.2 向已经创建好的表删除指定的列族或列
     *
     * @param tableName    表名
     * @param columnFamily 列族名
     * @param admin        org.apache.hadoop.hbase.client.Admin
     */
    public static void deleteColumnFamily(String tableName, String columnFamily,
                                          Admin admin) throws IOException {
        System.out.print("DELETE " + tableName + "{" + columnFamily + "}" + " >> ");
        TableName tname = TableName.valueOf(tableName);
        Asserts.check(admin.tableExists(tname), tableName + "does not exist");
        admin.deleteColumnFamily(tname, columnFamily.getBytes());
        System.out.println("FINISHED");
    }

    /**
     * 4. 清空指定表的所有记录数据
     *
     * @param tableName 表名
     * @param admin     org.apache.hadoop.hbase.client.Admin
     */
    public static void truncate(String tableName, Admin admin) throws IOException {
        System.out.println("TRUNCATE " + tableName + " >> ");
        TableName tname = TableName.valueOf(tableName);
        Asserts.check(admin.tableExists(tname), tableName + "does not exist");
        // disable firstly
        admin.disableTable(tname);
        admin.truncateTable(tname, true);
        System.out.println("FINISHED");
    }

    /**
     * 5. 统计表的行数
     *
     * @param tableName 表名
     * @param admin     org.apache.hadoop.hbase.client.Admin
     */
    public static void count(String tableName, Admin admin) throws IOException {
        System.out.print("COUNT ROWS OF " + tableName + " >> ");
        TableName tname = TableName.valueOf(tableName);
        Asserts.check(admin.tableExists(tname), tableName + "does not exist");
        Table table = admin.getConnection().getTable(tname);
        // 只获取每一行的第一个key
        Scan scan = new Scan().setFilter(new FirstKeyOnlyFilter());
        ResultScanner scanner = table.getScanner(scan);
        long count = 0;
        for (Result r = scanner.next(); r != null; r = scanner.next(), ++count) ;
        System.out.println(count);
    }
}
