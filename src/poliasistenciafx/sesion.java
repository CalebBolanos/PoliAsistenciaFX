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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Caleb
 */
public class sesion {
    private static int idSesion, tipo;
    private static String numT, cont;
    
    public sesion(){
        
    }
    public sesion(int idSes, int idT){
        this.idSesion = idSes;
        this.tipo = idT;
    }
    public int getID(){
        return idSesion;
    }
    public void cerrar(){
        idSesion = 0;
    }
    public String getNumT(){
        return numT;
    }
    public String getCont(){
        return cont;
    }
    public int getTipo(){
        return tipo;
    }
    public int[] iniciaSesion(String usr, String psw){
        int id[] = new int[2];
        numT = usr;
        cont = psw;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spValidaUsr('" + usr + "', '" + psw + "');");
            while(rs.next()){
                id[0] = rs.getInt("idP");
                id[1] = rs.getInt("idT");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        idSesion = id[0];
        tipo = id[1];
        return id;
    }
    
    public boolean checaHuella(){
        boolean ret = false;
        try{
            baseDeDatos bd = new baseDeDatos();
            bd.conectar();
            ResultSet rs = bd.ejecuta("call spPersonaConHuella(" + idSesion + ");");
            while(rs.next())
                ret = rs.getInt("ret")>0;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ret;
    }
    
}
