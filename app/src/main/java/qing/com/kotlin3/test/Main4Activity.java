package qing.com.kotlin3.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.posun.lightui.recyclerview.adapter.LightFormAdapterManager;

import qing.com.kotlin3.R;
import qing.com.kotlin3.test.defultmanager.DeTestListAdapter;
import qing.com.kotlin3.test.defultmanager.TestDefultLayoutManagerGroupAdapter;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final LightFormAdapterManager adapterManager = new LightFormAdapterManager(this);
        DeTestListAdapter mDeTestListAdapter = new DeTestListAdapter();
        adapterManager.addAdapter(mDeTestListAdapter);
        adapterManager.addAdapter(new TestDefultLayoutManagerGroupAdapter());
        adapterManager.setRecyclerView(recyclerView);
        mDeTestListAdapter.setOnItemCilckListener(new LightFormAdapterManager.OnItemCilckListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(Main4Activity.this, "position" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
    }
}
