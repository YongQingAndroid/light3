package qing.com.kotlin3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.posun.lightui.recyclerview.LightFormAdapterManager;

import qing.com.kotlin3.test.TestGradAdapter;
import qing.com.kotlin3.test.TestGroupAdapter;
import qing.com.kotlin3.test.TestListAdapter;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final LightFormAdapterManager adapterManager = new LightFormAdapterManager(this);
        View titleView = getLayoutInflater().inflate(R.layout.fix_uilayout, null);
        LightFormAdapterManager.LightRecyclerFixedAdapter fixedAdapter = new LightFormAdapterManager.LightRecyclerFixedAdapter(titleView);
        final TestGradAdapter testGradAdapter = new TestGradAdapter(3);
        /***********************************************/
        adapterManager.addAdapter(fixedAdapter);
        adapterManager.addAdapter(testGradAdapter);
        adapterManager.addAdapter(new TestGradAdapter(4));
        adapterManager.addAdapter(new TestListAdapter());
        adapterManager.addAdapter(new TestGroupAdapter());

        adapterManager.setRecyclerView(recyclerView);
        /**********************************************************/


        testGradAdapter.setOnItemCilckListener(new LightFormAdapterManager.OnItemCilckListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(Main2Activity.this, "OnItemCilckListener ="+position, Toast.LENGTH_SHORT).show();
                int cont = testGradAdapter.getCont();
                testGradAdapter.setCont(cont + 1);
                adapterManager.notifyChildItemRangeInserted(cont,1,testGradAdapter);
            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
    }
}
