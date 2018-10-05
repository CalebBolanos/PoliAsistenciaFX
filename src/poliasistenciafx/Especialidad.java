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
public class Especialidad {
    private final SimpleStringProperty nombre = new SimpleStringProperty("");
    private final SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    
    public Especialidad(){
        this("", 0);
    }
    
    public Especialidad(String nombre, int id){
        setNombre(nombre);
        setId(id);
    }
    
    
    public String getNombre() {
        return nombre.get();
    }
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }
    
    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }
}
