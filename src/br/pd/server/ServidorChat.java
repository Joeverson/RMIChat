package br.pd.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.pd.model.Membro;
import br.pd.model.MembroIF;

/**
 *
 * @author Eder e Joerverson
 */
public class ServidorChat extends UnicastRemoteObject implements ServidorChatIF {

    private ArrayList<MembroIF> membros;
    private Calendar data;

    public ServidorChat() throws RemoteException {
        this.membros = new ArrayList<MembroIF>();
    }

    @Override
    public MembroIF addMembro(MembroIF memb) throws RemoteException {
  
        for (MembroIF u : this.membros) {
            if(u.getNome().equals(memb.getNome())){
                memb.mensagem("Membro j� existente na sala do chat");
                return null;
          
            }
        }
        
        this.membros.add(memb);
        this.data = Calendar.getInstance();
        
        for (MembroIF m : this.membros) {
            m.mensagem("Membro " + memb.getNome() + " entrou na sala! <" + data.getTime() + ">");
        }
        return memb;

    }

    @Override
    public void sair(MembroIF memb) throws RemoteException {
        memb.mensagem("Saiu da sala!");
        this.data = Calendar.getInstance();
        
        for (MembroIF u : this.membros) {
            u.mensagem("O membro " + memb.getNome() + " saiu da sala! <" + data.getTime() + ">");
        }
        this.membros.remove(memb);
    }

    @Override
    public void sendAll(MembroIF memb, String mensagem) throws RemoteException {
        this.data = Calendar.getInstance();
        for (MembroIF m : this.membros) {
            
            SimpleDateFormat formatador = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            GregorianCalendar d = new GregorianCalendar();
            String datahora = formatador.format(d.getTime());
            
            m.mensagem("/~"+memb.getNome() + ": "+mensagem+" "+datahora);
        }
    }

    @Override
    public void sendMemb(MembroIF memb, String nomeDoMembro, String mensagem) throws RemoteException {
        MembroIF m = getMembro(nomeDoMembro);
        if(m == null){
            memb.mensagem("Membro n�o encontrado!");
        }else{
            this.data = Calendar.getInstance();
            
            SimpleDateFormat formatador = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            GregorianCalendar d = new GregorianCalendar();
            String datahora = formatador.format(d.getTime());
            m.mensagem("(Privada)/~"+memb.getNome() + " (Privada): "+mensagem+" "+datahora);
            
            memb.mensagem("(Privada)/~"+m.getNome()+": "+mensagem+" "+datahora);
            
        }
        
    }

    @Override
    public void list(MembroIF memb) throws RemoteException {
        memb.mensagem("Usuarios na sala: ");
        for (MembroIF m : this.membros) {
            memb.mensagem(m.getNome());
        }
    }

    @Override
    public void rename(MembroIF memb, String novoNome) throws RemoteException {
        for (MembroIF m : this.membros) {
            if(m.getNome().equals(novoNome)){
                memb.mensagem("Escolha outro, H� um membro com este nome na sala");
                return;
            }
        }
        memb.setNome(novoNome);
        memb.mensagem("Nome alterado com sucesso!");
    }

    @Override
    public MembroIF getMembro(String nome) throws RemoteException {
        for (MembroIF m : this.membros) {
            if (m.getNome().equals(nome)) {
                return m;
            }
        }
        return null;
    }

    public String help() throws RemoteException{
        return  "\nComandos dispon�veis: \n"+
                "send -all <Mensagem> (Enviar mensagem para todos)\n"+
                "send -user <NomeDoMembro> (Envia mensagem para um usuario espec�fico)\n"+
                "list (Lista os usuarios na sala)\n"+
                "rename (Mudar seu nome)\n"+
                "bye (Sair do chat)\n";
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.bind("ServidorRemoto", new ServidorChat());
            System.out.println("Servidor conectado com sucesso!");
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
        }catch (AlreadyBoundException ex) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

}
