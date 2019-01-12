/*
 * Copyright 2018 PoliAsistencia.
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
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import poliasistenciafx.ConsultarDatos;
import static poliasistenciafx.IniciarSesionController.ID_PERSONA;
import poliasistenciafx.Persona;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class InicioController implements Initializable {
   
    @FXML
    Button buttonProfesor, buttonGrupos, buttonUnidades, buttonAjustes;
    
    Preferences sesionUsr;
    ConsultarDatos consultar;
    private int idPersona = 0;
    Persona persona;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sesionUsr = Preferences.userRoot();
        idPersona = sesionUsr.getInt(ID_PERSONA, 0);
        consultar = new ConsultarDatos();
        persona = consultar.obtenerDatosUsuario(idPersona) == null ? new Persona() : consultar.obtenerDatosUsuario(idPersona);
        buttonAjustes.setText(persona.getNombre());
    }
    
    @FXML
    public void cerrarSesion(ActionEvent e) throws IOException{
        Stage stageInicioJefe= (Stage)((Node)e.getSource()).getScene().getWindow();
        FXMLLoader inicioSesion = new FXMLLoader(getClass().getResource("/poliasistenciafx/IniciarSesion.fxml"));
        Scene sceneInicioSesion = new Scene(inicioSesion.load());
        stageInicioJefe.setScene(sceneInicioSesion);
    }
    
    @FXML
    public void irA(ActionEvent e)throws IOException{
        Stage stageInicioJefe= (Stage)((Node)e.getSource()).getScene().getWindow();
        if(e.getSource().equals(buttonProfesor)){
            FXMLLoader profesores = new FXMLLoader(getClass().getResource("Profesores.fxml"));
            Scene sceneProfesores = new Scene(profesores.load());
            stageInicioJefe.setScene(sceneProfesores);
        }
        if(e.getSource().equals(buttonGrupos)){
            FXMLLoader grupos = new FXMLLoader(getClass().getResource("Grupos.fxml"));
            Scene sceneGrupos = new Scene(grupos.load());
            stageInicioJefe.setScene(sceneGrupos);
        }
        if(e.getSource().equals(buttonUnidades)){
            FXMLLoader unidades = new FXMLLoader(getClass().getResource("Unidades.fxml"));
            Scene sceneUnidades = new Scene(unidades.load());
            stageInicioJefe.setScene(sceneUnidades);
        }
        if(e.getSource().equals(buttonAjustes)){
            FXMLLoader ajustes = new FXMLLoader(getClass().getResource("Ajustes.fxml"));
            Scene sceneAjustes = new Scene(ajustes.load());
            stageInicioJefe.setScene(sceneAjustes);
        }
    }
    
}
