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
    
    public ObservableList<Unidad> obtenerUnidadesImpartidas(int idPer){
        ObservableList<Unidad> unidades = FXCollections.observableArrayList();
        Unidad unidadx;
        int idUnidad = 0;
        boolean recopilarDatos = false;
        String lunes = "---", martes = "---", miercoles = "---", jueves = "---", viernes = "---", nombreUnidad = "";
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("select * from vwunidadeshorarios where idProfesor = "+idPer+";");
            while(rs.next()){
                if(idUnidad == rs.getInt("idUnidad")){
                    recopilarDatos = true;
                    switch(rs.getInt("idDia")){
                        case 1://lunes
                            lunes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 2://martes
                            martes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 3://miercoles
                            miercoles = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 4://jueves
                            jueves = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 5://viernes
                            viernes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;  
                    }
                }
                else{
                    if(recopilarDatos){
                        unidadx = new Unidad(idUnidad, nombreUnidad, lunes, martes, miercoles, jueves, viernes);
                        unidades.add(unidadx);
                        lunes = "---";
                        martes = "---";
                        miercoles = "---"; jueves = "---";
                        viernes = "---"; 
                        nombreUnidad = "";
                        unidadx = null;
                        recopilarDatos = false;
                    }
                    idUnidad = rs.getInt("idUnidad");
                    nombreUnidad = rs.getString("materia");
                    switch(rs.getInt("idDia")){
                        case 1://lunes
                            lunes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 2://martes
                            martes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 3://miercoles
                            miercoles = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 4://jueves
                            jueves = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 5://viernes
                            viernes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;  
                    }
                    recopilarDatos = true;
                    
                }
                
            }
            if(recopilarDatos){
                unidadx = new Unidad(idUnidad, nombreUnidad, lunes, martes, miercoles, jueves, viernes);
                unidades.add(unidadx);
                lunes = "---";
                martes = "---";
                miercoles = "---"; jueves = "---";
                viernes = "---"; 
                nombreUnidad = "";
                unidadx = null;
                recopilarDatos = false;
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unidades;
    }
    
    public boolean agregarUnidadProfesor(int idUnidad, String numeroTrabajador){
        boolean unidadAgregada = false;
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spGuardaUnidadesProfesor("+idUnidad+", '"+numeroTrabajador+"');");
            if(rs.next()){
                unidadAgregada = rs.getString("msj").equals("ok");
            }
            bd.cierraConexion();
        } 
        catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unidadAgregada;
    }
    
    public boolean borrarUnidadProfesor(int idUnidad, String numeroTrabajador){
        boolean unidadBorrada = false;
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spBorraProfesorHorario("+idUnidad+", '"+numeroTrabajador+"');");
            if(rs.next()){
                unidadBorrada = rs.getString("msj").equals("ok");
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unidadBorrada;
    }
    
    public ObservableList<Unidad> obtenerUnidadesDisponibles(){
        ObservableList<Unidad> unidades = FXCollections.observableArrayList();
        Unidad unidadx;
        int idUnidad = 0;
        boolean recopilarDatos = false;
        String lunes = "---", martes = "---", miercoles = "---", jueves = "---", viernes = "---", nombreUnidad = "";
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("select * from vwunidadeshorarios where idProfesor < 0;");
            while(rs.next()){
                if(idUnidad == rs.getInt("idUnidad")){
                    recopilarDatos = true;
                    switch(rs.getInt("idDia")){
                        case 1://lunes
                            lunes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 2://martes
                            martes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 3://miercoles
                            miercoles = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 4://jueves
                            jueves = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 5://viernes
                            viernes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;  
                    }
                }
                else{
                    if(recopilarDatos){
                        unidadx = new Unidad(idUnidad, nombreUnidad, lunes, martes, miercoles, jueves, viernes);
                        unidades.add(unidadx);
                        lunes = "---";
                        martes = "---";
                        miercoles = "---"; jueves = "---";
                        viernes = "---"; 
                        nombreUnidad = "";
                        unidadx = null;
                        recopilarDatos = false;
                    }
                    idUnidad = rs.getInt("idUnidad");
                    nombreUnidad = rs.getString("materia");
                    switch(rs.getInt("idDia")){
                        case 1://lunes
                            lunes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 2://martes
                            martes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 3://miercoles
                            miercoles = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 4://jueves
                            jueves = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 5://viernes
                            viernes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;  
                    }
                    recopilarDatos = true;
                    
                }
                
            }
            if(recopilarDatos){
                unidadx = new Unidad(idUnidad, nombreUnidad, lunes, martes, miercoles, jueves, viernes);
                unidades.add(unidadx);
                lunes = "---";
                martes = "---";
                miercoles = "---"; jueves = "---";
                viernes = "---"; 
                nombreUnidad = "";
                unidadx = null;
                recopilarDatos = false;
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unidades;
    }
    
    public ObservableList<Especialidad> obtenerEspecialidades(){
        ObservableList<Especialidad> especialidad = FXCollections.observableArrayList();
        Especialidad especialidadx;
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("select * from areas;");
            while(rs.next()){
                especialidadx = new Especialidad();
                especialidadx.setNombre(rs.getString("area"));
                especialidadx.setId(rs.getInt("idArea"));
                especialidad.add(especialidadx);
                especialidadx = null;
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return especialidad;
    }
    
    public boolean crearNuevoGrupo(int idEspecialidad, String nombre, int semestre, int turno){
        boolean guardado = false;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spNuevoGrupo('"+nombre+"', "+semestre+", "+idEspecialidad+", "+turno+");");
            if(rs.next()){
                guardado = rs.getString("msj").equals("ok");
                System.out.println(rs.getString("idN"));
            }
            bd.cierraConexion();
        }catch(SQLException ex){
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return guardado;
    }
    
    public ObservableList<Grupo> obtenerGrupos(){
        ObservableList<Grupo> grupos = FXCollections.observableArrayList();
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("select * from vwgrupos where idGrupo > 0;");
            Grupo grupox;
            while(rs.next()){
                grupox = new Grupo();
                grupox.setIdGrupo(rs.getInt("idGrupo"));
                grupox.setGrupo(rs.getString("grupo"));
                grupox.setSemestre(rs.getInt("semestre"));
                grupox.setTurno(rs.getString("turmo"));
                grupox.setEspecialidad(rs.getString("area"));
                grupox.setIdTurno(rs.getInt("idTurno"));
                grupox.setIdEspecialidad(rs.getInt("idArea"));
                grupos.add(grupox);
                grupox = null;
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grupos;
    }
    
    public ObservableList<UnidadProfesor> obtenerUnidadesConProfesoresDisponibles(int semestre){
        ObservableList<UnidadProfesor> unidades = FXCollections.observableArrayList();
        UnidadProfesor unidadx;
        int idUnidad = 0;
        boolean recopilarDatos = false;
        String lunes = "---", martes = "---", miercoles = "---", jueves = "---", viernes = "---", nombreUnidad = "", profesor ="";
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("select * from vwunidadeshorarios where idGrupo <0 and idProfesor > 0 and semestre = "+semestre+";");
            while(rs.next()){
                if(idUnidad == rs.getInt("idUnidad")){
                    recopilarDatos = true;
                    switch(rs.getInt("idDia")){
                        case 1://lunes
                            lunes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 2://martes
                            martes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 3://miercoles
                            miercoles = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 4://jueves
                            jueves = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 5://viernes
                            viernes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;  
                    }
                }
                else{
                    if(recopilarDatos){
                        unidadx = new UnidadProfesor(idUnidad, nombreUnidad, profesor, lunes, martes, miercoles, jueves, viernes);
                        unidades.add(unidadx);
                        lunes = "---";
                        martes = "---";
                        miercoles = "---"; jueves = "---";
                        viernes = "---"; 
                        nombreUnidad = "";
                        profesor = "";
                        unidadx = null;
                        recopilarDatos = false;
                    }
                    idUnidad = rs.getInt("idUnidad");
                    nombreUnidad = rs.getString("materia");
                    profesor = rs.getString("Nombre");
                    switch(rs.getInt("idDia")){
                        case 1://lunes
                            lunes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 2://martes
                            martes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 3://miercoles
                            miercoles = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 4://jueves
                            jueves = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 5://viernes
                            viernes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;  
                    }
                    recopilarDatos = true;
                    
                }
                
            }
            if(recopilarDatos){
                unidadx = new UnidadProfesor(idUnidad, nombreUnidad, profesor, lunes, martes, miercoles, jueves, viernes);
                unidades.add(unidadx);
                lunes = "---";
                martes = "---";
                miercoles = "---"; jueves = "---";
                viernes = "---"; 
                nombreUnidad = "";
                profesor = "";
                unidadx = null;
                recopilarDatos = false;
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unidades;
    }
    
    public ObservableList<UnidadProfesor> obtenerProfesoresInscritosEnGrupo(String nombreGrupo){
        ObservableList<UnidadProfesor> unidades = FXCollections.observableArrayList();
        UnidadProfesor unidadx;
        int idUnidad = 0;
        boolean recopilarDatos = false;
        String lunes = "---", martes = "---", miercoles = "---", jueves = "---", viernes = "---", nombreUnidad = "", profesor ="";
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("select * from vwunidadeshorarios where grupo = '"+nombreGrupo+"';");
            while(rs.next()){
                if(idUnidad == rs.getInt("idUnidad")){
                    recopilarDatos = true;
                    switch(rs.getInt("idDia")){
                        case 1://lunes
                            lunes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 2://martes
                            martes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 3://miercoles
                            miercoles = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 4://jueves
                            jueves = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 5://viernes
                            viernes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;  
                    }
                }
                else{
                    if(recopilarDatos){
                        unidadx = new UnidadProfesor(idUnidad, nombreUnidad, profesor, lunes, martes, miercoles, jueves, viernes);
                        unidades.add(unidadx);
                        lunes = "---";
                        martes = "---";
                        miercoles = "---"; jueves = "---";
                        viernes = "---"; 
                        nombreUnidad = "";
                        profesor = "";
                        unidadx = null;
                        recopilarDatos = false;
                    }
                    idUnidad = rs.getInt("idUnidad");
                    nombreUnidad = rs.getString("materia");
                    profesor = rs.getString("Nombre");
                    switch(rs.getInt("idDia")){
                        case 1://lunes
                            lunes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 2://martes
                            martes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 3://miercoles
                            miercoles = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 4://jueves
                            jueves = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 5://viernes
                            viernes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;  
                    }
                    recopilarDatos = true;
                    
                }
                
            }
            if(recopilarDatos){
                unidadx = new UnidadProfesor(idUnidad, nombreUnidad, profesor, lunes, martes, miercoles, jueves, viernes);
                unidades.add(unidadx);
                lunes = "---";
                martes = "---";
                miercoles = "---"; jueves = "---";
                viernes = "---"; 
                nombreUnidad = "";
                profesor = "";
                unidadx = null;
                recopilarDatos = false;
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unidades;
    }
    
    public boolean agregarProfesorGrupo(int idUnidad, String nombreGrupo){
        boolean agregado = false;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spGuardaUnidadesGrupo("+idUnidad+", '"+nombreGrupo+"');");
            if(rs.next()){
                agregado = rs.getString("msj").equals("ok");
            }
            bd.cierraConexion();
        }catch(SQLException ex){
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return agregado;
    }
    
    public boolean borrarProfesorGrupo(int idUnidad, String nombreGrupo){
        boolean borrado = false;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spBorraGrupoHorario("+idUnidad+", '"+nombreGrupo+"');");
            if(rs.next()){
                borrado = rs.getString("msj").equals("ok");
            }
            bd.cierraConexion();
        }catch(SQLException ex){
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return borrado;
    }
    
    public boolean editarGrupo(String antiguoNombre, String nuevoNombre, int semestre, int idEspecialidad, int idTurno){
        boolean editado = false;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spEditaGrupo('"+antiguoNombre+"', '"+nuevoNombre+"', "+semestre+", "+idEspecialidad+", "+idTurno+");");
            if(rs.next()){
                editado = rs.getString("msj").equals("ok");
            }
            bd.cierraConexion();
        }catch(SQLException ex){
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return editado;
    }
    
    public boolean CrearUnidad(int idEspecialidad, String nombreMateria, int idSemestre){
        boolean creado = false;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spNuevaMateria("+idEspecialidad+", '"+nombreMateria+"', "+idSemestre+");");
            if(rs.next()){
                creado = rs.getString("msj").equals("Registro con exito");
            }
            bd.cierraConexion();
        }catch(SQLException ex){
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return creado;
    }
    
    public ObservableList<Materia> obtenerDirectorioUnidades(){
        ObservableList<Materia> materias = FXCollections.observableArrayList();
        Materia materiax;
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("select * from vwmaterias;");
            while(rs.next()){
                materiax = new Materia();
                materiax.setIdMateria(rs.getInt("idMateria"));
                materiax.setNombreUnidad(rs.getString("materia"));
                materiax.setIdSemestre(rs.getInt("semestre"));
                materiax.setArea(rs.getString("area"));
                materiax.setIdArea(rs.getInt("idArea"));
                materias.add(materiax);
                materiax = null;
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return materias;
    }
    
     public boolean borrarUnidadAprendizaje(String nombreUnidad){
        boolean borrado = false;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spBorrarMateria('"+nombreUnidad+"');");
            if(rs.next()){
                borrado = rs.getString("msj").equals("ok");
            }
            bd.cierraConexion();
        }catch(SQLException ex){
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return borrado;
    }
     
    public int crearUnidadHorario(String nombreUnidad, int cupo){
        int idUnidadHorario = 0;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spNuevaUnidad('"+nombreUnidad+"', "+cupo+");");
            if(rs.next()){
                if(rs.getString("msj").equals("Todo bien")){
                    idUnidadHorario = rs.getInt("idN");
                }else{
                    idUnidadHorario = -1;
                }
            }
            bd.cierraConexion();
        }catch(SQLException ex){
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idUnidadHorario;
    }
    
    public boolean guardarUnidadHorario(int idUnidadHorario, int horaInicio, int horaFinal, int dia){
        boolean borrado = false;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spUnidadHorario("+idUnidadHorario+", "+horaInicio+", "+horaFinal+", "+dia+");");
            if(rs.next()){
                borrado = rs.getString("msj").equals("Registro con exito");
            }
            bd.cierraConexion();
        }catch(SQLException ex){
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return borrado;
    }
    
    public ObservableList<UnidadProfesor> obtenerUnidadesConHorario(){
        ObservableList<UnidadProfesor> unidades = FXCollections.observableArrayList();
        UnidadProfesor unidadx;
        int idUnidad = 0;
        boolean recopilarDatos = false;
        String lunes = "---", martes = "---", miercoles = "---", jueves = "---", viernes = "---", nombreUnidad = "", profesor ="";
        baseDeDatos bd = new baseDeDatos();
        try {
            bd.conectar();
            ResultSet rs = bd.ejecuta("select * from vwunidadeshorarios;");
            while(rs.next()){
                if(idUnidad == rs.getInt("idUnidad")){
                    recopilarDatos = true;
                    switch(rs.getInt("idDia")){
                        case 1://lunes
                            lunes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 2://martes
                            martes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 3://miercoles
                            miercoles = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 4://jueves
                            jueves = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 5://viernes
                            viernes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;  
                    }
                }
                else{
                    if(recopilarDatos){
                        unidadx = new UnidadProfesor(idUnidad, nombreUnidad, profesor, lunes, martes, miercoles, jueves, viernes);
                        unidades.add(unidadx);
                        lunes = "---";
                        martes = "---";
                        miercoles = "---"; jueves = "---";
                        viernes = "---"; 
                        nombreUnidad = "";
                        profesor = "";
                        unidadx = null;
                        recopilarDatos = false;
                    }
                    idUnidad = rs.getInt("idUnidad");
                    nombreUnidad = rs.getString("materia");
                    profesor = rs.getString("Nombre");
                    switch(rs.getInt("idDia")){
                        case 1://lunes
                            lunes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 2://martes
                            martes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 3://miercoles
                            miercoles = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 4://jueves
                            jueves = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;
                        case 5://viernes
                            viernes = evaluarHora(rs.getInt("idHorarioI")) +" - "+ evaluarHora(rs.getInt("idHorarioF"));
                            break;  
                    }
                    recopilarDatos = true;
                }
            }
            if(recopilarDatos){
                unidadx = new UnidadProfesor(idUnidad, nombreUnidad, profesor, lunes, martes, miercoles, jueves, viernes);
                unidades.add(unidadx);
                lunes = "---";
                martes = "---";
                miercoles = "---"; jueves = "---";
                viernes = "---"; 
                nombreUnidad = "";
                profesor = "";
                unidadx = null;
                recopilarDatos = false;
            }
            bd.cierraConexion();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unidades;
    }
    
    public boolean borrarUnidadEspecifica(int idUnidad){
        boolean borrado = false;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spBorraUnidadEspecifica("+idUnidad+");");
            if(rs.next()){
                borrado = rs.getString("msj").equals("ok");
            }
            bd.cierraConexion();
        }catch(SQLException ex){
            Logger.getLogger(ConsultarDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return borrado;
    }
    
    public String evaluarHora(int valorHora) {
        String hora;
        switch (valorHora) {
            case 1:
                hora = "07:00";
                break;
            case 2:
                hora = "08:00";
                break;
            case 3:
                hora = "09:00";
                break;
            case 4:
                hora = "10:00";
                break;
            case 5:
                hora = "11:00";
                break;
            case 6:
                hora = "12:00";
                break;
            case 7:
                hora = "13:00";
                break;
            case 8:
                hora = "14:00";
                break;
            case 9:
                hora = "15:00";
                break;
            case 10:
                hora = "16:00";
                break;
            case 11:
                hora = "17:00";
                break;
            case 12:
                hora = "18:00";
                break;
            case 13:
                hora = "19:00";
                break;
            case 14:
                hora = "20:00";
                break;
            case 15:
                hora = "21:00";
                break;
            default:
                hora = "Valor invalido";
                break;
        }
        return hora;
    }
    
}
