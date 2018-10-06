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
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Grupo;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class ElegirGrupoEditarDatosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TableView<Grupo> tableviewGrupos;
    @FXML
    Text textInicio, textGrupos;
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
            irA(1);
        });
        
        textGrupos.setOnMouseEntered((MouseEvent me) -> {
            textGrupos.setUnderline(true);
            textGrupos.setFill(Color.BLUE);
        });
        textGrupos.setOnMouseExited((MouseEvent me) -> {
            textGrupos.setUnderline(false);
            textGrupos.setFill(Color.BLACK);
        });
        textGrupos.setOnMouseClicked((MouseEvent me) -> {
            irA(2);
        });
        
        inicializarTabla();
    }

    public void inicializarTabla(){
        ObservableList<Grupo> datos = consultar.obtenerGrupos();
        
        FilteredList<Grupo> datosFiltrados = new FilteredList<>(datos, g -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(grupo -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (grupo.getGrupo().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (Integer.toString(grupo.getSemestre()).toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (grupo.getTurno().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }else if (grupo.getEspecialidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }
                return false;
            });
        });
        
        SortedList<Grupo> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableviewGrupos.comparatorProperty());
        tableviewGrupos.setItems(datosOrdenados);   
    }
    
    @FXML
    public void obtenerDatos(MouseEvent e){
        Grupo grupox = tableviewGrupos.getSelectionModel().getSelectedItem();
        if(grupox != null){
            irAProfesoresGrupo(grupox);
        }
    }


    public void irA(int pantalla){
        String recurso = "";
        Stage stageElegirProfesorGrupo = (Stage) (textInicio.getScene().getWindow());
        if(pantalla == 1){
            recurso = "Inicio.fxml";
        }
        else{
            recurso = "Grupos.fxml";
        }
        FXMLLoader pantallax = new FXMLLoader(getClass().getResource(recurso));
        try {
            Scene scenePantallax = new Scene(pantallax.load());
            stageElegirProfesorGrupo.setScene(scenePantallax);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void irAProfesoresGrupo(Grupo grupo){
        Stage stageElegirProfesor = (Stage) (textInicio.getScene().getWindow());
        EditarDatosGrupoController editarController = new EditarDatosGrupoController(grupo);
        FXMLLoader editarGrupo = new FXMLLoader(getClass().getResource("EditarDatosGrupo.fxml"));
        editarGrupo.setController(editarController);
        try {
            Scene sceneEditarDatos = new Scene(editarGrupo.load());
            stageElegirProfesor.setScene(sceneEditarDatos);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
