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
package poliasistenciafx;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author caleb
 */
public final class Unidad {
    
    private final SimpleStringProperty unidad = new SimpleStringProperty("");
    private final SimpleStringProperty lunes = new SimpleStringProperty("");
    private final SimpleStringProperty martes = new SimpleStringProperty("");
    private final SimpleStringProperty miercoles = new SimpleStringProperty("");
    private final SimpleStringProperty jueves = new SimpleStringProperty("");
    private final SimpleStringProperty viernes = new SimpleStringProperty("");
    
    public Unidad(){
        this("", "", "", "", "", "");
    }
    
    public Unidad(String unidad, String lunes, String martes, String miercoles, String jueves, String viernes){
        setUnidad(unidad);
        setLunes(lunes);
        setMartes(martes);
        setMiercoles(miercoles);
        setJueves(jueves);
        setViernes(viernes);
    }
    
    
    public String getUnidad(){
        return unidad.get();
    }
    public void setUnidad(String unidad){
        this.unidad.set(unidad);
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
    
}
