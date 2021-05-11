package com.example.celulareport.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.celulareport.R;
import com.example.celulareport.db.model.ReportCard;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {

    private List<ReportCard> mReportsByMonth;
    private OnCardClickListener onCardClickListener;
    private OnCardLongCLickListener onCardLongCLickListener;
    private Context mContext;

    //Constructor get data set
    public CardAdapter(Context mContext,
                       OnCardClickListener cardClickListener,
                       OnCardLongCLickListener cardLongCLickListener) {

        this.mContext = mContext;
        this.onCardClickListener = cardClickListener;
        this.onCardLongCLickListener = cardLongCLickListener;
    }


    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.short_report_cardview, parent, false);
        return new CardHolder(view, onCardClickListener, onCardLongCLickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        if(mReportsByMonth != null){

            ReportCard current = mReportsByMonth.get(position);
            //create an animation for cards created in recycle view
            holder.mCardView.setAnimation((AnimationUtils.loadAnimation(mContext, R.anim.short_card_animation)));

            //Adding current data in view
            holder.textCelula.setText(current.getNomeCelula());
            holder.textLider.setText(current.getNomeLider());
            holder.textDate.setText(current.getDataReuniao());
        }else {
            holder.textCelula.setText("Nenhuma Relatório ");
        }

        //FeedBack when longClick

    }

    @Override
    public int getItemCount() {
        return mReportsByMonth == null ? 0 : mReportsByMonth.size();
    }

    //Obtain items in card view
    public static class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView textCelula, textLider, textDate;
        CardView mCardView;
        OnCardClickListener mCardClickListener;
        OnCardLongCLickListener mCardLongCLickListener;

        public CardHolder(@NonNull View itemView, OnCardClickListener cardClickListener, OnCardLongCLickListener cardLongCLickListener) {
            super(itemView);

            this.mCardClickListener = cardClickListener;
            this.mCardLongCLickListener = cardLongCLickListener;

            textCelula = (TextView)itemView.findViewById(R.id.card_celula_nome);
            textLider = (TextView)itemView.findViewById(R.id.card_lider_nome);
            textDate = (TextView)itemView.findViewById(R.id.card_data);
            mCardView = (CardView)itemView.findViewById(R.id.short_report_id);
            //set onClick method this class
            mCardView.setOnClickListener(this);
            mCardView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(getAdapterPosition() != RecyclerView.NO_POSITION){
                mCardClickListener.onCardClick(getAdapterPosition(), (CardView) v);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(getAdapterPosition() != RecyclerView.NO_POSITION){

                return mCardLongCLickListener.onCardLongClick(getAdapterPosition(), (CardView)v);

            }else return true; //true to don't consider simple click
        }
    }

    public void removeItem(int position){
        mReportsByMonth.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mReportsByMonth.size());
    }

    public long getReportId(int position){
        return mReportsByMonth.get(position).getId();
    }

    public ReportCard getReportCard(int position){
        return mReportsByMonth.get(position);
    }

    public void setReportCardsByMonth(List<ReportCard> mReportsByMonth){
        this.mReportsByMonth = mReportsByMonth;
        notifyDataSetChanged();
    }

    public interface OnCardClickListener{
        public void onCardClick(int position, CardView mCardView);
    }

    public interface OnCardLongCLickListener{
        public boolean onCardLongClick(int position, CardView mCardView);
    }

}
