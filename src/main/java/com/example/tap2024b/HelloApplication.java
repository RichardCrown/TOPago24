package com.example.tap2024b;


import com.example.tap2024b.models.Conexion;

import com.example.tap2024b.vistas.Calculadora;
import com.example.tap2024b.vistas.Loteria;
import com.example.tap2024b.vistas.UsuariosInterface;
import com.example.tap2024b.vistas.SimuladorExclusionMutua;
import com.example.tap2024b.vistas.Buscaminas;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private MenuBar menuBar;
    private Menu menuCompetencias;
    private MenuItem menuItemCalculadora;
    private MenuItem menuItemLoteria;
    private MenuItem menuItemUsuarios;
    private MenuItem menuItemSimuladorImpresion;
    private MenuItem menuItemBuscaminas;
    private StackPane stackPane;
    private VBox vBox;

    public void CreateUI() {
        // Crear barra de menú
        menuBar = new MenuBar();

        // Crear menú "Competencias"
        menuCompetencias = new Menu("Apps");

        // Crear ítem "Calculadora" dentro del menú "Competencias"
        menuItemCalculadora = new MenuItem("Calculadora");
        menuItemCalculadora.setOnAction(event -> {
            try {
                Calculadora calculadora = new Calculadora();
                Stage calculadoraStage = new Stage();
                calculadora.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Crear ítem "Lotería" dentro del menú "Competencias"
        menuItemLoteria = new MenuItem("Loteria");
        menuItemLoteria.setOnAction(event -> {
            try {
                Loteria loteria = new Loteria();
                Stage loteriaStage = new Stage();
                loteria.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Crear ítem "Usuarios" dentro del menú "Competencias"
        menuItemUsuarios = new MenuItem("Usuarios");
        menuItemUsuarios.setOnAction(event -> {
            new UsuariosInterface();
        });

        // Crear ítem "Simulador de Impresión" dentro del menú "Competencias"
        menuItemSimuladorImpresion = new MenuItem("Simulador de Impresión");
        menuItemSimuladorImpresion.setOnAction(event -> {
            try {
                new SimuladorExclusionMutua(); // El constructor se encarga de mostrar la ventana
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Crear ítem "Buscaminas" dentro del menú "Competencias"
        menuItemBuscaminas = new MenuItem("Buscaminas");
        menuItemBuscaminas.setOnAction(event -> {
            try {
                new Buscaminas(); // El constructor se encarga de mostrar la ventana
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Añadir los ítems al menú "Competencias"
        menuCompetencias.getItems().addAll(menuItemCalculadora, menuItemLoteria, menuItemUsuarios, menuItemSimuladorImpresion, menuItemBuscaminas);
        menuBar.getMenus().add(menuCompetencias);

        // Crear layout principal
        vBox = new VBox(menuBar);

        // Cargar la imagen de fondo desde el classpath
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/py.jpg"));

        // Crear un tamaño de fondo que cubra todo el VBox
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);

        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );

        // Asignar la imagen de fondo al VBox
        vBox.setBackground(new Background(background));

        // Crear el StackPane y añadir el VBox
        stackPane = new StackPane(vBox);

        // Cargar la imagen pequeña
        Image smallImage = new Image(getClass().getResourceAsStream("/images/bs.png"));
        ImageView imageView = new ImageView(smallImage);

        // Ajustar el tamaño de la imagen si es necesario
        imageView.setFitWidth(4);
        imageView.setFitHeight(800);
        imageView.setPreserveRatio(true);

        // Añadir la imagen pequeña al StackPane
        stackPane.getChildren().add(imageView);
        StackPane.setAlignment(imageView, Pos.CENTER);
    }

    @Override
    public void start(Stage stage) {
        CreateUI();

        // Establecer el ícono de la aplicación desde el classpath
        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/bs.png"));
            stage.getIcons().add(icon);
        } catch (NullPointerException e) {
            System.out.println("El ícono no se pudo cargar: " + e.getMessage());
        }

        Scene scene = new Scene(stackPane, 320, 240);
        stage.setTitle("TRABAJOS DE FRIAS PARCIAL 1-2 JAVAFX Y CSS");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}



/*
import com.example.tap2024b.models.Conexion;
import com.example.tap2024b.vistas.Calculadora;
import com.example.tap2024b.vistas.Loteria;
import com.example.tap2024b.vistas.UsuariosInterface; // Asegúrate de importar UsuariosInterface
import javafx.application.Application;
import javafx.geometry.Pos; // Importar Pos
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private MenuBar menuBar;
    private Menu menuCompetencias;
    private MenuItem menuItemCalculadora;
    private MenuItem menuItemLoteria;
    private MenuItem menuItemUsuarios; // Añadir la variable para el menú de Usuarios
    private StackPane stackPane; // Cambiar VBox a StackPane
    private VBox vBox;

    public void CreateUI() {
        // Crear barra de menú
        menuBar = new MenuBar();

        // Crear menú "Competencias"
        menuCompetencias = new Menu("Competencias");

        // Crear ítem "Calculadora" dentro del menú "Competencias"
        menuItemCalculadora = new MenuItem("Calculadora");

        // Crear ítem "Lotería" dentro del menú "Competencias"
        menuItemLoteria = new MenuItem("Loteria");

        // Crear ítem "Usuarios" dentro del menú "Competencias"
        menuItemUsuarios = new MenuItem("Usuarios"); // Crear el ítem Usuarios
        menuItemUsuarios.setOnAction(event -> {
            new UsuariosInterface(); // Abrir la interfaz de usuarios
        });

        // Añadir acción para abrir la calculadora
        menuItemCalculadora.setOnAction(event -> {
            try {
                Calculadora calculadora = new Calculadora();
                Stage calculadoraStage = new Stage();
                calculadora.show(); // Inicia la ventana de la calculadora
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Añadir acción para abrir la Lotería
        menuItemLoteria.setOnAction(event -> {
            try {
                Loteria loteria = new Loteria();
                Stage loteriaStage = new Stage();
                loteria.show(); // Inicia la ventana de la Lotería
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Añadir los ítems al menú "Competencias"
        menuCompetencias.getItems().addAll(menuItemCalculadora, menuItemLoteria, menuItemUsuarios); // Añadir Usuarios
        menuBar.getMenus().add(menuCompetencias);

        // Crear layout principal
        vBox = new VBox(menuBar);

        // Cargar la imagen de fondo desde el classpath
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/py.jpg")); // Ruta de la imagen dentro de resources

        // Crear un tamaño de fondo que cubra todo el VBox
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);

        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );

        // Asignar la imagen de fondo al VBox
        vBox.setBackground(new Background(background));

        // Crear el StackPane y añadir el VBox
        stackPane = new StackPane(vBox);

        // Cargar la imagen pequeña
        Image smallImage = new Image(getClass().getResourceAsStream("/images/bs.png")); // Ruta de la imagen pequeña
        ImageView imageView = new ImageView(smallImage);

        // Ajustar el tamaño de la imagen si es necesario
        imageView.setFitWidth(4); // Establecer el ancho
        imageView.setFitHeight(800); // Establecer la altura
        imageView.setPreserveRatio(true); // Mantener la proporción

        // Añadir la imagen pequeña al StackPane
        stackPane.getChildren().add(imageView);
        StackPane.setAlignment(imageView, Pos.CENTER); // Alinear la imagen en la parte superior
    }

    @Override
    public void start(Stage stage) {
        CreateUI();

        // Establecer el ícono de la aplicación desde el classpath
        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/bs.png"));
            stage.getIcons().add(icon);
        } catch (NullPointerException e) {
            System.out.println("El ícono no se pudo cargar: " + e.getMessage());
        }

        Scene scene = new Scene(stackPane, 320, 240);
        stage.setTitle("TRABAJOS DE FRIAS PARCIAL 1-2 JAVAFX Y CSS");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
*/