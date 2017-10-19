package org.lasseufpa.circular;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alberto on 03/09/2017.
 */

public class ParadaListFragment extends Fragment implements
        RepositorioParadas.OnRepositorioParadasChangeListener {


    private ParadaListRecyclerViewAdapter paradaListRecyclerViewAdapter;
    private OnParadasListFragmentInteractionListener listener;
    private RepositorioParadas repositorioParadas = MainActivity.repositorioParadas;
    private RecyclerView recyclerView;

    public ParadaListFragment() {

        paradaListRecyclerViewAdapter = new ParadaListRecyclerViewAdapter(repositorioParadas.getParadasList());

    }

    public void setOnListInteractionListener(OnParadasListFragmentInteractionListener listener) {
        this.listener = listener;
        //recyclerViewAdapter.setmListener(mListener);
    }


    @Override
    public void OnRepositorioParadasChanged() {
        Log.i("log","OnRepositorioParadasChanged chamado");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

    }

    public void update () {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_paradaslist,container,false);

        if (v instanceof RecyclerView) {
            Context context = v.getContext();
            recyclerView = (RecyclerView) v;


            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(paradaListRecyclerViewAdapter);


            paradaListRecyclerViewAdapter.notifyDataSetChanged();

            Log.i("log","numero de paradas: "+ repositorioParadas.getParadasList().size());
        }




        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        repositorioParadas.setOnRepositorioParadasChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        repositorioParadas.removeOnRepositorioParadasChangeListener(this);
    }

    public interface OnParadasListFragmentInteractionListener {

        void onListFragmentInteraction(String CircularName);
    }
}
