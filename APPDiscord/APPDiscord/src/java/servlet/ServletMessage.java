/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;
import javaClassServer.AccessMessagesJPA;
import javaClassServer.Messages;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Rémy
 */
@WebServlet("/ServletMessage")
public class ServletMessage extends HttpServlet { // Permets de retourner au client tous les messages d'un channel ou d'une conversation privée

  
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("je passe dans le get");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            
            String unElementDeLaRequete = reader.readLine();
            int indexEgale = unElementDeLaRequete.indexOf("=");
           
            if(indexEgale==7)//si le numéro de la position du égale est égal à 7 sa veut dire que j'ai récupérer soit le mot TI ou L3 par 
            { //au format de mon JSON
                request.setAttribute("typeChannel", unElementDeLaRequete.substring(indexEgale+1, unElementDeLaRequete.length()));
                request.getRequestDispatcher("/formateAffichageMessage.jsp").forward(request, response);
            }
            else//sinon j'ai récupérer un JSON qui demande entre guillemet à la servlet de retourner tout les messages d'une conversation privée
            {
                request.setAttribute("toutLeMessage", unElementDeLaRequete);//donc j'envoie à mon fichier jsp tout le fichier json
                request.getRequestDispatcher("/formateAffichageMessage.jsp").forward(request, response);
            }
            
            //
           
            
           
            
            
        }
    }

  

}
