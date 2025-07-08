# Empaquetar la aplicación usando jpackage

mvn clean package

jpackage --name VulcanoLite --input target --main-jar vulcano-lite-0.0.1-SNAPSHOT.jar --main-class main.Main --type app-image --java-options "--module-path /ruta/a/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml" --verbose
