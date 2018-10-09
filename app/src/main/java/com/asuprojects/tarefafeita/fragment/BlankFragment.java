package com.asuprojects.tarefafeita.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asuprojects.tarefafeita.R;

public class BlankFragment extends Fragment {

    private ImageView imagem;
    private TextView texto;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        imagem = view.findViewById(R.id.imagem);
        texto = view.findViewById(R.id.texto);

        return view;
    }

}
