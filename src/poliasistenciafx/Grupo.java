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

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Caleb
 */
public final class Grupo {
    private final SimpleIntegerProperty idGrupo = new SimpleIntegerProperty(0);
    private final SimpleStringProperty grupo = new SimpleStringProperty("");
    private final SimpleIntegerProperty semestre = new SimpleIntegerProperty(0);
    private final SimpleStringProperty turno = new SimpleStringProperty("");
    private final SimpleStringProperty especialidad = new SimpleStringProperty("");
    private final SimpleIntegerProperty idTurno = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty idEspecialdad = new SimpleIntegerProperty(0);
    
    public Grupo(){
        this(0, "", 0, "", "", 0, 0);
    }
    
    public Grupo(int idGrupo, String grupo, int semestre, String turno, String especialidad, int idTurno, int idEspecialidad){
        setIdGrupo(idGrupo);
        setGrupo(grupo);
        setSemestre(semestre);
        setTurno(turno);
        setEspecialidad(especialidad);
        setIdTurno(idTurno);
        setIdEspecialidad(idEspecialidad);
    }
    
    public int getIdGrupo(){
        return idGrupo.get();
    }
    public void setIdGrupo(int idGrupo){
        this.idGrupo.set(idGrupo);
    }
    
    public String getGrupo() {
        return grupo.get();
    }
    public void setGrupo(String grupo) {
        this.grupo.set(grupo);
    }
    
    public int getSemestre() {
        return semestre.get();
    }
    public void setSemestre(int semestre) {
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
    
    public int getIdTurno(){
        return idTurno.get();
    }
    public void setIdTurno(int idTurno){
        this.idTurno.set(idTurno);
    }
    
    public int getIdEspecialidad(){
        return idEspecialdad.get();
    }
    public void setIdEspecialidad(int idEspecialdad){
        this.idEspecialdad.set(idEspecialdad);
    }
}
