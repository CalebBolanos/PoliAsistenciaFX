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
import jefeAcademia.ProfesoresController;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Persona;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class ElegirAlumnoAsignarUnidadesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView<Persona> tableviewAlumnos;
    @FXML
    Text textInicio, textAlumnos;
    @FXML
    TextField textfieldBuscar;
    
    ConsultarDatos consultar;

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
            irAInicio();
        });

        textAlumnos.setOnMouseEntered((MouseEvent me) -> {
            textAlumnos.setUnderline(true);
            textAlumnos.setFill(Color.BLUE);
        });
        textAlumnos.setOnMouseExited((MouseEvent me) -> {
            textAlumnos.setUnderline(false);
            textAlumnos.setFill(Color.BLACK);
        });
        textAlumnos.setOnMouseClicked((MouseEvent me) -> {
            irAAlumnos();
        });

        inicializarTabla();
    }

    public void irAAlumnos() {
        Stage stageElegirAlumno = (Stage) (textAlumnos.getScene().getWindow());
        FXMLLoader Alumnos = new FXMLLoader(getClass().getResource("Alumnos.fxml"));
        try {
            Scene sceneAlumnos = new Scene(Alumnos.load());
            stageElegirAlumno.setScene(sceneAlumnos);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void irAInicio() {
        Stage stageElegirAlumno = (Stage) (textInicio.getScene().getWindow());
        FXMLLoader inicioGest = new FXMLLoader(getClass().getResource("Inicio.fxml"));
        try {
            Scene sceneInicioGest = new Scene(inicioGest.load());
            stageElegirAlumno.setScene(sceneInicioGest);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void irAGestionarUnidades(Persona persona, int idPersona){
        Stage stageElegirAlumno = (Stage) (textInicio.getScene().getWindow());
        GestionarUnidadesAlumnoController gestionar = new GestionarUnidadesAlumnoController(persona, idPersona);
        FXMLLoader gestionarUnidades = new FXMLLoader(getClass().getResource("GestionarUnidadesAlumno.fxml"));
        gestionarUnidades.setController(gestionar);
        try {
            Scene sceneGestionarUnidades = new Scene(gestionarUnidades.load());
            stageElegirAlumno.setScene(sceneGestionarUnidades);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void inicializarTabla(){
        ObservableList<Persona> datos = consultar.obtenerDatosAlumnos();
        
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
        datosOrdenados.comparatorProperty().bind(tableviewAlumnos.comparatorProperty());
        tableviewAlumnos.setItems(datosOrdenados);
           
    }
    
    @FXML
    public void obtenerDatos(MouseEvent click){
        Persona personax = tableviewAlumnos.getSelectionModel().getSelectedItem();
        if(personax != null){
            String[] datos = consultar.obtenerAlumno(personax.getNumero());
            irAGestionarUnidades(personax, Integer.parseInt(datos[7]));
        }
    }  
    
}
