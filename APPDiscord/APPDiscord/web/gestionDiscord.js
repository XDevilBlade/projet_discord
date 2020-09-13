
            "use strict";
            
            var webSocket;
            var unPseudo;
            var nomUnAmi;
            var ouSuisJe;
           
            /*procédure qui gère l'affichage des notifications desktop*/
            function notifyMe(unJSON) {
                const title = 'Message reçu';
                const options = {
                  body: "Message de "+unJSON.pseudo+".\n Contenue du message : "+unJSON.message+".\n Channel : "+unJSON.channel+""
                };
                // Voyons si le navigateur supporte les notifications
                if (!("Notification" in window)) {
                  alert("Ce navigateur ne supporte pas les notifications desktop");
                }

                // Voyons si l'utilisateur est OK pour recevoir des notifications
                else if (Notification.permission === "granted") {
                  // Si c'est ok, créons une notification
                   var notification = new Notification(title,options);
                   setTimeout(function() {
                                        notification.close();
                                        }, 2000);
                }

                // Sinon, nous avons besoin de la permission de l'utilisateur
                else if (Notification.permission !== 'denied') {
                  Notification.requestPermission(function (permission) {

                    // Quelque soit la réponse de l'utilisateur, nous nous assurons de stocker cette information
                    if(!('permission' in Notification)) {
                      Notification.permission = permission;
                    }

                    // Si l'utilisateur est OK, on crée une notification
                    if (permission === "granted") {
                      var notification = new Notification(title,options);
                      setTimeout(function() {
                                        notification.close();
                                        }, 2000);
                    }
                  });
                }

                // Comme ça, si l'utlisateur a refusé toute notification, et que vous respectez ce choix,
                // il n'y a pas besoin de l'ennuyer à nouveau.
              }
            
            /*envoi d'un objetJSON sous la forme d'un string vers le serveur*/
            function send(JSONString){
                webSocket.send(JSONString);
            }
            
            /*fermeture socket*/
            function closeSocket(){
                webSocket.close();
            }
 
            function writeResponse(text){
                messages.innerHTML += "<br/>" + text;
            }
            
            /*fonction qui permet d'ouvrir une websocket et d'ouvrir une session du coté serveur*/
            function openSocket(){
                // Ensures only one connection is open at a time
                if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
                   console.log("WebSocket is already opened.");
                    return;
                }
                // Create a new instance of the websocket
                webSocket = new WebSocket("ws://localhost:8080/APPDiscord/echo");
                
                /**
                 * Binds functions to the listeners for the websocket.
                 */
                /*une liste d'attente qui gère les ouvertures de websocket*/
                webSocket.onopen = function(event){
                    // For reasons I can't determine, onopen gets called twice
                    // and the first time event.data is undefined.
                    // Leave a comment if you know the answer.
                    if(event.data === undefined)
                        return;
 
                    console.log(event.data);
                };
 
                /*liste d'attente qui gère les ouvertures et arrivés de messages venant du serveur endpoint*/
                webSocket.onmessage = function(event){
                    
                    /*si l'utilisateur n'a pas bien renseigné les bons identifiants, le serveur endpoint renvoit un message "authentification failed" et on 
                     *on affiche dans une div le message du coté client*/
                    if(event.data==="authentification failed")
                    {
                         $( "#reponseAuthentification" ).append( "<h5>authentification failed</h5>" );
                    } /*sinon l'authentification est bonne et vide toute l'interface d'authentification et il charge le contenu du fichier pageAccueil dans une
                     * div qui permettra à l'utilisateur de pouvoir choisir dans quel channel il veut discuter*/
                    else if(event.data==="authentification succeed")
                    {
                        $( "#reponseAuthentification" ).append( "<h5>authentification succeed</h5>" );
                        $("#reponseAuthentification").empty();
                        $("#partieJeu").empty();
                        $("#EcrivezA").empty();
                         $("#formulaireAuthentification").empty();
                         $("#formulaireAuthentification").load("pageAccueil.html");
                         $('#unPseudo').val('');
                     $('#unMDP').val('');
                     $('#unPseudoNouvelleUtilisateur').val('');
                     $('#unPseudoNouvelleMDP').val('');
                         
                        $.ajax({ //envoie d'une requete en ajax vers une servlet me permettant d'afficher les boutons avec les noms
                                 //des autres utilisateurs stockés dans la BD
                            url: 'http://localhost:8080/APPDiscord/ServletAmis',
                            type: 'POST',
                            data: { pseudo: unPseudo} ,
                            contentType: 'application/json; charset=utf-8',
                            success: function (response) { 
                              $( "#zoneAmis" ).append( response );//affiche le contenu d'un fichier JSP dans une div avec  pour chaque bouton,
                                                                  //un nom d'utilisateur stocké dans la BD autre que le nom de celui qui c'est authentifié
                            },
                            error: function (errorThrown) {
                              //your error code
                              alert("not success :"+errorThrown);
                            }                         
                          }); 
                    }
                    else if(event.data==="inscription succeed") //si l'inscription de l'utilisateur c'est bien déroulée, il nettoie 
                                                               //la div qui contenait tout le formulaire d'inscription
                    {   $( "#loginExiste" ).empty();
                        $( "#loginExiste" ).append( "<h5>Inscription réussi</h5>" );   
                        $( "#loginExiste" ).empty();
                        $("#partieJeu").empty();
                        $("#EcrivezA").empty();
                        $("#formulaireAuthentification").empty();
                        $("#formulaireAuthentification").load("PageAuthentification.html"); 
                        $('#unPseudo').val('');
                        $('#unMDP').val('');
                        $('#unPseudoNouvelleUtilisateur').val('');
                        $('#unPseudoNouvelleMDP').val('');
                    }
                    else if(event.data==="Ce pseudo existe déjà")//partie verification que le login demandé par l'utilisateur exite ou non dans la BD
                    {   $( "#loginExiste" ).empty();
                        $( "#loginExiste" ).append( "<h5>Ce pseudo existe déjà</h5>" );
                    }
                    else //partie gestion des réceptions des messages venant du serveur endpoint lorsqu'on se trouve dans une room ou dans une discussion avec une personne
                    {
                        var objString = String(event.data);
                        var objJSON = JSON.parse(objString);
                        if(objJSON.pseudo!=unPseudo) //affichage d'une notification si une autre que vous, avait envoyé un message dans une rooms ou à vous
                                    {
                                        notifyMe(objJSON);
                                        setTimeout(function() {
                                        $.bootstrapGrowl("Message de "+objJSON.pseudo+". Contenue du message : "+objJSON.message+". Channel : "+objJSON.channel+"",{
                                            type: 'info',
                                            align: 'right',
                                            width: 'auto',
                                            allow_dismiss: false
                                        });
                                        }, 750);
                                    }
                        if(objString.includes("channel\":\"TI"))//si c'est un message qui a été envoyé à partir du room TI
                        {
                                if(ouSuisJe==="TI")//Si je me trouve dans le room TI, j'affiche le message dans la div qui affiche les messages du channel TI
                                {
                                    if(objJSON.message.includes("www.youtube.com"))//si dans le message contient un lien youtube, il m'affiche la vidéo dans une frame
                                    {
                                        var indexEgale = objJSON.message.indexOf("v=");
                                        var idVideo = objJSON.message.substring(indexEgale+2,objJSON.message.length); //récupération de l'ID de la vidéo youtube pour pouvoir appliquer un embed dessus pour pouvoir l'afficher dans l'iframe

                                        $("#reponseMessageTI").append("<li>"+objJSON.pseudo+" : <iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/"+idVideo+"\" frameborder=\"0\" allowfullscreen></iframe></li>");
                                    }
                                    else
                                    {
                                       $("#reponseMessageTI").append("<li>"+objJSON.pseudo+" : "+objJSON.message+"</li>");//sinon il m'affiche un simple message nomExpediteur : contenuMessage 
                                    }
                                }                            
                        }
                        else
                        {
                            if(objString.includes("channel\":\"L3")) //Si je reçois un message du channel L3
                            {
                                
                                console.log(objJSON);
                                if(ouSuisJe==="L3")//Si je me trouve dans le room L3, j'affiche le message dans la div qui affiche les messages du channel L3
                                {
                                     if(objJSON.message.includes("www.youtube.com"))
                                        {
                                            var indexEgale = objJSON.message.indexOf("v=");
                                            var idVideo = objJSON.message.substring(indexEgale+2,objJSON.message.length);

                                            $("#reponseMessageL3").append("<li>"+objJSON.pseudo+" : <iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/"+idVideo+"\" frameborder=\"0\" allowfullscreen></iframe></li>");
                                        }
                                        else
                                        {
                                             $("#reponseMessageL3").append("<li>"+objJSON.pseudo+" : "+objJSON.message+"</li>"); 
                                        }
                                }
                            }
                            else if(objString.includes("channel\":\"ConversationAmi"))//si je reçois un message d'un ami
                            {
                                    
                                    console.log(objJSON);
                                   if((ouSuisJe==objJSON.pseudo && unPseudo==objJSON.destinataire) || (ouSuisJe==objJSON.destinataire && unPseudo==objJSON.pseudo))//si c'est un message sortant ou entrant, un message privé avec cet ami,
                                                                                                                    //je l'affiche dans la div, à la suite des messages que j'aurai récupérer dans la BD à travers une servlet et fichier JSP
                                   {
                                        if(objJSON.message.includes("www.youtube.com"))
                                        {
                                            var indexEgale = objJSON.message.indexOf("v=");
                                            var idVideo = objJSON.message.substring(indexEgale+2,objJSON.message.length);

                                            $("#elementServlet").append("<li>"+objJSON.pseudo+" : <iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/"+idVideo+"\" frameborder=\"0\" allowfullscreen></iframe></li>");
                                        }
                                        else
                                        {
                                           $("#elementServlet").append("<li>"+objJSON.pseudo+" : "+objJSON.message+"</li>"); 
                                        }
                                   }                                    
                            }
                        }
                    }
                }
                webSocket.onclose = function(event){
                    console.log("Connection closed");
                };
        }
            function writeResponse(text){
                messages.innerHTML += "<br/>" + text;
            }
            
            var channel;
            window.onload =function(){//dès que la page web c'est affichée
                
                openSocket();//ouverture d'une websocket, donc d'une session du coté serveur endpoint
                 $(document).on('click','#btnValider',function(){ //si je clique sur le bouton valider de la page d'authentification, il envoit 
                                                                 //mes identifiants au serveur endpoint pour vérifier s'ils sont bons.
                    unPseudo = $('#unPseudo').val();
                    var unMDP = $('#unMDP').val();
                    var tabJSON = {"pseudo":unPseudo,"mdp":unMDP,"nomPage":"pageAuthentification"};
                    var tabJSONString=JSON.stringify(tabJSON);//envoie vers le serveur d'un objet json qui est enfaite un String
                    console.log(tabJSONString);
                    
                    
                    send(tabJSONString);
                    
                    
                    //closeSocket();
                    
                });
                $(document).on('click','#btnChannelTI',function(){//si j'ai cliqué sur un bouton dans le menu pour parler à un ami
                    $("#zoneAmis").empty();
                    $.ajax({ //il réactualise la liste des boutons qui se trouve dans la zone du menu, ami, pour afficher, si un nouvel utilisateur 
                            //c'est un inscrit au discord, pour pouvoir discuter avec lui
                            url: 'http://localhost:8080/APPDiscord/ServletAmis',//permet de faire appel à un fichier JSP qui va afficher les boutons qui me permettront d'accéder à un 
                                                                            //channel qui me permettra de parler à un ami en privée et directement.
                            type: 'POST',
                            data: { pseudo: unPseudo} ,//envoie d'une donnée en JSON permettant de dire au fichier JSP que j'appelerai via la servlet,
                                                       //de ne pas afficher un bouton avec mon pseudo dessus.
                            contentType: 'application/json; charset=utf-8',
                            success: function (response) {
                              
                              $( "#zoneAmis" ).append( response );
                            },
                            error: function (errorThrown) {
                              //your error code
                              alert("not success :"+errorThrown);
                            }                         
                          }); 
                          
                    channel = "TI";
                    ouSuisJe=channel;
                    var tabJSON = {"pseudo":unPseudo,"nomPage":"choixChannel","channel":"TI"};//envoie d'un fichier JSON au server endpoint, pour lui dire, je me trouve dans le
                                                                        //channel TI, tu me feras telles instructions du coté serveur.
                    var tabJSONString=JSON.stringify(tabJSON);
                    console.log(tabJSONString);
                    
                    
                    send(tabJSONString);
                    
                     $("#reponseMessageTI").empty();
                     $("#reponseMessageL3").empty();
                     $("#EcrivezA").empty();
                     $("#partieDiscussion").empty(); 
                     $("#elementServlet").empty(); 
                     $("#partieJeu").empty();
                     $("#partieDiscussion").load("pageDiscussion.html"); //il m'affiche dans une div l'interface qui me permettra d'écrire des messages
                     
                     $("#partieJeu").css({ zIndex: -1 });//gestion des différentes div pour dire cette div est en arrière plan et il ne m'empèchera pas d'appuyer sur tel bouton 
                     $("#EcrivezA").css({zIndex: 0});
                     $("#messageinput").css({ zIndex: 0 });
                     $("#toutlesmessages").css({ zIndex: 0 });
                     $("#partieDiscussion").css({ zIndex: 0 });
                     $("#EcrivezA").append("<h4>Ecrivez dans le channel TI : </h4>");
                    $.ajax({ //il m'affiche les messages qui ont déjà été postés dans le channel TI
                        url: 'http://localhost:8080/APPDiscord/ServletMessage',
                        type: 'POST',
                        data: { channel: "TI"} ,
                        contentType: 'application/json; charset=utf-8',
                        success: function (response) {
                          
                          
                          $( "#elementServlet" ).append( response );
                        },
                        error: function (errorThrown) {
                          //your error code
                          alert("not success :"+errorThrown);
                        }                         
                      });
                    
                   
                    
                });
                $(document).on('click','#btnChannelL3',function(){
                    $("#zoneAmis").empty();
                    $.ajax({ //il me met à jour les boutons amis dans mon menu
                            url: 'http://localhost:8080/APPDiscord/ServletAmis',
                            type: 'POST',
                            data: { pseudo: unPseudo} ,
                            contentType: 'application/json; charset=utf-8',
                            success: function (response) {
                              
                              $( "#zoneAmis" ).append( response );
                            },
                            error: function (errorThrown) {
                              //your error code
                              alert("not success :"+errorThrown);
                            }                         
                          }); 
                    channel = "L3";
                    ouSuisJe=channel;
                    var tabJSON = {"pseudo":unPseudo,"nomPage":"choixChannel","channel":"L3"};//envoie d'un fichier JSON au server endpoint, pour lui dire, je me trouve dans le
                                                                        //channel L3, tu me feras telles instructions du coté serveur.
                    var tabJSONString=JSON.stringify(tabJSON);
                    send(tabJSONString);
                    $("#reponseMessageTI").empty();
                    $("#reponseMessageL3").empty();
                    $("#partieDiscussion").empty(); 
                    $("#elementServlet").empty(); 
                    $("#EcrivezA").empty();
                    $("#partieJeu").empty();
                    $("#partieDiscussion").load("pageDiscussion.html"); 
                    $("#partieJeu").css({ zIndex: -1 });
                    $("#EcrivezA").css({zIndex: 0});
                    $("#messageinput").css({ zIndex: 0 });
                    $("#toutlesmessages").css({ zIndex: 0 });
                    $("#partieDiscussion").css({ zIndex: 0 });
                    $("#EcrivezA").append("<h4>Ecrivez dans le channel L3 : </h4>");
                    $.ajax({//il m'affiche les messages qui ont déjà été postés dans le channel L3
                        url: 'http://localhost:8080/APPDiscord/ServletMessage',
                        type: 'POST',
                        data: { channel: "L3"} ,
                        contentType: 'application/json; charset=utf-8',
                        success: function (response) {
                          
                          $( "#elementServlet" ).append( response );
                        },
                        error: function (errorThrown) {
                          //your error code
                          alert("not success :"+errorThrown);
                        }                         
                      });
                    
                });
                $(document).on('click','#btnSend',function(){//gestion du bouton d'envoi de message
                    if(channel==="L3")//si j'envoie un message à partir du channel L3
                    {
                        var unMessage = $('#messageinput').val();
                        var tabJSON = {"pseudo":unPseudo,"nomPage":"pageDiscussion","channel":"L3","message":unMessage};
                        var tabJSONString=JSON.stringify(tabJSON);
                        send(tabJSONString);//j'envoie mon message au serveur endpoint
                    }
                    else if(channel==="TI")//si j'envoie un message à partir du channel TI
                    {
                        var unMessage = $('#messageinput').val();
                        var tabJSON = {"pseudo":unPseudo,"nomPage":"pageDiscussion","channel":"TI","message":unMessage};
                        var tabJSONString=JSON.stringify(tabJSON);
                        send(tabJSONString);
                    }
                    else //si j'envoi un message à un ami
                    {
                        var unMessage = $('#messageinput').val();
                        var tabJSON = {"pseudo":unPseudo,"nomPage":"pageDiscussion","nomAmi":nomUnAmi,"channel":"ConversationAmi","message":unMessage}; //j'informe à qui je l'envoie
                        var tabJSONString=JSON.stringify(tabJSON);
                        send(tabJSONString);
                    }
                });
                $(document).on('click','#btnRetour',function(){//inutile puisque le bouton n'existe plus
                    $("#reponseMessageTI").empty();
                    $("#EcrivezA").empty();
                    $("#reponseMessageL3").empty();
                    $("#formulaireAuthentification").empty();
                    $("#elementServlet").empty();
                    $("#partieJeu").empty();
                    $("#formulaireAuthentification").load("pageAccueil.html");
                     $.ajax({
                            url: 'http://localhost:8080/APPDiscord/ServletAmis',
                            type: 'POST',
                            data: { pseudo: unPseudo} ,
                            contentType: 'application/json; charset=utf-8',
                            success: function (response) {
                              
                              $( "#zoneAmis" ).append( response );
                            },
                            error: function (errorThrown) {
                              //your error code
                              alert("not success :"+errorThrown);
                            }                         
                          }); 
                });
                $(document).on('click','[id^=btnAmis]',function(){//gestion des boutons de la zone ami
                    $("#zoneAmis").empty();
                    $.ajax({
                            url: 'http://localhost:8080/APPDiscord/ServletAmis',
                            type: 'POST',
                            data: { pseudo: unPseudo} ,
                            contentType: 'application/json; charset=utf-8',
                            success: function (response) {
                              
                              $( "#zoneAmis" ).append( response );
                            },
                            error: function (errorThrown) {
                              //your error code
                              alert("not success :"+errorThrown);
                            }                         
                          }); 
                    channel = "ConversationAmi";
                    var unIdBtnAmiString = String(this.id);//récupération de l'id du bouton
                    
                    var indexS = unIdBtnAmiString.indexOf("s");
                    var nomAmi = unIdBtnAmiString.substring(indexS+1,unIdBtnAmiString.length);//récupération du nom de la personne à qui on va envoyer des messages
                    nomUnAmi = nomAmi;
                    var tabJSON = {"pseudo":unPseudo,"nomAmi":nomAmi,"nomPage":"choixChannel","channel":"ConversationAmi"};// on informe au serveur endpoint qu'on va aller dans un salon où l'on va parler à une personne bien précise
                    var tabJSONString=JSON.stringify(tabJSON);
                    ouSuisJe=nomAmi;
                    send(tabJSONString);
                    $("#reponseMessageTI").empty();
                    $("#reponseMessageL3").empty();
                    $("#partieDiscussion").empty(); 
                    $("#elementServlet").empty(); 
                    $("#partieJeu").empty();
                    $("#EcrivezA").empty();
                    $("#partieDiscussion").load("pageDiscussion.html"); 
                    $("#EcrivezA").css({zIndex: 0});
                    $("#partieJeu").css({ zIndex: -1 });
                    $("#messageinput").css({ zIndex: 0 });
                    $("#toutlesmessages").css({ zIndex: 0 });
                    $("#partieDiscussion").css({ zIndex: 0 });
                    $("#EcrivezA").append("<h4>Ecrivez à "+ouSuisJe+" : </h4>");
                    $.ajax({//affichage des messages qu'on a reçu ou envoyé à cet ami
                        url: 'http://localhost:8080/APPDiscord/ServletMessage',
                        type: 'POST',
                        data: { pseudo:unPseudo,unNomAmi:nomAmi,channel: "ConversationAmi"} ,
                        contentType: 'application/json; charset=utf-8',
                        success: function (response) {
                          
                          
                          $( "#elementServlet" ).append( response );//div où j'affiche les messages stockés dans la BD. Cependant j'affiche les messages spécifiques au channel
                        },
                        error: function (errorThrown) {
                          //your error code
                          alert("not success :"+errorThrown);
                        }                         
                      });
                });
                
                $(document).on('click','#btnJeu',function(){//gestion du bouton pour afficher un jeu
                    $("#zoneAmis").empty();
                    $.ajax({
                            url: 'http://localhost:8080/APPDiscord/ServletAmis',
                            type: 'POST',
                            data: { pseudo: unPseudo} ,
                            contentType: 'application/json; charset=utf-8',
                            success: function (response) {
                              
                              $( "#zoneAmis" ).append( response );
                            },
                            error: function (errorThrown) {
                              //your error code
                              alert("not success :"+errorThrown);
                            }                         
                          }); 
                    ouSuisJe="PageJeu";
                    $("#reponseMessageTI").empty();
                    $("#EcrivezA").empty();
                     $("#reponseMessageL3").empty();
                     $("#partieDiscussion").empty(); 
                     $("#elementServlet").empty(); 
                     if(!$("#fenetreJeu").length)
                     {
                        $("#partieJeu").append("<iframe id=\"fenetreJeu\" width=\"800\" height=\"650\" src=\"http://localhost:8080/APPDiscord/jquery-picture-memory-game/index.html\"></iframe>"); //affichage d'une iframe contenant un jeu en HTML5
                     }
                     $("#partieJeu").css({ zIndex: 0 });
                     $("#EcrivezA").css({zIndex: -1});
                     $("#messageinput").css({ zIndex: -1 });
                     $("#toutlesmessages").css({ zIndex: -1 });
                     $("#partieDiscussion").css({ zIndex: -1 });
                    
                });
                
                $(document).on('click','#redirectionInscription',function(){//affichage du formulaire d'inscription 
                     $("#partieDiscussion").empty(); 
                     $("#EcrivezA").empty();
                     $("#elementServlet").empty(); 
                     $("#reponseAuthentification").empty();
                     $("#formulaireAuthentification").empty();
                     $("#formulaireAuthentification").load("pageInscription.html"); 
                });
                
                $(document).on('click','#btnValiderInscription',function(){//gestion du bouton de validation de l'inscription
                    
                    var unPseudoPourUnNouvelleUtilisateur = $('#unPseudoNouvelleUtilisateur').val();
                    var unMDP = $('#unMDPNouvelleUtilisateur').val();
                    if(unPseudoPourUnNouvelleUtilisateur.length==0 && unMDP.length===0)//si l'utilisateur n'a pas bien renseigné son pseudo et son mdp
                    {
                        $( "#loginExiste" ).empty();
                        $( "#loginExiste" ).append( "<h5>Vous avez oublié de renseigner le champ pseudo.</h5><br><h5>Vous avez oublié de renseigner le champ mot de passe</h5><br>" );
                        
                    }
                    else if(unPseudoPourUnNouvelleUtilisateur.length==0)//si l'utilisateur n'a pas bien renseigné son pseudo
                    {
                        $( "#loginExiste" ).empty();
                        $( "#loginExiste" ).append( "<h5>Vous avez oublié de renseigner le champ pseudo.</h5>");
                    }
                    else if(unMDP.length==0)//si l'utilisateur n'a pas bien renseigné son mdp
                    {
                        $( "#loginExiste" ).empty();
                         $( "#loginExiste" ).append( "<h5>Vous avez oublié de renseigner le champ mot de passe.</h5>");
                    }
                    else
                    {
                        var tabJSON = {"pseudo":unPseudoPourUnNouvelleUtilisateur,"mdp":unMDP,"nomPage":"inscription"};
                        var tabJSONString=JSON.stringify(tabJSON);
                        send(tabJSONString);
                        console.log(tabJSONString);
                    }                    
                });
                
                $(document).on('click','#btnRetourAuthenti',function(){//gestion du bouton pour retourner au formulaire d'authentification si l'on se trouvait sur la page d'inscription
                     $('#unPseudo').val('');
                     $('#unMDP').val('');
                     $('#unPseudoNouvelleUtilisateur').val('');
                     $('#unPseudoNouvelleMDP').val('');
                     $( "#loginExiste" ).empty();
                     $("#EcrivezA").empty();
                     $("#partieJeu").empty();
                     $("#formulaireAuthentification").empty();
                     $("#formulaireAuthentification").load("PageAuthentification.html"); 
                });
                
                $(document).on('click','#btnDeco',function(){//bouton déconnexion
                    
                     $( "#loginExiste" ).empty();
                     $('#unPseudo').val('');
                     $('#unMDP').val('');
                     $('#unPseudoNouvelleUtilisateur').val('');
                     $('#unPseudoNouvelleMDP').val('');
                     $("#partieJeu").empty();
                     $("#formulaireAuthentification").empty();
                     $("#reponseMessageTI").empty();
                     $("#EcrivezA").empty();
                     $("#reponseMessageL3").empty();
                     $("#partieDiscussion").empty(); 
                     $("#elementServlet").empty();
                     $("#partieJeu").css({ zIndex: -1 });
                     $("#EcrivezA").css({zIndex: -1});
                     $("#messageinput").css({ zIndex: -1 });
                     $("#toutlesmessages").css({ zIndex: -1 });
                     $("#partieDiscussion").css({ zIndex: -1 });
                     $("#formulaireAuthentification").load("PageAuthentification.html"); 

                     closeSocket();//fermeture de la socket
                });
            }
