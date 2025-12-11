module com.mycompany.jorge {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;

    // Controllers (FXML) use reflection
    opens com.mycompany.jorge to javafx.fxml;

    // Gson needs reflection for POJOs only
    opens com.mycompany.jorge.Models to com.google.gson;

    // Usually services do NOT need reflection
    // so you don't need to open or export them
    // opens com.mycompany.jorge.Services to com.google.gson; ← remove this

    exports com.mycompany.jorge;
    exports com.mycompany.jorge.Models; // optional, only if another module imports it
}
