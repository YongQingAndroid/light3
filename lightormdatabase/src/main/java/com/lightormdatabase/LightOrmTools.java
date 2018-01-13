package com.lightormdatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.lightormdatabase.fastjson.JSON;
import com.lightormdatabase.log.LightLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * orm数据库工具库
 * 兼容实现类
 * 兼容模式下bean 对象成员变量需要和所对应的表格字段相同（否则会丢失数据精度）
 * 作者：zyq on 2017/7/4 08:53
 * 邮箱：zyq@posun.com
 *
 * @author zyq
 */
public final class LightOrmTools {
    private static LightOrmTools self;
    private WeakReference<SQLiteOpenHelper> sqLiteOpenHelperWeakReference;

    private LightOrmTools() {
    }

    /**
     * 内部单例调用接口
     */
    protected static synchronized LightOrmTools getInstent() {
        if (self == null)
            self = new LightOrmTools();
        return self;
    }

    /**
     * 弱引用第三方拓展数据库对象
     *
     * @param sqLiteOpenHelper
     */
    public LightOrmTools setSQLiteOpenHelper(SQLiteOpenHelper sqLiteOpenHelper) {
        sqLiteOpenHelperWeakReference = new WeakReference<>(sqLiteOpenHelper);
        return this;
    }

    /**
     * 兼容模式执行删除操作
     *
     * @param table_name   表格名字
     * @param whereBulider sql条件
     */
    public void remove(String table_name, WhereBulider whereBulider) {
        if (sqLiteOpenHelperWeakReference.get() == null) {
            LightLog.e(" save file error : sqLiteDatabase is null");
            return;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("delete from ");
        sql.append(table_name);
        sql.append(whereBulider.toString());
        LightLog.i(sql.toString() + " values= " + Arrays.toString(whereBulider.getvalue()));
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = sqLiteOpenHelperWeakReference.get().getWritableDatabase();
            if (!havetable(table_name, sqLiteDatabase)) {
                LightLog.e(table_name + " Table does not exist");
                return;
            }
            executeSql(sqLiteDatabase, sql.toString(), whereBulider.getvalue());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

    }

    /**
     * 兼容模式执行自定义Sql语句
     *
     * @param sql
     * @param value
     */
    public void executeSql(String sql, String... value) {
        if (sqLiteOpenHelperWeakReference.get() == null) {
            LightLog.e(" save file error : sqLiteDatabase is null");
            return;
        }
        LightLog.i(sql);
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = sqLiteOpenHelperWeakReference.get().getWritableDatabase();
            if (value != null && value.length > 0) {
                sqLiteDatabase.execSQL(sql, value);
            } else {
                sqLiteDatabase.execSQL(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
    }

    /**
     * 兼容性执行sql语句
     *
     * @param sqLiteDatabase 数据库队像
     * @param value          数据
     * @param sql
     */
    private void executeSql(SQLiteDatabase sqLiteDatabase, String sql, String... value) {
        if (sqLiteOpenHelperWeakReference.get() == null) {
            LightLog.e(" save file error : sqLiteDatabase is null");
            return;
        }
        LightLog.i(sql);
        if (value != null && value.length > 0) {
            sqLiteDatabase.execSQL(sql, value);
        } else {
            sqLiteDatabase.execSQL(sql);
        }
    }

    /**
     * 兼容性执行数据库Update
     *
     * @param tableName    表格名字
     * @param object       数据实体或者集合
     * @param whereBulider 条件
     */
    public void update(Object object, String tableName, WhereBulider whereBulider) {
        if (sqLiteOpenHelperWeakReference.get() == null) {
            LightLog.e(tableName + " save file error : sqLiteDatabase is null");
            return;
        }
        Class clazz;
        if (object == null)
            return;
        boolean islist = object instanceof List;
        if (islist) {
            if (((List) object).size() < 1) {
                return;
            }
            clazz = ((List) object).get(0).getClass();
        } else {
            clazz = object.getClass();
        }
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = sqLiteOpenHelperWeakReference.get().getWritableDatabase();
            updateExecute(object, tableName, islist, clazz, sqLiteDatabase, whereBulider);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
    }

    /**
     * 兼容模式模式数据保存
     * 预处理表字段和模型的对应关系
     *
     * @param object    数据实体或者集合
     * @param tableName 表格名字
     */
    public void save(Object object, String tableName) {
        if (sqLiteOpenHelperWeakReference.get() == null) {
            LightLog.e(tableName + " save file error : sqLiteDatabase is null");
            return;
        }
        Class clazz;
        if (object == null)
            return;
        boolean islist = object instanceof List;
        if (islist) {
            if (((List) object).size() < 1) {
                return;
            }
            clazz = ((List) object).get(0).getClass();
        } else {
            clazz = object.getClass();
        }
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = sqLiteOpenHelperWeakReference.get().getWritableDatabase();
            saveExecute(object, tableName, islist, clazz, sqLiteDatabase);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
    }

    /**
     * 用于兼容模式字段保存
     *
     * @param islist         是否为集合
     * @param clazz
     * @param sqLiteDatabase 数据库对象
     * @param object         数据实体或者集合
     * @param tableName      表格名字
     */
    private void saveExecute(Object object, String tableName, boolean islist, Class clazz, SQLiteDatabase sqLiteDatabase) throws Exception {

        if (!havetable(tableName, sqLiteDatabase)) {
            LightLog.e(tableName + " Table does not exist");
            return;
        }
        OrmTableBean ormTableBean = PraseClazz.getInstent().getExtendTableMsg(clazz);
        if (ormTableBean == null) {
            ormTableBean = new OrmTableBean();
            preseTableWithOrmTableBean(ormTableBean, clazz, sqLiteDatabase, tableName);
        }
        try {
            sqLiteDatabase.beginTransaction();
            SQLiteStatement statement = sqLiteDatabase.compileStatement(PraseClazz.getInstent().getsaveSql(ormTableBean));
            if (islist) {
                List list = (List) object;
                for (Object item : list) {
                    PraseClazz.getInstent().saveData(statement, item, clazz);
                }
            } else {
                PraseClazz.getInstent().saveData(statement, object, clazz);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                sqLiteDatabase.endTransaction();
            } catch (Exception e) {
                sqLiteDatabase.close();
            }
        }
    }

    /**
     * 用于兼容模式字段更新
     *
     * @param islist         是否为集合
     * @param clazz
     * @param sqLiteDatabase 数据库对象
     * @param object         数据实体或者集合
     * @param tableName      表格名字
     */
    private void updateExecute(Object object, String tableName, boolean islist, Class clazz, SQLiteDatabase sqLiteDatabase, WhereBulider whereBulider) throws Exception {

        if (!havetable(tableName, sqLiteDatabase)) {
            LightLog.e(tableName + " Table does not exist");
            return;
        }
        OrmTableBean ormTableBean = PraseClazz.getInstent().getExtendTableMsg(clazz);
        if (ormTableBean == null) {
            ormTableBean = new OrmTableBean();
            preseTableWithOrmTableBean(ormTableBean, clazz, sqLiteDatabase, tableName);
        }
        try {
            sqLiteDatabase.beginTransaction();
            StringBuilder sql = new StringBuilder();
            sql.append(PraseClazz.getInstent().getUpdateSql(ormTableBean));
            sql.append(" ");
            sql.append(whereBulider.toString());
            SQLiteStatement statement = sqLiteDatabase.compileStatement(sql.toString());
            if (islist) {
                List list = (List) object;
                for (Object item : list) {
                    PraseClazz.getInstent().saveData(statement, item, clazz);
                }
            } else {
                PraseClazz.getInstent().saveData(statement, object, clazz);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                sqLiteDatabase.endTransaction();
            } catch (Exception e) {
                sqLiteDatabase.close();
            }
        }
    }

    /**
     * 用于兼容模式
     */
    public void preseTableWithOrmTableBean(OrmTableBean ormTableBean, Class clazz, SQLiteDatabase sqLiteDatabase, String tableName) {
        ormTableBean.setTableName(tableName);
        List<String> coumns = getTableCoumn(ormTableBean, sqLiteDatabase);
        if (coumns != null && coumns.size() > 0) {
            Field[] fields = getFieldFromCoumns(coumns, clazz);
            if (fields != null && fields.length > 0) {
                ormTableBean.setFields(fields);
                PraseClazz.getInstent().saveTableMsg(clazz, ormTableBean);
            }
        }

        if (ormTableBean.getFields() == null || ormTableBean.getFields().length < 1) {
            throw new RuntimeException(" Table can not get field");
        }

    }

    /**
     * 兼容模式查询数据库
     * 注意：兼容模式不支持关系映射以及子查询
     *
     * @param clazz     表格对象
     * @param tableName 表名
     */
    public <T> List<T> query(Class<T> clazz, String tableName) {
        return this.query(clazz, tableName, null);
    }

    /**
     * 兼容模式查询数据库
     * 注意：兼容模式不支持关系映射以及子查询
     *
     * @param clazz        表格对象
     * @param tableName    表名
     * @param whereBulider 条件
     */
    public <T> List<T> query(Class<T> clazz, String tableName, WhereBulider whereBulider) {
        if (sqLiteOpenHelperWeakReference.get() == null) {
            LightLog.e(tableName + " query file error");
        }
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelperWeakReference.get().getReadableDatabase();
        if (!havetable(tableName, sqLiteDatabase)) {
            LightLog.e(tableName + " Table does not exist");
            return null;
        }
        OrmTableBean ormTableBean = PraseClazz.getInstent().getExtendTableMsg(clazz);
        if (ormTableBean == null) {
            ormTableBean = new OrmTableBean();
            preseTableWithOrmTableBean(ormTableBean, clazz, sqLiteDatabase, tableName);
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select * from ");
        sql.append(tableName);
        if (whereBulider != null) {
            sql.append(whereBulider.toString());
        } else {
            whereBulider = WhereBulider.creat();
        }
        LightLog.i(sql.toString());
        int size = ormTableBean.getFields().length;
        List list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql.toString(), whereBulider.getvalue());
        try {
            while (cursor.moveToNext()) {
                Object item = clazz.newInstance();
                for (int i = 0; i < size; i++) {
                    Field field = ormTableBean.getFields()[i];
                    String name = field.getName();
                    int index = cursor.getColumnIndex(name);
                    if (index == -1)
                        continue;
                    field.setAccessible(true);
                    Object myobj = getvalue(cursor, field.getType(), index);
                    if (myobj != null)
                        field.set(item, myobj);
                }
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return list;
    }

    /**
     * 兼容模式查询数据库
     * 查询结果一JSON输出
     * 注意：兼容模式不支持关系映射以及子查询
     *
     * @param tableName    表名
     * @param whereBulider 条件
     */
    public String queryJSON(String tableName, WhereBulider whereBulider) {
        if (sqLiteOpenHelperWeakReference.get() == null) {
            LightLog.e(tableName + " query file error");
        }
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelperWeakReference.get().getReadableDatabase();
        if (!havetable(tableName, sqLiteDatabase)) {
            LightLog.e(tableName + " Table does not exist");
            return null;
        }

        StringBuffer sql = new StringBuffer();
        sql.append("select * from ");
        sql.append(tableName);
        if (whereBulider != null) {
            sql.append(whereBulider.toString());
        } else {
            whereBulider = WhereBulider.creat();
        }
        LightLog.i(sql.toString());
        JSONArray jsonArray = new JSONArray();
        Cursor cursor = sqLiteDatabase.rawQuery(sql.toString(), whereBulider.getvalue());
        int size = cursor.getColumnCount();
        try {
            while (cursor.moveToNext()) {
                JSONObject jb = new JSONObject();
                for (int i = 0; i < size; i++) {
                    jb.putOpt(cursor.getColumnName(i), cursor.getString(i));
                }
                jsonArray.put(jb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return jsonArray.toString();
    }

//    public List queryCustom(String tableName, WhereBulider whereBulider,List list) {
//        if (sqLiteOpenHelperWeakReference.get() == null) {
//            LightLog.e(tableName + " query file error");
//        }
//        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelperWeakReference.get().getReadableDatabase();
//        if (!havetable(tableName, sqLiteDatabase)) {
//            LightLog.e(tableName + " Table does not exist");
//            return null;
//        }
//    }
    /**
     * 获取游标数据
     */
    public Object getvalue(Cursor cursor, Class clazz, int index) throws Exception {
        if (int.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz)) {
            return cursor.getInt(index);
        } else if (long.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)) {
            return cursor.getLong(index);
        } else if (String.class.isAssignableFrom(clazz)) {
            return cursor.getString(index);
        } else {
            return praseReferenceClass(cursor.getBlob(index), clazz);
        }
    }

    /**
     * 解析引用对象
     */
    private Object praseReferenceClass(byte[] bytes, Class clazz) throws Exception {
        if (JSON.getFastJson() != null) {
            return fastByteToObject(bytes, clazz);
        } else {
            return byteToObject(bytes);
        }
    }

    /**
     * 用于兼容模式以数据库表字段为蓝本过滤模型字段
     */
    private Field[] getFieldFromCoumns(List<String> coumns, Class clazz) {
        List<Field> fields = new ArrayList<>();
        try {
            for (String item : coumns) {
                Field field = clazz.getDeclaredField(item);
                if (field != null)
                    fields.add(field);
            }
        } catch (Exception e) {
            LightLog.e(e.getMessage());
        }
        return fields.toArray(new Field[fields.size()]);

    }

    /**
     * 程序启动第一次操作该表检查和模型的一致性
     **/
    protected void checktable(Class clazz, SQLiteDatabase mysqLiteDatabase) {
        OrmTableBean ormTableBean = PraseClazz.getInstent().getTableMsg(clazz);
        if (!ormTableBean.isCheckColumn()) {
            LightLog.i("auto check table name" + ormTableBean.getTableName());
            List<String> coumns = getTableCoumn(ormTableBean, mysqLiteDatabase);
            if (coumns != null && coumns.size() > 0) {
                upDataTableColumns(coumns, ormTableBean, mysqLiteDatabase);
            }
        }
    }

    /**
     * 获取当前表的所有列
     */
    protected List<String> getTableCoumn(OrmTableBean ormTableBean, SQLiteDatabase mysqLiteDatabase) {
        String sql = "pragma table_info([" + ormTableBean.getTableName() + "])";
        Cursor cursor = null;
        List<String> mColumns = new ArrayList<>();
        try {
            cursor = mysqLiteDatabase.rawQuery(sql, new String[]{});
            if (cursor == null)
                return mColumns;
            while (cursor.moveToNext()) {
                mColumns.add(cursor.getString(cursor.getColumnIndex("name")));
            }
            return mColumns;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            ormTableBean.setCheckColumn(true);
        }
        return mColumns;
    }

    /**
     * 反序列化
     * byte[] 转为 对象
     */
    public Object byteToObject(byte[] bytes) throws Exception {
        ObjectInputStream ois = null;
        if (bytes == null || bytes.length < 0)
            return null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ois != null)
                ois.close();
        }
    }

    /**
     * 反序列化
     * byte[] 转为 对象
     */
    public Object fastByteToObject(byte[] bytes, Class clazz) throws Exception {
        return JSON.parseObject(bytes, clazz);
    }

    /**
     * 自动更新字段（只支持拓展增加）
     * 该方法只会check模型与数据库字段名并不会检查字段类型不可更改主键
     */
    private void upDataTableColumns(List<String> columns, OrmTableBean ormTableBean, SQLiteDatabase mysqLiteDatabase) {
        if (ormTableBean == null)
            return;
        List<Field> addcolumns = new ArrayList<>();
        int size = ormTableBean.getFields().length;
        for (int i = 0; i < size; i++) {
            if (!LightOrmTools.getInstent().haveColumn(ormTableBean.getFields()[i], columns)) {
                addcolumns.add(ormTableBean.getFields()[i]);
            }
        }
        if (addcolumns.size() > 0) {
            LightLog.e("auto update " + ormTableBean.getTableName() + " TableColumns");
            for (Field item : addcolumns) {
                String sql = "alter table " + ormTableBean.getTableName() + " add " + PraseClazz.getInstent().getColumn(item);
                mysqLiteDatabase.execSQL(sql);
                LightLog.e(sql);
            }
        }
    }

    /**
     * 探测模型字段是否在表中
     */
    public boolean haveColumn(Field field, List<String> columns) {
        String name = field.getName();
        for (String item : columns) {
            if (name.equals(item))
                return true;
        }
        return false;
    }

    /**
     * 判断当前表是否存在
     */
    public boolean havetable(String name, SQLiteDatabase mysqLiteDatabase) {
        String sql = "SELECT sql FROM  SQLITE_MASTER WHERE name =?";
        Cursor cursor = null;
        try {
            cursor = mysqLiteDatabase.rawQuery(sql, new String[]{name});
            if (cursor != null && cursor.moveToNext())
                return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    /**
     * 获取表字段信息
     */
    public Field[] getTableFields(Class mClass) {
        return PraseClazz.getInstent().getTableMsg(mClass).getFields();
    }
}
