package qing.com.kotlin3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jfz.lightkeyboard.SafeKeyboard;


public class Main5Activity extends AppCompatActivity {
    private SafeKeyboard safeKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        EditText safeEdit = (EditText) findViewById(R.id.safeEditText);
        EditText safeEdit2 = (EditText) findViewById(R.id.safeEditText2);
        EditText safeEdit3 = (EditText) findViewById(R.id.safeEditText3);
        EditText safeEdit5 = (EditText) findViewById(R.id.edtext);

        AppCompatEditText appCompatEditText = (AppCompatEditText) findViewById(R.id.safeAppCompactEditText);
        View rootView = findViewById(R.id.main_root);
        LinearLayout keyboardContainer = (LinearLayout) findViewById(R.id.keyboardViewPlace);
       View view = LayoutInflater.from(this).inflate(R.layout.layout_keyboard_containor, null);
        safeKeyboard = new SafeKeyboard(getApplicationContext(), keyboardContainer,
                R.layout.layout_keyboard_containor, view.findViewById(R.id.safeKeyboardLetter).getId(), rootView);
        safeKeyboard.putEditText(safeEdit.getId(), safeEdit);
        safeKeyboard.putEditText(safeEdit2.getId(), safeEdit2);
        safeKeyboard.putEditText(safeEdit3.getId(), safeEdit3);
        safeKeyboard.putEditText(appCompatEditText.getId(), appCompatEditText);
        safeKeyboard.putEditText2IdCardType(safeEdit3.getId(), safeEdit3);


        safeKeyboard.putEditText(safeEdit5.getId(), safeEdit5);
        safeKeyboard.putEditCustomerType(safeEdit5.getId(), safeEdit5);

//        safeKeyboard.setDelDrawable(this.getResources().getDrawable(R.drawable.icon_del));
//        safeKeyboard.setLowDrawable(this.getResources().getDrawable(R.drawable.icon_capital_default));
//        safeKeyboard.setUpDrawable(this.getResources().getDrawable(R.drawable.icon_capital_selected));
    }

    // 当点击返回键时, 如果软键盘正在显示, 则隐藏软键盘并是此次返回无效
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (safeKeyboard.isShow()) {
                safeKeyboard.hideKeyboard();
                return false;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (safeKeyboard != null) {
            safeKeyboard.release();
            safeKeyboard = null;
        }
        super.onDestroy();
    }


}
