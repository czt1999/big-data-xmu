import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.NavigableMap;

public class Task1 {

    /**
     * 1. 列出 HBase 所有表的相关信息
     */
    public static void showTables(Admin admin) throws IOException {
        System.out.println("=============== Tables ===============");
        for (TableDescriptor td : admin.listTableDescriptors()) {
            System.out.println("表名: " + td.getTableName().getNameAsString());
            System.out.print("字段: ");
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
     */
    public static void showRecord(String tableName, Admin admin) throws IOException {
        Table table = admin.getConnection().getTable(TableName.valueOf(tableName));
        ResultScanner scanner = table.getScanner(new Scan());
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

                }
            }
        }
    }

    /**
     * 3. 向已经创建好的表添加和删除指定的列族或列
     */
    public static void addColumn(String tableName, String columnFamily, Admin admin) {

    }

    /**
     * 4. 清空指定表的所有记录数据
     *
     * @param tableName 表名
     */
    public static void truncate(String tableName, Admin admin) {

    }

    /**
     * 5. 统计表的行数
     *
     * @param tableName 表名
     */
    public static void count(String tableName, Admin admin) {

    }
}
