package com.lightormdatabase;

/**
 * package kotlinTest:com.qing.lightormdatabase.lightorm.OrmBaseBean.class
 * 作者：zyq on 2017/10/30 15:31
 * 邮箱：zyq@posun.com
 */
public class LightOrmBaseBean {
    public void insert(){
     LightOrmHelper.getInstent().save(this);
    }
    public void updata(){
        LightOrmHelper.getInstent().save(this);
    }
    public void remove(){
        LightOrmHelper.getInstent().remove(this);
    }

}
