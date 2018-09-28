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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class EditarDatosProfesoresController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Text textInicio, textProfesores, textElegirProfesor;
    @FXML
    Button buttonHuella, buttonDatos;
    @FXML
    Pane paneDatosPersonales, paneHuellaDigital;
    @FXML
    TextField textfieldNombre, textfieldPaterno, textfieldMaterno, textfieldNumero;
    @FXML
    ComboBox comboboxGenero;
    @FXML
    DatePicker datePickerNacimiento;
    
    String nombre, paterno, materno, numero, fechaNacimiento, genero;
    
    public EditarDatosProfesoresController(String[] datos){
        nombre = datos[1];
        paterno = datos[2];
        materno = datos[3];
        genero = datos[4];
        numero = datos[5];
        fechaNacimiento = datos[6];
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        comboboxGenero.getItems().addAll(
                "Masculino",
                "Femenino",
                "Indefinido"
        );

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
        
        textElegirProfesor.setOnMouseEntered((MouseEvent me) -> {
            textElegirProfesor.setUnderline(true);
            textElegirProfesor.setFill(Color.BLUE);
        });
        textElegirProfesor.setOnMouseExited((MouseEvent me) -> {
            textElegirProfesor.setUnderline(false);
            textElegirProfesor.setFill(Color.BLACK);
        });
        textElegirProfesor.setOnMouseClicked((MouseEvent me) -> {
            irAElegirProfesores();
        });
        
        textfieldNombre.setText(nombre);
        textfieldPaterno.setText(paterno);
        textfieldMaterno.setText(materno);
        textfieldNumero.setText(numero);
        textfieldNumero.setEditable(false);
        comboboxGenero.setValue(genero);
        comboboxGenero.setEditable(false);
        System.out.println(fechaNacimiento);
        
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formato = formato.withLocale(Locale.getDefault());
        LocalDate nacimiento = LocalDate.parse(fechaNacimiento, formato);
        datePickerNacimiento.setValue(nacimiento);
    }
    
    @FXML
    public void cambiarPane(ActionEvent e){
        if(e.getSource().equals(buttonHuella)){
            paneDatosPersonales.setVisible(false);
            paneHuellaDigital.setVisible(true);
        }else if(e.getSource().equals(buttonDatos)){
            paneHuellaDigital.setVisible(false);
            paneDatosPersonales.setVisible(true);
        }
    }
    
    public void irAProfesores() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar accion");
        alert.setHeaderText("Estas seguro de que quieres cancelar el registro?");
        alert.setContentText("Se perderan los datos ingresados");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageEditarProfesor = (Stage) (textProfesores.getScene().getWindow());
            FXMLLoader Profesores = new FXMLLoader(getClass().getResource("Profesores.fxml"));
            try {
                Scene sceneProfesores = new Scene(Profesores.load());
                stageEditarProfesor.setScene(sceneProfesores);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

        }
    }

    public void irAInicio() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar accion");
        alert.setHeaderText("Estas seguro de que quieres cancelar el registro?");
        alert.setContentText("Se perderan los datos ingresados");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageEditarProfesor = (Stage) (textInicio.getScene().getWindow());
            FXMLLoader inicioJefe = new FXMLLoader(getClass().getResource("Inicio.fxml"));
            try {
                Scene sceneInicioJefe = new Scene(inicioJefe.load());
                stageEditarProfesor.setScene(sceneInicioJefe);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

        }
    }
    
    public void irAElegirProfesores() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar accion");
        alert.setHeaderText("Estas seguro de que quieres cancelar el registro?");
        alert.setContentText("Se perderan los datos ingresados");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageEditarProfesor = (Stage) (textInicio.getScene().getWindow());
            FXMLLoader elegirProfesor = new FXMLLoader(getClass().getResource("ElegirProfesorEditarDatos.fxml"));
            try {
                Scene sceneElegirProfesor = new Scene(elegirProfesor.load());
                stageEditarProfesor.setScene(sceneElegirProfesor);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

        }
    }
    
    

    public void crearDialogo(String titulo, String header, String contexto) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contexto);
        alert.showAndWait();
    }
    
}
