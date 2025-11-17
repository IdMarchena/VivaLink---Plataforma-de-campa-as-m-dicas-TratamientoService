/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;

import java.sql.SQLException;
import dao.TratamientoDao;
import dao.TratamientoDaoMongo;
import dao.TratamientoDaoMysql;
import dao.TratamientoDaoPostgre;
import dao.conexion.DatabaseConnection;
/**
 *
 * @author Usuario
 */
public class TratamientoDaoFactory {
        public static TratamientoDao dao(String tipoDao) throws SQLException{
                DatabaseConnection conn = DatabaseConnectionFactory.connection(tipoDao);
                switch (tipoDao.toLowerCase()) {
            case "postgres" -> {
                return new TratamientoDaoPostgre(conn);
            }
            case "mysql" -> {
                return new TratamientoDaoMysql(conn);
            }
            case "mongo" -> {
                return new TratamientoDaoMongo(conn);
            }
            default -> throw new AssertionError();
        }
    }
    
}
