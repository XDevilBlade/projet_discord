<%-- 
    Document   : formateAffichageMessage
    Created on : Apr 21, 2018, 9:38:06 PM
    Author     : remy
--%>

<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="javaClassServer.Messages"%>
<%@page import="javaClassServer.Messages"%>
<%@page import="javaClassServer.AccessMessagesJPA"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

        <% 
                AccessMessagesJPA lAccessMessagesJPA = new AccessMessagesJPA();
                String unTypeChannel = (String)request.getAttribute("typeChannel");
                try
                {
                    if(unTypeChannel.equals("TI") || unTypeChannel.equals("L3"))//Si je souhaite afficher tous les messages stockés dans la BD et qui avaient été écrits dans le channel TI ou L3,
                    {               //dans le channel TI ou L3
                            
                            
                            List<Messages> listMessages = lAccessMessagesJPA.getMessagesDUnChannel(unTypeChannel);
                            for(Messages unMessage : listMessages)
                            {
                                if(unMessage.getContenue().indexOf("www.youtube.com")!=-1)
                                {
                                    int indexEgale = unMessage.getContenue().indexOf("v=");
                                    String idVideo = unMessage.getContenue().substring(indexEgale+2,unMessage.getContenue().length());

                                    out.println("<li>"+unMessage.getNomExpediteur()+" : <iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/"+idVideo+"\" frameborder=\"0\" allowfullscreen></iframe></li>");
                                }
                                else
                                {
                                    out.println("<li>"+unMessage.getNomExpediteur()+" : "+unMessage.getContenue()+"</li>" );
                                }
                                

                            }

                    }
                }
                catch(java.lang.NullPointerException e)//Sinon je souhaite afficher tous les messages stockés dans la BD et qui avaient été écrits dans une conversation,
                {   //dans une conversation privée
                    
                    HashMap<Integer,Messages> listMessageNonTrier = new HashMap<Integer,Messages>();
                    
                    String toutLeMessage = (String)request.getAttribute("toutLeMessage"); 
                    int indexNomAmi= toutLeMessage.indexOf("&unNomAmi=");
                    int indexChannel = toutLeMessage.indexOf("&channel");
                    //je découpe tout le contenu du message
                    String pseudoExpediteurAvecSonIdentifiant=toutLeMessage.substring(0, indexNomAmi);//je récupère l'expéditeur du message puisqu'on travaille avec un String fait à partir d'un objet JSON
                    String pseudoDestinataireAvecSonIdentifiant=toutLeMessage.substring(indexNomAmi, indexChannel);//je récupère le destinataire avec son identifiant puisqu'on travaille avec un String fait à partir d'un objet JSON
                    String pseudoExpediteur=pseudoExpediteurAvecSonIdentifiant.substring(pseudoExpediteurAvecSonIdentifiant.indexOf("=")+1,pseudoExpediteurAvecSonIdentifiant.length());//récupèration de l'expéditeur du message
                    String pseudoDestinataire = pseudoDestinataireAvecSonIdentifiant.substring(pseudoDestinataireAvecSonIdentifiant.indexOf("=")+1,pseudoDestinataireAvecSonIdentifiant.length());//récupèration du destinataire du message
                    
                    //récupération des messages que l'expéditeur a reçu et envoyé
                    List<Messages> listMessagesSortant = lAccessMessagesJPA.getMessagesDUneConversation(pseudoExpediteur,pseudoDestinataire );
                    List<Messages> listMessagesEntrant = lAccessMessagesJPA.getMessagesDUneConversation(pseudoDestinataire, pseudoExpediteur);
                    
                    int nbMessagesEntrant = listMessagesEntrant.size();
                    int nbMessagesSortant = listMessagesSortant.size();
                    
                    
                        //on rassemble tous les messages dans un seul tableau
                        for(int i=0; i<nbMessagesEntrant; i++)
                        {
                            listMessagesSortant.add(listMessagesEntrant.get(i));
                        }
                        
                        //on met tous ces messages dans une HashMap, dont la clé c'est l'id du message, chaque id est unique
                        for(Messages unMessage : listMessagesSortant)
                        {
                           listMessageNonTrier.put(unMessage.getId(), unMessage);
                        }
                        
                        //on crée une variable de type comparator qui va mettre les clés dans l'ordre croissant
                        Comparator<Integer> keyComparator = new Comparator<Integer>(){
                            @Override
                            public int compare(Integer int1, Integer int2){
                               return int1.compareTo(int2);
                            }
                         };
                        
                        //on met tous les messages triés dans une map
                        Map<Integer, Messages> sortedOnKeysMap = new TreeMap(keyComparator);
                        sortedOnKeysMap.putAll(listMessageNonTrier);
                        Set cles = sortedOnKeysMap.keySet();
                        Iterator it = cles.iterator();
                      
                        //on parcourt la variable de type map, pour récupérer et afficher chaque message
                        while (it.hasNext()){
                            Object cle = it.next(); // tu peux typer plus finement ici
                            Messages valeur = sortedOnKeysMap.get(cle); // tu peux typer plus finement ici
                            if(valeur.getContenue().indexOf("www.youtube.com")!=-1)
                            {
                                int indexEgale = valeur.getContenue().indexOf("v=");
                                String idVideo = valeur.getContenue().substring(indexEgale+2,valeur.getContenue().length());
                                
                                    out.println("<li>"+valeur.getNomExpediteur()+" : <iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/"+idVideo+"\" frameborder=\"0\" allowfullscreen></iframe></li>");
                            }
                            else
                            {
                                 out.println("<li>"+valeur.getNomExpediteur()+" : "+valeur.getContenue()+"</li>");
                            }
                        }
                }
        %>        
   