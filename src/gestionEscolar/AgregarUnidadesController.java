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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.UnidadGrupo;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class AgregarUnidadesController implements Initializable {

    @FXML
    TableView<UnidadGrupo> tableviewUnidades;
    @FXML
    TextField textfieldBuscar;
    @FXML
    Button buttonCancelar;
    
    ConsultarDatos consultar;
    ObservableList<UnidadGrupo> datos;
    UnidadGrupo unidadElegida;
    
    public AgregarUnidadesController(){
        
    }
    
    public void setUnidadElegida(UnidadGrupo unidadElegida){
        this.unidadElegida = unidadElegida;
    }
    
    public UnidadGrupo getUnidadElegida(){
        return unidadElegida;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datos =  FXCollections.observableArrayList();
        consultar = new ConsultarDatos();
        inicializarTabla();
    }

    public void inicializarTabla(){
        datos = consultar.obtenerUnidadesAlumnoDisponibles();
        
        FilteredList<UnidadGrupo> datosFiltrados = new FilteredList<>(datos, p -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(unidad -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (unidad.getGrupo().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getUnidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getProfesor().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else  if (unidad.getLunes().toLowerCase().contains(busquedaEnMinusculas)) {
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
                }else if (Integer.toString(unidad.getCupo()).contains(busquedaEnMinusculas)) {
                    return true;
                } else if (Integer.toString(unidad.getDisponibilidad()).contains(busquedaEnMinusculas)) {
                    return true;
                }
                return false;
            });
        });
        
        SortedList<UnidadGrupo> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableviewUnidades.comparatorProperty());
        tableviewUnidades.setItems(datosOrdenados);
    }
    
    @FXML
    public void elegirUnidad(MouseEvent click){
        UnidadGrupo unidadx = tableviewUnidades.getSelectionModel().getSelectedItem();
        if(unidadx != null){
            setUnidadElegida(unidadx);
            Stage stageActual = (Stage) buttonCancelar.getScene().getWindow();
            stageActual.close();
        }
    }
    
    @FXML
    public void cerrarVentana(ActionEvent e) {
        Stage stageActual = (Stage) buttonCancelar.getScene().getWindow();
        stageActual.close();
    }
    
}
