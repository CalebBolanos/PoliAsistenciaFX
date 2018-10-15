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

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import jefeAcademia.ProfesoresController;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Huella;
import poliasistenciafx.HuellaDigitalController;
import poliasistenciafx.baseDeDatos;
import poliasistenciafx.validaciones;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class RegistrarAlumnoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Text textInicio, textAlumnos;
    @FXML
    Button buttonContinuar, buttonCancelar, buttonAgregarHuella, buttonBorrarHuella, buttonGuardar;
    @FXML
    Pane paneDatosPersonales, paneHuellaDigital;
    @FXML
    TextField textfieldNombre, textfieldPaterno, textfieldMaterno, textfieldBoleta;
    @FXML
    ComboBox comboboxGenero;
    @FXML
    DatePicker datePickerNacimiento;
    @FXML
    private TableView<Huella> tableviewHuellas;
    ObservableList<Huella> datos;

    int pasoRegistro = 1, idPer = 0;
    String mensajeBase = "";
    ConsultarDatos consultar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultar = new ConsultarDatos();
        datos =  FXCollections.observableArrayList();
                
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

        textAlumnos.setOnMouseEntered((MouseEvent me) -> {
            textAlumnos.setUnderline(true);
            textAlumnos.setFill(Color.BLUE);
        });
        textAlumnos.setOnMouseExited((MouseEvent me) -> {
            textAlumnos.setUnderline(false);
            textAlumnos.setFill(Color.BLACK);
        });
        textAlumnos.setOnMouseClicked((MouseEvent me) -> {
            irAAlumnos();
        });
        
        buttonBorrarHuella.setDisable(true);
    }

    @FXML
    public void ejecutarAccion(ActionEvent e) {
        if (e.getSource().equals(buttonContinuar)){
            registrarProfesor();
        }

        if (e.getSource().equals(buttonCancelar)) {
            irAAlumnos();
        }
        
        if(e.getSource().equals(buttonGuardar)){
            switch(buttonGuardar.getText()){
                case "Omitir":
                    omitirGuardarHuella();
                    break;
                case "Guardar y Salir":
                    Stage stageRegistrarAlumno = (Stage) (textAlumnos.getScene().getWindow());
                    FXMLLoader Alumnos = new FXMLLoader(getClass().getResource("Alumnos.fxml"));
                    try {
                        Scene sceneAlumnos = new Scene(Alumnos.load());
                        stageRegistrarAlumno.setScene(sceneAlumnos);
                    } catch (IOException ex) {
                        Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                    
            }
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
    
    public void registrarProfesor(){
        String nom, pat, mat, bol, fecha;
        int gen;
        nom = textfieldNombre.getText();
        pat = textfieldPaterno.getText();
        mat = textfieldMaterno.getText();
        bol = textfieldBoleta.getText();
        gen = comboboxGenero.getSelectionModel().getSelectedIndex() + 1;
        LocalDate nacimiento = datePickerNacimiento.getValue();
        if(nacimiento == null)
            fecha = "";
        else
            fecha = nacimiento.toString();
        System.out.println(fecha);
        validaciones val = new validaciones();
        if (val.soloLet(nom, "el nombre", 250)) {
            if (val.soloLet(pat, "el apellido paterno", 250)) {
                if (val.soloLet(mat, "el apellido materno", 250)) {
                    if (val.boleta(bol)) {
                        if (val.validarFecha(fecha)) {
                            if (gen != -1) {
                                baseDeDatos bd = new baseDeDatos();
                                try {
                                    bd.conectar();
                                    if (bol != null) {
                                        ResultSet rs = bd.ejecuta("call spGuardaAlumnos("+gen+", '"+pat+"', '"+ mat+"', '"+nom+"', '"+fecha+"', 'sinasignar', '"+bol+"', 1);");
                                        while (rs.next()) {
                                            idPer = rs.getInt("idP");
                                            mensajeBase = rs.getString("msj");
                                        }
                                        System.out.println("IDP: " + idPer);
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                if (idPer > 0) {
                                    pasoRegistro++;
                                    inicializarTabla();
                                    paneDatosPersonales.setVisible(false);
                                    paneHuellaDigital.setVisible(true);
                                    buttonCancelar.setVisible(false);
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
                        textfieldBoleta.requestFocus();
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

    public void irAAlumnos() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar accion");
        alert.setHeaderText("Estas seguro de que quieres cancelar el registro?");
        alert.setContentText("Se perderan los datos ingresados");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageRegistrarAlumno = (Stage) (textAlumnos.getScene().getWindow());
            FXMLLoader Alumnos = new FXMLLoader(getClass().getResource("Alumnos.fxml"));
            try {
                Scene sceneAlumnos = new Scene(Alumnos.load());
                stageRegistrarAlumno.setScene(sceneAlumnos);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

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
                alertError.showAndWait();
                actualizarTabla();
            }
        } else {

        }
    }
    
    public void omitirGuardarHuella() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar accion");
        alert.setHeaderText("Estas seguro de que quieres omitir el registro de las huellas digitales?");
        alert.setContentText("Puedes configurar tus hellas digitales posteriormente en el apartado de modificacón de datos");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageRegistrarAlumno = (Stage) (textAlumnos.getScene().getWindow());
            FXMLLoader Alumnos = new FXMLLoader(getClass().getResource("Alumnos.fxml"));
            try {
                Scene sceneAlumnos = new Scene(Alumnos.load());
                stageRegistrarAlumno.setScene(sceneAlumnos);
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
            Stage stageRegistrarProfesor = (Stage) (textInicio.getScene().getWindow());
            FXMLLoader inicioGestion = new FXMLLoader(getClass().getResource("Inicio.fxml"));
            try {
                Scene sceneInicioGestion = new Scene(inicioGestion.load());
                stageRegistrarProfesor.setScene(sceneInicioGestion);
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
            buttonGuardar.setText("Guardar y Salir");
            buttonGuardar.setStyle("-fx-text-fill: #2196F3");
            if(datos.size() == 10){
                buttonAgregarHuella.setDisable(true);
            }
            else{
                buttonAgregarHuella.setDisable(false);
            }
        }
        else{
            buttonGuardar.setText("Omitir");
            buttonGuardar.setStyle("-fx-text-fill:  #f44242");
        }
    }
    
}
