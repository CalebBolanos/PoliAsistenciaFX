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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Caleb
 */
public class ConsultarDatos {
    
    public ObservableList<Persona> obtenerDatosProfesores(){
         ObservableList<Persona> datos = FXCollections.observableArrayList();
         baseDeDatos base = new baseDeDatos();
        try{
            base.conectar();
            ResultSet resultado = base.ejecuta("select * from vwTrabajadores where (idTipo = 3 and idPersona > 0);");
            Persona personax;
            while(resultado.next()){
                personax = new Persona();
                personax.setNumero(resultado.getString("numTrabajador"));
                personax.setNombre(resultado.getString("nombre"));
                personax.setPaterno(resultado.getString("paterno"));
                personax.setMaterno(resultado.getString("materno"));
                personax.setGenero(resultado.getString("genero"));
                datos.add(personax);
                personax = null;
            }
            base.cierraConexion();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
         return datos;
    }
    
    public int obtenerIdPersonaProfesor(String numero){
        int idPersona = 0;
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spIdPersonaProfesor('"+numero+"');");
            if(rs.next()){
                if(rs.getString("msj").equals("ok")){
                    idPersona = rs.getInt("idPer");
                }
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idPersona;
    }
    
    public String[] obtenerPersona(String numero){
        String datos[] = new String[7];
        String genero="";
        baseDeDatos bd = new baseDeDatos();
        try{
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spTraerDatosProf('" + numero + "', 'Pol');");
            while(rs.next()){
                datos[0] = rs.getString("existe");
                datos[1] = rs.getString("nom");
                datos[2] = rs.getString("pat");
                datos[3] = rs.getString("mat");
                genero = rs.getString("gen");
                switch(genero){
                    case "masculino":
                        datos[4] = "1";
                        break;
                    case "femenino":
                        datos[4] = "2";
                        break;
                    case "otro":
                        datos[4] = "3";
                        break;
                    default:
                        datos[4] = "0";
                }
                datos[5] = rs.getString("bol");
                datos[6] = rs.getString("fec");
            }
            bd.cierraConexion();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return datos;
    }
    
    public ObservableList<Huella> obtenerHuellasDigitales(int idPer){
        ObservableList<Huella> huellas = FXCollections.observableArrayList();
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spHuellasPersona("+idPer+");");
            Huella huellax;
            int i = 1; 
            while(rs.next()){
                huellax = new Huella();
                huellax.setNombre("Huella "+i);
                huellax.setId(Integer.toString(rs.getInt("idHuella")));
                huellas.add(huellax);
                huellax = null;
                i++;
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return huellas;
    }
    
    public boolean borrarHuella(int idHuella){
        boolean huellaBorrada = false;
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spBorrarHuella("+idHuella+");");
            if(rs.next()){
                if(rs.getString("msj").equals("ok")){
                    huellaBorrada = true;
                }
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return huellaBorrada;
    }
    
    public int editarDatosProfesores(String numeroAntiguo, String numeroNuevo, String nombre, String paterno, String materno, String fecha){
        int id = 0;
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spEditaProfesor('"+numeroAntiguo+"', '"+numeroNuevo+"', '"+nombre+"', '"+paterno+"', '"+materno+"', '"+fecha+"');");
            if(rs.next()){
                switch (rs.getString("msj")){
                    case "ok":
                        id = rs.getInt("idP");
                        break;
                    case "Ya existe un profesor con ese número de trabajador":
                        id = -1;
                        break;
                    case "No se encuentra la persona":
                        id = -2;
                        break;
                }
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
}
