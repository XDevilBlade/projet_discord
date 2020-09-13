/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author remy
 */
@WebServlet("/ServletAmis")
public class ServletAmis extends HttpServlet {

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            
            String unElementDeLaRequete = reader.readLine();
            int indexEgale = unElementDeLaRequete.indexOf("=");
            
           
            
            
            
            request.setAttribute("pseudo", unElementDeLaRequete.substring(indexEgale+1, unElementDeLaRequete.length()));//j'affecte à l'identifiant pseudo , le nom de l'utilisateur qui se trouve dans la pageDiscussion
            request.getRequestDispatcher("/affichageBtnAmis.jsp").forward(request, response);//j'envoie le pseudo à mon fichier jsp et le contenu du fichier jsp sera renvoyé au client. 
           
            
           
            
            
        }
        
    }

    
}
