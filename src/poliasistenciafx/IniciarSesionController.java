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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class IniciarSesionController implements Initializable {

    @FXML
    TextField textfieldUsuario;
    @FXML
    PasswordField passwordfieldContrasena;
    
    Preferences sesionUsr;
    public static final String ID_PERSONA = "idPersona";
    public static final String ID_TIPO = "idTipo";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sesionUsr = Preferences.userRoot();
        sesionUsr.putInt(ID_PERSONA, 0);
        sesionUsr.putInt(ID_TIPO, 0);
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
                    sesionUsr.putInt(ID_PERSONA, id[0]);
                    Stage stageInicioSesion = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    switch (id[1]) {
                        case 1://gestion
                            sesionUsr.putInt(ID_TIPO, id[1]);
                            FXMLLoader inicioGestion = new FXMLLoader(getClass().getResource("/gestionEscolar/Inicio.fxml"));
                            Scene sceneInicioGestion = new Scene(inicioGestion.load());
                            stageInicioSesion.setScene(sceneInicioGestion);
                            break;
                        case 4:///jefe de academia
                            sesionUsr.putInt(ID_TIPO, id[1]);
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
    
    @FXML
    public void mostrarHuella(ActionEvent e) throws IOException{
        Stage stage = new Stage();
        
        IniciarSesionHuellaController huella = new IniciarSesionHuellaController();
        FXMLLoader huellaDigital = new FXMLLoader(getClass().getResource("IniciarSesionHuella.fxml"));
        huellaDigital.setController(huella);
        
        Parent root = huellaDigital.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Iniciar Sesión con Huella Digital");
        stage.getIcons().add(new Image("/imagenes/poliAsistencia.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)e.getSource()).getScene().getWindow());
        stage.setOnHidden((WindowEvent evento) -> {
            huella.cerrar();
            int idPersona = huella.getIdPersona();
            int idTipo = huella.getIdTipo();
            if(idPersona != 0 && idTipo != 0){
                try {
                    sesionUsr.putInt(ID_PERSONA, idPersona);
                    Stage stageInicioSesion = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    switch (idTipo) {
                        case 1://gestion
                            sesionUsr.putInt(ID_TIPO, idTipo);
                            FXMLLoader inicioGestion = new FXMLLoader(getClass().getResource("/gestionEscolar/Inicio.fxml"));
                            Scene sceneInicioGestion = new Scene(inicioGestion.load());
                            stageInicioSesion.setScene(sceneInicioGestion);
                            break;
                        case 4:///jefe de academia
                            sesionUsr.putInt(ID_TIPO, idTipo);
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
                } catch (IOException ex) {
                    Logger.getLogger(IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
        stage.showAndWait();
    }

    public void crearDialogo(String titulo, String header, String contexto) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contexto);
        alert.showAndWait();
    }

}
