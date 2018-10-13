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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Hora;
import poliasistenciafx.Materia;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class AgregarHorarioUnidadController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Pane paneHorario;
    @FXML
    Text textInicio, textUnidades, textElegirUnidad, textNombreUnidad, textSemestre, textEspecialidad;
    @FXML
    ComboBox<Hora> comboboxInicioLunes, comboboxFinLunes, 
            comboboxInicioMartes, comboboxFinMartes,
            comboboxInicioMiercoles, comboboxFinMiercoles,
            comboboxInicioJueves, comboboxFinJueves,
            comboboxInicioViernes, comboboxFinViernes;
    @FXML
    Button buttonCrearHorario, buttonCancelar;
    @FXML
    TextField textFieldCupo;
    @FXML
    CheckBox checkboxLunes, checkboxMartes, checkboxMiercoles, checkboxJueves, checkboxViernes;
    
    Materia materia;
    ArrayList<ComboBox<Hora>> comoboboxDias;
    ArrayList<ComboBox<Hora>> comboboxInicio;
    ArrayList<ComboBox<Hora>> comboboxFinal;
    ArrayList<CheckBox> checkboxes;
    ConsultarDatos consultar;
    ObservableList<Integer> semestres;
    int idUnidadHorario = 0; 
    
    public AgregarHorarioUnidadController(Materia materia){
        this.materia = materia;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultar = new ConsultarDatos();
        comoboboxDias = new ArrayList<>();
        comboboxInicio = new ArrayList<>();
        comboboxFinal = new ArrayList<>();
        checkboxes = new ArrayList<>();
        
        textInicio.setOnMouseEntered((MouseEvent me) -> {
            textInicio.setUnderline(true);
            textInicio.setFill(Color.BLUE);
        });
        textInicio.setOnMouseExited((MouseEvent me) -> {
            textInicio.setUnderline(false);
            textInicio.setFill(Color.BLACK);
        });
        textInicio.setOnMouseClicked((MouseEvent me) -> {
            Stage stageAgregarHorario = (Stage) (textInicio.getScene().getWindow());
            FXMLLoader inicioJefe = new FXMLLoader(getClass().getResource("Inicio.fxml"));
            Scene sceneInicioJefe;
            try {
                sceneInicioJefe = new Scene(inicioJefe.load());
                stageAgregarHorario.setScene(sceneInicioJefe);
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
            Stage stageAgregarHorario = (Stage) (textUnidades.getScene().getWindow());
            FXMLLoader unidades = new FXMLLoader(getClass().getResource("Unidades.fxml"));
            Scene sceneUnidades;
            try {
                sceneUnidades = new Scene(unidades.load());
                stageAgregarHorario.setScene(sceneUnidades);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        textElegirUnidad.setOnMouseEntered((MouseEvent me) -> {
            textElegirUnidad.setUnderline(true);
            textElegirUnidad.setFill(Color.BLUE);
        });
        textElegirUnidad.setOnMouseExited((MouseEvent me) -> {
            textElegirUnidad.setUnderline(false);
            textElegirUnidad.setFill(Color.BLACK);
        });
        textElegirUnidad.setOnMouseClicked((MouseEvent me) -> {
            Stage stageAgregarHorario = (Stage) (textElegirUnidad.getScene().getWindow());
            FXMLLoader elegirUnidad = new FXMLLoader(getClass().getResource("ElegirUnidadAgregarHorario.fxml"));
            Scene sceneElegirUnidad;
            try {
                sceneElegirUnidad = new Scene(elegirUnidad.load());
                stageAgregarHorario.setScene(sceneElegirUnidad);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        textNombreUnidad.setText(textNombreUnidad.getText() + materia.getNombreUnidad());
        textSemestre.setText(textSemestre.getText() + materia.getIdSemestre());
        textEspecialidad.setText(textEspecialidad.getText() + materia.getArea());
        
        textFieldCupo.textProperty().addListener((ObservableValue<? extends String> observable, String viejoValor, String nuevoValor) -> {
            if (!nuevoValor.matches("\\d*")) {
                textFieldCupo.setText(nuevoValor.replaceAll("[^\\d]", ""));
            }
        });
        
        ObservableList<Hora> horas = crearHoras();
        
        comoboboxDias.add(comboboxInicioLunes);
        comoboboxDias.add(comboboxFinLunes);
        comoboboxDias.add(comboboxInicioMartes);
        comoboboxDias.add(comboboxFinMartes);
        comoboboxDias.add(comboboxInicioMiercoles);
        comoboboxDias.add(comboboxFinMiercoles);
        comoboboxDias.add(comboboxInicioJueves);
        comoboboxDias.add(comboboxFinJueves);
        comoboboxDias.add(comboboxInicioViernes);
        comoboboxDias.add(comboboxFinViernes);
        
        for(ComboBox<Hora> combobox : comoboboxDias){
            combobox.setDisable(true);
            combobox.setItems(horas);
            combobox.setConverter(new StringConverter<Hora>() {
            @Override
            public String toString(Hora horax) {
                return horax.getHora();
            }
            @Override
            public Hora fromString(String string) {
                return combobox.getItems().stream().filter(hora -> hora.getHora().equals(string)).findFirst().orElse(null);
            }
        });
        }

        checkboxLunes.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean viejoValor, Boolean nuevoValor) -> {
            comboboxInicioLunes.setDisable(!nuevoValor);
            comboboxFinLunes.setDisable(!nuevoValor);
        });
        checkboxMartes.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean viejoValor, Boolean nuevoValor) -> {
            comboboxInicioMartes.setDisable(!nuevoValor);
            comboboxFinMartes.setDisable(!nuevoValor);
        });
        checkboxMiercoles.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean viejoValor, Boolean nuevoValor) -> {
            comboboxInicioMiercoles.setDisable(!nuevoValor);
            comboboxFinMiercoles.setDisable(!nuevoValor);
        });
        checkboxJueves.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean viejoValor, Boolean nuevoValor) -> {
            comboboxInicioJueves.setDisable(!nuevoValor);
            comboboxFinJueves.setDisable(!nuevoValor);
        });
        checkboxViernes.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean viejoValor, Boolean nuevoValor) -> {
            comboboxInicioViernes.setDisable(!nuevoValor);
            comboboxFinViernes.setDisable(!nuevoValor);
        });
        
        checkboxes.add(0, checkboxLunes);
        checkboxes.add(1, checkboxMartes);
        checkboxes.add(2, checkboxMiercoles);
        checkboxes.add(3, checkboxJueves);
        checkboxes.add(4, checkboxViernes);
        
        comboboxInicio.add(0, comboboxInicioLunes);
        comboboxFinal.add(0, comboboxFinLunes);
        comboboxInicio.add(1, comboboxInicioMartes);
        comboboxFinal.add(1, comboboxFinMartes);
        comboboxInicio.add(2, comboboxInicioMiercoles);
        comboboxFinal.add(2, comboboxFinMiercoles);
        comboboxInicio.add(3, comboboxInicioJueves);
        comboboxFinal.add(3, comboboxFinJueves);
        comboboxInicio.add(4, comboboxInicioViernes);
        comboboxFinal.add(4, comboboxFinViernes);
    }

    @FXML
    public void ejecutarAccion(ActionEvent e){
        if(e.getSource().equals(buttonCrearHorario)){
            if(validarHorarioUnidad()){
                String stringCupo = textFieldCupo.getText() == null || textFieldCupo.getText().equals("") ? "0" : textFieldCupo.getText();
                int cupo = Integer.parseInt(stringCupo);
                idUnidadHorario = consultar.crearUnidadHorario(materia.getNombreUnidad(), cupo);
                if(idUnidadHorario == -1){
                    crearDialogo("PoliAsistencia", "No se pude crear el horario", null, Alert.AlertType.WARNING);
                    return;
                }
                int inicio = 0, fin = 0;
                for (int i = 0; i < checkboxes.size(); i++) {
                    inicio = 0;
                    fin = 0;
                    if(checkboxes.get(i).isSelected()){
                        inicio = comboboxInicio.get(i).getValue().getId() == 0 ? -1 : comboboxInicio.get(i).getValue().getId();
                        fin = comboboxFinal.get(i).getValue().getId() == 0 ? -1 : comboboxFinal.get(i).getValue().getId();
                        if(!consultar.guardarUnidadHorario(idUnidadHorario, inicio, fin, i+1)){
                            crearDialogo("PoliAsistencia", "No se pude crear el horario", null, Alert.AlertType.WARNING);
                            return;
                        }
                    }
                }
                crearDialogo("PoliAsistencia", "Horario creado satisfactoriamente", null, Alert.AlertType.WARNING);
            }
        }
        
        if(e.getSource().equals(buttonCancelar)){
            
        }
    }

    public boolean validarHorarioUnidad(){
        String stringCupo = textFieldCupo.getText() == null || textFieldCupo.getText().equals("") ? "0" : textFieldCupo.getText();
            int cupo;
            try {
                cupo = Integer.parseInt(stringCupo);
            } catch (NumberFormatException ex) {
                cupo = -1;
            }
            if(cupo == 0){
                crearDialogo("PoliAsistencia", "Escribe un numero en el cupo de la Unidad", null, Alert.AlertType.WARNING);
                return false;
            }
            if(cupo == -1){
                crearDialogo("PoliAsistencia", "Escribe un numero valido en el cupo de la Unidad", null, Alert.AlertType.WARNING);
                return false;
            }
            if(cupo > 300){
                crearDialogo("PoliAsistencia", "Escribe un numero positivo no excedente de 300 en el cupo de la Unidad", null, Alert.AlertType.WARNING);
                return false;
            }
            
            int inicio = 0, fin = 0;
            for (int i = 0; i < checkboxes.size(); i++) {
                inicio = 0;
                fin = 0;
                if(checkboxes.get(i).isSelected()){
                    if(comboboxInicio.get(i).getValue() == null || comboboxFinal.get(i).getValue() == null){
                        crearDialogo("PoliAsistencia", "Asegurate de asignar la hora de inicio y fin de cada dia", null, Alert.AlertType.WARNING);
                        return false;
                    }
                    inicio = comboboxInicio.get(i).getValue().getId() == 0 ? -1 : comboboxInicio.get(i).getValue().getId();
                    fin = comboboxFinal.get(i).getValue().getId() == 0 ? -1 : comboboxFinal.get(i).getValue().getId();
                    if(inicio == -1 || fin == -1){
                        crearDialogo("PoliAsistencia", "Asegurate de asignar la hora de inicio y fin de cada dia", null, Alert.AlertType.WARNING);
                        return false;
                    }
                    if(inicio>=fin){
                        crearDialogo("PoliAsistencia", "La hora de inicio no debe de ser mayor o igual a la hora final", null, Alert.AlertType.WARNING);
                        return false;
                    }
                }
            }
            boolean alMenosUnoElegido = false;
            for(CheckBox checkbox : checkboxes){
               alMenosUnoElegido = checkbox.isSelected();
               if(alMenosUnoElegido){
                   return true;
               }
            }
            crearDialogo("PoliAsistencia", "Elige al menos un dia de la semana para asignar horario a la unidad", null, Alert.AlertType.WARNING);
            return false;
    }
    
    public ObservableList<Hora> crearHoras(){
        ObservableList<Hora> horas = FXCollections.observableArrayList();
        Hora horax;
        for (int i = 1; i <= 15; i++) {
            horax = new Hora();
            horax.setId(i);
            horax.setHora(consultar.evaluarHora(i));
            horas.add(horax);
            horax = null;
        }
        return horas;
    }
    
    public void crearDialogo(String titulo, String header, String contexto, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contexto);
        alert.showAndWait();
    }
    
}
