module com.bros.HissAndHit {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.lz4.java;
    requires protobuf.java;

    opens com.bros.HissAndHit to javafx.fxml;
    exports com.bros.HissAndHit;
}