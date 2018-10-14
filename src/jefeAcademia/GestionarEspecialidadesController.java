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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Especialidad;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class GestionarEspecialidadesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    ListView<String> listviewEspecialidades;
    @FXML
    Text textInicio, textUnidades;
    @FXML
    TextField textfieldBuscar;
    @FXML
    Button buttonAgregarEspecialidad;
    
    ConsultarDatos consultar;
    ObservableList<Especialidad> datos;
    ObservableList<String> nombreEspecialidades;
    
    
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
            FXMLLoader unidades = new FXMLLoader(getClass().getResource("Unidades.fxml"));
            Scene sceneUnidades;
            try {
                sceneUnidades = new Scene(unidades.load());
                stageCrearUnidad.setScene(sceneUnidades);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        inicializarListview();
    }    
    
    
    public void inicializarListview(){
        nombreEspecialidades = FXCollections.observableArrayList();
        datos = consultar.obtenerEspecialidades();
        if(datos != null){
            datos.forEach((especialidad) -> {
                nombreEspecialidades.add(especialidad.getNombre());
            });
        }
        
        FilteredList<String> datosFiltrados = new FilteredList<>(nombreEspecialidades, s -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(str -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                return str.toLowerCase().contains(busquedaEnMinusculas);
            });
        });
        
        listviewEspecialidades.setItems(datosFiltrados);
       
    }
    
    public void actualizarListView(){
        nombreEspecialidades.removeAll(nombreEspecialidades);
        datos.removeAll(datos);
        datos = consultar.obtenerEspecialidades();
        if(datos != null){
            datos.forEach((especialidad) -> {
                nombreEspecialidades.add(especialidad.getNombre());
            });
        }
        
        FilteredList<String> datosFiltrados = new FilteredList<>(nombreEspecialidades, s -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(str -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                return str.toLowerCase().contains(busquedaEnMinusculas);
            });
        });
       
        listviewEspecialidades.setItems(datosFiltrados);
    }
    
    public void guardarEspecialidad(ActionEvent e){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("PoliAsietncia");
        dialog.setHeaderText("Agregar Especialidad");
        dialog.setContentText("Escribe el nombre de la nueva especialidad:");
        
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            result.ifPresent(nombreEspecialidad -> {
                if(!nombreEspecialidad.equals("")){
                    if(consultar.agregarEspecialidad(nombreEspecialidad)){
                        Alert alertOk = new Alert(Alert.AlertType.INFORMATION);
                        alertOk.setTitle("PoliAsistencia");
                        alertOk.setHeaderText(null);
                        alertOk.setContentText("Especialidad agregada");
                        alertOk.showAndWait();
                        actualizarListView();
                    }
                    else{
                        Alert alertError = new Alert(Alert.AlertType.ERROR);
                        alertError.setTitle("PoliAsistencia");
                        alertError.setHeaderText("Error al crear especialidad");
                        alertError.setContentText("El nombre de la especialidad que estas agregando es la misma que una de las especialidades existentes.\nVuelve a nombrar de nuevo la especialidad");
                        alertError.showAndWait();
                        actualizarListView();
                    }
                }
                else{
                    Alert alertError = new Alert(Alert.AlertType.WARNING);
                    alertError.setTitle("PoliAsistencia");
                    alertError.setHeaderText("Error al crear especialidad");
                    alertError.setContentText("Escribe un nombre para agregar la especialidad");
                    alertError.showAndWait();
                    actualizarListView();
                }
            });
        }
    }
}
