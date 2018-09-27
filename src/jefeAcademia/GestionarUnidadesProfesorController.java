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
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import poliasistenciafx.Unidad;

/**
 * FXML Controller class
 *
 * @author caleb
 */
public class GestionarUnidadesProfesorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TableView<Unidad> tableViewUnidades;
    @FXML
    Text textInicio, textProfesores, textElegirProfesor;
    @FXML
    Button buttonAgregarUnidades;
    @FXML
    TextField textfieldBuscar;
    
    String idProfesor = "";
    
    public GestionarUnidadesProfesorController(String idProfesor){
        this.idProfesor = idProfesor;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(idProfesor);
        textInicio.setOnMouseEntered((MouseEvent me) -> {
            textInicio.setUnderline(true);
            textInicio.setFill(Color.BLUE);
        });
        textInicio.setOnMouseExited((MouseEvent me) -> {
            textInicio.setUnderline(false);
            textInicio.setFill(Color.BLACK);
        });
        textInicio.setOnMouseClicked((MouseEvent me) -> {
            irAInicio();
        });

        textProfesores.setOnMouseEntered((MouseEvent me) -> {
            textProfesores.setUnderline(true);
            textProfesores.setFill(Color.BLUE);
        });
        textProfesores.setOnMouseExited((MouseEvent me) -> {
            textProfesores.setUnderline(false);
            textProfesores.setFill(Color.BLACK);
        });
        textProfesores.setOnMouseClicked((MouseEvent me) -> {
            irAProfesores();
        });
        
        textElegirProfesor.setOnMouseEntered((MouseEvent me) -> {
            textElegirProfesor.setUnderline(true);
            textElegirProfesor.setFill(Color.BLUE);
        });
        textElegirProfesor.setOnMouseExited((MouseEvent me) -> {
            textElegirProfesor.setUnderline(false);
            textElegirProfesor.setFill(Color.BLACK);
        });
        textElegirProfesor.setOnMouseClicked((MouseEvent me) -> {
            irAElegirProfesores();
        });
        
        inicializarTabla();
    }
    
    public void inicializarTabla(){
        
        ObservableList<Unidad> datos = tableViewUnidades.getItems();
        for (int i = 1; i < 11; i++) {
            datos.add(new Unidad("" + i,
                    "lunes",
                    "martes",
                    "miercoles",
                    "jueves",
                    "viernes"
            ));
        }
        
        FilteredList<Unidad> datosFiltrados = new FilteredList<>(datos, p -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(unidad -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (unidad.getUnidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getLunes().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getMartes().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }else if (unidad.getMiercoles().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }else if (unidad.getJueves().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }else if (unidad.getJueves().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }else if (unidad.getViernes().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }
                return false;
            });
        });
        
        SortedList<Unidad> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableViewUnidades.comparatorProperty());
        tableViewUnidades.setItems(datosOrdenados);
    }
    
    @FXML
    public void obtenerDatos(MouseEvent click){
        Unidad unidadx = tableViewUnidades.getSelectionModel().getSelectedItem();
    }
    
    public void irAProfesores() {
        Stage stageEditarProfesor = (Stage) (textProfesores.getScene().getWindow());
        FXMLLoader Profesores = new FXMLLoader(getClass().getResource("Profesores.fxml"));
        try {
            Scene sceneProfesores = new Scene(Profesores.load());
            stageEditarProfesor.setScene(sceneProfesores);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void irAInicio() {
        Stage stageGestionarUnidades = (Stage) (textInicio.getScene().getWindow());
        FXMLLoader inicioJefe = new FXMLLoader(getClass().getResource("Inicio.fxml"));
        try {
            Scene sceneInicioJefe = new Scene(inicioJefe.load());
            stageGestionarUnidades.setScene(sceneInicioJefe);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void irAElegirProfesores() {
        Stage stageGestionarUnidades = (Stage) (textInicio.getScene().getWindow());
        FXMLLoader elegirProfesor = new FXMLLoader(getClass().getResource("ElegirProfesorUnidades.fxml"));
        try {
            Scene sceneElegirProfesor = new Scene(elegirProfesor.load());
            stageGestionarUnidades.setScene(sceneElegirProfesor);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
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
