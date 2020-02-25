package org.techtown.cafe.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.cafe.R;
import org.techtown.cafe.util.CafeMenuItem;
import org.techtown.cafe.util.RecyclerAdapter;

import java.util.ArrayList;

public class AdeFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    RecyclerAdapter recyclerAdapter;
    private ArrayList<CafeMenuItem> cafeMenuItemArrayList=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter=new RecyclerAdapter(getContext(),cafeMenuItemArrayList,getActivity());
        recyclerAdapter.addItem(cafeMenuItemArrayList,"레몬에이드",R.drawable.americano);
        recyclerAdapter.addItem(cafeMenuItemArrayList,"유자에이드",R.drawable.ic_android_black_24dp);
        recyclerAdapter.addItem(cafeMenuItemArrayList,"자몽에이드",R.drawable.ic_android_black_24dp);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_menuitems,null);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=getView().findViewById(R.id.recyclerView);
        manager=new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(manager);
        Log.d("Tea",recyclerAdapter.toString());
        recyclerView.setAdapter(recyclerAdapter);
    }

}
