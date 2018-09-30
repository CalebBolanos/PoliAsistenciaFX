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

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Caleb
 */
public final class Huella {
    private final SimpleStringProperty nombre = new SimpleStringProperty("");
    private final SimpleStringProperty id = new SimpleStringProperty("");
    
    public Huella(){
        this("", "");
    }
    
    public Huella(String nombre, String id){
        setNombre(nombre);
        setId(id);
    }
    
    
    public String getNombre() {
        return nombre.get();
    }
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }
    
    public String getId() {
        return id.get();
    }
    public void setId(String id) {
        this.id.set(id);
    }
}
