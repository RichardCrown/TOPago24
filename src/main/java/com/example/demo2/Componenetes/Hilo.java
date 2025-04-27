//package com.example.demo2.Componenetes;
//
//import static java.lang.Thread.sleep;
//
//import java.util.Random;
//
//public class Hilo extends Thread {
//
//    public Hilo(String nombre) {
//        super(nombre);
//
//    }
//
//
//    @Override
//    public void run() {
//        super.run();
//        for (int i = 0; i < 10; i++) {// ejecutar el slit 10 veces
//            try {
//                sleep(((long)(Math.random()*3000)));//dormir el ciclo por 3 segundos
//            } catch (InterruptedException e) {
//            }
//            System.out.println("El corredor "+ this.getName() +" llego al KM "+i);
//        }
//    }
//}
//
//public Celayork(){
//
//    this.setTitle("Calles");
//
//}
