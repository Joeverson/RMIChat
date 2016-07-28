package br.pd.cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.pd.model.Membro;
import br.pd.server.ServidorChat;
import br.pd.server.ServidorChatIF;

/**
 *
 * @author Eder e Joerverson
 */
public class AplicacaoCliente {

    public static void main(String[] args) {
        try {
            Scanner tc = new Scanner(System.in);
            ServidorChatIF serv = (ServidorChatIF) Naming.lookup("ServidorRemoto");
            Membro m;


            while (true) {
                System.out.println("Seu nome: ");
                String nome = tc.nextLine();

                if(nome.equals("")){
                    System.out.println("O nome n�o pode ser vazio!");

                }else{
                    m = new Membro(nome.toUpperCase());
                    if (serv.addMembro(m) != null) {
                        break;
                    }
                }
            }

            String frase;

            System.out.println("\n\nComandos dispon�veis: ");
            System.out.println("send -all <Mensagem> (Enviar mensagem para todos)");
            System.out.println("send -user <NomeDoMembro> (Envia mensagem para um usuario espec�fico)");
            System.out.println("list (Lista os usuarios na sala)");
            System.out.println("rename (Mudar seu nome)");
            System.out.println("bye (Sair do chat)");
            System.out.println("\n");

            do{


                System.out.print("");
                frase = tc.nextLine();

                String array[] = new String[3];
                array = frase.split(" ");

                String opcao = array[0];

                String mensagem = "";

                if(array[0].equals("send")){
                    if(array[1]!=null){
                        opcao = array[0]+" "+array[1];
                    }
                }

                switch (opcao.toLowerCase()) {
                    case "bye":
                        serv.sair(m);
                        System.exit(0);
                        break;
                    case "send -all":
                        for(int i = 2; i < array.length;i++){
                            mensagem += array[i];
                            mensagem += " ";
                        }
                        serv.sendAll(m, mensagem);
                        break;
                    case "send -user":
                        for(int i = 3; i < array.length;i++){
                            mensagem += array[i];
                            mensagem += " ";
                        }

                        String nomeMembro = array[2];
                        serv.sendMemb(m, nomeMembro.toUpperCase(), mensagem);
                        break;
                    case "list":
                        serv.list(m);
                        break;
                    case "rename":
                        String novoNome = array[1];

                        serv.rename(m, novoNome.toUpperCase());
                        break;
                    case "help":
                        System.out.print(serv.help());
                        break;
                    default:
                        System.out.println("Comando invalido!");
                        break;
                }
            }while(frase.toLowerCase() != "bye");


        } catch (NotBoundException ex) {
            Logger.getLogger(AplicacaoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteException ex) {
            Logger.getLogger(AplicacaoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }catch (MalformedURLException ex) {
            Logger.getLogger(AplicacaoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

