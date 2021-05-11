package com.example.celulareport.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.celulareport.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.LineHolder> {

    private final List<String> months;
    private OnLineClickListerner onLineClickListerner;
    private String month;
    private TextView texMoth;
    Context mContext;

    public LineAdapter(ArrayList monthReports, OnLineClickListerner onLineClickListerner) {
        this.months = monthReports;
        this.onLineClickListerner = onLineClickListerner;
    }

    @NonNull
    @Override
    public LineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_line, parent, false);
        return new LineHolder(view, onLineClickListerner);
    }

    @Override
    public void onBindViewHolder(@NonNull LineHolder holder, int position) {

            holder.textMonth.setText(months.get(position));

            //set animation in line of recycleView
            CardView mCardView = holder.itemView.findViewById(R.id.month_line_id);

            month = months.get(position);
            ViewCompat.setTransitionName(holder.textMonth, month);
            texMoth = (TextView) holder.textMonth;
    }

    @Override
    public int getItemCount() {
        return (months == null)? 0: months.size();
    }


    //ViewHolder important to gets the views on adapter
    public static class LineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textMonth;
        OnLineClickListerner onLineClickListerner;

        public LineHolder(@NonNull View itemView, OnLineClickListerner onLineClickListerner) {
            super(itemView);

            this.onLineClickListerner = onLineClickListerner;
            textMonth = (TextView) itemView.findViewById(R.id.month_text);

            //listen onClick from this class
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onLineClickListerner.onLineClick(getAdapterPosition(), textMonth);
        }
    }


    public interface OnLineClickListerner{
        void onLineClick(int position, TextView textMonth);
    }

}
