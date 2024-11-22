package com.example.tap2024b.vistas;



import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.InputStream;
import java.util.*;

public class Loteria extends Stage {

    private HBox hBoxMain, hBoxButtons;
    private VBox vbxTablilla, vbxMazo;
    private Button btnIniciar;
    private Label lblTimer, lblVictoria;
    private GridPane gdpTablilla;
    private ImageView imvMazo;
    private Scene escena;
    private List<String[]> tableros;
    private int currentTablero = 0;
    private Button[][] arBtnTab;

    private Panel pnlPrincipal;
    private List<String> mazoCartas;
    private Set<String> cartasMostradas; // Cartas que ya han aparecido en el mazo
    private int currentCartaIndex = 0;
    private boolean juegoIniciado = false;
    private long tiempoInicio;

    public Loteria() {
        CrearTableros();
        CrearMazoCartas();
        CrearUI();
        this.setTitle("Lotería Mexicana :)");
        this.setScene(escena);
        this.show();
    }

    private void CrearUI() {
        gdpTablilla = new GridPane();
        CrearTablilla();

        // Crear botón para seleccionar la tablilla
        Button btnSeleccionarTablilla = new Button("Seleccionar Tablilla");
        btnSeleccionarTablilla.setOnAction(e -> mostrarDialogoSeleccionTablilla());

        vbxTablilla = new VBox(gdpTablilla, btnSeleccionarTablilla);
        vbxTablilla.setSpacing(10);

        CrearMazo();

        hBoxMain = new HBox(vbxTablilla, vbxMazo);
        pnlPrincipal = new Panel("Lotería Mexicana :)");
        pnlPrincipal.getStyleClass().add("panel-success");
        pnlPrincipal.setBody(hBoxMain);
        hBoxMain.setSpacing(20);
        hBoxMain.setPadding(new Insets(20));
        escena = new Scene(pnlPrincipal, 1000, 700); // Tamaño de ventana aumentado
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        escena.getStylesheets().add(getClass().getResource("/styles/loteria.css").toExternalForm());
    }

    private void CrearMazo() {
        lblTimer = new Label("00:00");
        lblTimer.setStyle("-fx-font-size: 30px; -fx-text-fill: #fafafa;");

        lblVictoria = new Label();
        lblVictoria.setStyle("-fx-font-size: 24; -fx-text-fill: #d0de15;");
        imvMazo = new ImageView(new Image(getClass().getResource("/images/dorso.jpeg").toString()));
        imvMazo.setFitHeight(450);
        imvMazo.setFitWidth(300);
        btnIniciar = new Button("Iniciar Juego");
        btnIniciar.getStyleClass().setAll("btn-sm", "btn-danger");
        vbxMazo = new VBox(lblTimer, imvMazo, btnIniciar, lblVictoria);
        vbxMazo.setSpacing(10);
        btnIniciar.setOnAction(e -> iniciarJuego());
    }

    private void CrearTablilla() {
        arBtnTab = new Button[4][4];
        ActualizarTablilla();
    }

    private void mostrarDialogoSeleccionTablilla() {
        List<String> opcionesTableros = new ArrayList<>();
        for (int i = 0; i < tableros.size(); i++) {
            opcionesTableros.add("Tablilla " + (i + 1));
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(opcionesTableros.get(0), opcionesTableros);
        dialog.setTitle("Seleccionar Tablilla");
        dialog.setHeaderText("Elige una tablilla para jugar");
        dialog.setContentText("Tablilla:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selected -> {
            currentTablero = opcionesTableros.indexOf(selected);
            ActualizarTablilla();
        });
    }

    private void ActualizarTablilla() {
        gdpTablilla.getChildren().clear();
        String[] tableroActual = tableros.get(currentTablero);
        Image img;
        ImageView imv;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String nombreCarta = tableroActual[(i * 4 + j) % tableroActual.length];
                img = cargarImagen(nombreCarta);
                imv = new ImageView(img);
                imv.setFitWidth(70);
                imv.setFitHeight(120);
                arBtnTab[j][i] = new Button();
                arBtnTab[j][i].setGraphic(imv);

                final String nombreCartaFinal = nombreCarta;
                int finalJ = j;
                int finalI = i;
                arBtnTab[j][i].setOnAction(e -> seleccionarCarta(arBtnTab[finalJ][finalI], nombreCartaFinal));

                gdpTablilla.add(arBtnTab[j][i], j, i);
            }
        }
    }

    private void seleccionarCarta(Button btn, String nombreCarta) {
        // Verificar si la carta ya ha aparecido en el mazo
        if (cartasMostradas.contains(nombreCarta)) {
            btn.setDisable(true);
            verificarVictoria();
        } else {
            System.out.println("Carta seleccionada NO ha aparecido en el mazo: " + nombreCarta);
        }
    }

    private void verificarVictoria() {
        for (Button[] fila : arBtnTab) {
            for (Button btn : fila) {
                if (!btn.isDisabled()) {
                    return; // Aún hay cartas sin seleccionar
                }
            }
        }
        mostrarMensajeVictoria();
    }

    private void mostrarMensajeVictoria() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Victoria!");
        alert.setHeaderText("¡Felicidades!");
        alert.setContentText("¡Has ganado al completar todo el tablero!");
        alert.showAndWait();
    }

    private Image cargarImagen(String nombre) {
        String[] extensiones = {".png", ".jpg", ".jpeg"};
        for (String extension : extensiones) {
            InputStream is = getClass().getResourceAsStream("/images/" + nombre + extension);
            if (is != null) {
                return new Image(is);
            }
        }
        return new Image(getClass().getResource("/images/default.png").toString());
    }

    private void CrearTableros() {
        tableros = new ArrayList<>();
        tableros.add(new String[]{"pescado", "musico", "valiente", "calavera", "negrito", "paraguas", "chalupa", "sandia",
                "rana", "barril", "arpa", "cotorro", "botella", "violoncello", "corona", "árbol"});
        tableros.add(new String[]{"alacran", "apache", "cazo", "diablito", "cantarito", "jaras", "palma", "arana",
                "bota", "escalera", "muerte", "cotorro", "luna", "nopal", "pera", "bandolón"});
        tableros.add(new String[]{"maceta", "camaron", "mano", "pajaro", "garza", "estrella", "pescado", "musico",
                "valiente", "calavera", "negrito", "paraguas", "chalupa", "sandia", "rana", "barril"});
        tableros.add(new String[]{"arpa", "cotorro", "botella", "violoncello", "corona", "árbol", "alacran", "apache",
                "cazo", "diablito", "cantarito", "jaras", "palma", "arana", "bota", "escalera"});
        tableros.add(new String[]{"muerte", "cotorro", "luna", "nopal", "pera", "bandolón", "maceta", "camaron",
                "mano", "pajaro", "garza", "estrella", "pescado", "musico", "valiente", "calavera"});
    }

    private void CrearMazoCartas() {
        mazoCartas = new ArrayList<>();
        cartasMostradas = new HashSet<>(); // Inicializamos el conjunto de cartas mostradas
        mazoCartas.addAll(Arrays.asList("pescado", "musico", "valiente", "calavera", "negrito", "paraguas", "chalupa", "sandia",
                "rana", "barril", "arpa", "cotorro", "botella", "violoncello", "corona", "árbol", "alacran", "apache",
                "cazo", "diablito", "cantarito", "jaras", "palma", "arana", "bota", "escalera", "muerte", "cotorro", "luna",
                "nopal", "pera", "bandolón", "maceta", "camaron", "mano", "pajaro", "garza", "estrella"));
        Collections.shuffle(mazoCartas);
    }

    private void iniciarJuego() {
        if (!juegoIniciado) {
            juegoIniciado = true;
            btnIniciar.setDisable(true);
            tiempoInicio = System.currentTimeMillis();
            currentCartaIndex = 0;

            new Thread(() -> {
                while (currentCartaIndex < mazoCartas.size()) {
                    try {
                        Thread.sleep(4000); // Cambiar carta cada 4 segundos
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String nombreCarta = mazoCartas.get(currentCartaIndex);
                    cartasMostradas.add(nombreCarta); // Añadir carta al conjunto de cartas mostradas
                    Image imgCarta = cargarImagen(nombreCarta);
                    Platform.runLater(() -> imvMazo.setImage(imgCarta));
                    currentCartaIndex++;
                    actualizarTiempoTranscurrido();
                }
                Platform.runLater(() -> lblVictoria.setText("Juego terminado"));
            }).start();
        }
    }

    private void actualizarTiempoTranscurrido() {
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
        long segundos = (tiempoTranscurrido / 1000) % 60;
        long minutos = (tiempoTranscurrido / (1000 * 60)) % 60;
        Platform.runLater(() -> lblTimer.setText(String.format("%02d:%02d", minutos, segundos)));
    }
}




/*
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.InputStream;
import java.util.*;

public class Loteria extends Stage {

    private HBox hBoxMain, hBoxButtons;
    private VBox vbxTablilla, vbxMazo;
    private Button btnAnterior, btnSiguiente, btnIniciar;
    private Label lblTimer, lblVictoria;
    private GridPane gdpTablilla;
    private ImageView imvMazo;
    private Scene escena;
    private List<String[]> tableros;
    private int currentTablero = 0;
    private Button[][] arBtnTab;

    private Panel pnlPrincipal;
    private List<String> mazoCartas;
    private Set<String> cartasMostradas; // Cartas que ya han aparecido en el mazo
    private int currentCartaIndex = 0;
    private boolean juegoIniciado = false;
    private long tiempoInicio;

    public Loteria() {
        CrearTableros();
        CrearMazoCartas();
        CrearUI();
        this.setTitle("Loteria Mexicana :)");
        this.setScene(escena);
        this.show();
    }

    private void CrearUI() {
        ImageView imvAnt, imvSig;
        imvAnt = new ImageView(new Image(getClass().getResource("/images/prev.png").toString()));
        imvAnt.setFitHeight(50);
        imvAnt.setFitWidth(50);
        imvSig = new ImageView(new Image(getClass().getResource("/images/next.png").toString()));
        imvSig.setFitWidth(50);
        imvSig.setFitHeight(50);

        gdpTablilla = new GridPane();
        CrearTablilla();

        btnAnterior = new Button();
        btnAnterior.setGraphic(imvAnt);
        btnAnterior.setOnAction(e -> CambiarTablero(-1));

        btnSiguiente = new Button();
        btnSiguiente.setGraphic(imvSig);
        btnSiguiente.setOnAction(e -> CambiarTablero(1));

        hBoxButtons = new HBox(btnAnterior, btnSiguiente);
        vbxTablilla = new VBox(gdpTablilla, hBoxButtons);

        CrearMazo();

        hBoxMain = new HBox(vbxTablilla, vbxMazo);
        pnlPrincipal = new Panel("Loteria Mexicana :)");
        pnlPrincipal.getStyleClass().add("panel-success");
        pnlPrincipal.setBody(hBoxMain);
        hBoxMain.setSpacing(20);
        hBoxMain.setPadding(new Insets(20));
        escena = new Scene(pnlPrincipal, 800, 600);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        escena.getStylesheets().add(getClass().getResource("/styles/loteria.css").toExternalForm());
    }

    private void CrearMazo() {
        lblTimer = new Label("00:00");
        lblTimer.setStyle("-fx-font-size: 30px; -fx-text-fill: #fafafa;");

        lblVictoria = new Label();
        lblVictoria.setStyle("-fx-font-size: 24; -fx-text-fill: #d0de15;");
        imvMazo = new ImageView(new Image(getClass().getResource("/images/dorso.jpeg").toString()));
        imvMazo.setFitHeight(450);
        imvMazo.setFitWidth(300);
        btnIniciar = new Button("Iniciar Juego");
        btnIniciar.getStyleClass().setAll("btn-sm", "btn-danger");
        vbxMazo = new VBox(lblTimer, imvMazo, btnIniciar, lblVictoria);
        vbxMazo.setSpacing(10);
        btnIniciar.setOnAction(e -> iniciarJuego());
    }

    private void CrearTablilla() {
        arBtnTab = new Button[4][4];
        ActualizarTablilla();
    }

    private void CambiarTablero(int direccion) {
        currentTablero = (currentTablero + direccion + tableros.size()) % tableros.size();
        ActualizarTablilla();
    }

    private void ActualizarTablilla() {
        gdpTablilla.getChildren().clear();
        String[] tableroActual = tableros.get(currentTablero);
        Image img;
        ImageView imv;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String nombreCarta = tableroActual[(i * 4 + j) % tableroActual.length];
                img = cargarImagen(nombreCarta);
                imv = new ImageView(img);
                imv.setFitWidth(70);
                imv.setFitHeight(120);
                arBtnTab[j][i] = new Button();
                arBtnTab[j][i].setGraphic(imv);

                final String nombreCartaFinal = nombreCarta;
                int finalJ = j;
                int finalI = i;
                arBtnTab[j][i].setOnAction(e -> seleccionarCarta(arBtnTab[finalJ][finalI], nombreCartaFinal));

                gdpTablilla.add(arBtnTab[j][i], j, i);
            }
        }
    }

    private void seleccionarCarta(Button btn, String nombreCarta) {
        // Verificar si la carta ya ha aparecido en el mazo
        if (cartasMostradas.contains(nombreCarta)) {
            System.out.println("Carta seleccionada: " + nombreCarta);
            btn.setDisable(true);
            verificarVictoria();
        } else {
            System.out.println("Carta seleccionada NO ha aparecido en el mazo: " + nombreCarta);
        }
    }

    private void verificarVictoria() {
        for (Button[] fila : arBtnTab) {
            for (Button btn : fila) {
                if (!btn.isDisabled()) {
                    return; // Aún hay cartas sin seleccionar
                }
            }
        }
        lblVictoria.setText("¡Victoria  has sido ganador!!!");
    }

    private Image cargarImagen(String nombre) {
        String[] extensiones = {".png", ".jpg", ".jpeg"};
        for (String extension : extensiones) {
            InputStream is = getClass().getResourceAsStream("/images/" + nombre + extension);
            if (is != null) {
                return new Image(is);
            }
        }
        return new Image(getClass().getResource("/images/default.png").toString());
    }

    private void CrearTableros() {
        tableros = new ArrayList<>();
        tableros.add(new String[]{"pescado", "musico", "valiente", "calavera", "negrito", "paraguas", "chalupa", "sandia",
                "rana", "barril", "arpa", "cotorro", "botella", "violoncello", "corona", "árbol"});
        tableros.add(new String[]{"alacran", "apache", "cazo", "diablito", "cantarito", "jaras", "palma", "arana",
                "bota", "escalera", "muerte", "cotorro", "luna", "nopal", "pera", "bandolón"});
        tableros.add(new String[]{"maceta", "camaron", "mano", "pajaro", "garza", "estrella", "pescado", "musico",
                "valiente", "calavera", "negrito", "paraguas", "chalupa", "sandia", "rana", "barril"});
        tableros.add(new String[]{"arpa", "cotorro", "botella", "violoncello", "corona", "árbol", "alacran", "apache",
                "cazo", "diablito", "cantarito", "jaras", "palma", "arana", "bota", "escalera"});
        tableros.add(new String[]{"muerte", "cotorro", "luna", "nopal", "pera", "bandolón", "maceta", "camaron",
                "mano", "pajaro", "garza", "estrella", "pescado", "musico", "valiente", "calavera"});
    }

    private void CrearMazoCartas() {
        mazoCartas = new ArrayList<>();
        cartasMostradas = new HashSet<>(); // Inicializamos el conjunto de cartas mostradas
        mazoCartas.addAll(Arrays.asList("pescado", "musico", "valiente", "calavera", "negrito", "paraguas", "chalupa", "sandia",
                "rana", "barril", "arpa", "cotorro", "botella", "violoncello", "corona", "árbol", "alacran", "apache",
                "cazo", "diablito", "cantarito", "jaras", "palma", "arana", "bota", "escalera", "muerte", "cotorro", "luna",
                "nopal", "pera", "bandolón", "maceta", "camaron", "mano", "pajaro", "garza", "estrella"));
        Collections.shuffle(mazoCartas);
    }

    private void iniciarJuego() {
        if (!juegoIniciado) {
            juegoIniciado = true;
            btnAnterior.setDisable(true);
            btnSiguiente.setDisable(true);
            btnIniciar.setDisable(true);
            tiempoInicio = System.currentTimeMillis();
            currentCartaIndex = 0;

            new Thread(() -> {
                while (currentCartaIndex < mazoCartas.size()) {
                    try {
                        Thread.sleep(4000); // Cambiar carta cada 4 segundos
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String nombreCarta = mazoCartas.get(currentCartaIndex);
                    cartasMostradas.add(nombreCarta); // Añadir carta al conjunto de cartas mostradas
                    Image imgCarta = cargarImagen(nombreCarta);
                    Platform.runLater(() -> imvMazo.setImage(imgCarta));
                    currentCartaIndex++;
                    actualizarTiempoTranscurrido();
                }
                Platform.runLater(() -> lblVictoria.setText("Juego terminado"));
            }).start();
        }
    }

    private void actualizarTiempoTranscurrido() {
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
        long segundos = (tiempoTranscurrido / 1000) % 60;
        long minutos = (tiempoTranscurrido / (1000 * 60)) % 60;
        Platform.runLater(() -> lblTimer.setText(String.format("%02d:%02d", minutos, segundos)));
    }
}
*/