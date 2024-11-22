package com.example.tap2024b.vistas;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Buscaminas extends Stage {

    private TextField txtNumeroBombas;
    private Button btnCrearCampo;
    private GridPane gridCampoMinado;
    private int filas = 10;
    private int columnas = 10;
    private List<int[]> posicionesBombas;
    private Button[][] botones;

    public Buscaminas() {
        CrearUI();
        this.setTitle("Buscaminas");
        this.show();
    }

    private void CrearUI() {
        txtNumeroBombas = new TextField();
        txtNumeroBombas.setPromptText("Cantidad de bombas");

        btnCrearCampo = new Button("Crear Campo Minado");
        btnCrearCampo.setOnAction(e -> crearCampoMinado());

        gridCampoMinado = new GridPane();
        gridCampoMinado.setPadding(new Insets(10));
        gridCampoMinado.setHgap(5);
        gridCampoMinado.setVgap(5);

        VBox vbox = new VBox(10, new Label("Buscaminas"), txtNumeroBombas, btnCrearCampo, gridCampoMinado);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 600, 600);
        this.setScene(scene);
    }

    private void crearCampoMinado() {
        try {
            int numeroBombas = Integer.parseInt(txtNumeroBombas.getText());
            if (numeroBombas < 1 || numeroBombas > filas * columnas) {
                throw new NumberFormatException();
            }

            // Limpiar el campo minado
            gridCampoMinado.getChildren().clear();
            posicionesBombas = new ArrayList<>();
            botones = new Button[filas][columnas];

            // Generar posiciones aleatorias para las bombas
            Random random = new Random();
            while (posicionesBombas.size() < numeroBombas) {
                int fila = random.nextInt(filas);
                int columna = random.nextInt(columnas);
                int[] posicion = {fila, columna};
                boolean existe = posicionesBombas.stream().anyMatch(b -> b[0] == fila && b[1] == columna);
                if (!existe) {
                    posicionesBombas.add(posicion);
                }
            }

            // Crear la cuadricula de botones
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    Button boton = new Button();
                    boton.setPrefSize(40, 40);
                    final int fila = i;
                    final int columna = j;
                    boton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                descubrirCasilla(fila, columna);
                            } else if (event.getButton() == MouseButton.SECONDARY) {
                                marcarCasilla(boton);
                            }
                        }
                    });
                    botones[i][j] = boton;
                    gridCampoMinado.add(boton, j, i);
                }
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Entrada no válida");
            alert.setContentText("Por favor, ingrese un número válido de bombas.");
            alert.showAndWait();
        }
    }

    private void descubrirCasilla(int fila, int columna) {
        if (esBomba(fila, columna)) {
            mostrarMensajePerdedor();
            revelarBombas();
        } else {
            int bombasAdyacentes = contarBombasAdyacentes(fila, columna);
            botones[fila][columna].setText(String.valueOf(bombasAdyacentes));
            botones[fila][columna].setDisable(true);
            if (bombasAdyacentes == 0) {
                descubrirCasillasAdyacentes(fila, columna);
            }
        }
        verificarVictoria();
    }

    private void marcarCasilla(Button boton) {
        if (boton.getText().equals("F")) {
            boton.setText("");
        } else {
            boton.setText("F");
        }
    }

    private boolean esBomba(int fila, int columna) {
        return posicionesBombas.stream().anyMatch(b -> b[0] == fila && b[1] == columna);
    }

    private int contarBombasAdyacentes(int fila, int columna) {
        int contador = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int nuevaFila = fila + i;
                int nuevaColumna = columna + j;
                if (nuevaFila >= 0 && nuevaFila < filas && nuevaColumna >= 0 && nuevaColumna < columnas) {
                    if (esBomba(nuevaFila, nuevaColumna)) {
                        contador++;
                    }
                }
            }
        }
        return contador;
    }

    private void descubrirCasillasAdyacentes(int fila, int columna) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int nuevaFila = fila + i;
                int nuevaColumna = columna + j;
                if (nuevaFila >= 0 && nuevaFila < filas && nuevaColumna >= 0 && nuevaColumna < columnas) {
                    if (!botones[nuevaFila][nuevaColumna].isDisabled()) {
                        descubrirCasilla(nuevaFila, nuevaColumna);
                    }
                }
            }
        }
    }

    private void mostrarMensajePerdedor() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin del juego");
        alert.setHeaderText("Has perdido");
        alert.setContentText("Has encontrado una bomba.");
        alert.showAndWait();
    }

    private void revelarBombas() {
        for (int[] bomba : posicionesBombas) {
            botones[bomba[0]][bomba[1]].setText("B");
            botones[bomba[0]][bomba[1]].setStyle("-fx-background-color: red;");
        }
    }

    private void verificarVictoria() {
        boolean todasDescubiertas = true;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (!esBomba(i, j) && !botones[i][j].isDisabled()) {
                    todasDescubiertas = false;
                    break;
                }
            }
        }
        if (todasDescubiertas) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Victoria");
            alert.setHeaderText("¡Felicidades!");
            alert.setContentText("Has ganado el juego.");
            alert.showAndWait();
        }
    }
}
