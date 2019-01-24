/*
 * Copyright 2019 Caleb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jefeAcademia;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import poliasistenciafx.ConsultarDatos;
import static poliasistenciafx.IniciarSesionController.ID_PERSONA;
import poliasistenciafx.Persona;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class CambiarContrasenaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Text textAjustes;
    @FXML
    Button buttonCancelar, buttonCambiar;
    @FXML
    PasswordField passwordFieldActual, passwordFieldNueva, passwordFieldNuevaConfirmacion;
    
    Preferences sesion;
    public static final int  CONTRASENA_CAMBIADA = 1, CONTRASENA_INCORRECTA = 2,  NUMERO_INVALIDO = 3;
    protected String contrasenaActual, contrasenaNueva, contrasenaNuevaConf;
    ConsultarDatos consultar;
    private Persona persona;
    private int idPersona = 0;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultar = new ConsultarDatos();
        sesion = Preferences.userRoot();
        idPersona = sesion.getInt(ID_PERSONA, 0);
        persona = consultar.obtenerDatosUsuario(idPersona) == null ? new Persona() : consultar.obtenerDatosUsuario(idPersona);
              
        
        textAjustes.setOnMouseEntered((MouseEvent me) -> {
            textAjustes.setUnderline(true);
            textAjustes.setFill(Color.BLUE);
        });
        textAjustes.setOnMouseExited((MouseEvent me) -> {
            textAjustes.setUnderline(false);
            textAjustes.setFill(Color.BLACK);
        });
        textAjustes.setOnMouseClicked((MouseEvent me) -> {
            irAInicio();
        });
    }
    
    @FXML
    public void cambiarContrasena(){
        contrasenaActual = passwordFieldActual.getText();
        contrasenaNueva = passwordFieldNueva.getText();
        contrasenaNuevaConf = passwordFieldNuevaConfirmacion.getText();
        
        if(contrasenaActual.equals("") || contrasenaNueva.equals("") || contrasenaNuevaConf.equals("")){
            crearDialogo("No se puede cambiar la contraseña", "Asegurate de no dejar campos vacios", null);
            passwordFieldActual.setText("");
            passwordFieldActual.requestFocus();
            passwordFieldNueva.setText("");
            passwordFieldNuevaConfirmacion.setText("");
            contrasenaActual = "";
            contrasenaNueva = "";
            contrasenaNuevaConf = "";
            return;
        }
        
        if(!contrasenaNueva.equals(contrasenaNuevaConf)){
            crearDialogo("No se puede cambiar la contraseña", "Las nuevas contraseñas no son identicas", null);
            passwordFieldNueva.setText("");
            passwordFieldNueva.requestFocus();
            passwordFieldNuevaConfirmacion.setText("");
            contrasenaActual = "";
            contrasenaNueva = "";
            contrasenaNuevaConf = "";
            return;
        }
        
        int estatus = consultar.cambiarContrasena(4, persona.getNumero(), contrasenaActual, contrasenaNueva);
        
        if(estatus == CONTRASENA_CAMBIADA){
            crearDialogo("Contraseña actualizada", "Tu contraseña se ha actualizado correctamente", null);
            Stage stageAjustes = (Stage) (textAjustes.getScene().getWindow());
            FXMLLoader ajustes = new FXMLLoader(getClass().getResource("Ajustes.fxml"));
            try {
                Scene sceneAjuestes = new Scene(ajustes.load());
                stageAjustes.setScene(sceneAjuestes);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            if(estatus == CONTRASENA_INCORRECTA){
                crearDialogo("Error al cambiar contraseña", "La contraseña que ingresaste no es la correcta. Intentalo de nuevo", null);
                passwordFieldActual.setText("");
                passwordFieldActual.requestFocus();
                passwordFieldNueva.setText("");
                passwordFieldNuevaConfirmacion.setText("");
                contrasenaActual = "";
                contrasenaNueva = "";
                contrasenaNuevaConf = "";
                return;
            }
            
            if(estatus == NUMERO_INVALIDO || estatus == -1 || estatus != CONTRASENA_CAMBIADA || estatus != CONTRASENA_INCORRECTA){
                crearDialogo("Error al cambiar contraseña", "No se puede cambiar la contraseña", null);
                passwordFieldActual.setText("");
                passwordFieldActual.requestFocus();
                passwordFieldNueva.setText("");
                passwordFieldNuevaConfirmacion.setText("");
                contrasenaActual = "";
                contrasenaNueva = "";
                contrasenaNuevaConf = "";
            }
            
        }
        
    }
    
    @FXML
    public void cancelarCambioContrasena(){
        irAInicio();
    }
    

    public void irAInicio() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar acción");
        alert.setHeaderText("¿Estás seguro de que quieres salir?");
        alert.setContentText("No se guardaran los datos ingresados");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageAjustes = (Stage) (textAjustes.getScene().getWindow());
            FXMLLoader ajustes = new FXMLLoader(getClass().getResource("Ajustes.fxml"));
            try {
                Scene sceneAjuestes = new Scene(ajustes.load());
                stageAjustes.setScene(sceneAjuestes);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

        }
    }
    
    public void crearDialogo(String titulo, String header, String contexto) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contexto);
        alert.showAndWait();
    }
    
}
