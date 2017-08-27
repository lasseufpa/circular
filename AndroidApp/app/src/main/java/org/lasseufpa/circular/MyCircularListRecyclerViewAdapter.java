package org.lasseufpa.circular;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lasseufpa.circular.CircularListFragment.OnListFragmentInteractionListener;
import org.lasseufpa.circular.Domain.Circular;
import java.util.ArrayList;


public class MyCircularListRecyclerViewAdapter extends RecyclerView.Adapter<MyCircularListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Circular> circularList;
    private final OnListFragmentInteractionListener mListener;

    public MyCircularListRecyclerViewAdapter(ArrayList<Circular> circulares, OnListFragmentInteractionListener listener) {
        circularList = circulares;
        mListener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_circularlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = circularList.get(position);
        holder.mIdView.setText(circularList.get(position).getNome());
        holder.mContentView.setText(circularList.get(position).getPosition().toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return circularList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Circular mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
