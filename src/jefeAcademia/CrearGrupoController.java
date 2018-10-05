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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Especialidad;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class CrearGrupoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Text textInicio, textGrupos;
    @FXML
    TextField textfieldNombreGrupo;
    @FXML
    ComboBox<Integer> comboboxSemestre;
    @FXML
    ComboBox<Especialidad> comboboxEspecialidad;
    @FXML
    Button buttonCrearGrupo;
    @FXML
    RadioButton radiobuttonMatutino, radiobuttonVespertino;
    @FXML
    ToggleGroup turno;
    
    ConsultarDatos consultar;
    ObservableList<Integer> semestres;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        semestres = FXCollections.observableArrayList();
        semestres.addAll(1, 2, 3, 4, 5, 6);
        comboboxSemestre.setItems(semestres);
        consultar = new ConsultarDatos();
        ObservableList<Especialidad> especialidades = FXCollections.observableArrayList(consultar.obtenerEspecialidades());
        comboboxEspecialidad.setItems(especialidades);
        comboboxEspecialidad.setConverter(new StringConverter<Especialidad>() {
            @Override
            public String toString(Especialidad especialidadx) {
                return especialidadx.getNombre();
            }
            @Override
            public Especialidad fromString(String string) {
                return comboboxEspecialidad.getItems().stream().filter(especialidad -> especialidad.getNombre().equals(string)).findFirst().orElse(null);
            }
        });
        
        comboboxEspecialidad.valueProperty().addListener((observable, viejoValor, nuevoValor) -> {
            if(nuevoValor != null){
                System.out.println(nuevoValor.getId());
            }
        });
    }

    @FXML
    public void guardarGrupo(ActionEvent e){
        String nombre;
        int semestre, idEspecialidad, turnoInt;
        nombre = textfieldNombreGrupo.getText();
        semestre = comboboxSemestre.getValue();
        Especialidad especialidadx = comboboxEspecialidad.getValue();
        idEspecialidad = especialidadx.getId();
        System.out.println("idEspecialidad: "+idEspecialidad);
        RadioButton selectedRadioButton = (RadioButton) turno.getSelectedToggle();
        String turnoString = selectedRadioButton.getText();
        if(turnoString.equals("Matutino")){
            turnoInt = 1;
        }
        else{
            turnoInt = 2;
        }
        if(consultar.crearNuevoGrupo(idEspecialidad, nombre, semestre, turnoInt)){
            System.out.println("ok");
        }
        //faltan validaciones
    }
    
}
