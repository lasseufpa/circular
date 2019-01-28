package org.lasseufpa.circular;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class EmptyCircularListReciclerViewAdapter extends RecyclerView.Adapter<EmptyCircularListReciclerViewAdapter.ViewHolder> {


    public EmptyCircularListReciclerViewAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empty_circularlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;


        public ViewHolder(View view) {
            super(view);
            mView = view;

        }

    }
}
