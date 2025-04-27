package com.example.demo2.vistas;

import com.example.demo2.modulos.ClientesDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class cliente extends Stage {

    private Scene escena;

    private Label txtnombre,txtdireccion,txttelefono,txtemail;
    private VBox contenedor_padre;
    private Button enviar;
    private TextField nombre;
    private TextField direccion;
    private TextField telefono;
    private TextField email;
    private ClientesDAO objeto;
    private Label title;
    private Button salir;

    private TableView<ClientesDAO> tabla_clientes;

    public cliente(TableView<ClientesDAO> tabla_clientes){
        this.tabla_clientes=tabla_clientes;
        objeto = new ClientesDAO();
        CREAR_GUI();
        this.setTitle("Registrar Cliente");
        this.setScene(escena);
        this.show();
    }

    private void CREAR_GUI(){
        this.salir=new Button("Cancelar");
        this.title =new Label("Nuevo Cliente");
        txtnombre=new Label("Nombre:");
        nombre=new TextField();
        txtdireccion=new Label("Direccion:");
        direccion=new TextField();
        txttelefono=new Label("Telefono:");
        telefono=new TextField();
        txtemail=new Label("Correo Electronico:");
        email=new TextField();
        salir.setOnAction(event->{
            this.close();
        });
        enviar=new Button("Guardar");
        enviar.setOnAction(event->{
            objeto.setNombre(nombre.getText());
            objeto.setDireccion(direccion.getText());
            objeto.setTelefono(telefono.getText());
            objeto.setEmail(email.getText());
            objeto.INSERT();
            //ACTUALIZAR LA TABLA DE CLIENTES
            this.tabla_clientes.setItems(objeto.SELECT());
            this.tabla_clientes.refresh();
            this.close();
            //
        });
        contenedor_padre=new VBox(title,txtnombre,nombre,txtdireccion,direccion,txttelefono,telefono,txtemail,email,enviar,salir);
        contenedor_padre.setSpacing(20);
        contenedor_padre.setPadding(new Insets(20));
        contenedor_padre.setAlignment(Pos.CENTER);
        escena=new Scene(contenedor_padre,370,700);
        escena.getStylesheets().add(getClass().getResource("/css/estilo_restaurante.css").toString());
        nombre.getStyleClass().add("display-fondo");
        direccion.getStyleClass().add("display-fondo");
        telefono.getStyleClass().add("display-fondo");
        email.getStyleClass().add("display-fondo");
        enviar.getStyleClass().add("botones-negros");
        this.title.getStyleClass().add("title_space");
        this.salir.getStyleClass().add("botones-rojos");
    }

}


class ventana_mensaje extends Stage{

    private String mensaje;

    public ventana_mensaje(String mensaje){
        this.mensaje=mensaje;
    }
}
