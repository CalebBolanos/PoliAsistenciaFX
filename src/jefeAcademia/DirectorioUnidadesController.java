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
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Materia;
import poliasistenciafx.Unidad;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class DirectorioUnidadesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView<Materia> tableviewUnidades;
    @FXML
    Text textInicio, textUnidades;
    @FXML
    Button buttonCrearUnidad, buttonBorrarUnidad;
    @FXML
    TextField textfieldBuscar;
    
    ConsultarDatos consultar;
    ObservableList<Materia> datos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultar = new ConsultarDatos();
        buttonBorrarUnidad.setDisable(true);
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
        
        inicializarTabla();
    }    
    
    public void inicializarTabla(){
        
        datos = consultar.obtenerDirectorioUnidades();
        
        FilteredList<Materia> datosFiltrados = new FilteredList<>(datos, u -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(materia -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (materia.getNombreUnidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (Integer.toString(materia.getIdSemestre()).contains(busquedaEnMinusculas)) {
                    return true;
                } else if (materia.getArea().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }
                return false;
            });
        });
        
        SortedList<Materia> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableviewUnidades.comparatorProperty());
        tableviewUnidades.setItems(datosOrdenados);
        
        tableviewUnidades.getSelectionModel().selectedItemProperty().addListener((observable, viejaSeleccion, nuevaSeleccion) -> {
                buttonBorrarUnidad.setDisable(nuevaSeleccion == null);
        });
    }
    
    public void actualizarTabla(){
        datos.removeAll(datos);
        datos = consultar.obtenerDirectorioUnidades();
        
        textfieldBuscar.setText("");
        FilteredList<Materia> datosFiltrados = new FilteredList<>(datos, u -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(materia -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (materia.getNombreUnidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (Integer.toString(materia.getIdSemestre()).contains(busquedaEnMinusculas)) {
                    return true;
                } else if (materia.getArea().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }
                return false;
            });
        });
        
        SortedList<Materia> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableviewUnidades.comparatorProperty());
        tableviewUnidades.setItems(datosOrdenados);
    }
    
    @FXML
    public void ejecutarAccion(ActionEvent e){
        if(e.getSource().equals(buttonCrearUnidad)){
            Stage stageDirectorio = (Stage) ((Node)e.getSource()).getScene().getWindow();
            FXMLLoader crearUnidades = new FXMLLoader(getClass().getResource("CrearUnidad.fxml"));
            Scene sceneCrearUnidades;
            try {
                sceneCrearUnidades = new Scene(crearUnidades.load());
                stageDirectorio.setScene(sceneCrearUnidades);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(e.getSource().equals(buttonBorrarUnidad)){
            Materia materiax = tableviewUnidades.getSelectionModel().getSelectedItem();
            if(materiax != null){
                borrarUnidad(materiax.getNombreUnidad());
            }
        }
    }
    
    public void borrarUnidad(String nombreUnidad){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar acción");
        alert.setHeaderText("¿Estas seguro de que quieres borrar esta unidad de aprendizaje? ("+nombreUnidad+")");
        alert.setContentText("Si borras esta unidad se borraran TODAS las unidades que estan inscritas a un grupo y a un profesor");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            if(consultar.borrarUnidadAprendizaje(nombreUnidad)){
                Alert alertOk = new Alert(Alert.AlertType.INFORMATION);
                alertOk.setTitle("PoliAsistencia");
                alertOk.setHeaderText(null);
                alertOk.setContentText("Unidad de Aprendizaje borrada");
                alertOk.showAndWait();
                actualizarTabla();
            }
            else{
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("PoliAsistencia");
                alertError.setHeaderText("Error al borrar la unidad de aprendizaje");
                alertError.setContentText("La unidad de aprendizaje no existe");
                alert.showAndWait();
                actualizarTabla();
            }
        } else {

        }
    }
    
}
