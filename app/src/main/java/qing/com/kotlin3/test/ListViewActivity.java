package qing.com.kotlin3.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.posun.lightui.listview.LightListViewGroupManager;

import qing.com.kotlin3.R;

public class ListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ListView listview = (ListView) findViewById(R.id.listview);
        TextView textView = new TextView(this);
        textView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 200));
//        listview.addHeaderView(textView);
        listview.setAdapter(new TestLightSectionAdapter());
        LightListViewGroupManager manager = new LightListViewGroupManager();
        manager.init(this, listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ListViewActivity.this, "" + i, Toast.LENGTH_SHORT).show();
            }
        });
//        listview.setAdapter(new PinAdapter(this, getData()));
    }

}
