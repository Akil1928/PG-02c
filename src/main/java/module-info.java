module ucr.lab.pg02 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ucr.lab.pg02 to javafx.fxml;
    exports ucr.lab.pg02;
    exports controller;
    opens controller to javafx.fxml;

    //para que funcione el tableview con las clases de model en el TAB MONEDAS
    exports model;
    opens model to javafx.fxml;
}