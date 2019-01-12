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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Huella;
import poliasistenciafx.HuellaDigitalController;
import poliasistenciafx.validaciones;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class EditarDatosCuentaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Text textAjustes;
    @FXML
    Button buttonHuella, buttonDatos, buttonAgregarHuella, buttonBorrarHuella, buttonGuardar;
    @FXML
    Pane paneDatosPersonales, paneHuellaDigital;
    @FXML
    TextField textfieldNombre, textfieldPaterno, textfieldMaterno, textfieldNumero;
    @FXML
    ComboBox comboboxGenero;
    @FXML
    DatePicker datePickerNacimiento;
    @FXML
    private TableView<Huella> tableviewHuellas;
    ObservableList<Huella> datos;
    
    String nombre, paterno, materno, numero, fechaNacimiento, genero;
    int idPer = 0;
    ConsultarDatos consultar;
    
    
    public EditarDatosCuentaController(String[] datos){
        nombre = datos[1];
        paterno = datos[2];
        materno = datos[3];
        genero = datos[4];
        numero = datos[5];
        fechaNacimiento = datos[6];
        consultar = new ConsultarDatos();
        idPer = consultar.obtenerIdPersonaProfesor(numero);
        System.out.println(idPer);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datos =  FXCollections.observableArrayList();
        
        comboboxGenero.getItems().addAll(
                "Masculino",
                "Femenino",
                "Indefinido"
        );

        textAjustes.setOnMouseEntered((MouseEvent me) -> {
            textAjustes.setUnderline(true);
            textAjustes.setFill(Color.BLUE);
        });
        textAjustes.setOnMouseExited((MouseEvent me) -> {
            textAjustes.setUnderline(false);
            textAjustes.setFill(Color.BLACK);
        });
        textAjustes.setOnMouseClicked((MouseEvent me) -> {
            irAInicio();
        });
        
        textfieldNombre.setText(nombre);
        textfieldPaterno.setText(paterno);
        textfieldMaterno.setText(materno);
        textfieldNumero.setText(numero);
        comboboxGenero.setValue(generoString(Integer.parseInt(genero)));
        comboboxGenero.setEditable(false);
        System.out.println(fechaNacimiento);
        
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formato = formato.withLocale(Locale.getDefault());
        LocalDate nacimiento = LocalDate.parse(fechaNacimiento, formato);
        datePickerNacimiento.setValue(nacimiento);
        
        buttonBorrarHuella.setDisable(true);
    }

    @FXML
    public void cambiarPane(ActionEvent e){
        if(e.getSource().equals(buttonHuella)){
            inicializarTabla();
            paneDatosPersonales.setVisible(false);
            paneHuellaDigital.setVisible(true);
        }else if(e.getSource().equals(buttonDatos)){
            paneHuellaDigital.setVisible(false);
            paneDatosPersonales.setVisible(true);
        }
    }
    
    @FXML
    public void guardarDatos(ActionEvent e){
        String nom, pat, mat, bol, fecha;
        int gen;
        nom = textfieldNombre.getText();
        pat = textfieldPaterno.getText();
        mat = textfieldMaterno.getText();
        bol = textfieldNumero.getText();
        gen = comboboxGenero.getSelectionModel().getSelectedIndex() + 1;
        LocalDate nacimiento = datePickerNacimiento.getValue();
        if(nacimiento == null)
            fecha = "";
        else
            fecha = nacimiento.toString();
        System.out.println(fecha);
        validaciones val = new validaciones();
        if (val.soloLet(nom, "el nombre", 200)) {
            if (val.soloLet(pat, "el apellido paterno", 200)) {
                if (val.soloLet(mat, "el apellido materno", 200)) {
                    if (val.sinVacios(bol, "el número de trabajador", 15)) {
                        if (val.validarFecha(fecha)) {
                            if (gen != -1) {
                                int id = consultar.editarDatosProfesores(numero, bol, nom, pat, mat, fecha);
                                if( id > 0){
                                    Alert alertOk = new Alert(Alert.AlertType.INFORMATION);
                                    alertOk.setTitle("PoliAsistencia");
                                    alertOk.setHeaderText(null);
                                    alertOk.setContentText("Datos Actualizados");
                                    alertOk.showAndWait();
                                    Stage stageAjustes = (Stage) (textAjustes.getScene().getWindow());
                                    FXMLLoader ajustes = new FXMLLoader(getClass().getResource("Ajustes.fxml"));
                                    try {
                                        Scene sceneAjustes = new Scene(ajustes.load());
                                        stageAjustes.setScene(sceneAjustes);
                                    } catch (IOException ex) {
                                        Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                else{
                                    Alert alertError = new Alert(Alert.AlertType.ERROR);
                                    alertError.setTitle("PoliAsistencia");
                                    alertError.setHeaderText("Error al guardar datos");
                                    switch(id){
                                        case -1:
                                            alertError.setContentText("Ya existe un profesor con ese número de trabajador");
                                            break;
                                        case -2:
                                            alertError.setContentText("No se encuentra la persona");
                                            break;
                                    }
                                    alertError.showAndWait();
                                }
                            } else {
                                comboboxGenero.requestFocus();
                                crearDialogo("Error", "Seleccione un genero", null);
                            }
                        } else {
                            datePickerNacimiento.requestFocus();
                            crearDialogo("Error", "Introduzca una fecha valida", null);
                        }
                    } else {
                        textfieldNumero.requestFocus();
                        crearDialogo("Error", val.err(), null);
                    }
                } else {
                    textfieldMaterno.requestFocus();
                    crearDialogo("Error", val.err(), null);
                }
            } else {
                crearDialogo("Error", val.err(), null);
                textfieldPaterno.requestFocus();
            }
        } else {
            crearDialogo("Error", val.err(), null);
            textfieldNombre.requestFocus();
        }
                            
                            
    }
    
    @FXML
    public void mostrarRegistroHuella(ActionEvent e) throws IOException{
        Stage stage = new Stage();
        
        HuellaDigitalController huella = new HuellaDigitalController(idPer);
        FXMLLoader huellaDigital = new FXMLLoader(getClass().getResource("/poliasistenciafx/HuellaDigital.fxml"));
        huellaDigital.setController(huella);
        
        Parent root = huellaDigital.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Huella Digital");
        stage.getIcons().add(new Image("/imagenes/poliAsistencia.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)e.getSource()).getScene().getWindow());
        stage.setOnHidden((WindowEvent evento) -> {
            huella.cerrar();
            actualizarTabla();
        }); 
        stage.showAndWait();
    }
    
    @FXML
    public void borrarHuella(ActionEvent e){
        Huella huellax = tableviewHuellas.getSelectionModel().getSelectedItem();
        if(huellax != null){
            borrarHuellaDigital(Integer.parseInt(huellax.getId()), huellax.getNombre());
        }
    }
    
   
    public void borrarHuellaDigital(int idHuella, String numeroHuella){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar acción");
        alert.setHeaderText("¿Estas seguro de que quieres borrar esta huella digital?");
        alert.setContentText("Huella a borrar: "+ numeroHuella);
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            if(consultar.borrarHuella(idHuella)){
                Alert alertOk = new Alert(Alert.AlertType.INFORMATION);
                alertOk.setTitle("PoliAsistencia");
                alertOk.setHeaderText(null);
                alertOk.setContentText("Huella Borrada");
                alertOk.showAndWait();
                actualizarTabla();
            }
            else{
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("PoliAsistencia");
                alertError.setHeaderText("Error al borrar la huella digital");
                alertError.setContentText("Lo sentimos, pero no se puede borrar la huella digital");
                alert.showAndWait();
                actualizarTabla();
            }
        } else {

        }
    }
    
    public void inicializarTabla(){
        datos = consultar.obtenerHuellasDigitales(idPer);
        tableviewHuellas.setItems(datos);
        
        tableviewHuellas.getSelectionModel().selectedItemProperty().addListener((observable, viejaSeleccion, nuevaSeleccion) -> {
                buttonBorrarHuella.setDisable(nuevaSeleccion == null);
        });
    }
    
    public void actualizarTabla(){
        datos.removeAll(datos);
        datos = consultar.obtenerHuellasDigitales(idPer);
        tableviewHuellas.setItems(datos);
        if(!datos.isEmpty()){
            if(datos.size() == 10){
                buttonAgregarHuella.setDisable(true);
            }
            else{
                buttonAgregarHuella.setDisable(false);
            }
        }
    }

    public void irAInicio() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar acción");
        alert.setHeaderText("¿Estás seguro de que quieres salir?");
        alert.setContentText("No se guardaran los datos ingresados");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageAjustes = (Stage) (textAjustes.getScene().getWindow());
            FXMLLoader ajustes = new FXMLLoader(getClass().getResource("Ajustes.fxml"));
            try {
                Scene sceneAjuestes = new Scene(ajustes.load());
                stageAjustes.setScene(sceneAjuestes);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

        }
    }
    
    public String generoString(int idGenero){
        switch(idGenero){
            case 1:
                return "Masculino";
            case 2:
                return "Femenino";
            case 3:
                return "Femenino"; 
        }
        return "";
    }
    
    public void crearDialogo(String titulo, String header, String contexto) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contexto);
        alert.showAndWait();
    }
    
}
