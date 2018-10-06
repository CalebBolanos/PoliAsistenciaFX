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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Especialidad;
import poliasistenciafx.Grupo;
import poliasistenciafx.validaciones;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class EditarDatosGrupoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Text textInicio, textGrupos, textElegirGrupo;
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
    Grupo grupo;
    String antiguoNombre, especialidad;
    int semestre, idEspecilidad, idTurno;
    
    public EditarDatosGrupoController(Grupo grupo){
        this.grupo = grupo;
        antiguoNombre = grupo.getGrupo();
        semestre = grupo.getSemestre();
        especialidad = grupo.getEspecialidad();
        idEspecilidad = grupo.getIdEspecialidad();
        idTurno = grupo.getIdTurno();
    }
    
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
        
        textElegirGrupo.setOnMouseEntered((MouseEvent me) -> {
            textElegirGrupo.setUnderline(true);
            textElegirGrupo.setFill(Color.BLUE);
        });
        textElegirGrupo.setOnMouseExited((MouseEvent me) -> {
            textElegirGrupo.setUnderline(false);
            textElegirGrupo.setFill(Color.BLACK);
        });
        textElegirGrupo.setOnMouseClicked((MouseEvent me) -> {
            irA(3);
        });
        
        
        
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
        
        textfieldNombreGrupo.setText(antiguoNombre);
        comboboxSemestre.setValue(semestre);
        comboboxEspecialidad.getSelectionModel().select(new Especialidad(especialidad, idEspecilidad));
        if(idTurno == 1){
            radiobuttonMatutino.setSelected(true);
        }
        else{
            radiobuttonVespertino.setScaleShape(true);
        }
        comboboxSemestre.setDisable(true);
        comboboxEspecialidad.setDisable(true);
        radiobuttonMatutino.setDisable(true);
        radiobuttonVespertino.setDisable(true);
    }    
    
    @FXML
    public void guardarCambios(ActionEvent e){
        validaciones validar = new validaciones();
        String nombre;
        int semestre, idEspecialidad, turnoInt;
        nombre = textfieldNombreGrupo.getText();
        semestre = comboboxSemestre.getValue() == null ? 0 : comboboxSemestre.getValue();
        Especialidad especialidadx = comboboxEspecialidad.getValue();
        if(especialidadx == null) especialidadx = new Especialidad("", 0);
        idEspecialidad = especialidadx.getId();
        System.out.println("idEspecialidad: "+idEspecialidad);
        RadioButton selectedRadioButton = (RadioButton) turno.getSelectedToggle();
        if(selectedRadioButton != null){
            String turnoString = selectedRadioButton.getText() == null ? "" : selectedRadioButton.getText();
            if(turnoString.equals("Matutino")){
            turnoInt = 1;
            }
            else{
                turnoInt = 2;
            }
            if(turnoString.equals(""))
                turnoInt = 0;
        }
        else{
            turnoInt = 0;
        }
        String msj = validar.evaluarCrearGrupo(nombre, semestre, idEspecialidad, turnoInt);
        if(msj.equals("ok")){
            if(consultar.editarGrupo(antiguoNombre, nombre, semestre, idEspecialidad, idTurno)){
                try {
                    crearDialogo("PoliAsistencia", "Datos guardados", "Nombre de grupo: "+nombre, Alert.AlertType.INFORMATION);
                    Stage stageEditarProfesor = (Stage) (textInicio.getScene().getWindow());
                    FXMLLoader grupos = new FXMLLoader(getClass().getResource("Grupos.fxml"));
                    Scene sceneGrupos = new Scene(grupos.load());
                    stageEditarProfesor.setScene(sceneGrupos);
                } catch (IOException ex) {
                    Logger.getLogger(CrearGrupoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                crearDialogo("PoliAsistencia", "No se puede editar el grupo", "El nombre que le asignaste a este grupo ya es ocupado por otro grupo existente", Alert.AlertType.INFORMATION);
            }
        }
        else{
            crearDialogo("PoliAsistencia", msj, null, Alert.AlertType.WARNING);
        }
    }
    
    public void irA(int pantalla){
        String recurso = "";
        Stage stageElegirProfesorGrupo = (Stage) (textInicio.getScene().getWindow());
        switch(pantalla){
            case 1:
                recurso = "Inicio.fxml";
                break;
            case 2:
                recurso = "Grupos.fxml";
                break;
            case 3:
                recurso = "ElegirGrupoEditarDatos.fxml";
                break;
        }
        FXMLLoader pantallax = new FXMLLoader(getClass().getResource(recurso));
        try {
            Scene scenePantallax = new Scene(pantallax.load());
            stageElegirProfesorGrupo.setScene(scenePantallax);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void crearDialogo(String titulo, String header, String contexto, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contexto);
        alert.showAndWait();
    }
    
}
