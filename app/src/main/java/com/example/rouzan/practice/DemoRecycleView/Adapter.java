package com.example.rouzan.practice.DemoRecycleView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rouzan.practice.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {

    List <DemoDTO> demoDTO;

    public Adapter(List<DemoDTO> demoDTO) {
        this.demoDTO = demoDTO;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo_feed,parent,false);
        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.bindView(demoDTO.get(position));

    }

    @Override
    public int getItemCount() {
        return demoDTO.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView demoText;
        public viewHolder(View itemView) {
            super(itemView);
            demoText=itemView.findViewById(R.id.demo_text);
        }

        public void bindView(DemoDTO demoDTO){
            demoText.setText(demoDTO.getDemoText());
        }
    }


}
