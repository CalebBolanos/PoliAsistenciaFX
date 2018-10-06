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
import poliasistenciafx.Grupo;
import poliasistenciafx.Persona;
import poliasistenciafx.Unidad;
import poliasistenciafx.UnidadProfesor;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class GestionarProfesoresGrupoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView<UnidadProfesor> tableViewUnidadesProfesores;
    @FXML
    Text textInicio, textGrupos, textElegirGrupo, textTitulo, textSubtitulo;
    @FXML
    Button buttonAgregarProfesor, buttonBorrarProfesor;
    @FXML
    TextField textfieldBuscar;
    
    Grupo grupo;
    ConsultarDatos consultar;
    String nombreGrupo;
    int semestre;
    ObservableList<UnidadProfesor> datos;
    
    
    public GestionarProfesoresGrupoController(Grupo grupo){
        this.grupo = grupo;
        nombreGrupo = grupo.getGrupo();
        semestre = grupo.getSemestre();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultar = new ConsultarDatos();
        
        textTitulo.setText(textTitulo.getText()+nombreGrupo);
        textSubtitulo.setText(textSubtitulo.getText()+nombreGrupo);
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
        
        inicializarTabla();
        buttonBorrarProfesor.setDisable(true);
    }
    
    public void inicializarTabla(){
        
        datos = consultar.obtenerProfesoresInscritosEnGrupo(nombreGrupo);
        
        FilteredList<UnidadProfesor> datosFiltrados = new FilteredList<>(datos, u -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(unidad -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (unidad.getUnidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getProfesor().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }else if (unidad.getLunes().toLowerCase().contains(busquedaEnMinusculas)) {
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
        
        SortedList<UnidadProfesor> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableViewUnidadesProfesores.comparatorProperty());
        tableViewUnidadesProfesores.setItems(datosOrdenados);
        
        tableViewUnidadesProfesores.getSelectionModel().selectedItemProperty().addListener((observable, viejaSeleccion, nuevaSeleccion) -> {
                buttonBorrarProfesor.setDisable(nuevaSeleccion == null);
        });
    }
    
    public void actualizarTabla(){
        datos.removeAll(datos);
        datos = consultar.obtenerProfesoresInscritosEnGrupo(nombreGrupo);
        textfieldBuscar.setText("");
        FilteredList<UnidadProfesor> datosFiltrados = new FilteredList<>(datos, u -> true);
        textfieldBuscar.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(unidad -> {
                if (nuevoValor == null || nuevoValor.isEmpty()) {
                    return true;
                }
                String busquedaEnMinusculas = nuevoValor.toLowerCase();
                if (unidad.getUnidad().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                } else if (unidad.getProfesor().toLowerCase().contains(busquedaEnMinusculas)) {
                    return true;
                }else if (unidad.getLunes().toLowerCase().contains(busquedaEnMinusculas)) {
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
        
        SortedList<UnidadProfesor> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tableViewUnidadesProfesores.comparatorProperty());
        tableViewUnidadesProfesores.setItems(datosOrdenados);
    }

    @FXML
    public void mostrarProfesoresUnidad(ActionEvent e) throws IOException{
        Stage stage = new Stage();
        
        AgregarProfesoresConUnidadesController agregarProfesorController = new AgregarProfesoresConUnidadesController(semestre);
        FXMLLoader agregar = new FXMLLoader(getClass().getResource("AgregarProfesoresConUnidades.fxml"));
        agregar.setController(agregarProfesorController);
        
        Parent root = agregar.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Profesores con unidades de aprendizaje disponibles");
        stage.getIcons().add(new Image("/imagenes/poliAsistencia.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node)e.getSource()).getScene().getWindow());
        stage.setOnHidden((WindowEvent evento) -> {
            UnidadProfesor profesorEscogido = agregarProfesorController.getProfesorElegido();
            if(profesorEscogido != null){
                System.out.println(profesorEscogido.getUnidad());
                guardarProfesor(profesorEscogido);
            }
            
        }); 
        stage.showAndWait();
    }
    
    @FXML
    public void borrarProfesorUnidad(ActionEvent e){
        UnidadProfesor profesorx = tableViewUnidadesProfesores.getSelectionModel().getSelectedItem();
        if(profesorx != null){
            borrarProfesor(profesorx.getProfesor(), profesorx.getUnidad(), profesorx.getIdUnidad());
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
                recurso = "ElegirProfesoresAsignarGrupo.fxml";
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
    
    public void borrarProfesor(String nombreProfesor, String nombreUnidad, int idUnidad){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar acción");
        alert.setHeaderText("¿Estas seguro de que quieres borrar al profesor de este grupo?");
        alert.setContentText("Profesor seleccionado: "+nombreProfesor+"\n Unidad de Aprendizaje: "+nombreUnidad);
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            if(consultar.borrarProfesorGrupo(idUnidad, nombreGrupo)){
                Alert alertOk = new Alert(Alert.AlertType.INFORMATION);
                alertOk.setTitle("PoliAsistencia");
                alertOk.setHeaderText(null);
                alertOk.setContentText("Profesor dado de baja del grupo");
                alertOk.showAndWait();
                actualizarTabla();
            }
            else{
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("PoliAsistencia");
                alertError.setHeaderText("Error al borrar profesor");
                alertError.setContentText("Lo sentimos, pero no podemos dar de baja al profesor");
                alert.showAndWait();
                actualizarTabla();
            }
        } else {

        }
    }

    public void guardarProfesor(UnidadProfesor profesor) {
        ObservableList<UnidadProfesor> profesores = tableViewUnidadesProfesores.getItems();
        boolean sinTraslapes = true;
        for(UnidadProfesor profesorx : profesores){
            sinTraslapes = validarTraslapes(profesorx, profesor);
            if(!sinTraslapes){
                crearDialogo("PoliAsistencia", "No se puede agregar el profesor al grupo", "La unidad de aprendizaje del profesor se traslapa con las unidades existentes del grupo");
                return;
            }
        }
        if(consultar.agregarProfesorGrupo(profesor.getIdUnidad(), nombreGrupo)){
            crearDialogo("PoliAsistencia", "Profesor agregado al grupo", null);
            actualizarTabla();
        }
        else{
            crearDialogo("PoliAsistencia", "El profesor no pudo ser agregado al grupo", null);
            actualizarTabla();
        }
        
    }
    
    
    public boolean validarTraslapes(UnidadProfesor unidadInscrita, UnidadProfesor unidadNueva){
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
    
    public void crearDialogo(String titulo, String header, String contexto) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contexto);
        alert.showAndWait();
    }
    
}
