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
import gestionEscolar.AgregarUnidadesController;
import javafx.beans.binding.Bindings;
import jefeAcademia.ProfesoresController;
import poliasistenciafx.ConsultarDatos;
import poliasistenciafx.Grupo;
import poliasistenciafx.Persona;
import poliasistenciafx.Unidad;
import poliasistenciafx.UnidadGrupo;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class GestionarUnidadesAlumnoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TableView<UnidadGrupo> tableViewUnidades;
    @FXML
    Text textInicio, textAlumnos, textElegirAlumno, textSubtitulo;
    @FXML
    Button buttonAgregarUnidad, buttonBorrarUnidad, buttonUnidadesGrupo;
    @FXML
    TextField textfieldBuscar;
    
    Persona alumno;
    ConsultarDatos consultar;
    ObservableList<UnidadGrupo> datos;
    
    int idPersona = 0;
    
    
    public GestionarUnidadesAlumnoController(Persona alumno, int idPersona){
          this.alumno = alumno;  
          this.idPersona = idPersona;
    }
    
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
        
        textElegirAlumno.setOnMouseEntered((MouseEvent me) -> {
            textElegirAlumno.setUnderline(true);
            textElegirAlumno.setFill(Color.BLUE);
        });
        textElegirAlumno.setOnMouseExited((MouseEvent me) -> {
            textElegirAlumno.setUnderline(false);
            textElegirAlumno.setFill(Color.BLACK);
        });
        textElegirAlumno.setOnMouseClicked((MouseEvent me) -> {
            irAElegirAlumnos();
        });
        
        textSubtitulo.setText(textSubtitulo.getText() + alumno.getNombre());
        
        inicializarTabla();
    }
    
    public void inicializarTabla(){
        
        datos = consultar.obtenerUnidadesAlumnoInscritas(alumno.getNumero());
        
        FilteredList<UnidadGrupo> datosFiltrados = new FilteredList<>(datos, p -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(unidad -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (unidad.getGrupo().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getUnidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getProfesor().toLowerCase().contains(busquedaEnMinusculas)) {
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
        
        SortedList<UnidadGrupo> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableViewUnidades.comparatorProperty());
        tableViewUnidades.setItems(datosOrdenados);
        
        tableViewUnidades.getSelectionModel().selectedItemProperty().addListener((observable, viejaSeleccion, nuevaSeleccion) -> {
                buttonBorrarUnidad.setDisable(nuevaSeleccion == null);
        });

        buttonUnidadesGrupo.setDisable(!tableViewUnidades.getItems().isEmpty());
    }
    
    public void actualizarTabla(){
        datos.removeAll(datos);
        datos = consultar.obtenerUnidadesAlumnoInscritas(alumno.getNumero());
        textfieldBuscar.setText("");
        
        FilteredList<UnidadGrupo> datosFiltrados = new FilteredList<>(datos, p -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(unidad -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (unidad.getGrupo().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getUnidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getProfesor().toLowerCase().contains(busquedaEnMinusculas)) {
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
        
        SortedList<UnidadGrupo> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableViewUnidades.comparatorProperty());
        tableViewUnidades.setItems(datosOrdenados);
        
        buttonUnidadesGrupo.setDisable(!tableViewUnidades.getItems().isEmpty());
    }
    
    @FXML
    public void mostrarGrupos(ActionEvent e) throws IOException{
        Stage stage = new Stage();
        
        AgregarUnidadesGrupoController agregarUnidadesGrupoController = new AgregarUnidadesGrupoController();
        FXMLLoader agregar = new FXMLLoader(getClass().getResource("AgregarUnidadesGrupo.fxml"));
        agregar.setController(agregarUnidadesGrupoController);
        
        Parent root = agregar.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Grupos");
        stage.getIcons().add(new Image("/imagenes/poliAsistencia.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)e.getSource()).getScene().getWindow());
        stage.setOnHidden((WindowEvent evento) -> {
            Grupo grupoEscogido = agregarUnidadesGrupoController.getGrupoElegido();
            if(grupoEscogido != null){
                System.out.println(grupoEscogido.getGrupo());
                guardarUnidadesGrupo(grupoEscogido);
            }
        }); 
        stage.showAndWait();
        
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
            UnidadGrupo unidadEscogida = agregarUnidadesController.getUnidadElegida();
            if(unidadEscogida != null){
                System.out.println(unidadEscogida.getUnidad());
                guardarUnidad(unidadEscogida);
            }
            
        }); 
        stage.showAndWait();
        
    }
    
    @FXML
    public void borrarUnidadDeAprendizaje(ActionEvent e){
        UnidadGrupo unidadx = tableViewUnidades.getSelectionModel().getSelectedItem();
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
            if(consultar.borrarUnidadAlumno(idUnidad, alumno.getNumero())){
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
                alertError.setContentText("Lo sentimos, pero no se puede borrar la unidad");
                alert.showAndWait();
                actualizarTabla();
            }
        } else {

        }
    }
    
    public void guardarUnidadesGrupo(Grupo grupo){
        if(tableViewUnidades.getItems().isEmpty()){
            if(consultar.agregarUnidadGrupoAlumno(alumno.getNumero(), grupo.getGrupo())){
                crearDialogo("PoliAsistencia", "Unidades de Aprendizaje del grupo "+grupo.getGrupo()+" agregadas", null);
            }
            else{
                crearDialogo("PoliAsistencia", "No se pudieron agregar la unidades de aprendizaje del grupo "+grupo.getGrupo(), null);
            }
        }
        else{
            crearDialogo("PoliAsistencia", "No se pueden agregar las Unidades de Aprendizaje del grupo"+grupo.getGrupo(), null);
        }
        actualizarTabla();
    }
    
    public void guardarUnidad(UnidadGrupo unidad){
        ObservableList<UnidadGrupo> unidades = tableViewUnidades.getItems();
        boolean sinTraslapes = true;
        for(UnidadGrupo unidadx : unidades){
            sinTraslapes = validarTraslapes(unidadx, unidad);
            if(!sinTraslapes){
                crearDialogo("PoliAsistencia", "No se puede agregar Unidad de Aprendizaje", "La unidad de aprendizaje se traslapa con las unidades inscritas");
                return;
            }
        }
        if(consultar.agregarUnidadAlumno(unidad.getIdUnidad(), alumno.getNumero())){
            crearDialogo("PoliAsistencia", "Unidad de Aprendizaje agregada", null);
            actualizarTabla();
        }
        else{
            crearDialogo("PoliAsistencia", "No se pudo agregar la unidad de aprendizaje", null);
            actualizarTabla();
        }

    }
    
    public boolean validarTraslapes(UnidadGrupo unidadInscrita, UnidadGrupo unidadNueva){
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
                if((lunesInicioNueva>=lunesInicioInscrito) && (lunesInicioNueva<lunesFinalInscrito)){
                    ok = false;
                    return ok;
                }
                if((lunesFinalNueva>lunesInicioInscrito) && (lunesFinalNueva<=lunesFinalInscrito)){
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
    
    public void irAAlumnos() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar accion");
        alert.setHeaderText("Estas seguro de que quieres cancelar el registro?");
        alert.setContentText("Se perderan los datos ingresados");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageEditarProfesor = (Stage) (textAlumnos.getScene().getWindow());
            FXMLLoader Profesores = new FXMLLoader(getClass().getResource("Alumnos.fxml"));
            try {
                Scene sceneProfesores = new Scene(Profesores.load());
                stageEditarProfesor.setScene(sceneProfesores);
            } catch (IOException ex) {
                Logger.getLogger(ProfesoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

        }
    }
    
    public void irAElegirAlumnos() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar accion");
        alert.setHeaderText("Estas seguro de que quieres cancelar el registro?");
        alert.setContentText("Se perderan los datos ingresados");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            Stage stageEditarProfesor = (Stage) (textInicio.getScene().getWindow());
            FXMLLoader elegirProfesor = new FXMLLoader(getClass().getResource("ElegirAlumnoAsignarUnidades.fxml"));
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
