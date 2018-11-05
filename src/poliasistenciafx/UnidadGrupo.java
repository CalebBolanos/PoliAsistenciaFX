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

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Caleb
 */
public final class UnidadGrupo {
    
    private final SimpleIntegerProperty idUnidad = new SimpleIntegerProperty(0);
    private final SimpleStringProperty grupo = new SimpleStringProperty("");
    private final SimpleStringProperty unidad = new SimpleStringProperty("");
    private final SimpleStringProperty profesor = new SimpleStringProperty("");
    private final SimpleStringProperty lunes = new SimpleStringProperty("");
    private final SimpleStringProperty martes = new SimpleStringProperty("");
    private final SimpleStringProperty miercoles = new SimpleStringProperty("");
    private final SimpleStringProperty jueves = new SimpleStringProperty("");
    private final SimpleStringProperty viernes = new SimpleStringProperty("");
    private final SimpleIntegerProperty cupo = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty disponibilidad = new SimpleIntegerProperty(0);
    
    public UnidadGrupo(){
        this(0, "", "", "", "", "", "", "", "", 0, 0);
    }
    
    public UnidadGrupo(int idUnidad, String grupo, String unidad, String profesor, String lunes, String martes, String miercoles, String jueves, String viernes, int cupo, int disponibilidad){
        setIdUnidad(idUnidad);
        setGrupo(grupo);
        setUnidad(unidad);
        setProfesor(profesor);
        setLunes(lunes);
        setMartes(martes);
        setMiercoles(miercoles);
        setJueves(jueves);
        setViernes(viernes);
        setCupo(cupo);
        setDisponibilidad(disponibilidad);
    }
    
    
    public int getIdUnidad(){
        return idUnidad.get();
    }
    public void setIdUnidad(int idUnidad){
        this.idUnidad.set(idUnidad);
    }
    
    public String getGrupo(){
        return grupo.get();
    }
    public void setGrupo(String grupo){
        this.grupo.set(grupo);
    }
    
    public String getUnidad(){
        return unidad.get();
    }
    public void setUnidad(String unidad){
        this.unidad.set(unidad);
    }
    
    public String getProfesor(){
        return profesor.get();
    }
    public void setProfesor(String profesor){
        this.profesor.set(profesor);
    }
    
    public String getLunes(){
        return lunes.get();
    }
    public void setLunes(String lunes){
        this.lunes.set(lunes);
    }
    
    public String getMartes(){
        return martes.get();
    }
    public void setMartes(String martes){
        this.martes.set(martes);
    }
    
    public String getMiercoles(){
        return miercoles.get();
    }
    public void setMiercoles(String miercoles){
        this.miercoles.set(miercoles);
    }
    
    public String getJueves(){
        return jueves.get();
    }
    public void setJueves(String jueves){
        this.jueves.set(jueves);
    }
    
    public String getViernes(){
        return viernes.get();
    }
    public void setViernes(String viernes){
        this.viernes.set(viernes);
    }
    
    public int getCupo(){
        return cupo.get();
    }
    public void setCupo(int cupo){
        this.cupo.set(cupo);
    }
    
    public int getDisponibilidad(){
        return disponibilidad.get();
    }
    public void setDisponibilidad(int disponibilidad){
        this.disponibilidad.set(disponibilidad);
    }
    
}
