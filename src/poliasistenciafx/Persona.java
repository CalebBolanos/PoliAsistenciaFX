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
public final class Persona {
    
    private final SimpleStringProperty numero = new SimpleStringProperty("");
    private final SimpleStringProperty nombre = new SimpleStringProperty("");
    private final SimpleStringProperty paterno = new SimpleStringProperty("");
    private final SimpleStringProperty materno = new SimpleStringProperty("");
    private final SimpleStringProperty genero = new SimpleStringProperty("");  
    
    public Persona() {
        this("", "", "", "", "");
    }
    
    public Persona(String numero, String nombre, String paterno, String materno, String genero) {
           setNumero(numero);
           setNombre(nombre);
           setPaterno(paterno);
           setMaterno(materno);
           setGenero(genero);
    }
 
    public String getNumero() {
        return numero.get();
    }
    public void setNumero(String numero) {
        this.numero.set(numero);
    }
        
    public String getNombre() {
        return nombre.get();
    }
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }
    
    public String getPaterno() {
        return paterno.get();
    }
    public void setPaterno(String paterno) {
        this.paterno.set(paterno);
    }
    
    public String getMaterno() {
        return materno.get();
    }
    public void setMaterno(String materno) {
        this.materno.set(materno);
    }
    
    public String getGenero() {
        return genero.get();
    }
    public void setGenero(String genero) {
        this.genero.set(genero);
    }
    
}
