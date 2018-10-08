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
package jefeAcademia;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Hora;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class AgregarHorarioUnidadController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Pane paneHorario;
    @FXML
    Text textInicio, textUnidades;
    @FXML
    ComboBox<Hora> comboboxInicioLunes, comboboxFinLunes, 
            comboboxInicioMartes, comboboxFinMartes,
            comboboxInicioMiercoles, comboboxFinMiercoles,
            comboboxInicioJueves, comboboxFinJueves,
            comboboxInicioViernes, comboboxFinViernes;
    @FXML
    Button buttonGuardar, buttonCancelar;
    @FXML
    CheckBox checkboxLunes, checkboxMartes, checkboxMiercoles, checkboxJueves, checkboxViernes;
    
    ConsultarDatos consultar;
    ObservableList<Integer> semestres;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultar = new ConsultarDatos();
        
        textInicio.setOnMouseEntered((MouseEvent me) -> {
            textInicio.setUnderline(true);
            textInicio.setFill(Color.BLUE);
        });
        textInicio.setOnMouseExited((MouseEvent me) -> {
            textInicio.setUnderline(false);
            textInicio.setFill(Color.BLACK);
        });
        textInicio.setOnMouseClicked((MouseEvent me) -> {
            Stage stageCrearUnidad = (Stage) (textInicio.getScene().getWindow());
            FXMLLoader inicioJefe = new FXMLLoader(getClass().getResource("Inicio.fxml"));
            Scene sceneInicioJefe;
            try {
                sceneInicioJefe = new Scene(inicioJefe.load());
                stageCrearUnidad.setScene(sceneInicioJefe);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        textUnidades.setOnMouseEntered((MouseEvent me) -> {
            textUnidades.setUnderline(true);
            textUnidades.setFill(Color.BLUE);
        });
        textUnidades.setOnMouseExited((MouseEvent me) -> {
            textUnidades.setUnderline(false);
            textUnidades.setFill(Color.BLACK);
        });
        textUnidades.setOnMouseClicked((MouseEvent me) -> {
            Stage stageCrearUnidad = (Stage) (textUnidades.getScene().getWindow());
            FXMLLoader unidades = new FXMLLoader(getClass().getResource("Inicio.fxml"));
            Scene sceneUnidades;
            try {
                sceneUnidades = new Scene(unidades.load());
                stageCrearUnidad.setScene(sceneUnidades);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        ObservableList<Hora> horas = crearHoras();
        
        comboboxInicioLunes.setItems(horas);
        comboboxFinLunes.setItems(horas);
        comboboxInicioMartes.setItems(horas);
        comboboxFinMartes.setItems(horas);
        comboboxInicioMiercoles.setItems(horas);
        comboboxFinMiercoles.setItems(horas);
        comboboxInicioJueves.setItems(horas);
        comboboxFinJueves.setItems(horas);
        comboboxInicioViernes.setItems(horas);
        comboboxFinViernes.setItems(horas);
        
        checkboxLunes.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean viejoValor, Boolean nuevoValor) -> {
            comboboxInicioLunes.setDisable(!nuevoValor);
            comboboxFinLunes.setDisable(!nuevoValor);
        });
        checkboxMartes.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean viejoValor, Boolean nuevoValor) -> {
            comboboxInicioMartes.setDisable(!nuevoValor);
            comboboxFinMartes.setDisable(!nuevoValor);
        });
        checkboxMiercoles.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean viejoValor, Boolean nuevoValor) -> {
            comboboxInicioMiercoles.setDisable(!nuevoValor);
            comboboxFinMiercoles.setDisable(!nuevoValor);
        });
        checkboxJueves.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean viejoValor, Boolean nuevoValor) -> {
            comboboxInicioJueves.setDisable(!nuevoValor);
            comboboxFinJueves.setDisable(!nuevoValor);
        });
        checkboxViernes.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean viejoValor, Boolean nuevoValor) -> {
            comboboxInicioViernes.setDisable(!nuevoValor);
            comboboxFinViernes.setDisable(!nuevoValor);
        });
    }


    public ObservableList<Hora> crearHoras(){
        ObservableList<Hora> horas = FXCollections.observableArrayList();
        Hora horax;
        for (int i = 1; i <= 15; i++) {
            horax = new Hora();
            horax.setId(i);
            horax.setHora(consultar.evaluarHora(i));
            horas.add(horax);
            horax = null;
        }
        return horas;
    }
    
}
