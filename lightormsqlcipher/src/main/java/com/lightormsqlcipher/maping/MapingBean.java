package com.lightormsqlcipher.maping;

import com.lightormsqlcipher.LightOrmHelper;
import com.lightormsqlcipher.WhereBulider;

import java.util.List;

/**
 * package kotlinTest:com.qing.orm.maping.MapingBean.class
 * 作者：zyq on 2017/7/4 09:42
 * 邮箱：zyq@posun.com
 * 子查询包装类
 * 该功能不支持兼容模式
 */
public class MapingBean {
    /***子查询条件句柄**/
    private WhereBulider whereBulider;
    private Class tableClass;

    public MapingBean( WhereBulider whereBulider, Class tableClass) {
        this.tableClass = tableClass;
        this.whereBulider = whereBulider;
    }

    public void setWhereBulider(WhereBulider whereBulider) {
        this.whereBulider = whereBulider;
    }

    public <T> List<T> getData() {
        return LightOrmHelper.getInstent().query(tableClass, whereBulider);
    }
}
