package com.example.tap2024b.vistas;

import com.example.tap2024b.models.ClienteDAO;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class UsuariosInterface {
    private Stage stage;
    private TextField nombreField;
    private TextField telefonoField;
    private TextField emailField;
    private TableView<ClienteDAO> tablaClientes;
    private ClienteDAO clienteDAO;

    public UsuariosInterface() {
        this.clienteDAO = new ClienteDAO();
        this.stage = new Stage();
        inicializarUI();
    }

    private void inicializarUI() {
        nombreField = new TextField();
        nombreField.setPromptText("Nombre");

        telefonoField = new TextField();
        telefonoField.setPromptText("Teléfono");

        emailField = new TextField();
        emailField.setPromptText("Email");

        Button agregarButton = new Button("Agregar Cliente");
        agregarButton.setOnAction(e -> agregarCliente());

        Button eliminarButton = new Button("Eliminar Cliente");
        eliminarButton.setOnAction(e -> eliminarCliente());

        Button editarButton = new Button("Editar Cliente");
        editarButton.setOnAction(e -> editarCliente());

        tablaClientes = new TableView<>();
        configurarTabla();

        VBox layout = new VBox(10, nombreField, telefonoField, emailField, agregarButton, eliminarButton, editarButton, tablaClientes);
        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Gestión de Usuarios");
        stage.show();

        // Actualiza la tabla al iniciar la interfaz
        actualizarTabla();
    }

    private void configurarTabla() {
        // Configuración de las columnas de la tabla
        TableColumn<ClienteDAO, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idCte"));

        TableColumn<ClienteDAO, String> nombreColumn = new TableColumn<>("Nombre");
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nomCte"));

        TableColumn<ClienteDAO, String> telefonoColumn = new TableColumn<>("Teléfono");
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telCte"));

        TableColumn<ClienteDAO, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailCte"));

        tablaClientes.getColumns().addAll(idColumn, nombreColumn, telefonoColumn, emailColumn);

        // Permitir seleccionar un cliente de la tabla
        tablaClientes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void agregarCliente() {
        String nombre = nombreField.getText();
        String telefono = telefonoField.getText();
        String email = emailField.getText();

        if (!nombre.isEmpty() && !telefono.isEmpty() && !email.isEmpty()) {
            clienteDAO.setNomCte(nombre);
            clienteDAO.setTelCte(telefono);
            clienteDAO.setEmailCte(email);
            clienteDAO.INSERT(); // Inserta el cliente en la base de datos

            // Actualiza la tabla
            actualizarTabla();

            // Limpia los campos
            nombreField.clear();
            telefonoField.clear();
            emailField.clear();
        } else {
            System.out.println("Por favor completa todos los campos.");
        }
    }

    private void eliminarCliente() {
        ClienteDAO clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            clienteDAO.setIdCte(clienteSeleccionado.getIdCte());
            clienteDAO.DELETE(); // Elimina el cliente de la base de datos

            // Actualiza la tabla
            actualizarTabla();
        } else {
            System.out.println("Por favor selecciona un cliente para eliminar.");
        }
    }

    private void editarCliente() {
        ClienteDAO clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            // Establece los datos del cliente seleccionado en los campos
            nombreField.setText(clienteSeleccionado.getNomCte());
            telefonoField.setText(clienteSeleccionado.getTelCte());
            emailField.setText(clienteSeleccionado.getEmailCte());

            // Asigna el ID del cliente seleccionado para la actualización
            clienteDAO.setIdCte(clienteSeleccionado.getIdCte());

            // Cambiar el botón de agregar a editar
            Button editarButton = new Button("Actualizar Cliente");
            editarButton.setOnAction(e -> {
                clienteDAO.setNomCte(nombreField.getText());
                clienteDAO.setTelCte(telefonoField.getText());
                clienteDAO.setEmailCte(emailField.getText());
                clienteDAO.UPDATE(); // Actualiza el cliente en la base de datos

                // Actualiza la tabla
                actualizarTabla();

                // Limpia los campos
                nombreField.clear();
                telefonoField.clear();
                emailField.clear();

                // Vuelve a configurar el botón a agregar
                Button agregarButton = new Button("Agregar Cliente");
                agregarButton.setOnAction(event -> agregarCliente());
            });
        } else {
            System.out.println("Por favor selecciona un cliente para editar.");
        }
    }

    private void actualizarTabla() {
        tablaClientes.getItems().clear();
        List<ClienteDAO> clientes = clienteDAO.SELECT_ALL(); // Obtiene todos los clientes
        tablaClientes.getItems().addAll(clientes); // Agrega todos los clientes a la tabla
    }
}
