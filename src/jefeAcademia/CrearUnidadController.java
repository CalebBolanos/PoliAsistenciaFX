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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Especialidad;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class CrearUnidadController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Pane paneDatos;
    @FXML
    Text textInicio, textUnidades, textDirectorio;
    @FXML
    TextField textfieldNombre;
    @FXML
    ComboBox<Integer> comboboxSemestre;
    @FXML
    ComboBox<Especialidad> comboboxEspecialidad;
    @FXML
    Button buttonGuardarUnidad, buttonCancelar;
    
    ConsultarDatos consultar;
    ObservableList<Integer> semestres;
    
    
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
        
        textDirectorio.setOnMouseEntered((MouseEvent me) -> {
            textDirectorio.setUnderline(true);
            textDirectorio.setFill(Color.BLUE);
        });
        textDirectorio.setOnMouseExited((MouseEvent me) -> {
            textDirectorio.setUnderline(false);
            textDirectorio.setFill(Color.BLACK);
        });
        textDirectorio.setOnMouseClicked((MouseEvent me) -> {
            Stage stageCrearUnidad = (Stage) (textDirectorio.getScene().getWindow());
            FXMLLoader directorioUnidades = new FXMLLoader(getClass().getResource("DirectorioUnidades.fxml"));
            Scene sceneDirectorioUnidades;
            try {
                sceneDirectorioUnidades = new Scene(directorioUnidades.load());
                stageCrearUnidad.setScene(sceneDirectorioUnidades);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        semestres = FXCollections.observableArrayList();
        semestres.addAll(1, 2, 3, 4, 5, 6);
        comboboxSemestre.setItems(semestres);
        
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
    public void ejecutarAccion(ActionEvent e){
        if(e.getSource().equals(buttonGuardarUnidad)){
            String nombreUnidad = textfieldNombre.getText() == null ? "" : textfieldNombre.getText();
            Especialidad especialidadx = comboboxEspecialidad.getValue() == null ? new Especialidad("", 0) : comboboxEspecialidad.getValue();
            int idEspecialidad = especialidadx.getId();
            int idSemestre = comboboxSemestre.getValue() == null ? 0 : comboboxSemestre.getValue();
            if(nombreUnidad.equals("")){
                crearDialogo("PoliAsistencia", "Escribe el nombre de la Unidad de Aprendizaje", null, Alert.AlertType.WARNING);
                return;
            }
            if(idSemestre == 0){
                crearDialogo("PoliAsistencia", "Elige un Semestre", null, Alert.AlertType.WARNING);
                return;
            }
            if(idEspecialidad == 0){
                crearDialogo("PoliAsistencia", "Elige una Especialidad", null, Alert.AlertType.WARNING);
                return;
            }
            
            CrearUnidad(idEspecialidad, nombreUnidad, idSemestre);
            
        }
        if(e.getSource().equals(buttonCancelar)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar accion");
            alert.setHeaderText("Estas seguro de que quieres cancelar el registro?");
            alert.setContentText("Se perderan los datos ingresados");
            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.get() == ButtonType.OK) {
                Stage stageCrearUnidad = (Stage) (textDirectorio.getScene().getWindow());
                FXMLLoader directorioUnidades = new FXMLLoader(getClass().getResource("DirectorioUnidades.fxml"));
                Scene sceneDirectorioUnidades;
                try {
                    sceneDirectorioUnidades = new Scene(directorioUnidades.load());
                    stageCrearUnidad.setScene(sceneDirectorioUnidades);
                } catch (IOException ex) {
                    Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
             
        }
    }
    
    public void CrearUnidad(int idEspecialidad, String nombreUnidad, int idSemestre){
        boolean creado = consultar.CrearUnidad(idEspecialidad, nombreUnidad, idSemestre);
        if(creado){
            crearDialogo("PoliAsistencia", "Unidad de Aprendizaje Creada", "Nombre de la Unidad: "+nombreUnidad, Alert.AlertType.INFORMATION);
            Stage stageCrearUnidad = (Stage) (textDirectorio.getScene().getWindow());
            FXMLLoader directorioUnidades = new FXMLLoader(getClass().getResource("DirectorioUnidades.fxml"));
            Scene sceneDirectorioUnidades;
            try {
                sceneDirectorioUnidades = new Scene(directorioUnidades.load());
                stageCrearUnidad.setScene(sceneDirectorioUnidades);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            crearDialogo("PoliAsistencia", "No se pudo crear la Unidad de Aprendizaje", "Ya existe una unidad con el mismo nombre", Alert.AlertType.ERROR);
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
