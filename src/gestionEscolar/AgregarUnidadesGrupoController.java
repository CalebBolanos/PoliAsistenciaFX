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
import poliasistenciafx.Grupo;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class AgregarUnidadesGrupoController implements Initializable {

    @FXML
    private TableView<Grupo> tableviewGrupos;
    @FXML
    TextField textfieldBuscar;
    @FXML
    Button buttonCancelar;
    
    ConsultarDatos consultar;
    ObservableList<Grupo> datos;
    Grupo grupoElegido;
    
    public AgregarUnidadesGrupoController(){
        
    }
    
    public void setGrupoElegido(Grupo grupoElegido){
        this.grupoElegido = grupoElegido;
    }
    
    public Grupo getGrupoElegido(){
        return grupoElegido;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datos =  FXCollections.observableArrayList();
        consultar = new ConsultarDatos();
        inicializarTabla();
    }

    public void inicializarTabla(){
        datos = consultar.obtenerGrupos();
        
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
    public void elegirGrupo(MouseEvent click){
        Grupo grupox = tableviewGrupos.getSelectionModel().getSelectedItem();
        if(grupox != null){
            setGrupoElegido(grupox);
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
