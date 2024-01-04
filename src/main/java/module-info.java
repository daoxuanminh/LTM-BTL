module ltm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
	requires javafx.graphics;
	requires javafx.base;
	requires java.logging;
	requires java.sql;
    opens caro to javafx.fxml;
    exports caro;
    opens caro.controllers;
}