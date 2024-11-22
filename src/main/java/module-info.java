module com.example.tap2024b {
    requires javafx.controls;
    requires javafx.fxml;
    //opens org.example.tap2024b.models;
    requires java.sql;
    requires mysql.connector.j;
    opens com.example.tap2024b.models;


    opens com.example.tap2024b to javafx.fxml;
    exports com.example.tap2024b;

    //opens com.example.tap2024b.models to javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
}