package qing.com.kotlin3.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import qing.com.kotlin3.R;

public class Main3Activity extends AppCompatActivity {
    int arg = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        final ViewGroup viewGroup = (ViewGroup) findViewById(R.id.viewGroup);
        final TextView textView = new TextView(this);

        textView.setText("item" + (++arg));
        viewGroup.addView(textView);
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Main3Activity.this, "click="+textView.getText(), Toast.LENGTH_SHORT).show();
//                if (textView.getParent() != null) {
//                    ((ViewGroup) textView.getParent()).removeView(textView);
//                }
//                textView.setText("item" + (++arg));
//                viewGroup.addView(textView);
            }
        });
    }
}
