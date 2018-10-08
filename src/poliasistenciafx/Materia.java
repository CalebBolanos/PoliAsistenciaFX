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
public final class Materia {
    private final SimpleIntegerProperty idMateria = new SimpleIntegerProperty(0);
    private final SimpleStringProperty nombreUnidad = new SimpleStringProperty("");
    private final SimpleIntegerProperty idSemestre = new SimpleIntegerProperty(0);
    private final SimpleStringProperty area = new SimpleStringProperty("");
    private final SimpleIntegerProperty idArea = new SimpleIntegerProperty(0);
    
    public Materia(){
        this(0, "", 0, "", 0);
    }
    
    public Materia(int idMateria, String nombreUnidad, int idSemestre, String area, int idArea){
        setIdMateria(idMateria);
        setNombreUnidad(nombreUnidad);
        setIdSemestre(idSemestre);
        setArea(area);
        setIdArea(idArea);
    }
    
    
    public int getIdMateria(){
        return idMateria.get();
    }
    public void setIdMateria(int idMateria){
        this.idMateria.set(idMateria);
    }
    
    public String getNombreUnidad(){
        return nombreUnidad.get();
    }
    public void setNombreUnidad(String nombreUnidad){
        this.nombreUnidad.set(nombreUnidad);
    }
    
    public int getIdSemestre(){
        return idSemestre.get();
    }
    public void setIdSemestre(int idSemestre){
        this.idSemestre.set(idSemestre);
    }
    
    public String getArea(){
        return area.get();
    }
    public void setArea(String area){
        this.area.set(area);
    }
    
    public int getIdArea(){
        return idArea.get();
    }
    public void setIdArea(int idArea){
        this.idArea.set(idArea);
    }
}
