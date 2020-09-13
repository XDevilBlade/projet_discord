<%-- 
    Document   : affichageBtnAmis
    Created on : Apr 21, 2018, 11:28:31 PM
    Author     : remy
--%>

<%@page import="javaClassServer.Utilisateurs"%>
<%@page import="java.util.List"%>
<%@page import="javaClassServer.AccessUtilisateurJPA"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

    <body>
        <%
          AccessUtilisateurJPA lAccessUtilisateurJPA = new AccessUtilisateurJPA();  
          String unPseudo = (String)request.getAttribute("pseudo");//on stocke la valeur de l'identifiant pseudo, la valeur qu'à envoyé ma servlet à mon fichier JSP 
          List<Utilisateurs> listUtilisateurs = lAccessUtilisateurJPA.getUtilisateurs();//récupération des utilisateurs stockés dans la BD
          
          for(Utilisateurs unUtilisateur : listUtilisateurs)
          {
              if(!(unUtilisateur.getPseudoUtilisateur().equals(unPseudo)))//si le pseudo récupéré dans le tableau n'est pas égal au pseudo que la servlet à transmis à mon fichier jsp,  
              {                                             //je crée un bouton à partir du pseudo récupéré du tableau
                  String unNomUtilisateur = unUtilisateur.getPseudoUtilisateur();
                  
                out.println("<button id=\"btnAmis"+unNomUtilisateur+"\">"+unNomUtilisateur+"</button><br>");
              }
              
          }
          
            
        %>    
    </body>

