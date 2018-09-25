/*
Copyright 2018 PoliAsistencia.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
 /* 
    Created on : 13-sep-2018, 6:12:27
    Author     : Caleb
 */
package poliasistenciafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class IniciarSesionController implements Initializable {

    @FXML
    TextField textfieldUsuario;
    @FXML
    PasswordField passwordfieldContrasena;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
        @FXML
    private void iniciarSesion(ActionEvent e) throws IOException {
        String usr = textfieldUsuario.getText();
        String pass = passwordfieldContrasena.getText();
        sesion ses = new sesion();
        int id[] = new int[2];
        validaciones val = new validaciones();
        if (val.sinVacios(usr, "el usuario", 40)) {
            if (val.sinVacios(pass, "la contraseña", 60)) {
                id = ses.iniciaSesion(usr, pass);
                if (id[0] > 0) {
                    switch (id[1]) {
                        case 1://gestion
                            System.out.println("gestion");
                            break;
                        case 4:///jefe de academia
                            Stage stageInicioSesion = (Stage) ((Node) e.getSource()).getScene().getWindow();
                            FXMLLoader inicioJefe = new FXMLLoader(getClass().getResource("/jefeAcademia/Inicio.fxml"));
                            Scene sceneInicioJefe = new Scene(inicioJefe.load());
                            stageInicioSesion.setScene(sceneInicioJefe);
                            break;
                        default:
                            crearDialogo("Error al iniciar Sesion", "Ususario o contraseña incorrecta", null);
                            passwordfieldContrasena.setText("");
                            textfieldUsuario.requestFocus();
                            break;
                    }
                } else {
                    crearDialogo("Error al iniciar Sesion", "Ususario o contraseña incorrecta", null);
                    passwordfieldContrasena.setText("");
                    textfieldUsuario.requestFocus();
                }
            } else {
                crearDialogo("Error al iniciar Sesion", "Error al iniciar Sesion", val.err());
                textfieldUsuario.requestFocus();
            }
        } else {
            crearDialogo("Error al iniciar Sesion", "Error al iniciar Sesion", val.err());
            textfieldUsuario.requestFocus();
        }
    }

    public void crearDialogo(String titulo, String header, String contexto) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contexto);
        alert.showAndWait();
    }

}
