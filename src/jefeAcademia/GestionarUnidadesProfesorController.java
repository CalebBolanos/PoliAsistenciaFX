/*
 * Copyright 2018 caleb.
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
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Persona;
import poliasistenciafx.Unidad;

/**
 * FXML Controller class
 *
 * @author caleb
 */
public class GestionarUnidadesProfesorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TableView<Unidad> tableViewUnidades;
    @FXML
    Text textInicio, textProfesores, textElegirProfesor, textSubtitulo;
    @FXML
    Button buttonAgregarUnidad, buttonBorrarUnidad;
    @FXML
    TextField textfieldBuscar;
    
    Persona profesor;
    ConsultarDatos consultar;
    String nombre, paterno, materno, numero;
    ObservableList<Unidad> datos;
    
    int idPersona = 0;
    
    public GestionarUnidadesProfesorController(Persona profesor){
        this.profesor = profesor;
        consultar = new ConsultarDatos();
        idPersona = consultar.obtenerIdPersonaProfesor(this.profesor.getNumero());
        nombre = this.profesor.getNombre();
        paterno = this.profesor.getPaterno();
        materno = this.profesor.getMaterno();
        numero = this.profesor.getNumero();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(idPersona);
        buttonBorrarUnidad.setDisable(true);
        textSubtitulo.setText(textSubtitulo.getText() + nombre);
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
        
        inicializarTabla();
    }
    
    public void inicializarTabla(){
        
        datos = consultar.obtenerUnidadesImpartidas(idPersona);
        
        FilteredList<Unidad> datosFiltrados = new FilteredList<>(datos, p -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(unidad -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (unidad.getUnidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getLunes().toLowerCase().contains(busquedaEnMinusculas)) {
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
                }
                return false;
            });
        });
        
        SortedList<Unidad> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableViewUnidades.comparatorProperty());
        tableViewUnidades.setItems(datosOrdenados);
        
        tableViewUnidades.getSelectionModel().selectedItemProperty().addListener((observable, viejaSeleccion, nuevaSeleccion) -> {
                buttonBorrarUnidad.setDisable(nuevaSeleccion == null);
        });
    }
    
    @FXML
    public void mostrarUnidades(ActionEvent e) throws IOException{
        Stage stage = new Stage();
        
        AgregarUnidadesController agregarUnidadesController = new AgregarUnidadesController();
        FXMLLoader agregar = new FXMLLoader(getClass().getResource("AgregarUnidades.fxml"));
        agregar.setController(agregarUnidadesController);
        
        Parent root = agregar.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Unidades de Aprendizaje");
        stage.getIcons().add(new Image("/imagenes/poliAsistencia.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)e.getSource()).getScene().getWindow());
        stage.setOnHidden((WindowEvent evento) -> {
            Unidad unidadEscogida = agregarUnidadesController.getUnidadElegida();
            if(unidadEscogida != null){
                System.out.println(unidadEscogida.getUnidad());
                guardarUnidad(unidadEscogida);
            }
            
        }); 
        stage.showAndWait();
        
    }
    
    public void actualizarTabla(){
        datos.removeAll(datos);
        datos = consultar.obtenerUnidadesImpartidas(idPersona);
        textfieldBuscar.setText("");
        FilteredList<Unidad> datosFiltrados = new FilteredList<>(datos, p -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(unidad -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (unidad.getUnidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getLunes().toLowerCase().contains(busquedaEnMinusculas)) {
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
                }
                return false;
            });
        });
        
        SortedList<Unidad> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableViewUnidades.comparatorProperty());
        tableViewUnidades.setItems(datosOrdenados);
    }
    
    @FXML
    public void borrarUnidadDeAprendizaje(ActionEvent e){
        Unidad unidadx = tableViewUnidades.getSelectionModel().getSelectedItem();
        if(unidadx != null){
            borrarUnidad(unidadx.getIdUnidad(), unidadx.getUnidad());
        }
    }
    
    public void borrarUnidad(int idUnidad, String nombreUnidad){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar acción");
        alert.setHeaderText("¿Estas seguro de que quieres borrar esta unidad de aprendizaje?");
        alert.setContentText("Unidad de aprendizaje a borrar: "+ nombreUnidad);
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            if(consultar.borrarUnidadProfesor(idUnidad, numero)){
                Alert alertOk = new Alert(Alert.AlertType.INFORMATION);
                alertOk.setTitle("PoliAsistencia");
                alertOk.setHeaderText(null);
                alertOk.setContentText("Unidad de Aprendizaje borrada");
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
    
    public void irAProfesores() {
        Stage stageEditarProfesor = (Stage) (textProfesores.getScene().getWindow());
        FXMLLoader Profesores = new FXMLLoader(getClass().getResource("Profesores.fxml"));
        try {
            Scene sceneProfesores = new Scene(Profesores.load());
            stageEditarProfesor.setScene(sceneProfesores);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void irAInicio() {
        Stage stageGestionarUnidades = (Stage) (textInicio.getScene().getWindow());
        FXMLLoader inicioJefe = new FXMLLoader(getClass().getResource("Inicio.fxml"));
        try {
            Scene sceneInicioJefe = new Scene(inicioJefe.load());
            stageGestionarUnidades.setScene(sceneInicioJefe);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void irAElegirProfesores() {
        Stage stageGestionarUnidades = (Stage) (textInicio.getScene().getWindow());
        FXMLLoader elegirProfesor = new FXMLLoader(getClass().getResource("ElegirProfesorUnidades.fxml"));
        try {
            Scene sceneElegirProfesor = new Scene(elegirProfesor.load());
            stageGestionarUnidades.setScene(sceneElegirProfesor);
        } catch (IOException ex) {
            Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearDialogo(String titulo, String header, String contexto) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contexto);
        alert.showAndWait();
    }
    
    public void guardarUnidad(Unidad unidad){
        ObservableList<Unidad> unidades = tableViewUnidades.getItems();
        boolean sinTraslapes = true;
        for(Unidad unidadx : unidades){
            sinTraslapes = validarTraslapes(unidadx, unidad);
            if(!sinTraslapes){
                crearDialogo("PoliAsistencia", "No se puede agregar Unidad de Aprendizaje", "La unidad de aprendizaje se traslapa con las unidades inscritas");
                return;
            }
        }
        consultar.agregarUnidadProfesor(unidad.getIdUnidad(), numero);
        crearDialogo("PoliAsistencia", "Unidad de Aprendizaje agregada", null);
        actualizarTabla();
        
        
    }
    
    public boolean validarTraslapes(Unidad unidadInscrita, Unidad unidadNueva){
        boolean ok;
        ok = traslapes(unidadInscrita.getLunes(), unidadNueva.getLunes());
        if(!ok){
            return ok;
        }
        ok = traslapes(unidadInscrita.getMartes(), unidadNueva.getMartes());
        if(!ok){
            return ok;
        }
        ok = traslapes(unidadInscrita.getMiercoles(), unidadNueva.getMiercoles());
        if(!ok){
            return ok;
        }
        ok = traslapes(unidadInscrita.getJueves(), unidadNueva.getJueves());
        if(!ok){
            return ok;
        }
        ok = traslapes(unidadInscrita.getViernes(), unidadNueva.getViernes());
        if(!ok){
            return ok;
        }
        return ok;
    }
    
    public boolean traslapes(String unidadInscrita, String nuevaUnidad){
        boolean ok = true;
        if(!unidadInscrita.equals("---")){
            int lunesInicioInscrito = Integer.parseInt(unidadInscrita.charAt(0)+""+unidadInscrita.charAt(1));
            int lunesFinalInscrito = Integer.parseInt(unidadInscrita.charAt(8)+""+unidadInscrita.charAt(9));
            if(!nuevaUnidad.equals("---")){
                int lunesInicioNueva = Integer.parseInt(nuevaUnidad.charAt(0)+""+nuevaUnidad.charAt(1));
                int lunesFinalNueva = Integer.parseInt(nuevaUnidad.charAt(8)+""+nuevaUnidad.charAt(9));
                if((lunesInicioNueva>=lunesInicioInscrito) && (lunesInicioNueva<=lunesFinalInscrito)){
                    ok = false;
                    return ok;
                }
                if((lunesFinalNueva>=lunesInicioInscrito) && (lunesFinalNueva<=lunesFinalInscrito)){
                    ok = false;
                    return ok;
                }
                if((lunesInicioInscrito>=lunesInicioNueva) && (lunesFinalInscrito<=lunesFinalNueva)){
                    ok = false;
                    return ok;
                }
            }
        }
        return ok;
    }
    
}
