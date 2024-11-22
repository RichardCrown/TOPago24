package com.example.tap2024b.vistas;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Calculadora extends Stage {

    private Button[][] arrBtns;
    private TextField txtPantalla;
    private GridPane gdpTeclado;
    private VBox vBox;
    private Scene escena;
    private String[] strTeclas = {"7", "8", "9", "*", "4", "5", "6", "/", "1", "2", "3", "+", "0", ".", "=", "-"};
    private Button btnClear;

    private boolean esperandoOperador = false;
    private boolean operacionCompleta = false;
    private boolean divisionPorCero = false;
    private boolean errorSintaxis = false;
    private boolean puntoEnNumero = false; // Control para permitir solo un punto por número

    private void CreateUI() {
        arrBtns = new Button[4][4];
        txtPantalla = new TextField("0");
        txtPantalla.setAlignment(Pos.CENTER_RIGHT);
        gdpTeclado = new GridPane();
        CrearTeclado();
        btnClear = new Button("CE");
        btnClear.setOnAction(event -> reiniciarCalculadora());
        vBox = new VBox(txtPantalla, gdpTeclado, btnClear);
        btnClear.setId("font-button");
        escena = new Scene(vBox, 300, 300);
        escena.getStylesheets().add(getClass().getResource("/styles/cal.css").toString());
    }

    private void CrearTeclado() {
        for (int i = 0; i < arrBtns.length; i++) {
            for (int j = 0; j < arrBtns.length; j++) {
                arrBtns[j][i] = new Button(strTeclas[4 * i + j]);
                arrBtns[j][i].setPrefSize(75, 75);
                String tecla = strTeclas[4 * i + j];
                arrBtns[j][i].setOnAction(event -> DetectarTecla(tecla));
                gdpTeclado.add(arrBtns[j][i], j, i);
            }
        }
    }

    private void DetectarTecla(String tecla) {
        if (divisionPorCero || errorSintaxis) {
            reiniciarCalculadora();
        }

        if ("0123456789.".contains(tecla)) {
            manejarEntradaNumero(tecla);
        } else if ("+-*/".contains(tecla)) {
            manejarOperador(tecla);
        } else if ("=".equals(tecla)) {
            manejarIgual();
        }
    }

    private void manejarEntradaNumero(String tecla) {
        String textoActual = txtPantalla.getText();

        if (operacionCompleta || textoActual.equals("0")) {
            txtPantalla.setText(tecla.equals(".") ? "0." : tecla);
            operacionCompleta = false;
        } else {
            // Manejo del punto decimal
            if (tecla.equals(".")) {
                if (puntoEnNumero || textoActual.endsWith(" ") || textoActual.endsWith(".")) {
                    txtPantalla.setText("Error de sintaxis");
                    errorSintaxis = true;
                    return;
                }
                puntoEnNumero = true;
            }
            txtPantalla.appendText(tecla);
        }
        esperandoOperador = true;
    }

    private void manejarOperador(String operador) {
        String text = txtPantalla.getText();

        if (operacionCompleta) {
            operacionCompleta = false;
        }

        // Permitir el uso de '-' después de un operador para números negativos
        if (operador.equals("-") && (esperandoOperador || text.endsWith(" "))) {
            txtPantalla.appendText(operador);
            esperandoOperador = false;
            return;
        }

        if (!esperandoOperador && !operador.equals("-")) {
            txtPantalla.setText("Error de sintaxis");
            errorSintaxis = true;
            return;
        }

        if (!text.endsWith(" ") && !text.equals("Error")) {
            txtPantalla.appendText(" " + operador + " ");
            esperandoOperador = false;
            puntoEnNumero = false; // Restablecer para permitir el punto en el siguiente número
        }
    }

    private void manejarIgual() {
        String expresion = txtPantalla.getText();

        // Validación para asegurar que haya una sola operación
        int operatorCount = 0;
        for (char c : expresion.toCharArray()) {
            if ("+-*/".indexOf(c) != -1) {
                operatorCount++;
            }
        }
        if (operatorCount != 1) {
            txtPantalla.setText("Error: Solo una operación a la vez");
            errorSintaxis = true;
            return;
        }

        String resultado = evaluarExpresion(expresion);
        if (divisionPorCero) {
            txtPantalla.setText("Error: Div. por 0");
        } else {
            txtPantalla.setText(resultado);
        }
        operacionCompleta = true;
    }

    private String evaluarExpresion(String expresion) {
        int operatorIndex = -1;

        // Buscar la posición del operador
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if ("+-*/".indexOf(c) != -1) {
                operatorIndex = i;
                break;
            }
        }

        // Dividir la expresión en operandos y operador
        String operador = String.valueOf(expresion.charAt(operatorIndex));
        String operando1 = expresion.substring(0, operatorIndex).trim();
        String operando2 = expresion.substring(operatorIndex + 1).trim();

        // Validar que ambos operandos sean números
        if (!esNumero(operando1) || !esNumero(operando2)) {
            errorSintaxis = true;
            return "Error de sintaxis";
        }

        // Convertir operandos a números
        double num1 = Double.parseDouble(operando1);
        double num2 = Double.parseDouble(operando2);
        double resultado = 0;

        // Realizar la operación según el operador
        if (operador.equals("/") && num2 == 0) {
            divisionPorCero = true;
            return "Error: Div. por 0";
        }

        switch (operador) {
            case "+":
                resultado = num1 + num2;
                break;
            case "-":
                resultado = num1 - num2;
                break;
            case "*":
                resultado = num1 * num2;
                break;
            case "/":
                resultado = num1 / num2;
                break;
            default:
                errorSintaxis = true;
                return "Error";
        }

        return String.valueOf(resultado);
    }

    // Método auxiliar para verificar si una cadena es un número válido
    private boolean esNumero(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void reiniciarCalculadora() {
        txtPantalla.setText("0");
        divisionPorCero = false;
        errorSintaxis = false;
        esperandoOperador = false;
        operacionCompleta = false;
        puntoEnNumero = false;
    }

    public Calculadora() {
        CreateUI();
        this.setTitle("Calculadora");
        this.setScene(escena);
        this.show();
    }
}
