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
package poliasistenciafx;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPErrorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Caleb
 */
public class IniciarSesionHuellaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button buttonCancelar;

    private DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();
    private DPFPEnrollment Reclutador = DPFPGlobal.getEnrollmentFactory().createEnrollment();
    private DPFPVerification Verificador = DPFPGlobal.getVerificationFactory().createVerification();
    private DPFPTemplate template;
    public static String TEMPLATE_PROPERTY = "template";
    public DPFPFeatureSet featuresinscripcion;
    public DPFPFeatureSet featuresverificacion;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public static final int GESTION = 1, JEFE_ACADEMIA = 4;
    boolean capturaHuella = false;
    boolean huellaGuardada = false;
    Glow glow;
    SepiaTone sepia;
    Timeline timelineEntrada, timelineBlanco, timelineSalidaVerde, timelineSalidaRojo;
    ColorAdjust rojo, verde;
    int idPersona, idTipo;
    
    @FXML
    Text textIndicaciones, textEstatus, textError;
    @FXML
    ImageView imageviewHuella;

    public void setIdPersona(int idPersona){
        this.idPersona = idPersona;
    }
    public int getIdPersona(){
        return idPersona;
    }
    
    public void setIdTipo(int idTipo){
        this.idTipo = idTipo;
    }
    public int getIdTipo(){
        return idTipo;
    }
    
    public DPFPTemplate getTemplate() {
        return template;
    }

    public void setTemplate(DPFPTemplate template) {
        DPFPTemplate old = this.template;
        this.template = template;
        this.pcs.firePropertyChange(TEMPLATE_PROPERTY, old, template);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarSensor();
        Lector.startCapture();
        System.out.println("Utilizando el Lector de Huella Dactilar");
        inicializarEfectos();
    }

    public void cerrar() {
        Lector.stopCapture();
        System.out.println("No se está usando el Lector de Huella Dactilar");
    }

    protected void inicializarSensor() {
        Lector.addDataListener(new DPFPDataAdapter() {
            @Override
            public void dataAcquired(final DPFPDataEvent e) {
                Platform.runLater(() -> {
                    System.out.println("La Huella Digital ha sido Capturada");
                    try {
                        procesarCaptura(e.getSample());
                    } catch (SQLException ex) {
                        Logger.getLogger(HuellaDigitalController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        });

        Lector.addReaderStatusListener(new DPFPReaderStatusAdapter() {
            @Override
            public void readerConnected(final DPFPReaderStatusEvent e) {
                Platform.runLater(() -> {
                    System.out.println("El Sensor de Huella Digital esta Activado o Conectado");
                });
            }
            @Override
            public void readerDisconnected(final DPFPReaderStatusEvent e) {
                Platform.runLater(() -> {
                    System.out.println("El Sensor de Huella Digital esta Desactivado o no Conectado");
                });
            }
        });

        Lector.addSensorListener(new DPFPSensorAdapter() {
            @Override
            public void fingerTouched(final DPFPSensorEvent e) {
                Platform.runLater(() -> {
                    System.out.println("El dedo ha sido colocado sobre el Lector de Huella");
                    efectosDedoColocado();
                });
            }
            @Override
            public void fingerGone(final DPFPSensorEvent e) {
                Platform.runLater(() -> {
                    System.out.println("El dedo ha sido quitado del Lector de Huella");
                    efectosDedoQuitado();
                });
            }
        });

        Lector.addErrorListener(new DPFPErrorAdapter() {
            public void errorReader(final DPFPErrorEvent e) {
                Platform.runLater(() -> {
                    System.out.println("Error: " + e.getError());
                });
            }
        });
    }

    public void finalizarSensor() {
        Lector.stopCapture();
        System.out.println("No se está usando el Lector de Huella Dactilar");
    }

    public void procesarCaptura(DPFPSample sample) throws SQLException {
        featuresinscripcion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
        featuresverificacion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
        if (featuresinscripcion != null) {
            try {
                System.out.println("Las Caracteristicas de la Huella han sido creada");
                Reclutador.addFeatures(featuresinscripcion);
                identificarHuella();
                Reclutador.clear();
            } catch (DPFPImageQualityException ex) {
                System.err.println("Error: " + ex.getMessage());
                efectosHuellaIrregular();
                //rojo
            } 
        }
    }

    public DPFPFeatureSet extraerCaracteristicas(DPFPSample sample, DPFPDataPurpose purpose) {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            return null;
        }
    }
    
    public void identificarHuella() throws SQLException{
        int idPer, idT;
        String nombre = "";
        boolean identificado = false;
        baseDeDatos bd = new baseDeDatos();
        bd.conectar();
        Connection coneccion = bd.getConexion();
        try (PreparedStatement identificar = coneccion.prepareStatement("select * from vwHuellasPersonas")) {
            ResultSet rs = identificar.executeQuery(); 
            while(rs.next()){
                byte huellaBase[] = rs.getBytes("huella");
                DPFPTemplate referenceHuellaBase = DPFPGlobal.getTemplateFactory().createTemplate(huellaBase);
                setTemplate(referenceHuellaBase);
                DPFPVerificationResult resultado = Verificador.verify(featuresverificacion, getTemplate());
                
                if(resultado.isVerified()){
                    idPer = rs.getInt("idPersona");
                    idT = rs.getInt("idTipo");
                    nombre = rs.getString("nombre");
                    if(idPer > 0){
                        switch(idT){
                            case GESTION:
                                identificado = true;
                                setIdPersona(idPer);
                                setIdTipo(idT);
                                efectosHuellaIdentificada(nombre);
                                break;
                            case JEFE_ACADEMIA:
                                identificado = true;
                                setIdPersona(idPer);
                                setIdTipo(idT);
                                efectosHuellaIdentificada(nombre);
                                break;
                            default:
                                identificado = true;
                                efectosHuellaIdentificadaSinPermiso();
                                break;
                        }
                    }
                }
            }
         }

        bd.cierraConexion();
        if(identificado){
            if(idPersona != 0){
                Stage stageActual = (Stage) buttonCancelar.getScene().getWindow();
                stageActual.close();
            }
        }
        else{
            efectosHuellaNoIdentificada();
        }
    }
    
    public void inicializarEfectos(){
        glow = new Glow(0);
        
        rojo = new ColorAdjust();
        rojo.setContrast(0.0);
        rojo.setHue(0.1);
        rojo.setBrightness(0.1);
        rojo.setSaturation(0.97);
        rojo.setInput(glow);
        
        verde = new ColorAdjust();
        verde.setContrast(0.0);
        verde.setHue(0.5);
        verde.setBrightness(0.1);
        verde.setSaturation(.97);
        verde.setInput(glow);
        
        sepia = new SepiaTone(0);
        sepia.setInput(glow);
        imageviewHuella.setEffect(sepia);
        
        timelineEntrada = new Timeline(
            new KeyFrame(Duration.ZERO, 
                    new KeyValue(glow.levelProperty(), 0),
                    new KeyValue(sepia.levelProperty(), 0)),
            new KeyFrame(Duration.seconds(.2),
                    new KeyValue(glow.levelProperty(), 1),
                    new KeyValue(sepia.levelProperty(),1))
        );
        timelineSalidaVerde = new Timeline(
            new KeyFrame(Duration.ZERO, 
                    new KeyValue(glow.levelProperty(), 1),
                    new KeyValue(verde.brightnessProperty(), 1)),
            new KeyFrame(Duration.seconds(1),
                    new KeyValue(glow.levelProperty(), 0),
                    new KeyValue(verde.brightnessProperty(),0))
        );
        timelineSalidaRojo = new Timeline(
            new KeyFrame(Duration.ZERO, 
                    new KeyValue(glow.levelProperty(), 1),
                    new KeyValue(rojo.brightnessProperty(), 1)),
            new KeyFrame(Duration.seconds(1),
                    new KeyValue(glow.levelProperty(), 0),
                    new KeyValue(rojo.brightnessProperty(),0))
        );
        timelineBlanco = new Timeline(
            new KeyFrame(Duration.ZERO, 
                    new KeyValue(glow.levelProperty(), 0.7),
                    new KeyValue(sepia.levelProperty(), 1)),
            new KeyFrame(Duration.seconds(1),
                    new KeyValue(glow.levelProperty(), 0),
                    new KeyValue(sepia.levelProperty(),0))
        );
    }
    
    public void efectosDedoColocado(){
        textError.setVisible(false);
        textError.setText("El sensor no pudo identificar tu huella. Vuelve a intentarlo");
        textIndicaciones.setText("Escaneando huella...");
        imageviewHuella.setEffect(sepia);
        timelineEntrada.play();
        capturaHuella = false;
    }
    
    public void efectosDedoQuitado(){
        if(!capturaHuella){
            imageviewHuella.setEffect(rojo);
            timelineSalidaRojo.play();
            textIndicaciones.setText("Levanta el dedo y vuelve a tocar el sensor");
            textError.setVisible(true);
        }
    }
    
    public void efectosHuellaIdentificada(String nombrePersona){
        textIndicaciones.setText("Bienvenido "+nombrePersona);
        capturaHuella = true;
        imageviewHuella.setEffect(verde);
        timelineSalidaVerde.play();
    }
    
    public void efectosHuellaIdentificadaSinPermiso(){
        capturaHuella = false;
        imageviewHuella.setEffect(rojo);
        timelineSalidaRojo.play();
        textIndicaciones.setText("Levanta el dedo y vuelve a tocar el sensor");
        textError.setText("La huella digital capturada no pertenece a personal de gestion escolar o jefe de academia");
        textError.setVisible(true);
    }
    
    public void efectosHuellaIrregular(){
        capturaHuella = false;
        imageviewHuella.setEffect(rojo);
        timelineSalidaRojo.play();
    }
      
    public void efectosHuellaNoIdentificada(){
        capturaHuella = false;
        imageviewHuella.setEffect(rojo);
        timelineSalidaRojo.play();
        textError.setText("La huella digital capturada no se encuentra registrada. Vuelve a intentarlo");
        textError.setVisible(true);
        textIndicaciones.setText("Levanta el dedo y vuelve a tocar el sensor");
    }

    @FXML
    public void cerrarVentana(ActionEvent e) {
        Stage stageActual = (Stage) buttonCancelar.getScene().getWindow();
        stageActual.close();
    }    
    
}
