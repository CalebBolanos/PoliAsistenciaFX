/*
 * Copyright 2018 Caleb.
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
package gestionEscolar;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import poliasistenciafx.ConsultarDatos;
import static poliasistenciafx.IniciarSesionController.ID_PERSONA;
import poliasistenciafx.Persona;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class AjustesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button buttonInicio, buttonPersonal, buttonEditarDatos, buttonCambiarContrasena, buttonInfo;
    
    private Persona persona;
    Preferences sesion;
    private int idPersona = 0;
    ConsultarDatos consultar;
    String[] datos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sesion = Preferences.userRoot();
        idPersona = sesion.getInt(ID_PERSONA, 0);
        consultar = new ConsultarDatos();
        persona = consultar.obtenerDatosUsuario(idPersona) == null ? new Persona() : consultar.obtenerDatosUsuario(idPersona);
        datos = consultar.obtenerPersona(persona.getNumero());
    }
    
     @FXML
    public void irA(ActionEvent e) throws IOException{
    
    }
    
    @FXML
    public void abrirNavegador(ActionEvent e) throws IOException, URISyntaxException{
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            URI info = new URI("http://localhost:8080/poliAsistenciaWeb/info.html");
            Desktop.getDesktop().browse(info);
        }
    }
    
}
