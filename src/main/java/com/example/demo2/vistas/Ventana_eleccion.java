package com.example.demo2.vistas;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.kordamp.bootstrapfx.BootstrapFX;
import javafx.scene.layout.StackPane;

public class Ventana_eleccion extends Stage {

    private Scene mainScene;
    private VBox buttonPanel;
    private TextField difficultyField;
    private Button highDifficulty;
    private Button mediumDifficulty;
    private Button lowDifficulty;
    private Button startButton;
    private int selectedDifficulty;
    private Label difficultyLabel;
    private rompecabezas puzzle;


    public Ventana_eleccion() {
        initializeUI();
        this.setScene(mainScene);
        this.setTitle("PUZZLE MASTER - Select Difficulty");
        this.show();
    }

    private void initializeUI() {
        // Estilo moderno con fondo oscuro y acentos brillantes
        Rectangle background = new Rectangle(400, 550);
        background.setFill(Color.web("#2C3E50"));


        this.difficultyLabel = new Label("GAME DIFFICULTY:");
        difficultyLabel.setFont(Font.font("Arial", 16));
        difficultyLabel.setTextFill(Color.WHITE);

        this.selectedDifficulty = 1;
        this.startButton = new Button("START PUZZLE");
        startButton.setOnAction(event -> startGame());

        this.difficultyField = new TextField("EASY");
        difficultyField.setAlignment(Pos.CENTER);
        difficultyField.setEditable(false);
        difficultyField.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        highDifficulty = new Button("EXPERTO: 6x6");
        mediumDifficulty = new Button("MEDIO: 5x5");
        lowDifficulty = new Button("PRINCIPIANTE: 4x4");

        // Estilo de botones
        styleButton(highDifficulty, "#E74C3C");
        styleButton(mediumDifficulty, "#F39C12");
        styleButton(lowDifficulty, "#2ECC71");
        styleButton(startButton, "#3498DB");

        highDifficulty.setOnAction(event -> setDifficulty(3));
        mediumDifficulty.setOnAction(event -> setDifficulty(2));
        lowDifficulty.setOnAction(event -> setDifficulty(1));

        buttonPanel = new VBox(15, difficultyLabel, difficultyField,
                createSeparator(), lowDifficulty,
                createSeparator(), mediumDifficulty,
                createSeparator(), highDifficulty,
                createSeparator(), startButton);
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.setPadding(new Insets(30));
        buttonPanel.setStyle("-fx-background-color: rgba(44, 62, 80, 0.9);");

        StackPane root = new StackPane(background, buttonPanel);
        mainScene = new Scene(root, 400, 550);
        mainScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        mainScene.getStylesheets().add(getClass().getResource("/css/modern_puzzle.css").toString());
    }

    private Rectangle createSeparator() {
        Rectangle separator = new Rectangle(250, 2);
        separator.setFill(Color.web("#34495E"));
        return separator;
    }

    private void styleButton(Button button, String color) {
        button.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 10 20; -fx-background-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(" + color + ", 20%); " +
                "-fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 10 20; -fx-background-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 10 20; -fx-background-radius: 5;"));
    }

    private void startGame() {
        puzzle = new rompecabezas(selectedDifficulty);
        this.close();
    }

    private void setDifficulty(int difficulty) {
        this.selectedDifficulty = difficulty;
        this.difficultyField.clear();
        switch(difficulty) {
            case 3 -> this.difficultyField.appendText("EXPERT");
            case 2 -> this.difficultyField.appendText("ADVANCED");
            default -> this.difficultyField.appendText("BEGINNER");
        }
    }

    public int getSelectedDifficulty() {
        return this.selectedDifficulty;
    }
}
