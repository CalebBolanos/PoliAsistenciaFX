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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import poliasistenciafx.Persona;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class ElegirProfesorEditarDatosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView<Persona> tableviewProfesores;
    @FXML
    Text textInicio, textProfesores;
    @FXML
    TextField textfieldBuscar;

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

        inicializarTabla();
        

    }

    public void irAProfesores() {
        Stage stageElegirProfesor = (Stage) (textProfesores.getScene().getWindow());
        FXMLLoader Profesores = new FXMLLoader(getClass().getResource("Profesores.fxml"));
        try {
            Scene sceneProfesores = new Scene(Profesores.load());
            stageElegirProfesor.setScene(sceneProfesores);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void irAInicio() {
        Stage stageElegirProfesor = (Stage) (textInicio.getScene().getWindow());
        FXMLLoader inicioJefe = new FXMLLoader(getClass().getResource("Inicio.fxml"));
        try {
            Scene sceneInicioJefe = new Scene(inicioJefe.load());
            stageElegirProfesor.setScene(sceneInicioJefe);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void irAEditarDatos(String idProfesor){
        Stage stageElegirProfesor = (Stage) (textInicio.getScene().getWindow());
        EditarDatosProfesoresController editar = new EditarDatosProfesoresController(idProfesor);
        FXMLLoader editarDatos = new FXMLLoader(getClass().getResource("EditarDatosProfesores.fxml"));
        editarDatos.setController(editar);
        try {
            Scene sceneEditarDatos = new Scene(editarDatos.load());
            stageElegirProfesor.setScene(sceneEditarDatos);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void inicializarTabla(){
        
        ObservableList<Persona> datos = tableviewProfesores.getItems();
        for (int i = 1; i < 11; i++) {
            datos.add(new Persona("" + i,
                    "nombre",
                    "paterno",
                    "materno",
                    "genero"
            ));
        }
        
        
        FilteredList<Persona> datosFiltrados = new FilteredList<>(datos, p -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(persona -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (persona.getNumero().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (persona.getNombre().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (persona.getPaterno().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }else if (persona.getMaterno().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }else if (persona.getGenero().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }
                return false;
            });
        });
        
        SortedList<Persona> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableviewProfesores.comparatorProperty());
        tableviewProfesores.setItems(datosOrdenados);
           
    }
    
    @FXML
    public void obtenerDatos(MouseEvent click){
        Persona personax = tableviewProfesores.getSelectionModel().getSelectedItem();
        if(personax != null){
            irAEditarDatos(personax.getNumero());
        }
    }
}
