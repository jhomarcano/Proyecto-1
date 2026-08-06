module co.edu.poli.sw2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.poli.sw2 to javafx.fxml;
    exports co.edu.poli.sw2;
}