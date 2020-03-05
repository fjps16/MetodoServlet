
package Servlet;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class InsertarServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter out = response.getWriter();
            String nombre = request.getParameter("nombre");
            double precio = Double.parseDouble(request.getParameter("precio"));
            String color = request.getParameter("color");
            int anio = Integer.parseInt(request.getParameter("anio"));
            //
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/chevrolet?user=root&password=mysqladmin";
            Connection connect = DriverManager.getConnection(url);
            
                      
            String query = "SELECT MAX(id_vehiculo) + 1 AS new_id FROM vehiculo";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            int idProd = 0;
            while(resultSet.next()) {
                idProd = resultSet.getInt("new_id");
            }   
            if(idProd == 0 ) {
                idProd = 1;
            }
            
            query = "INSERT INTO vehiculo VALUES (?,?,?,?,?)";
            PreparedStatement ps = connect.prepareStatement(query);
            
            ps.setInt(1,idProd);
            ps.setString(2,nombre);
            ps.setDouble(3,precio);
            ps.setString(4,color);
            ps.setInt(5, anio);
            ps.executeUpdate();
            
            query = "SELECT precio * 3.5 AS Precio_Dolar FROM vehiculo WHERE id_vehiculo = "+idProd;
            //Statement statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            Double precio_dolar = 0.0;
            while(resultSet.next()) {
                precio_dolar = resultSet.getDouble("precio_dolar");
            }
            
            JsonObject gson = new JsonObject();
            gson.addProperty("mensaje", "VEHICULO REGISTRADO");
            gson.addProperty("id_Prod", idProd);
            gson.addProperty("precio_dolar", precio_dolar);
            gson.addProperty("Saludo", "CHAU");
            out.print(gson.toString()); //ENVIAR RESPUESTA JS               
        }catch(Exception e) {
            System.err.println(e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
