package com.ufv.strafe.controller;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.auth.FirebaseAuth;
import com.ufv.strafe.dao.PartidaDAO;
import com.ufv.strafe.dao.UserDAO;
import com.ufv.strafe.databinding.FragmentFeedBinding;
import com.ufv.strafe.model.Aposta;
import com.ufv.strafe.model.Partida;

import java.util.Calendar;

public class DialogApostaController {
    private UserDAO userDAO;
    private PartidaDAO partidaDAO;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public DialogApostaController(String idPartida){
        this.userDAO = new UserDAO();
        this.partidaDAO = new PartidaDAO(idPartida);
    }

    public  void addPartida(Partida partida){
        partidaDAO.createPartida(partida);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void  addAposta(String idPartida, String time, Double valor, Double multiplicador){
        Calendar data;
        data = Calendar.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();

        String idAposta = userId + (data.getTimeInMillis());
        Aposta aposta = new Aposta(idAposta, time, valor, multiplicador, FirebaseAuth.getInstance().getUid());

        userDAO.addAposta(idPartida,  idAposta, valor);
        userDAO.updateUser();

        partidaDAO.addAposta(aposta);
        partidaDAO.updatePartida();

    }

    public Double getSaldo(){
        return userDAO.usuario.getValue().getSaldo();
    }


}
