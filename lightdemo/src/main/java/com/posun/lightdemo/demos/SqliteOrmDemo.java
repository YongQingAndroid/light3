package com.posun.lightdemo.demos;

import android.app.Activity;
import android.app.Application;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lightormdatabase.LightIgnore;
import com.lightormdatabase.LightOrmHelper;
import com.lightormdatabase.LightPrimaryKey;
import com.lightormdatabase.LightTableName;
import com.lightormdatabase.WhereBulider;
import com.posun.lightdemo.R;
import com.posun.lightui.citypicker.LightDialog;

import java.math.BigDecimal;
import java.util.List;

/**
 * package light3:com.posun.lightdemo.demos.SqliteOrmDemo.class
 * 作者：zyq on 2018/3/1 14:41
 * 邮箱：zyq@posun.com
 */

public class SqliteOrmDemo implements View.OnClickListener {
    String[] data = new String[]{"增", "删", "改", "查"};
    private LinearLayout linearLayout;

    public SqliteOrmDemo(Activity activity) {
        LightOrmHelper.initSdk(activity.getApplication());
        linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.color.colorPrimary);
        for (String item : data) {
            Button button = new Button(activity);
            button.setOnClickListener(this);
            button.setText(item);
            linearLayout.addView(button);
        }
    }

    private TextBean textBean;

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        switch (button.getText().toString()) {
            case "增":
                textBean = new TextBean();
                textBean.setId("1");
                textBean.setName("Tom");
                LightOrmHelper.getInstent().save(textBean);
                Toast.makeText(view.getContext(), "插入tom成功",Toast.LENGTH_SHORT).show();
                break;
            case "删":
                if(textBean==null){
                    LightOrmHelper.getInstent().remove(TextBean.class);
                    return;
                }
                LightOrmHelper.getInstent().remove(textBean);
                Toast.makeText(view.getContext(), "删除"+textBean.getName()+"成功",Toast.LENGTH_SHORT).show();
                break;
            case "改":
                if(textBean==null){
                    Toast.makeText(view.getContext(), "需要创建一个对象才能修改请点击查找或插入", Toast.LENGTH_SHORT).show();
                    return;
                }
                textBean.setName("lisa");
                LightOrmHelper.getInstent().updata(textBean);
                Toast.makeText(view.getContext(), "更新为lisa",Toast.LENGTH_SHORT).show();
                break;
            case "查":
                List<TextBean> list = LightOrmHelper.getInstent().query(TextBean.class);
                if (list == null || list.size() < 1) {
                    Toast.makeText(view.getContext(), "没有数据", Toast.LENGTH_SHORT).show();
                } else {
                    textBean=list.get(0);
                    Toast.makeText(view.getContext(), "name= " + textBean.getName(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
//    {   //初始化
//        LightOrmHelper.initSdk(Application);
//        //增
//        LightOrmHelper.getInstent().save(textBean);
//        LightOrmHelper.getInstent().save(List);
//        //刪
//        LightOrmHelper.getInstent().remove(textBean);
//        LightOrmHelper.getInstent().remove(List);
//        LightOrmHelper.getInstent().remove(TextBean.class);//删除当前表
//        //改
//        LightOrmHelper.getInstent().updata(textBean);
//        LightOrmHelper.getInstent().updata(List);
//        //查
//        LightOrmHelper.getInstent().query(TextBean.class);
//        //带条件的查询
//        LightOrmHelper.getInstent()
//                .query(TextBean.class,
//                        WhereBulider.creat().where("name=","lisa")
//                                .AN("id =","1")
//                                .limit(1,10));//实现分页
//        /*****************************兼容原有数据库（非必要情况不建议使用）**************************************/
//        LightOrmHelper.with(null).save(textBean,"TableName");
//        LightOrmHelper.with(null).update(textBean,"TableName",WhereBulider.creat());
//        LightOrmHelper.with(null).remove("TableName",WhereBulider.creat());
//        LightOrmHelper.with(null).query(TextBean.class,"TableName");
//        LightOrmHelper.with(null).query(TextBean.class,"TableName",WhereBulider.creat());
//        LightOrmHelper.with(null).queryJSON("TableName",WhereBulider.creat())
//    }

    //自定表名默认会自动生成表名
    @LightTableName("TextBean")
    public static class TextBean {
        //数据模型的主键（必须包含主键）
        @LightPrimaryKey
        private String id;
        private String name;
        //忽略的字段
        @LightIgnore
        private BigDecimal price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }

    public void show() {
        LightDialog.MakeDialog(linearLayout).show();
    }
}
