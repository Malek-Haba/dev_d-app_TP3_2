package Tp3Partie2;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            // Étape 1 : Création de l'adresse du serveur
            InetAddress serverAddress = InetAddress.getByName("localhost");
            InetSocketAddress serverSocketAddress = new InetSocketAddress(serverAddress, 1234);

            // Étape 2 : Création d'un socket client et connexion au serveur
            Socket clientSocket = new Socket();

            // Étape 3 : Établissement de la connexion avec le serveur
            clientSocket.connect(serverSocketAddress);

            // Étape 4 : Obtention du flux de sortie du socket
            OutputStream output = clientSocket.getOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(output);

            // Étape 5 : Création d'un objet "operation" (opération mathématique)
            operation op = new operation(40, 20, '+');

            // Étape 6 : Envoi de l'objet "operation" au serveur
            os.writeObject(op);

            // Étape 7 : Obtention du flux d'entrée du socket
            InputStream input = clientSocket.getInputStream();
            ObjectInputStream is = new ObjectInputStream(input);

            // Étape 8 : Réception de l'objet "operation" résultat du serveur
            op = (operation) is.readObject();

            // Étape 9 : Affichage du résultat
            System.out.println("Result received from the server: " + op.getRes());
        } catch (Exception e) {
            // En cas d'erreur, affichage d'un message d'erreur et lancement d'une exception
            System.out.println("Client: An error occurred - " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}