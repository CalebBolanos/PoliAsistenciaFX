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
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class HuellaDigitalController implements Initializable {

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
    public int idPer = 0;
    boolean capturaHuella = false;
    boolean huellaGuardada = false;
    Glow glow;
    SepiaTone sepia;
    Timeline timelineEntrada, timelineBlanco, timelineSalidaVerde, timelineSalidaRojo;
    ColorAdjust rojo, verde;
    
    @FXML
    Text textIndicaciones, textEstatus, textError;
    @FXML
    ImageView imageviewHuella;
    
    public HuellaDigitalController(int idPer){
        this.idPer = idPer;
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
        estatusGuardarHuellas();
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

    public void estatusGuardarHuellas() {
        if(Reclutador.getFeaturesNeeded() == 4){
            textEstatus.setText("Coloca el dedo en el sensor y levantalo cuatro veces seguidas para registrarte");
        }
        else{
            textEstatus.setText("Huellas necesarias para guardar tu huella digital: " + Reclutador.getFeaturesNeeded());
        }    
    }

    public void procesarCaptura(DPFPSample sample) throws SQLException {
        featuresinscripcion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
        featuresverificacion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
        if (featuresinscripcion != null) {
            try {
                System.out.println("Las Caracteristicas de la Huella han sido creada");
                efectosHuellaEscaneada();
                Reclutador.addFeatures(featuresinscripcion);
            } catch (DPFPImageQualityException ex) {
                System.err.println("Error: " + ex.getMessage());
                efectosHuellaIrregular();
                //rojo
            } finally {
                estatusGuardarHuellas();
                switch (Reclutador.getTemplateStatus()) {
                    case TEMPLATE_STATUS_READY:
                        cerrar();
                        setTemplate(Reclutador.getTemplate());
                        System.out.println("La Plantilla de la Huella ha Sido Creada, ya puede Verificarla o Identificarla");
                        guardarHuellaBD();
                        break;
                    case TEMPLATE_STATUS_FAILED:
                        Reclutador.clear();
                        cerrar();
                        estatusGuardarHuellas();
                        setTemplate(null);
                        System.out.println("La Plantilla de la Huella no pudo ser creada, Repita el Proceso");
                        textError.setText("La plantilla de la huella no pudo ser creada, existen varias irregularidades, repite el proceso");
                        Lector.startCapture();
                        System.out.println("Utilizando el Lector de Huella Dactilar");
                        break;
                }
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
    
    public void guardarHuellaBD() throws SQLException{
        ByteArrayInputStream datosHuella = new ByteArrayInputStream(template.serialize());
        Integer tamanoHuella=template.serialize().length;
        baseDeDatos bd = new baseDeDatos();
        bd.conectar();
        Connection coneccion = bd.getConexion();
         try (PreparedStatement guardar = coneccion.prepareStatement("call fGuardaHuella(?,?)")) {
             guardar.setInt(1, idPer);
             guardar.setBinaryStream(2, datosHuella, tamanoHuella);
             guardar.execute();
             guardar.close();
         }
         System.out.println("Huella Guardada");
         bd.cierraConexion();
         Stage stageActual = (Stage) buttonCancelar.getScene().getWindow();
         stageActual.close();
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
        textIndicaciones.setText("Vuelve a tocar el sesor");
        imageviewHuella.setEffect(sepia);
        timelineBlanco.play();
        if(!capturaHuella){
            imageviewHuella.setEffect(rojo);
            timelineSalidaRojo.play();
            textError.setVisible(true);
            textIndicaciones.setText("Levanta el dedo y vuelve a tocar el sesor");
        }
    }
    
    public void efectosHuellaEscaneada(){
        textIndicaciones.setText("Huella escaneada, levanta el dedo");
        capturaHuella = true;
        imageviewHuella.setEffect(verde);
        timelineSalidaVerde.play();
    }
    
    public void efectosHuellaIrregular(){
        capturaHuella = false;
        imageviewHuella.setEffect(rojo);
        timelineSalidaRojo.play();
    }

    @FXML
    public void cerrarVentana(ActionEvent e) {
        Stage stageActual = (Stage) buttonCancelar.getScene().getWindow();
        stageActual.close();
    }

}
