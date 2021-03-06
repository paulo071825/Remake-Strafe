package com.ufv.strafe.model;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class PartidaTest {

    Usuario usuario = new Usuario("", "usuario", "");

    Partida partida;

    {
        try {
            partida = new Partida(
                    "Cruzeiro",
                    "Atletico",
                    "Overwatch",
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("16/02/2022 12:00:00"),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("20/02/2022 12:39:00"),
                    "1",
                    1,
                    0
            );
        } catch (ParseException ignored) {

        }
    }

    Aposta aposta = new Aposta("aposta1",
            "Cruzeiro",
            20.0,
            2.0,
            "usuario1");

    Aposta aposta2 = new Aposta("aposta2",
            "Atletico",
            10.0,
            2.0,
            "usuario1");


    Partida partida2;

    {
        try {
            partida2 = new Partida(
                    "Cruzeiro",
                    "Atletico",
                    "Overwatch",
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("16/02/2022 12:00:00"),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("20/02/2022 12:39:00"),
                    "2",
                    0,
                    1
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    Aposta aposta3 = new Aposta("aposta3",
            "Cruzeiro",
            5.0,
            2.0,
            "usuario1");

    Aposta aposta4 = new Aposta("aposta4",
            "Cruzeiro",
            5.0,
            2.0,
            "usuario1");


    @Test
    public void testGetSaldoAposta() {

        partida.addAposta(aposta);

        assert (40.0 == partida.getSaldoAposta(aposta.getId()));

    }

    @Test
    public void verificaFim() {
        assert (partida.verificaFim() == Boolean.TRUE);
        assert (partida2.verificaFim() == Boolean.TRUE);
    }

    @Test
    public void apostaPerdeTest() {


        partida.addAposta(aposta2);

        assert (0.0 == partida.getSaldoAposta(aposta2.getId()));
    }

    @Test
    public void useRecebeApostas() {
        partida.addAposta(aposta);
        partida.addAposta(aposta2);
        usuario.addAposta(partida.getId(), aposta.getId(), aposta.getValor()); // -20
        usuario.addAposta(partida.getId(), aposta2.getId(), aposta2.getValor()); // -10
        ArrayList<String> apostas = Objects.requireNonNull(usuario.getApostas().get(partida.getId()));
        Double vAposta = partida.getSaldoNApostas(apostas);
        usuario.addSaldo(vAposta); //+ 40

        assert (usuario.getSaldo() == 310);
    }

    @Test
    public void multiplasPartidas() {
        partida.addAposta(aposta);
        partida.addAposta(aposta2);
        usuario.addAposta(partida.getId(), aposta.getId(), aposta.getValor()); // -20
        usuario.addAposta(partida.getId(), aposta2.getId(), aposta2.getValor()); // -10
        partida2.addAposta(aposta3);
        partida2.addAposta(aposta4);
        usuario.addAposta(partida2.getId(), aposta3.getId(), aposta3.getValor()); // -5
        usuario.addAposta(partida2.getId(), aposta4.getId(), aposta4.getValor()); // -5

        ArrayList<String> apostas = Objects.requireNonNull(usuario.getApostas().get(partida.getId())); //+40
        ArrayList<String> apostas2 = Objects.requireNonNull(usuario.getApostas().get(partida2.getId())); //+0
        Double vAposta = partida.getSaldoNApostas(apostas);
        vAposta += partida2.getSaldoNApostas(apostas2);
        usuario.addSaldo(vAposta);
        assert (usuario.getSaldo() == 300);
    }


    @Test
    public void partidaNAcabou() {
        Date data = null;
        try {
            data = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("01/01/3000 01:01");
        } catch (ParseException e) {
            assert false;
        }
        partida.setDataFim(data);
        partida.addAposta(aposta);
        partida.addAposta(aposta2);
        usuario.addAposta(partida.getId(), aposta.getId(), aposta.getValor()); // -20
        usuario.addAposta(partida.getId(), aposta2.getId(), aposta2.getValor()); // -10
        partida2.addAposta(aposta3);
        partida2.addAposta(aposta4);
        usuario.addAposta(partida2.getId(), aposta3.getId(), aposta3.getValor()); // -5
        usuario.addAposta(partida2.getId(), aposta4.getId(), aposta4.getValor()); // -5

        ArrayList<String> apostas = Objects.requireNonNull(usuario.getApostas().get(partida.getId())); //+0
        ArrayList<String> apostas2 = Objects.requireNonNull(usuario.getApostas().get(partida2.getId())); //+0
        //simula a coletagem de recompensas pelo usuario
        Double vAposta = partida.getSaldoNApostas(apostas);
        vAposta += partida2.getSaldoNApostas(apostas2);
        usuario.addSaldo(vAposta);
        assert (usuario.getSaldo() == 260);
    }

}