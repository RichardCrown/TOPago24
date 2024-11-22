package com.example.tap2024b.vistas;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SimuladorExclusionMutua extends Stage {

    private TableView<Tarea> tabla;
    private Button btnAgregarTarea, btnToggleSimulador;
    private ProgressBar barraProgreso;
    private boolean simuladorEncendido = false;
    private Thread hiloImpresion;

    public SimuladorExclusionMutua() {
        CrearUI();
        this.IniciarHiloImpresion(); // Iniciar el hilo de impresión
        this.setTitle("Simulador de Exclusión Mutua - Servidor de Impresión");
        this.show();
    }

    private void CrearUI() {
        tabla = new TableView<>();
        tabla.setPrefHeight(300);

        // Configuración de columnas de la tabla
        TableColumn<Tarea, Integer> columnaNumero = new TableColumn<>("No. Archivo");
        columnaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));

        TableColumn<Tarea, String> columnaNombre = new TableColumn<>("Nombre de archivo");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Tarea, Integer> columnaHojas = new TableColumn<>("Número de hojas");
        columnaHojas.setCellValueFactory(new PropertyValueFactory<>("numeroHojas"));

        TableColumn<Tarea, String> columnaHora = new TableColumn<>("Hora de acceso a la cola");
        columnaHora.setCellValueFactory(new PropertyValueFactory<>("horaAcceso"));

        // Añadir columnas a la tabla
        tabla.getColumns().addAll(columnaNumero, columnaNombre, columnaHojas, columnaHora);

        // Botón para agregar tarea
        btnAgregarTarea = new Button("Agregar Tarea");
        btnAgregarTarea.setOnAction(e -> agregarTarea());

        // Botón para encender/apagar simulador
        btnToggleSimulador = new Button("Encender Simulador");
        btnToggleSimulador.setOnAction(e -> toggleSimulador());

        // Barra de progreso
        barraProgreso = new ProgressBar(0);
        barraProgreso.setPrefWidth(300);

        HBox hboxBotones = new HBox(10, btnAgregarTarea, btnToggleSimulador);
        VBox vbox = new VBox(10, tabla, hboxBotones, barraProgreso);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 600, 400);
        this.setScene(scene);
    }

    private void agregarTarea() {
        Random random = new Random();
        int numero = tabla.getItems().size() + 1;
        String nombre = "Archivo_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";
        int numeroHojas = random.nextInt(20) + 1; // Número aleatorio de hojas entre 1 y 20
        String horaAcceso = new SimpleDateFormat("HH:mm:ss").format(new Date());

        Tarea tarea = new Tarea(numero, nombre, numeroHojas, horaAcceso);
        tabla.getItems().add(tarea);
    }

    private void toggleSimulador() {
        simuladorEncendido = !simuladorEncendido;
        btnToggleSimulador.setText(simuladorEncendido ? "Apagar Simulador" : "Encender Simulador");
    }

    private void IniciarHiloImpresion() {
        hiloImpresion = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Verificar cada segundo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (simuladorEncendido && !tabla.getItems().isEmpty()) {
                    Tarea tarea = tabla.getItems().get(0); // Obtener la primera tarea (FIFO)
                    int hojas = tarea.getNumeroHojas();

                    Platform.runLater(() -> {
                        barraProgreso.setProgress(0);
                        btnAgregarTarea.setDisable(false); // Asegurar que agregar tarea siempre esté disponible
                    });

                    for (int i = 1; i <= hojas; i++) {
                        try {
                            Thread.sleep(500); // Simular tiempo de impresión por hoja (0.5 segundos)
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        double progreso = (double) i / hojas;
                        Platform.runLater(() -> barraProgreso.setProgress(progreso));
                    }

                    // Eliminar la tarea de la tabla una vez impresa
                    Platform.runLater(() -> {
                        tabla.getItems().remove(tarea);
                        barraProgreso.setProgress(0);
                    });
                }
            }
        });

        hiloImpresion.setDaemon(true); // Hilo se detendrá al cerrar la aplicación
        hiloImpresion.start();
    }

    public static class Tarea {
        private final int numero;
        private final String nombre;
        private final int numeroHojas;
        private final String horaAcceso;

        public Tarea(int numero, String nombre, int numeroHojas, String horaAcceso) {
            this.numero = numero;
            this.nombre = nombre;
            this.numeroHojas = numeroHojas;
            this.horaAcceso = horaAcceso;
        }

        public int getNumero() {
            return numero;
        }

        public String getNombre() {
            return nombre;
        }

        public int getNumeroHojas() {
            return numeroHojas;
        }

        public String getHoraAcceso() {
            return horaAcceso;
        }
    }
}
