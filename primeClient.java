package com.example.prime;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class primeClient extends Application {
    // IO streams for primeClientApp
    DataOutputStream outputToServer = null;
    DataInputStream inputFromServer = null;

    @Override
    public void start(Stage primaryStage) {
        // Pane for label and text field
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(new Label("Enter number: "));

        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        borderPane.setCenter(tf);

        BorderPane mainPane = new BorderPane();

        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(borderPane);

        //Set stage and scene
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Prime Number Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        tf.setOnAction(e -> {
            try {
                // Get the radius from the text field
                Integer request = Integer.parseInt(tf.getText().trim());

                // Send the radius to the server
                outputToServer.writeInt(request);
                outputToServer.flush();

                // Get area from the server
                boolean response = inputFromServer.readBoolean();

                // Display to the text area
                ta.appendText("Number entered: " + request + '\n');

                if(response == true) {
                    ta.appendText("Server response: \n" + "   " + request + " is prime" + '\n' + '\n');
                }
                else {
                    ta.appendText("Server response: \n" + "   " + request + " is not prime" + '\n' + '\n');
                }

                tf.setText("");

            }
            catch (IOException ex) {
                System.err.println(ex);
            }
        });

        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            inputFromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            outputToServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            ta.appendText(ex.toString() + '\n');
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

