package com.example.demo2.vistas;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Insets;
import java.io.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import java.util.ArrayList;

public class rompecabezas extends Stage{

    private Timeline timer;


    private Ventana_eleccion ventana_eleccion;
    private GridPane tablero;
    private Scene escena_principal;
    private Ventana_contador ventana_contador;
    private int tamano;

    private ArrayList<String> imagenes;
    private ArrayList<String> imagenes_desordenadas;

    private int valor;
    private Random rand;
    //tamaño de las casillas
    private int tamano_casilla;



    //VALORES DE PROCESO
    boolean ban=false;
    StackPersonal casilla_1;
    StackPersonal casilla_2;
    String img_1="";
    String img_2="";

    public rompecabezas(int valor){
        this.valor=valor;
        ventana_contador=new Ventana_contador(this);
        GUI_ROMPECABEZAS();
        this.setScene(escena_principal);
        this.setTitle("ii");
        this.show();
    }

    private void GUI_ROMPECABEZAS(){

        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), e->{
                    this.ventana_contador.marcador_tiempo.Aumentar_intentos();
                })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        imagenes = new ArrayList<>();
        rand=new Random();
        if(valor==3){
            this.tamano=6;
            this.tamano_casilla=55;

        }
        else if(valor==2){
            this.tamano=5;
            this.tamano_casilla=72;
        }
        else{
            this.tamano=4;
            this.tamano_casilla=105;
        }


        tablero= new GridPane();
        tablero.setAlignment(Pos.CENTER);
        crear_tablero();
        escena_principal= new Scene(tablero,1000,500);
    }


    public void cerrar_ventana_contador(){
        this.ventana_contador.close();
    }

    private void crear_tablero(){
        StringBuilder romp_a= new StringBuilder(seleccionar_rompecabezas());

        for(int iterrator_fila=1;iterrator_fila<=tamano;iterrator_fila++){
            for(int iterator_columna=1;iterator_columna<=tamano;iterator_columna++){


                int valor_casilla_f=iterrator_fila;
                int valor_casilla_columna=iterator_columna;
                String imagen =imagenes_desordenadas.get(((valor_casilla_f-1)*tamano+(valor_casilla_columna-1)+1)-1);
                int num_imagen = ((valor_casilla_f-1)*tamano+(valor_casilla_columna-1)+1);
                Casilla casilla = new Casilla(tamano_casilla,imagen,num_imagen,valor_casilla_f,valor_casilla_columna);

                String romp = romp_a.toString();
                romp=romp+(imagenes_desordenadas.get(num_imagen - 1));
                System.out.println(romp);
                ImageView img = new ImageView(getClass().getResource(romp).toString());
                img.setFitWidth(tamano_casilla*2);
                img.setFitHeight(tamano_casilla);

                StackPersonal stack = new StackPersonal(casilla,img);
                stack.setOnMouseClicked(event -> {System.out.println("Imagen: "+casilla.getImagen()+" Fila: "+casilla.getFila()+" Columna: "+casilla.getColumna()+" NO: "+casilla.getNumero_imagen());
                    process(stack,romp_a.toString(),casilla);
                });

                tablero.add(stack,iterator_columna,iterrator_fila);
            }
        }
    }

    private void process(StackPersonal stack,String romp_a,Casilla casilla){



        if(ban==false){
            ban=true;
            img_1=casilla.getImagen();
            casilla_1=stack;
        }
        else{
            if(!(casilla_1==stack)){
                ban=false;
                casilla_2=stack;
                img_2=casilla.getImagen();

                casilla_1.cambiar_imagen(romp_a+img_2);
                casilla_2.cambiar_imagen(romp_a+img_1);
                casilla_1.casilla.setImagen(img_2);
                casilla_2.casilla.setImagen(img_1);

                int indice_1 = imagenes_desordenadas.indexOf(img_1);
                int indice_2 = imagenes_desordenadas.indexOf(img_2);
                imagenes_desordenadas.set(indice_1,img_2);
                imagenes_desordenadas.set(indice_2,img_1);


                //OPRECION DE CAMBIO DE IMAGENES
                img_1="";
                img_2="";
                actualizar_tablero();
            }
            else{

            }
        }


    }

    public void detener_contador(){
        this.timer.stop();

    }

    private void actualizar_tablero(){
        if(!validar()){
            //CONTINUAR CON EL JUEGO
            ventana_contador.marcador_intentos.Aumentar_intentos();
        }
        else{
            //CREAR VENTANA DE FELICITACIONES
            this.detener_contador();
            Ventana_ganar ventana_fel = new Ventana_ganar(this,ventana_contador.marcador_intentos.getIntentos(),ventana_contador.marcador_tiempo.getIntentos());
        }
    }
    private boolean validar(){
        //COMPARACION
        boolean bandera=false;
        for(int index=0;index<(tamano*tamano);index++){
            if(imagenes_desordenadas.get(index).equals(imagenes.get(index))){
                bandera=true;
            }
            else{
                bandera=false;
                break;
            }
        }
        return bandera;
    }

    private String seleccionar_rompecabezas(){
        int rompecabezas_opcion=0;
        String val_init;
        String val_data;
        String carpeta_rompecabezas;
        String extension = ".jpg";

        rompecabezas_opcion= rand.nextInt(5)+1;


        val_init = switch (rompecabezas_opcion) {
            case 1 -> "1_";
            case 2 -> "1_";
            case 3 -> "1_";
            default -> "1_";
        };

        carpeta_rompecabezas = switch (rompecabezas_opcion) {
            case 1 -> "r_1_";
            case 2 -> "r_1_";
            case 3 -> "r_1_";
            default -> "r_1_";
        };

        carpeta_rompecabezas += switch (valor) {
            case 1 -> "1";
            case 2 -> "2";
            case 3 -> "3";
            default -> "1";
        };


        val_init += switch (valor) {
            case 1 -> "1_";
            case 2 -> "2_";
            case 3 -> "3_";
            default -> "1_";
        };


        int cant = tamano*tamano;
        for(int i=0;i<cant;i++){
            imagenes.add(val_init+(i+1)+extension);//AÑADIR CADA IMAGEN DENTRO DEL ARREGLO
        }

        //DESORDENAR ROMPECABEZAS
        desordenar();

        for(String imagen:imagenes_desordenadas){
            System.out.println(imagen);
        }
        String seleccion_rompecabezas;
        return seleccion_rompecabezas="/imagenes/"+carpeta_rompecabezas+"/";
    }

    private void desordenar(){
        imagenes_desordenadas = new ArrayList<>(imagenes);
        Collections.shuffle(imagenes_desordenadas);
    }

}

class StackPersonal extends StackPane{
    public Casilla casilla;
    public ImageView img;

    public StackPersonal(Casilla casilla,ImageView img){
        super();
        this.casilla=casilla;
        this.img=img;
        this.getChildren().addAll(this.casilla,this.img);
        // En la clase StackPersonal, agrega la clase CSS para las casillas

    }

    public void cambiar_imagen(String imagen_nueva){
        System.out.println(imagen_nueva);
        Image new_i = new Image(getClass().getResource(imagen_nueva).toString());
        this.img.setImage(new_i);
    }

}

class Casilla extends Rectangle{

    private String imagen;
    private int numero_imagen;
    private int fila;
    private int columna;

    public Casilla(int tam,String imagen,int numero_imagen,int fila,int columna){
        super(tam,tam, Color.RED);
        this.imagen=imagen;
        this.numero_imagen=numero_imagen;
        this.fila=fila;
        this.columna=columna;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setNumero_imagen(int numero_imagen) {
        this.numero_imagen = numero_imagen;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getNumero_imagen() {
        return numero_imagen;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public String getImagen(){
        return imagen;
    }

}

class Ventana_contador extends Stage{

    private Generador_Archivos archivo;

    Scene escena_principal;
    VBox contenedor_padre;
    Marcador_tiempo marcador_tiempo;
    Marcador_intentos marcador_intentos;
    Button salir;

    Label txt_intentos;
    Label txt_tiempo;

    public Ventana_contador(rompecabezas ventana_principal){
        INIT_VENTANA(ventana_principal);
        this.setScene(escena_principal);
        this.setTitle("ggg");
        this.setX(ventana_principal.getX()+ventana_principal.getWidth());
        this.setY(ventana_principal.getY()+ventana_principal.getHeight());

        this.show();
    }

    private void INIT_VENTANA(rompecabezas ventana_principal){
        this.salir=new Button("Salir");
        this.salir.setOnAction(e -> {
            ventana_principal.detener_contador();
            System.exit(0);
        });
        this.txt_intentos=new Label("Intentos:");
        this.txt_tiempo=new Label("Tiempo:");
        this.marcador_intentos= new Marcador_intentos();
        this.marcador_tiempo= new Marcador_tiempo();
        this.contenedor_padre=new VBox(txt_intentos,marcador_intentos,txt_tiempo,marcador_tiempo,salir);

        escena_principal = new Scene(contenedor_padre,230,200);
    }
}

class Marcador_tiempo extends Marcador{

    public Marcador_tiempo(){
        super();
    }

    public void esperarSegundo(){
        Aumentar_intentos();
    }


}

class Marcador_intentos extends Marcador{
    public Marcador_intentos(){
        super();
    }
}

abstract class Marcador extends TextField{

    private int intentos;

    public Marcador(){
        super("0");
        this.setEditable(false);
        this.setAlignment(Pos.BASELINE_CENTER);
        this.intentos=0;
    }

    public void Aumentar_intentos(){
        this.intentos+=1;
        this.clear();
        this.appendText(""+this.intentos);
    }

    public void Reset_intentos(){
        this.intentos=0;
        this.clear();
        this.appendText(""+this.intentos);
    }

    public int getIntentos(){
        return this.intentos;
    }


}


//CLASE DE REPORTE DEL JUEGO
class Generador_Archivos{

    private int intentos;
    private int tiempo;
    private boolean is_finalizada;
    private String descripcion;
    private LocalDateTime fecha_a;

    public Generador_Archivos(int intentos,int tiempo,boolean is_finalizada){
        this.fecha_a=LocalDateTime.now();
        this.intentos=intentos;
        this.tiempo=tiempo;
        this.is_finalizada=is_finalizada;
        exportar_archivo();
    }

    private void exportar_archivo(){

        String DIRECTORIO = "..";
        File carpeta = new File(DIRECTORIO);

        if(!carpeta.exists()){
            carpeta.mkdir();
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd_MM_yyyy HH-mm-ss");
        String fecha= fecha_a.format(format);

        this.descripcion="Fecha y Hora: "+ fecha +" Tus estadisticas de juego del Rompecabezas fueron de...";


        try (BufferedWriter archivo = new BufferedWriter(new FileWriter(DIRECTORIO+(fecha+"_stats_rompecabezas.txt")))) {

            archivo.write("ESTADISTICAS DE JUEGO DE ROMPECABEZAS");
            archivo.newLine();
            archivo.write(this.descripcion);
            archivo.newLine();
            archivo.newLine();
            String status;
            if(is_finalizada){
                status="FINALIZADA.";
            }
            else{
                status="No Finalizado.";
            }
            archivo.write("Status del Juego: " + status);
            archivo.newLine();
            archivo.write("Intentos->"+intentos);
            archivo.newLine();
            archivo.write("Tiempo->"+tiempo);
            archivo.newLine();


        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}

class Ventana_ganar extends Stage{

    private String mensaje;
    private int intentos;
    private int tiempo;
    private Button boton_otro;
    private Button boton_salir;
    private Generador_Archivos archivo;
    private Scene escena_principal;
    private VBox contenedor_padre;
    private Label titulo;
    private Label descripcion;

    public Ventana_ganar(rompecabezas ventana_principal,int intentos,int tiempo){
        this.intentos=intentos;
        this.tiempo=tiempo;
        mensaje="Haz completado \nel rompecabezas.";
        CREAR_GUI(ventana_principal);
        this.setTitle("FELICIDADES!!!");
        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(ventana_principal);
        this.setScene(escena_principal);
        this.setResizable(false);
        this.showAndWait();

    }

    private void CREAR_GUI(rompecabezas ventana_principal){
        this.descripcion=new Label(mensaje);
        this.titulo = new Label("FELICIDADES!!!");



        this.contenedor_padre=new VBox(titulo,descripcion);
        this.contenedor_padre.setSpacing(20);
        this.escena_principal=new Scene(contenedor_padre,352,100);


    }

}