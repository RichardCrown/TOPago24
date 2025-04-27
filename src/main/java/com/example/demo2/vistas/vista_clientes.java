package com.example.demo2.vistas;

import com.example.demo2.Componenetes.ButtonCell;
import com.example.demo2.modulos.ClientesDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class vista_clientes extends Stage{


    private ToolBar menu;
    private TableView<ClientesDAO> table_clientes;
    private VBox contenedor_padre;
    private Scene escena;
    private Button agregar ;
    private Button salir;

    public vista_clientes(){
        CREAR_UI();
        this.setTitle("LISTA DE CLIENTES");
        this.setScene(escena);
        this.show();
    }

    private void CREAR_UI(){
        this.salir=new Button("Salir");
        this.table_clientes=new TableView<>();
        this.agregar=  new Button();
        ImageView img = new ImageView(getClass().getResource("/imagenes/interback.jpg").toString());
        img.setFitWidth(50);
        img.setFitHeight(50);
        this.agregar.setGraphic(img);
        this.menu= new ToolBar();
        this.salir.setOnAction(event->{
            this.close();
        });
        this.agregar.setOnAction(event->{
            new cliente(table_clientes);
        });
        create_table();
        this.contenedor_padre= new VBox(menu,agregar,salir,table_clientes);
        this.contenedor_padre.setSpacing(30);
        contenedor_padre.setPadding(new Insets(20));
        this.contenedor_padre.setAlignment(Pos.CENTER);
        this.escena=new Scene(contenedor_padre,1200,600);
        this.escena.getStylesheets().add(getClass().getResource("/css/estilo_restaurante.css").toString());
        this.agregar.getStyleClass().add("botones-negros");
        this.salir.getStyleClass().add("botones-rojos");
    }

    private void create_table(){
        ClientesDAO objeto_clientes = new ClientesDAO();
        TableColumn<ClientesDAO,Integer> table_id = new TableColumn<>("ID_Cliente");
        table_id.setCellValueFactory(new PropertyValueFactory<>("id_cliente"));
        TableColumn<ClientesDAO,String> table_nombre = new TableColumn<>("Nombre");
        table_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<ClientesDAO,String> table_direccion = new TableColumn<>("Direccion");
        table_direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        TableColumn<ClientesDAO,String> table_telefono = new TableColumn<>("Telefono");
        table_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        TableColumn<ClientesDAO,String> table_email = new TableColumn<>("Correo Electronico");
        table_email.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<ClientesDAO,String> editar_ = new TableColumn<>("Editar");
        editar_.setCellFactory(new Callback<TableColumn<ClientesDAO, String>, TableCell<ClientesDAO, String>>() {
            @Override
            public TableCell<ClientesDAO, String> call(TableColumn<ClientesDAO, String> clientesDAOStringTableColumn) {
                return new ButtonCell("Editar");
            }
        });
        TableColumn<ClientesDAO,String> eliminar_ = new TableColumn<>("Eliminar");
        eliminar_.setCellFactory(new Callback<TableColumn<ClientesDAO, String>, TableCell<ClientesDAO, String>>() {
            @Override
            public TableCell<ClientesDAO, String> call(TableColumn<ClientesDAO, String> clientesDAOStringTableColumn) {
                return new ButtonCell("Eliminar");
            }
        });

        this.table_clientes.getColumns().addAll(table_id,table_nombre,table_direccion,table_telefono,table_email,editar_,eliminar_);
        this.table_clientes.setItems(objeto_clientes.SELECT());
    }

}
