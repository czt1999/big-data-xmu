import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.http.util.Asserts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.stream.Collectors;

public class Task2 {

    /**
     * 创建表
     * 若已存在同名的表，则删除原有的表，再创建新表
     *
     * @param tableName 表名
     * @param fields    列族
     * @param admin     org.apache.hadoop.hbase.client.Admin
     */
    public static void createTable(String tableName, String[] fields, Admin admin) throws IOException {
        Asserts.check(ArrayUtils.isNotEmpty(fields), "Table should have at least one column family");
        TableName tname = TableName.valueOf(tableName);
        if (admin.tableExists(tname)) {
            admin.disableTable(tname);
            admin.deleteTable(tname);
        }
        List<ColumnFamilyDescriptor> families = Arrays.stream(fields)
                .map(ColumnFamilyDescriptorBuilder::of)
                .collect(Collectors.toList());
        admin.createTable(TableDescriptorBuilder.newBuilder(tname).setColumnFamilies(families).build());
    }

    /**
     * 向指定单元格添加对应的数据
     * 如果 fields 每个元素对应的列族下还有限定符，用 family:column 表示
     *
     * @param tableName 表名
     * @param rowKey    行号
     * @param fields    列族:限定符
     * @param values    值
     * @param admin     org.apache.hadoop.hbase.client.Admin
     */
    public static void addRecord(String tableName, String rowKey, String[] fields,
                                 String[] values, Admin admin) throws IOException {
        TableName tname = TableName.valueOf(tableName);
        Asserts.check(admin.tableExists(tname), tableName + "does not exist");
        Asserts.check(ArrayUtils.isNotEmpty(fields) && ArrayUtils.isSameLength(fields, values),
                "Illegal length of fields or values");
        Table table = admin.getConnection().getTable(tname);
        byte[] rowBytes = rowKey.getBytes();
        List<Put> puts = new ArrayList<>(fields.length);
        CellBuilder cellBuilder = CellBuilderFactory.create(CellBuilderType.SHALLOW_COPY);
        // SHALLOW_COPY: the builder just copy the references of passed bytes array
        for (int i = 0; i < fields.length; ++i) {
            String[] split = fields[i].split(":");
            Asserts.check(2 >= split.length, "Wrong format of field: " + fields[i]);
            Put put = new Put(rowBytes);
            cellBuilder = cellBuilder.setType(Cell.Type.Put).setRow(rowBytes)
                    .setFamily(split[0].getBytes()).setValue(values[i].getBytes());
            if (2 == split.length) {
                cellBuilder.setQualifier(split[1].getBytes());
            }
            puts.add(new Put(rowBytes).add(cellBuilder.build()));
        }
        table.put(puts);
    }

    /**
     * 浏览表某一列的数据
     * 1）当某一行记录中该列数据不存在时，返回null
     * 2）当 column 为列族名称时，若底下有若干限定符，列出每个限定符代表的列数据
     * 3）当 column 为某一列具体名称，只需列出该列数据
     *
     * @param tableName 表名
     * @param column    列族或列
     * @param admin     org.apache.hadoop.hbase.client.Admin
     */
    public static void scanColumn(String tableName, String column, Admin admin) throws IOException {
        TableName tname = TableName.valueOf(tableName);
        Asserts.check(admin.tableExists(tname), tableName + "does not exist");
        String split[] = column.split(":");
        Asserts.check(2 >= split.length, "Wrong format of column: " + column);
        Table table = admin.getConnection().getTable(tname);
        // 获取列族
        String family = split[0];
        byte[] familyBytes = family.getBytes();
        // 限定符
        String qualifier = (2 == split.length ? split[1] : "");
        byte[] qualifierBytes = qualifier.getBytes();
        // 开始扫描
        ResultScanner scanner = table.getScanner(new Scan().addFamily(familyBytes));
        System.out.println("=================== " + tableName + " ===================");
        for (Result res : scanner) {
            String rowKey = new String(res.getRow());
            NavigableMap<byte[], byte[]> familyMap = res.getFamilyMap(familyBytes);
            if (familyMap.isEmpty()) {
                System.out.printf("row_key: %s, column family: %s, value: null\n", rowKey, family);
                continue;
            }
            if (!qualifier.isEmpty()) {
                // 给出了具体的限定符
                byte[] value = familyMap.get(qualifierBytes);
                System.out.printf("row_key: %s, column family: %s, qualifier: %s, value: %s\n",
                        rowKey, family, qualifier, null == value ? "null" : new String(value));
                continue;
            }
            for (byte[] qBytes : familyMap.keySet()) {
                System.out.printf("row_key: %s, column family: %s, qualifier: %s, value: %s\n",
                        rowKey, family, new String(qBytes), new String(familyMap.get(qBytes)));
            }
        }
        System.out.println(StringUtils.repeat('=', 40 + tableName.length()));
    }

    /**
     * 修改指定的单元格数据
     *
     * @param tableName 表名
     * @param row       行
     * @param column    列
     * @param value     数据
     * @param admin     org.apache.hadoop.hbase.client.Admin
     */
    public static void modifyData(String tableName, String row, String column,
                                  String value, Admin admin) throws IOException {
        TableName tname = TableName.valueOf(tableName);
        Asserts.check(admin.tableExists(tname), tableName + "does not exist");
        Table table = admin.getConnection().getTable(tname);
        byte[] rowBytes = row.getBytes();
        Asserts.check(table.exists(new Get(rowBytes)), "row " + row + " does not exist");
        String split[] = column.split(":");
        Asserts.check(2 >= split.length, "Wrong format of column: " + column);
        CellBuilder cellBuilder = CellBuilderFactory.create(CellBuilderType.SHALLOW_COPY)
                .setType(Cell.Type.Put).setRow(rowBytes)
                .setFamily(split[0].getBytes()).setValue(value.getBytes());
        if (2 == split.length) {
            cellBuilder.setQualifier(split[1].getBytes());
        }
        table.put(new Put(rowBytes).add(cellBuilder.build()));
    }

    /**
     * 删除指定的行的记录
     *
     * @param tableName 表名
     * @param row       行号
     * @param admin     org.apache.hadoop.hbase.client.Admin
     */
    public static void deleteRow(String tableName, String row, Admin admin) throws IOException {
        TableName tname = TableName.valueOf(tableName);
        Asserts.check(admin.tableExists(tname), tableName + "does not exist");
        admin.getConnection().getTable(tname).delete(new Delete(row.getBytes()));
    }
}
