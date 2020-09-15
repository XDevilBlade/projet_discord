/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClassServer;

/**
 *
 * @author rtaillandier
 */
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
 
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.simple.JSONObject;

 
/** 
 * @ServerEndpoint gives the relative name for the end point
 * This will be accessed via ws://localhost:8080/EchoChamber/echo
 * Where "localhost" is the address of the host,
 * "EchoChamber" is the name of the package
 * and "echo" is the address to access this class from the server
 */
@ServerEndpoint("/echo") 
public class EchoServer {
    /**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */
    
    
    @Inject
    private SessionHandler sessionHandler;

    public SessionHandler getSessionHandler() {
        return sessionHandler;
    }
    
    
    
    @OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection"); 
        try {
            this.getSessionHandler().addSession(session);
            System.out.println(session);
            session.getBasicRemote().sendText("Connection Established");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
 
    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException{
        
            JsonReader reader = Json.createReader(new StringReader(message)); // réception du message en String
            JsonObject json = reader.readObject(); //transformation du message en objetJSON
            System.out.println(json);
            AccessUtilisateurJPA lAccessUtilisateurJPA = new AccessUtilisateurJPA();
            
            
            
            
            
            if(json.getString("nomPage").equals("pageAuthentification")){//si mon serveur reçoit un message venant du client qui se trouve sur la page d'authentification
                Utilisateurs unUtilisateur = lAccessUtilisateurJPA.getUnUtilisateurParSonNomUtilisateurEtSonMDP(json.getString("pseudo"), json.getString("mdp"));//j'essaye de récupérer un utilisateur avec les identifiants stockés dans le JSON
                message=null;
                
                if(unUtilisateur==null)//si le serveur n'a pas réussi à récupérer un utilisateur
                {
                    message="authentification failed";
                }
                else//si oui
                {
                    message="authentification succeed";
                    this.sessionHandler.addSessionInChannelTI(session); //ajout de la session de l'utilisateur dans les tableaux qui permettront de renvoyer les messages dans les bons channels
                    
                    this.sessionHandler.addSessionInChannelL3(session);
                    
                    this.sessionHandler.addCorrespondanceNomUtilisateurAvecSession(session, json.getString("pseudo"));
                }
              session.getBasicRemote().sendText(message);//renvoi d'une réponse vers l'utilisateur pour qu'il sache si les informations qui la rentrée dans
                                                        //le formulaire d'authentification, s'ils sont bons ou pas
            }
            else
            {
                if(json.getString("nomPage").equals("choixChannel"))// partie inutile vu que je le fais lorsque la personne s'authentifie
                {
                    
                    if(json.getString("channel").equals("TI"))
                    {
                        this.sessionHandler.addSessionInChannelTI(session);
                    }
                    else if(json.getString("channel").equals("L3"))
                    {
                        this.sessionHandler.addSessionInChannelL3(session);
                    }
                    else if(json.getString("channel").equals("ConversationAmi"))
                    {
                        this.sessionHandler.addCorrespondanceNomUtilisateurAvecSession(session, json.getString("pseudo"));
                        System.out.println("session add friend");
                    }
                    
                    
                }
                else if(json.getString("nomPage").equals("pageDiscussion"))//gestion d'arriver de messages venant de channel
                {
                    if(json.getString("channel").equals("TI"))//si le serveur reçoit un message venant du channel TI
                    {
                        AccessMessagesJPA lAccessMessagesJPA = new AccessMessagesJPA();
                        
                        long longNBElementTableMessages = lAccessMessagesJPA.getNbElement();
                        int intNBElementTablesMessages = (int)longNBElementTableMessages; //création de l'identifiant du message
                        
                        Messages unMessage = new Messages();//création du message
                        unMessage.setId(intNBElementTablesMessages);
                        unMessage.setContenue(json.getString("message"));
                        unMessage.setNomRoom(json.getString("channel"));
                        unMessage.setNomExpediteur(json.getString("pseudo"));
                        lAccessMessagesJPA.persist(unMessage);//stockage du message dans la BD
                        
                         JSONObject obj = new JSONObject();//création de l'objet JSON qu'on va envoyer au client
                         obj.put("pseudo",json.getString("pseudo") );
                         obj.put("message",json.getString("message") );
                         obj.put("channel", json.getString("channel"));
                         StringWriter out = new StringWriter();
                         obj.writeJSONString(out);

                         String jsonText = out.toString();
                         System.out.print(jsonText);
                        this.getSessionHandler().sendToAllConnectedSessions(jsonText,session,json.getString("channel"),"","");//envoit de l'objet JSON à tout ce qui sont à l'intérieur ou non du channel TI 
                    }
                    else if(json.getString("channel").equals("L3"))//si le serveur reçoit un message venant du channel L3
                    {
                        AccessMessagesJPA lAccessMessagesJPA = new AccessMessagesJPA();
                        long longNBElementTableMessages = lAccessMessagesJPA.getNbElement();
                        int intNBElementTablesMessages = (int)longNBElementTableMessages; 
                        
                        Messages unMessage = new Messages();
                        unMessage.setId(intNBElementTablesMessages);
                        unMessage.setContenue(json.getString("message"));
                        unMessage.setNomRoom(json.getString("channel"));
                        unMessage.setNomExpediteur(json.getString("pseudo"));
                        lAccessMessagesJPA.persist(unMessage);
                        
                        JSONObject obj = new JSONObject();
                        obj.put("pseudo",json.getString("pseudo") );
                         obj.put("message",json.getString("message") );
                         obj.put("channel", json.getString("channel"));
                         StringWriter out = new StringWriter();
                         obj.writeJSONString(out);

                         String jsonText = out.toString();
                         System.out.print(jsonText);
                        this.getSessionHandler().sendToAllConnectedSessions(jsonText,session,json.getString("channel"),"","");
                    }
                    else if(json.getString("channel").equals("ConversationAmi"))//si le serveur reçoit un message privée
                    {
                        AccessMessagesJPA lAccessMessagesJPA = new AccessMessagesJPA();
                        long longNBElementTableMessages = lAccessMessagesJPA.getNbElement();
                        int intNBElementTablesMessages = (int)longNBElementTableMessages; 
                        
                        Messages unMessage = new Messages();
                        unMessage.setId(intNBElementTablesMessages);
                        unMessage.setContenue(json.getString("message"));
                        unMessage.setNomRoom(json.getString("channel"));
                        unMessage.setNomDestinataire(json.getString("nomAmi"));
                        unMessage.setNomExpediteur(json.getString("pseudo"));
                        lAccessMessagesJPA.persist(unMessage);
                        
                       
                        JSONObject obj = new JSONObject();
                        obj.put("pseudo",json.getString("pseudo") );
                        obj.put("destinataire",json.getString("nomAmi"));
                         obj.put("message",json.getString("message") );
                         obj.put("channel", json.getString("channel"));
                         StringWriter out = new StringWriter();
                         obj.writeJSONString(out);

                         String jsonText = out.toString();
                         System.out.print(jsonText);
                        this.getSessionHandler().sendToAllConnectedSessions(jsonText,session,json.getString("channel"),json.getString("pseudo"),json.getString("nomAmi"));
                    }
                }
                else if(json.getString("nomPage").equals("inscription"))//si le serveur reçoit une demande d'inscription
                {
                    if(!(lAccessUtilisateurJPA.isExist(json.getString("pseudo"))))//si l'utilisateur ne c'était pas déjà inscrit avec ce pseudo
                    {
                        Utilisateurs unUtilisateur = new Utilisateurs();
                        long longNBElementTableUtilisateurs = lAccessUtilisateurJPA.getNbElement();
                        int intNBElementTablesUtilisateurs = (int)longNBElementTableUtilisateurs; 
                        unUtilisateur.setId(intNBElementTablesUtilisateurs);
                        unUtilisateur.setMdpUtilisateur(json.getString("mdp"));
                        unUtilisateur.setPseudoUtilisateur(json.getString("pseudo"));
                        lAccessUtilisateurJPA.persist(unUtilisateur);
                        
                        message="inscription succeed";//le serveur envoie ce message au client
                        
                    }
                    else //sinon le serveur envoie un message au client disant que ce pseudo existe déjà dans la BD 
                    {
                        message="Ce pseudo existe déjà";
                    }
                    session.getBasicRemote().sendText(message);  
                }
                
            }
            //
            
            
            
        
    }
 
    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){
        System.out.println("Session " +session.getId()+" has ended");
        this.getSessionHandler().deleteSession(session);
    }
}