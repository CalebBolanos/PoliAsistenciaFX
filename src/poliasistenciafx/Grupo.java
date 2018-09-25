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
package poliasistenciafx;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Caleb
 */
public class Grupo {
    private final SimpleStringProperty grupo = new SimpleStringProperty("");
    private final SimpleStringProperty semestre = new SimpleStringProperty("");
    private final SimpleStringProperty turno = new SimpleStringProperty("");
    private final SimpleStringProperty especialidad = new SimpleStringProperty("");
    
    public Grupo(){
        this("", "", "", "");
    }
    
    public Grupo(String grupo, String semestre, String turno, String especialidad){
        
    }
    
    public String getGrupo() {
        return grupo.get();
    }
    public void setGrupo(String grupo) {
        this.grupo.set(grupo);
    }
    
    public String getSemestre() {
        return semestre.get();
    }
    public void setSemestre(String semestre) {
        this.semestre.set(semestre);
    }
    
    public String getTurno() {
        return turno.get();
    }
    public void setTurno(String turno) {
        this.turno.set(turno);
    }
    
    public String getEspecialidad() {
        return especialidad.get();
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad.set(especialidad);
    }
    
}
