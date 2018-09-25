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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.Alert.AlertType;
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
import poliasistenciafx.baseDeDatos;
import poliasistenciafx.validaciones;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class RegistrarProfesorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Text textInicio, textProfesores;
    @FXML
    Button buttonContinuar, buttonCancelar;
    @FXML
    Pane paneDatosPersonales, paneHuellaDigital;
    @FXML
    TextField textfieldNombre, textfieldPaterno, textfieldMaterno, textfieldNumero;
    @FXML
    ComboBox comboboxGenero;
    @FXML
    DatePicker datePickerNacimiento;

    int pasoRegistro = 1, idPer = 0;
    String mensajeBase = "";

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
    }

    @FXML
    public void ejecutarAccion(ActionEvent e) {
        if (e.getSource().equals(buttonContinuar)) {
            String nom, pat, mat, bol, fecha;
            int gen;
            nom = textfieldNombre.getText();
            pat = textfieldPaterno.getText();
            mat = textfieldMaterno.getText();
            bol = textfieldNumero.getText();
            gen = comboboxGenero.getSelectionModel().getSelectedIndex();
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
                                    baseDeDatos bd = new baseDeDatos();
                                    try {
                                        bd.conectar();
                                        //spGuardaDocente(in idT int,in g int, in pat nvarchar(250),in mat nvarchar(250), in nom nvarchar(250),
                                        //in fech date, in mail nvarchar(250),in numT nvarchar(15),in hu longblob)
                                        if (bol != null) {
                                            ResultSet rs = bd.ejecuta("call spGuardaDocente(" + 3 + ", " + gen+1 + ", '" + pat + "', '" + mat
                                                    + "', '" + nom + "', '" + fecha + "', 'sinasignar@gmail.com', '" + bol + "');");
                                            while (rs.next()) {
                                                idPer = rs.getInt("idP");
                                                mensajeBase = rs.getString("msj");
                                            }
                                            System.out.println("IDP: " + idPer);
                                        }
                                    } catch (SQLException ex) {

                                    }
                                    if (idPer > 0) {
                                        pasoRegistro++;
                                        paneDatosPersonales.setVisible(false);
                                        paneHuellaDigital.setVisible(true);
                                        buttonCancelar.setText("Omitir");
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

        if (e.getSource().equals(buttonCancelar)) {
            irAProfesores();
        }
    }

    public void irAProfesores() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar accion");
        alert.setHeaderText("Estas seguro de que quieres cancelar el registro?");
        alert.setContentText("Se perderan los datos ingresados");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageRegistrarProfesor = (Stage) (textProfesores.getScene().getWindow());
            FXMLLoader Profesores = new FXMLLoader(getClass().getResource("Profesores.fxml"));
            try {
                Scene sceneProfesores = new Scene(Profesores.load());
                stageRegistrarProfesor.setScene(sceneProfesores);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

        }
    }

    public void irAInicio() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar accion");
        alert.setHeaderText("Estas seguro de que quieres cancelar el registro?");
        alert.setContentText("Se perderan los datos ingresados");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageRegistrarProfesor = (Stage) (textInicio.getScene().getWindow());
            FXMLLoader inicioJefe = new FXMLLoader(getClass().getResource("Inicio.fxml"));
            try {
                Scene sceneInicioJefe = new Scene(inicioJefe.load());
                stageRegistrarProfesor.setScene(sceneInicioJefe);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

        }
    }

    public void crearDialogo(String titulo, String header, String contexto) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contexto);
        alert.showAndWait();
    }

}
