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
/**
 *
 * @author Usuario
 */
public class TratamientoDaoFactory {
        public static TratamientoDao dao(String tipoDao) throws SQLException{
                switch (tipoDao.toLowerCase()) {
            case "postgre":
                return new TratamientoDaoPostgre(tipoDao);
            case "mysql":
                return new TratamientoDaoMysql(tipoDao);
            case "mongo":
                return new TratamientoDaoMongo(tipoDao);               
            default:
                throw new AssertionError();
        }
    }
    
}
