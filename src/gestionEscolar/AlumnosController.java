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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jefeAcademia.ProfesoresController;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class AlumnosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Text textInicio;
    @FXML
    Button buttonRegistro, buttonEditarDatos;
    
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
            Stage stageAlumnos = (Stage) (textInicio.getScene().getWindow());
            FXMLLoader inicioGestion = new FXMLLoader(getClass().getResource("Inicio.fxml"));
            Scene sceneInicioGestion;
            try {
                sceneInicioGestion = new Scene(inicioGestion.load());
                stageAlumnos.setScene(sceneInicioGestion);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @FXML
    public void irA(ActionEvent e){
        Stage stageAlumnos = (Stage) (textInicio.getScene().getWindow());
        if(e.getSource().equals(buttonRegistro)){
            FXMLLoader registroAlumno = new FXMLLoader(getClass().getResource("RegistrarAlumno.fxml"));
            Scene sceneRegistroAlumno;
            try {
                sceneRegistroAlumno = new Scene(registroAlumno.load());
                stageAlumnos.setScene(sceneRegistroAlumno);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(e.getSource().equals(buttonEditarDatos)){
            FXMLLoader elegirAlumnoDatos = new FXMLLoader(getClass().getResource("ElegirAlumnoEditarDatos.fxml"));
            Scene sceneElegirAlumnoDatos;
            try {
                sceneElegirAlumnoDatos = new Scene(elegirAlumnoDatos.load());
                stageAlumnos.setScene(sceneElegirAlumnoDatos);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
