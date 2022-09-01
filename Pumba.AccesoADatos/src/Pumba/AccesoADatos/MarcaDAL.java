
package Pumba.AccesoADatos;
import java.util.ArrayList;
import java.sql.*;
import Pumba.entidadesdenegocio.*;

public class MarcaDAL {

   
    static String ObtenerCampos(){
        return "m.Id,m.Nombre";
    }
    
    private static String ObtenerSelect(Marca pMarca){
        String sql;
        sql = "SELECT ";
        if (pMarca.getTop_aux() > 0 ) {
            sql += " TOP "+pMarca.getTop_aux() + " ";
        } 
        sql += (ObtenerCampos()+ " FROM Marcas m");
        return sql;
    }
    
    private static String AgregarOrderBy(Marca pMarca) {
        String sql = " ORDER BY m.Id DESC";
        return sql;
    }
    
    private static boolean ExisteMarca(Marca pMarca) throws Exception {
        boolean existe = false;
        ArrayList<Marca> marcas = new ArrayList();
        try (Connection conn = ComunDB.ObtenerConexion();) { 
            String sql = ObtenerSelect(pMarca);  
            sql += " WHERE m.Id<>? AND m.Nombre=?";
            try (PreparedStatement ps = ComunDB.CreatePreparedStatement(conn,sql);){
                ps.setInt(1, pMarca.getId()); 
                ps.setString(2, pMarca.getNombre());  
                ObtenerDatos(ps, marcas);
                ps.close(); 
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close(); 
        }
        catch (SQLException ex) {
            throw ex; 
        }
        if (!marcas.isEmpty()) { 
            Marca marca;
            marca = marcas.get(0); 
            if (marca.getId() > 0 && marca.getNombre().equals(pMarca.getNombre())) {
                existe = true;
            }
        }
        return existe;
    }
   
    public static int Crear(Marca pMarca) throws Exception {
        int result;
        String sql;
        boolean existe = ExisteMarca(pMarca);
        if (existe == false) {
            try (Connection conn = ComunDB.ObtenerConexion();) { 
                sql = "INSERT INTO Marcas(Nombre) VALUES(?)";
                try (PreparedStatement ps = ComunDB.CreatePreparedStatement(conn,sql);) {
                    ps.setString(1, pMarca.getNombre());
                    result = ps.executeUpdate(); 
                    ps.close(); 
                } catch (SQLException ex) {
                    throw ex;
                }
                conn.close();
            } 
            catch (SQLException ex) {
                throw ex; 
            }
        } else {
            result = 0;
            throw new RuntimeException("Marca ya existe");
        }
        return result; 
    }

    public static int Modificar(Marca pMarca) throws Exception {
        int result;
        String sql;
        boolean existe = ExisteMarca(pMarca);
        if (existe == false) {
            try (Connection conn = ComunDB.ObtenerConexion();) { 
                sql = "UPDATE Marcas SET Nombre=? WHERE Id=?";
                try (PreparedStatement ps = ComunDB.CreatePreparedStatement(conn, sql);) { 
                    ps.setString(1, pMarca.getNombre()); 
                    result = ps.executeUpdate(); 
                    ps.close(); 
                } catch (SQLException ex) {
                    throw ex; 
                }
                conn.close(); 
            } 
            catch (SQLException ex) {
                throw ex; 
            }
        } else {
            result = 0;
            throw new RuntimeException("Marca ya existe"); 
        }
        return result; 
    }
    
    public static int Eliminar(Marca pMarca) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.ObtenerConexion();) { 
            sql = "DELETE FROM Marcas WHERE Id=?"; 
            try (PreparedStatement ps = ComunDB.CreatePreparedStatement(conn,sql);) { 
                ps.setInt(1, pMarca.getId());
                result = ps.executeUpdate(); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex; 
        }
        return result;
    }
    
    static int AsignarDatosResultSet(Marca pMarca, ResultSet pResultSet, int pIndex) 
            throws Exception {
        pIndex++;
        pMarca.setId(pResultSet.getInt(pIndex)); 
        pIndex++;
        pMarca.setNombre(pResultSet.getString(pIndex));
        pIndex++;
        return pIndex;
    }
     
    private static void ObtenerDatos(PreparedStatement pPS, ArrayList<Marca> pMarca) throws Exception {
        try (ResultSet resultSet = ComunDB.ObtenerResultSet(pPS);) {  
            while (resultSet.next()) { 
                Marca marca = new Marca();               
                AsignarDatosResultSet(marca, resultSet, 0);
                pMarca.add(marca); 
            }
            resultSet.close(); 
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    public static Marca ObtenerPorId(Marca pMarca) throws Exception {
        Marca marca = new Marca();
        ArrayList<Marca> marcas = new ArrayList();
        try (Connection conn = ComunDB.ObtenerConexion();) {
            String sql = ObtenerSelect(pMarca);
            sql += " WHERE m.Id=?";
            try (PreparedStatement ps = ComunDB.CreatePreparedStatement(conn, sql);) { 
                ps.setInt(1, pMarca.getId()); 
                ObtenerDatos(ps, marcas); 
                ps.close();
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close(); 
        }
        catch (SQLException ex) {
            throw ex; 
        }
        if (!marcas.isEmpty()) { 
            marca = marcas.get(0); 
        }
        return marca; 
    }
    
    public static ArrayList<Marca> ObtenerTodos() throws Exception {
        ArrayList<Marca> marcas;
        marcas = new ArrayList<>();
        try (Connection conn = ComunDB.ObtenerConexion();) { 
            String sql = ObtenerSelect(new Marca()); 
            sql += AgregarOrderBy(new Marca());
            try (PreparedStatement ps = ComunDB.CreatePreparedStatement(conn, sql);) { 
                ObtenerDatos(ps, marcas); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close(); 
        }
        catch (SQLException ex) {
            throw ex; 
        }
        return marcas; 
    }

    static void QuerySelect(Marca pMarca, ComunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement();
        if (pMarca.getId() > 0) { 
            pUtilQuery.AgregarWhereAnd(" m.Id=? "); 
            if (statement != null) {
                statement.setInt(pUtilQuery.getNumWhere(), pMarca.getId());
            }
        }
        if (pMarca.getNombre() != null && pMarca.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" m.Nombre LIKE ? "); 
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pMarca.getNombre() + "%");
            }
        }  
    }
    
    public static ArrayList<Marca> Buscar(Marca pMarca) throws Exception {
        ArrayList<Marca> marcas = new ArrayList();
        try (Connection conn = ComunDB.ObtenerConexion();) {
            String sql = ObtenerSelect(pMarca); 
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0);
            QuerySelect(pMarca, utilQuery); 
            sql = utilQuery.getSQL();
            sql += AgregarOrderBy(pMarca); 
            try (PreparedStatement ps = ComunDB.CreatePreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                QuerySelect(pMarca, utilQuery); 
                ObtenerDatos(ps, marcas); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close(); 
        } 
        catch (SQLException ex) {
            throw ex; 
        }
        return marcas; 
    }

   

}