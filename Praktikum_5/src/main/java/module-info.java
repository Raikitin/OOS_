module OOS.P5 {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    //requires java.desktop;

    opens com.oos to javafx.fxml;
    opens bank;
    opens bank.exceptions;

    exports bank.exceptions;
    exports bank;
    exports com.oos;
}