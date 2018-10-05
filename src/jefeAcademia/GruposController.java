/*
 * Copyright 2018 caleb.
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author caleb
 */
public class GruposController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    Text textInicio;
    @FXML
    Button buttonNuevoGrupo, buttonAsignarProfesor, buttonEditarGrupo;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textInicio.setOnMouseEntered((MouseEvent me) -> {
            textInicio.setUnderline(true);
            textInicio.setFill(Color.BLUE);
        });
        textInicio.setOnMouseExited((MouseEvent me) -> {
            textInicio.setUnderline(false);
            textInicio.setFill(Color.BLACK);
        });
        textInicio.setOnMouseClicked((MouseEvent me) -> {
            Stage stageGrupos = (Stage) (textInicio.getScene().getWindow());
            FXMLLoader inicioJefe = new FXMLLoader(getClass().getResource("Inicio.fxml"));
            Scene sceneInicioJefe;
            try {
                sceneInicioJefe = new Scene(inicioJefe.load());
                stageGrupos.setScene(sceneInicioJefe);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML
    public void irA(ActionEvent e) throws IOException{
        Stage stageGrupos = (Stage) ((Node) e.getSource()).getScene().getWindow();
        if (e.getSource().equals(buttonNuevoGrupo)) {
            FXMLLoader crearGrupos = new FXMLLoader(getClass().getResource("CrearGrupo.fxml"));
            Scene sceneCrearGrupos = new Scene(crearGrupos.load());
            stageGrupos.setScene(sceneCrearGrupos);
        }
        if (e.getSource().equals(buttonAsignarProfesor)) {
            
        }
        if (e.getSource().equals(buttonEditarGrupo)) {
            
        }
    }
    
}
