package org.lasseufpa.circular;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by alberto on 03/09/2017.
 */

public class ParadaListFragment extends Fragment implements
        RepositorioParadas.OnRepositorioParadasChangeListener {


    private ParadaListRecyclerViewAdapter paradaListRecyclerViewAdapter;
    private RepositorioParadas repositorioParadas = MainActivity.repositorioParadas;
    private RecyclerView recyclerView;

    public ParadaListFragment() {

        paradaListRecyclerViewAdapter = new ParadaListRecyclerViewAdapter();
    }


    @Override
    public void OnRepositorioParadasChanged() {

    }
}
