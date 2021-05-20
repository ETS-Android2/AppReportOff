package com.example.celulareport.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.celulareport.R;
import com.example.celulareport.db.model.ReportCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> implements Filterable {

    private List<ReportCard> mReportsByMonth;
    private List<ReportCard> mReportsByMonthAll;
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

    public void setReportCardsByMonth(List<ReportCard> mReportsByMonth){
        this.mReportsByMonth = mReportsByMonth;
        this.mReportsByMonthAll = new ArrayList<>(mReportsByMonth);
        notifyDataSetChanged();
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

    //Filter Recycle card list
    @Override
    public Filter getFilter() {
        return filter;
    }
    //run on background
    Filter filter =  new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<ReportCard> filteredList = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                filteredList.addAll(mReportsByMonthAll);
            }else {
                for(ReportCard reportCard:mReportsByMonthAll){
                    if(reportCard.getNomeLider().toLowerCase()
                            .contains(constraint.toString().toLowerCase())||
                            reportCard.getNomeCelula().toLowerCase()
                            .contains(constraint.toString().toLowerCase())||
                            reportCard.getDataReuniao().toLowerCase()
                            .contains(constraint.toString().toLowerCase())){

                        filteredList.add(reportCard);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        //run on a ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mReportsByMonth.clear();
            mReportsByMonth.addAll((Collection<? extends ReportCard>) results.values);
            notifyDataSetChanged();
        }
    };

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
            if(getBindingAdapterPosition() != RecyclerView.NO_POSITION){
                mCardClickListener.onCardClick(getBindingAdapterPosition(), (CardView) v);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(getBindingAdapterPosition() != RecyclerView.NO_POSITION){

                return mCardLongCLickListener.onCardLongClick(getBindingAdapterPosition(), (CardView)v);

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

    public interface OnCardClickListener{
        public void onCardClick(int position, CardView mCardView);
    }

    public interface OnCardLongCLickListener{
        public boolean onCardLongClick(int position, CardView mCardView);
    }
}
