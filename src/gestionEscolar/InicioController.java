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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class InicioController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    Button buttonAlumnos, buttonGruposUnidades, buttonCerrarSesion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
        Stage stageInicioGestion = (Stage)((Node)e.getSource()).getScene().getWindow();
        if(e.getSource().equals(buttonAlumnos)){
            FXMLLoader alumnos = new FXMLLoader(getClass().getResource("Alumnos.fxml"));
            Scene sceneProfesores = new Scene(alumnos.load());
            stageInicioGestion.setScene(sceneProfesores);
        }
        if(e.getSource().equals(buttonGruposUnidades)){
            FXMLLoader grupos = new FXMLLoader(getClass().getResource("Grupos.fxml"));
            Scene sceneGrupos = new Scene(grupos.load());
            stageInicioGestion.setScene(sceneGrupos);
        }
    }
    
}
