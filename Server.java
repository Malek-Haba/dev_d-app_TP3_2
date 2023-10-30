package Tp3Partie2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private static int nbclients = 0; // Initialise un compteur de clients.
    ServerSocket ss; // ServerSocket pour accepter les connexions client.

    public static void main(String[] args) throws IOException {
        new Server().start(); // Démarre le thread serveur.
    }

    public void run() {
    	System.out.println("Démarrage du serveur");
        try 
        {
        	ServerSocket ss = new ServerSocket(1234); // Crée un ServerSocket pour écouter sur le port 1234.
            while (true) {
                Socket s = ss.accept(); // Accepte les connexions entrantes des clients.
                new ClientProcess(s, nbclients++).start(); // Crée un nouveau thread pour gérer le client et incrémente le compteur de clients.
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Classe interne pour gérer les connexions client.
    public class ClientProcess extends Thread {
         private Socket s; // La socket client.
        private int num; // Ordre dans lequel le client s'est connecté.

        public ClientProcess(Socket s, int num) {
            this.s = s;
            this.num = num;
        }

        public void run() {
            System.out.println("Client connecté " + s.getRemoteSocketAddress() + " ordre : " + this.num);
           
            InputStream input = null;
            try {
                input = s.getInputStream();
                ObjectInputStream is = new ObjectInputStream(input);

                operation op = (operation) is.readObject();

                // Extraction des données de l'objet Operation
                int nb1 = op.getNb1();
                int nb2 = op.getNb2();
                char ops = op.getOp();

                int res = 0;

                // Effectue l'opération arithmétique demandée
                switch (ops) {
                    case '+':
                        res = nb1 + nb2;
                        break;
                    case '-':
                        res = nb1 - nb2;
                        break;
                    case '*':
                        res = nb1 * nb2;
                        break;
                    case '/':
                        res = nb1 / nb2;
                        break;
                }

                // Stocke le résultat dans l'objet Operation
                op.setRes(res);

                // Configuration du flux de sortie pour renvoyer l'objet Operation modifié
                OutputStream output = s.getOutputStream();
                ObjectOutputStream oo = new ObjectOutputStream(output);

                // Envoie l'objet Operation modifié au client
                oo.writeObject(op);

                // Ferme la socket client
                s.close();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}