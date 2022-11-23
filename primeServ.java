package com.example.prime;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class primeServ extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        TextArea textArea = new TextArea();

        //create scene and stage.
        Scene scene = new Scene(new ScrollPane(textArea), 450, 200);
        stage.setTitle("Prime Number Server");
        stage.setScene(scene);
        stage.show();

        textArea.appendText("\nServer started:   " + new Date() + '\n');

        new Thread(()-> {
            try  {
                ServerSocket serverSocket = new ServerSocket(8000);

                textArea.appendText("Connected");

                Socket socket = serverSocket.accept();

                //Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());

                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                while (true) {

                    // Integer variable for client request.
                    int primeRequest = inputFromClient.readInt();
                    // Boolean variable for server response.
                    boolean primeResponse = primeCheck(primeRequest);

                    // Display client request.
                    textArea.appendText('\n' + "Number received from client: " + primeRequest + '\n');
                    // Dispaly server response.
                    textArea.appendText("The number is prime: " + primeResponse + '\n');

                    //output server response to client.
                    outputToClient.writeBoolean(primeResponse);

                }

            }catch (IOException ex) {
                ex.printStackTrace();

            }
        }).start();

    }

    public static void main(String[] args) {
        launch();
    }

   // Method to determine if client request is a prime number.
    static boolean primeCheck(int number) {

        boolean isPrime = true;

        if (number <= 1) {
            isPrime = false;
            return isPrime;
        } else {
            for (int i = 2; i <= number / 2; i++) {
                if ((number % i) == 0) {
                    isPrime = false;
                    break;
                }
            }
        }
        return isPrime;
    }

}

