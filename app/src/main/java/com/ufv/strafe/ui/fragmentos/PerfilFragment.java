package com.ufv.strafe.ui.fragmentos;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.ufv.strafe.R;
import com.ufv.strafe.controller.PerfilController;

import com.ufv.strafe.databinding.FragmentPerfilBinding;
import com.ufv.strafe.ui.activitys.ConfiguracoesActivity;
import com.ufv.strafe.ui.Adapters.ItemPerfilAdapter;

import java.util.ArrayList;


public class PerfilFragment extends Fragment {


    private PerfilController perfilController;
    private FragmentPerfilBinding binding;
    private NavController navCtr;
    private ArrayList<Integer> icons = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //infla o fragmento e passa os dados para o binding
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View view = binding.getRoot();



        perfilController = new PerfilController(this);


        //observa mudanças no usuario
        perfilController.userObserve(getViewLifecycleOwner(), binding, getActivity());


        return view;
    }


    @SuppressLint({"NonConstantResourceId", "ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //logout
        startMenu();

        navCtr = Navigation.findNavController(view);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updatePerfil(ArrayList<Integer> icons,
                                         String saldo,
                                         String nome,
                                         String foto,
                                         String patente,
                                            int erros,
                                            int acertos) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        ItemPerfilAdapter adapter = new ItemPerfilAdapter(icons);
        binding.recycleJogosPerfil.setAdapter(adapter);
        binding.recycleJogosPerfil.setLayoutManager(layoutManager);

        binding.txtSaldo.setText(saldo);
        binding.txtNome.setText(nome);
        binding.txtRankingValor.setText(patente);
        binding.txtAcertosValor.setText(String.valueOf(acertos));
        binding.txtErrosValor.setText(String.valueOf(erros));
        Picasso.get()
               .load(foto)
                .into(binding.imagePerfil);
    }

    @SuppressLint("RestrictedApi")
    public void startMenu(){

        binding.topAppBarPerfil.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.configurations:
                    Intent intent = new Intent(getContext(), ConfiguracoesActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.button_logout:
                    perfilController.signOut();
                    perfilController.verifyAuthentication();
                    break;
            }
            return false;

        });
    }


}