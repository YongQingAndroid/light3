package qing.com.kotlin3.test;

import com.posun.lightui.richView.LightActionType;
import com.posun.lightui.richView.LightUINB;
import com.posun.lightui.richView.annotation.LightCheckBox;
import com.posun.lightui.richView.annotation.LightItemsGroups;
import com.posun.lightui.richView.annotation.LightRichUI;
import com.posun.lightui.richView.annotation.LightSelect;
import com.posun.lightui.richView.annotation.LightSimpleClick;
import com.posun.lightui.richView.annotation.LightTextLab;

/**
 * Created by qing on 2018/1/11.
 */

public class RichTest {
    @LightItemsGroups(end = 1, labename = "个人信息")
    @LightRichUI(value = LightUINB.ONE, order = 0)
    @LightTextLab(lab = "名字")
    String name = "tom";


    @LightRichUI(value = LightUINB.ONE, order = 1)
    @LightTextLab(lab = "性别")
    String sex = "man";

    @LightRichUI(value = LightUINB.ONE, order = 2)
    @LightTextLab(lab = "输入框")
    String input = "56566565";

    @LightRichUI(value = LightUINB.ONE, order = 3)
    @LightSelect(lab = "select")
    @LightSimpleClick(type = LightActionType.TIMEPICKER, value = "yyyy-MM-dd")
    String select = "";

    @LightRichUI(value = LightUINB.ONE, order = 4)
    @LightCheckBox(lab = "時間选择")
    String checkbox = "";


    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }
}
