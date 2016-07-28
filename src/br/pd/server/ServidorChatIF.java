package br.pd.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.pd.model.Membro;
import br.pd.model.MembroIF;

/**
 *
 * @author Eder e Joerverson
 */
public interface ServidorChatIF extends Remote{
    
    public MembroIF addMembro(MembroIF memb)throws RemoteException;
    
    public void sair(MembroIF memb)throws RemoteException;
    
    public void sendAll(MembroIF memb, String mensagem)throws RemoteException;
    
    public void sendMemb(MembroIF memb, String nomeDoMembro, String mensagem)throws RemoteException;
    
    public void list(MembroIF memb)throws RemoteException;
    
    public void rename(MembroIF memb, String novoNome)throws RemoteException;
    
    public MembroIF getMembro(String nome)throws RemoteException;

    public String help() throws RemoteException;
    
}
