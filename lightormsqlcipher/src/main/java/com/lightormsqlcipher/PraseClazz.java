package com.lightormsqlcipher;

import com.lightormsqlcipher.fastjson.JSON;
import com.lightormsqlcipher.log.LightLog;
import com.lightormsqlcipher.maping.lightMapping;

import net.sqlcipher.database.SQLiteStatement;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * package kotlinTest:qing.posun.com.calender.orm.PraseClazz.class
 * 作者：zyq on 2017/6/9 16:04
 * 邮箱：zyq@posun.com
 * 字段反射处理类
 *
 * @author zyq
 */
public class PraseClazz {
    private static PraseClazz self;

    private PraseClazz() {
    }

    public static synchronized PraseClazz getInstent() {
        if (self == null)
            self = new PraseClazz();
        return self;
    }

    /**
     * 缓存表信息 避免反复反射浪费性能
     */
    private Map<Class, OrmTableBean> tables = new HashMap<>();

    /**
     * 根据模型创建表
     */
    public String getCreaTabSql(Class clazz) {
        OrmTableBean ormTableBean = getTableMsg(clazz);
        ormTableBean.setCheckColumn(true);
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(ormTableBean.getTableName());
        sql.append(" (");
        Field[] fields = ormTableBean.getFields();
        int size = fields.length;
        for (int i = 0; i < size; i++) {
            Field myfield = fields[i];
            String typeValue=getColumn(myfield);
            if (isPrimaryKey(myfield)) {
                if(Integer.class.isAssignableFrom(myfield.getType()) || int.class.isAssignableFrom(myfield.getType())){
                    LightPrimaryKey myfieldAnnotation = myfield.getAnnotation(LightPrimaryKey.class);
                    if(LightPrimaryKey.AUTOINCREMENT.equalsIgnoreCase(myfieldAnnotation.value())){
                        sql.append(myfield.getName());
                        sql.append(" INTEGER ");
                        sql.append(" PRIMARY KEY");
                        sql.append(" AUTOINCREMENT ");
                    }else {
                        sql.append(typeValue);
                        sql.append(" PRIMARY KEY");
                    }
                }else{
                    sql.append(typeValue);
                    sql.append(" PRIMARY KEY");
                }
                ormTableBean.setPrimaryKey(myfield);
            }else{
                sql.append(typeValue);
            }
            if (i + 1 == size) {
                sql.append(")");
            } else {
                sql.append(",");
            }
        }
        LightLog.i(sql.toString());
        return sql.toString();
    }

    /**
     * 根据模型获取表信息
     */
    public OrmTableBean getTableMsg(Class clazz) {
        OrmTableBean mOrmTableBean;
        if (tables.containsKey(clazz)) {
            return tables.get(clazz);
        }
        LightLog.i("getTableMsg from Class search");
        List<Field> table_field = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        Field primaryField = null;
        Field defult_primaryField = null;
        int size = fields.length;
        Map<String, Field> mapping = new HashMap<>();
        for (int i = 0; i < size; i++) {
            Field myfield = fields[i];
            if (myfield.getName().equalsIgnoreCase("$change") || (myfield.getAnnotation(LightIgnore.class) != null)) {
                continue;
            }
            if (isPrimaryKey(myfield)) {
                primaryField = myfield;
            }
            if (myfield.getName().equalsIgnoreCase("id"))
                defult_primaryField = myfield;
            myfield.setAccessible(true);
            if (praseMapping(myfield, mapping))
                continue;
            table_field.add(myfield);
        }
        if (table_field.size() < 0) {
            throw new RuntimeException("can not get " + getTableName(clazz) + "TableMsg");
        }
        Field[] myfields = table_field.toArray(new Field[table_field.size()]);
        mOrmTableBean = new OrmTableBean(getTableName(clazz), myfields);
        /**解析关系**/
        if (mapping.size() > 0) {
            mOrmTableBean.setMaping(mapping);
        }
        if (primaryField != null) {
            mOrmTableBean.setPrimaryKey(primaryField);
        } else if (defult_primaryField != null) {
            mOrmTableBean.setPrimaryKey(defult_primaryField);
        }
        tables.put(clazz, mOrmTableBean);
        return mOrmTableBean;
    }

    /***
     * 兼容模式表信息字段获取
     */
    public OrmTableBean getExtendTableMsg(Class clazz) {
        if (tables.containsKey(clazz)) {
            return tables.get(clazz);
        }
        return null;
    }

    /**
     * 兼容模式下保存表信息到缓存
     */
    public void saveTableMsg(Class clazz, OrmTableBean arg) {
        tables.put(clazz, arg);
    }

    /**
     * 生成表名
     */
    public String getTableName(Class clazz) {
        Object obj = clazz.getAnnotation(LightTableName.class);
        if (obj != null) {
            LightTableName tableName = (LightTableName) obj;
            return tableName.value();
        }
        return clazz.getName().replaceAll("\\.", "_");
    }

    /**
     * 根据模型获取预编译保存sql语句
     */
    public String getsaveSql(Class clazz) throws Exception {
        OrmTableBean ormTableBean = getTableMsg(clazz);
        if (ormTableBean == null)
            throw new RuntimeException("Table does not exist the bean object is invalid");
        return this.getsaveSql(ormTableBean);
    }

    /**
     * 根据表信息获取预编译保存sql语句
     */
    public String getsaveSql(OrmTableBean ormTableBean) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("REPLACE INTO ");
        sql.append(ormTableBean.getTableName());
        sql.append("(");
        StringBuffer databulider = new StringBuffer();
        databulider.append(" values(");
        int size = ormTableBean.getFields().length;
        for (int i = 0; i < size; i++) {
            Field field = ormTableBean.getFields()[i];
            sql.append(field.getName());
            field.setAccessible(true);
            databulider.append("?");
            if (i + 1 == size) {
                sql.append(")");
                databulider.append(")");
            } else {
                sql.append(",");
                databulider.append(",");
            }

        }
        sql.append(databulider);
        LightLog.i(sql.toString());
        return sql.toString();
    }

    /**
     * 根据表信息获取预编译保存sql语句
     */
    public String getUpdateSql(OrmTableBean ormTableBean) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append(ormTableBean.getTableName());
        sql.append(" SET ");
        int size = ormTableBean.getFields().length;
        for (int i = 0; i < size; i++) {
            Field field = ormTableBean.getFields()[i];
            sql.append(field.getName());
            sql.append("= ?");
        }
        return sql.toString();
    }

    /**
     * 绑定预编语句value 执行插入
     */
    public void saveData(SQLiteStatement statement, Object obj, Class TableClass) throws Exception {
        OrmTableBean ormTableBean = getTableMsg(TableClass);
        if (ormTableBean == null)
            throw new RuntimeException("Table does not exist the bean object is invalid");
        int size = ormTableBean.getFields().length;
        for (int i = 0; i < size; i++) {
            Field field = ormTableBean.getFields()[i];
            Class myclass = field.getType();
            bindStatement(statement, i, myclass, field, obj);
        }
        statement.executeInsert();
    }

    /**
     * @param field     当前字段描述
     * @param i         预编译的字段位置
     * @param myclass   当前需要绑定的数据类型
     * @param statement 预编译Sql
     * @param obj       当前字段的值
     */
    public void bindStatement(SQLiteStatement statement, int i, Class myclass, Field field, Object obj) throws Exception {
        if (Integer.class.isAssignableFrom(myclass) || int.class.isAssignableFrom(myclass)) {
            statement.bindString(i + 1, String.valueOf(field.get(obj)));
        } else if (Long.class.isAssignableFrom(myclass) || long.class.isAssignableFrom(myclass)) {
            statement.bindLong(i + 1, field.getLong(obj));
        } else if (String.class.isAssignableFrom(myclass)) {
            statement.bindString(i + 1, String.valueOf(field.get(obj)));
        } else {
            praseReferenceObject(field.get(obj), statement, i);
        }
    }

    /**
     * 解析引用对象
     */
    private void praseReferenceObject(Object obj, SQLiteStatement statement, int i) throws Exception {
        if (JSON.getFastJson() != null) {
            statement.bindBlob(i + 1, fastObjectToByte(obj));
        } else if (obj instanceof List) {
            statement.bindBlob(i + 1, objectToByte((ArrayList) obj));/**必须指明类型否则会反序列化失败*/
        } else if (obj instanceof Map) {
            statement.bindBlob(i + 1, objectToByte((HashMap) obj));
        } else if (obj instanceof Serializable) {
            statement.bindBlob(i + 1, objectToByte(obj));
        }
    }

    /**
     * 序列化对象
     * 对象 转为 byte[]
     */
    public byte[] objectToByte(Object obj) throws IOException {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            return bos.toByteArray();
        } finally {
            if (oos != null)
                oos.close();
        }
    }

    /**
     * 序列化对象
     * 对象 转为 byte[]
     */
    public byte[] fastObjectToByte(Object obj) throws IOException {
        return JSON.toJSONBytes(obj);
    }

    /**
     * 获取表字段名称及类型
     */
    public String getColumn(Field field) {
        Class type = field.getType();
        String sqltype;
        if (String.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type) || double.class.isAssignableFrom(type) || short.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
            sqltype = "TEXT";
        } else if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
            sqltype = "TEXT";
        } else if (Date.class.isAssignableFrom(type)) {
            sqltype = "TEXT";
        } else {
            sqltype = "BLOB";
        }
        return field.getName() + " " + sqltype;
    }

    /**
     * 判断主键
     */
    private boolean isPrimaryKey(Field field) {
        Object o = field.getAnnotation(LightPrimaryKey.class);
//        return o != null||"id".equalsIgnoreCase(field.getName());
        return o != null;
    }

    /**
     * 判断映射关系
     */
    private boolean praseMapping(Field field, Map<String, Field> map) {
        Object o = field.getAnnotation(lightMapping.class);
        if (o != null) {
            lightMapping mapping = (lightMapping) o;
            map.put(mapping.value(), field);
            return true;
        }
        return false;
    }
}
