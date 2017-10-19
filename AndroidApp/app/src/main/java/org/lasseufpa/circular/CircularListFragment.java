package org.lasseufpa.circular;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnCircularListFragmentInteractionListener}
 * interface.
 */
public class CircularListFragment extends Fragment
        implements RepositorioCircular.RepositorioCircularChangeListener {


    private EmptyCircularListReciclerViewAdapter emptyRecyclerViewAdapter;
    private CircularListRecyclerViewAdapter recyclerViewAdapter;
    private OnCircularListFragmentInteractionListener mListener;
    private RepositorioCircular repositorio = CircularMapFragment.repositorioCirculares;
    private RecyclerView recyclerView;
    private FragmentActivity activity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CircularListFragment() {
         recyclerViewAdapter = new CircularListRecyclerViewAdapter(repositorio.getCircularList());
         emptyRecyclerViewAdapter = new EmptyCircularListReciclerViewAdapter();
    }


    public void setOnListFragmentInteractionListener(OnCircularListFragmentInteractionListener listener) {
        mListener = listener;
        recyclerViewAdapter.setmListener(mListener);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        repositorio.setRepositorioCircularChangeListener(this);


    }

    @Override
    public void onStop() {
        super.onStop();
        repositorio.removeRepositorioCircularChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circularlist_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            if (repositorio.getCircularList().size()!=0) {

                recyclerView.setAdapter(recyclerViewAdapter);
            }
            else {
                recyclerView.setAdapter(emptyRecyclerViewAdapter);
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRepositorioCircularChanged() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (repositorio.getCircularList().size()!=0) {
                    if (!recyclerView.getAdapter().equals(recyclerViewAdapter))
                        recyclerView.setAdapter(recyclerViewAdapter);
                }
                else {
                    recyclerView.setAdapter(emptyRecyclerViewAdapter);
                }
                recyclerView.getAdapter().notifyDataSetChanged();

            }
        });


    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCircularListFragmentInteractionListener {

        void onListFragmentInteraction(String CircularName);
    }
}
