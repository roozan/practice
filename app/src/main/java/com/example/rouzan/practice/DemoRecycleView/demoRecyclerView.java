package com.example.rouzan.practice.DemoRecycleView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.rouzan.practice.R;

import java.util.ArrayList;
import java.util.List;

public class demoRecyclerView extends AppCompatActivity {

    RecyclerView demo_showlist;
    private List<DemoDTO> getList(){
        List <DemoDTO> demoDTO =new ArrayList<>();
        DemoDTO demoDTO1=new DemoDTO();
        demoDTO1.setDemoText("uyuguhb");
        demoDTO.add(demoDTO1);
        return demoDTO;

    }
    void defineView(){

        demo_showlist=findViewById(R.id.demo_showlist);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        demo_showlist.setLayoutManager(linearLayoutManager);

        Adapter adapter=new Adapter(getList());
        demo_showlist.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_recycler_view);

        defineView();
    }
}
