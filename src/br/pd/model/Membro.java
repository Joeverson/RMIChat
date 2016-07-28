package br.pd.model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Eder e Joerverson
 */
public class Membro extends UnicastRemoteObject implements MembroIF {

    private String nome;

    public Membro(String nome) throws RemoteException {
        this.nome = nome;
    }

    @Override
    public void mensagem(String mensagem) throws RemoteException {
        System.out.println(mensagem);
    }

    @Override
    public String getNome()throws RemoteException{
        return nome;
    }

    @Override
    public void setNome(String nome) throws RemoteException{
        this.nome = nome;
    }

}
