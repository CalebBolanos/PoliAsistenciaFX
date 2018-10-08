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
   
    @FXML
    Button buttonProfesor,buttonGrupos, buttonUnidades;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
    }
    
}
