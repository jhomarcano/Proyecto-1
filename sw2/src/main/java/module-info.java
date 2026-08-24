module co.edu.poli.sw2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens co.edu.poli.sw2 to javafx.fxml;
    opens co.edu.poli.sw2.controlador to javafx.fxml;
    opens co.edu.poli.sw2.modelo to javafx.base, javafx.fxml;

    exports co.edu.poli.sw2;
}