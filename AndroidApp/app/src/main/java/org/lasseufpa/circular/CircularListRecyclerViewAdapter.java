package org.lasseufpa.circular;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.lasseufpa.circular.CircularListFragment.OnListFragmentInteractionListener;
import org.lasseufpa.circular.Domain.Circular;
import java.util.ArrayList;


public class CircularListRecyclerViewAdapter extends RecyclerView.Adapter<CircularListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Circular> circularList;
    private OnListFragmentInteractionListener mListener;

    public CircularListRecyclerViewAdapter(ArrayList<Circular> circulares) {
        circularList = circulares;


    }

    public void setmListener(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_circularlist, parent, false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = circularList.get(position);
        holder.mCircularName = holder.mItem.getNome();
        holder.mIdView.setText(holder.mItem.getNome());
        holder.mContentView.setText(holder.mItem.getPosition().toString());

        if (holder.mItem.isObsolet()) {
            holder.mImageView.setImageResource(R.drawable.circg);
        }


    }

    @Override
    public int getItemCount() {
        return circularList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public final CardView mCardView;
        public final OnListFragmentInteractionListener mListener;
        public Circular mItem;
        public String mCircularName;

        public ViewHolder(View view, OnListFragmentInteractionListener listener ) {
            super(view);
            mView = view;
            mListener = listener;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.circimg);
            mCardView = (CardView) view.findViewById(R.id.card_view);
            mCardView.setOnClickListener(this);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            this.mListener.onListFragmentInteraction(mCircularName);
        }
    }
}
