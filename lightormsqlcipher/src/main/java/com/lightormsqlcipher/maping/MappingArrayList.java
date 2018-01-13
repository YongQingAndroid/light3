package com.lightormsqlcipher.maping;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 映射包装类
 * package kotlinTest:com.qing.orm.maping.MapingArrayList.class
 * 作者：zyq on 2017/7/4 08:40
 * 邮箱：zyq@posun.com
 */
public class MappingArrayList<E> extends ArrayList<E>{
    private MapingBean mapingBean;
    private boolean issearch=false;
    public void setMapingBean(MapingBean mapingBean) {
        this.mapingBean = mapingBean;
    }
    /**
     * 触发子查询
     * */
    private void getdata(){
        if(!issearch){
            issearch=true;
          super.addAll((Collection<? extends E>) mapingBean.getData());
        }
    }
    @Override
    public int size() {
        getdata();
        return super.size();
    }

    @Override
    public E get(int index) {
        getdata();
        return super.get(index);
    }

    @Override
    public boolean add(E e) {
        getdata();
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        getdata();
        return super.addAll(c);
    }

    @Override
    public void add(int index, E element) {
        getdata();
        super.add(index, element);
    }


}
