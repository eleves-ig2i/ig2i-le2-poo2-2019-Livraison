/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planification;


import java.io.*;
import reseau.*;
import planning.*;
import java.util.*;
import mesExceptions.*;


/**
 *
 * @author user
 */
public class Routage {
    
    private Depot depot;
    
    /**
     * Les clients à livrer.
     */
    private Set<Client> mesClients;
    
    private Planning planningLivraison;
    
    private int capaciteCamion;

    /**
     * initialise le dépôt au point de coordonnées x = 0, y = 0.
     * @param capaciteCamion 
     */
    public Routage(int capacite) 
    {
        if( capacite >= 0)
            this.capaciteCamion = capacite;
        else
            this.capaciteCamion = 0;

        this.depot = new Depot(0, 0);
        
        this.mesClients = new HashSet<>();
        
        this.planningLivraison = new Planning(this.capaciteCamion);
    }

    
    public Routage(Depot depot, int capaciteCamion) {
        this(capaciteCamion);
        if( depot != null)
        {
            this.depot = new Depot( depot.x(), depot.y() );
        }   
    }
    
    public void creationClientsTest1() {
        try {
            Client c0 = new Client(-99.7497, 12.7171, 4);
            Client c1 = new Client(61.7481, 17.0019, 10);
            Client c2 = new Client(-29.9417, 79.1925, 17);
            Client c3 = new Client(49.321, -65.1784, 18);
            Client c4 = new Client(42.1003, 2.70699, 7);
            Client c5 = new Client(-97.0031, -81.7194, 8);
            Client c6 = new Client(-70.5374, -66.8203, 20);
            Client c7 = new Client(-10.8615, -76.1834, 1);
            Client c8 = new Client(-98.2177, -24.424, 11);
            Client c9 = new Client(14.2369, 20.3528, 13);
            mesClients.clear();
            mesClients.add(c0);
            mesClients.add(c1);
            mesClients.add(c2);
            mesClients.add(c3);
            mesClients.add(c4);
            mesClients.add(c5);
            mesClients.add(c6);
            mesClients.add(c7);
            mesClients.add(c8);
            mesClients.add(c9);
        }
        catch( ErrQuantite ex)
        {
            System.out.println("Erreur: quantité negative (" + ex.getQuantite() + ")");
            System.exit(-1);
        }
    }
    
    
    /**
     * initialise les routes qui partent du dépôt et de chaque client.
     */
    private void initialiserRoutes()
    {
       /* for( Client c : this.mesClients )
        {
            c.ajouterRoutes(mesClients);
        }*/
        Set<Point> mesPoints = new HashSet<>();
        mesPoints.add(this.depot);
        for( Client c : this.mesClients)
        {
            mesPoints.add(c);
        }
        
        for( Point p : mesPoints)
        {
            p.ajouterRoutes(mesPoints);
        }
    }
    
    
    void planificationBasique()
    {
        this.planningLivraison.planificationBasique(this.depot,this.mesClients);
    }
    
    
    public static void test()
    {
        Routage r = new Routage(50);
        
        System.out.println("On crée la liste de clients.");
        r.creationClientsTest1();
        System.out.println("On initialise les routes.");
        r.initialiserRoutes();
        System.out.println("Et on effectue la planification !");
        r.planificationBasique();
        
        System.out.println("Nombre de tournées : " + r.planningLivraison.getNombreTournees() );
        System.out.println("Distance totale : " + r.planningLivraison.getDistanceTotaleParcourue() );
        System.out.println("Nombre de caisses livrées : " + r.planningLivraison.getNbTotalCaissesLivrees() );
        
        // TP6
        r.ecriturePlanning("fichierSortie.txt");
    }

    @Override
    public String toString() {
       return this.planningLivraison.toString();
    }
    
    
    public String toStringSimple() {
       return this.planningLivraison.toStringSimple();
    }
    
    // TP6
    /**
     * Cette méthode initialise un objet de type PrintWriter avec le constructeur par donnée du nom du fichier 
     * et puis elle écrit le planning complet dans le fichier texte  
     */
    public void ecriturePlanning(String fichierSortie)
    {
        System.out.println(fichierSortie);
        PrintWriter p = null;
        try
        {
            p = new PrintWriter(fichierSortie);
            p.print(this.toStringSimple());
            p.close();
        }
        catch( Exception e)
        {
            e.getMessage();
        }
    }
    
    /**
     * Traite les lignes ayant le format suivant : "<abscisseClient>\t<ordonneeClient>\t<nbCaissesBieres>"
     */
    public void traitementLigne(String ligne)
    {
        // Traitement des autres lignes : abscisse et ordonnée du client et le nombre de caisses de bières.
        try
        {
            String[] tabValeurs = ligne.split("\t");
            if( tabValeurs.length == 3)
            {            
                Client c = new Client( Double.parseDouble(tabValeurs[0]), Double.parseDouble(tabValeurs[1]), Integer.parseInt(tabValeurs[2]) );
                // System.out.println(c.toString()); // OK
                this.mesClients.add(c);
            }
            else
            {
                System.err.println("Erreur ou fin de fichier !!!");
                //System.exit(-1);
            }
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Lit un fichier de commandes et modifie l'instance en conséquence.
     */
    public void lectureClient(String fichierEntree)
    {
        try
        {
            FileReader f = new FileReader(fichierEntree);
            BufferedReader br = new BufferedReader(f);
            String ligne = br.readLine();
            
            
            // Traitement de la 1ere ligne => capacité camion
            String[] tabString = ligne.split("\t");
            if( tabString.length == 1)
            {
                this.capaciteCamion = Integer.parseInt(tabString[0]);
                this.planningLivraison = new Planning(this.capaciteCamion);
                // System.out.println(this.capaciteCamion);  // OK
            }
            else
            {
                System.err.println("ERREUR !!!!");
                System.exit(-2);
            }
            
            
            ligne = br.readLine();
            // Traitement de la 2e ligne => coordonnées du dépot.
            tabString = ligne.split("\t");
            if( tabString.length == 2)
            {
                this.depot.setAbscisse(Double.parseDouble(tabString[0]));
                this.depot.setOrdonnee( Double.parseDouble( tabString[1]) );
                // System.out.println(this.depot.toString());  // OK
            }
            else
            {
                System.err.println("ERREUR !!!!");
                System.exit(-3);
            }
            
            
            // Traitement des lignes suivantes :            
            while(ligne != null) {
                traitementLigne(ligne);
                ligne = br.readLine();
                }              
                    
            br.close();
            f.close();
        }   
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        
       
        
    }
    
    
    public static void testLecture(String nomFichier)
    {
        Routage r = new Routage(0);
        r.lectureClient(nomFichier);
        r.initialiserRoutes();
        r.planificationBasique();
        
        
        System.out.println("Nombre de tournées : " + r.planningLivraison.getNombreTournees() );
        System.out.println("Distance totale : " + r.planningLivraison.getDistanceTotaleParcourue() );
        System.out.println("Nombre de caisses livrées : " + r.planningLivraison.getNbTotalCaissesLivrees() );
       
        
        r.ecriturePlanning("test5_sol.txt");
    }
    
    
    public void planificationMinTournee()
    {
        this.planningLivraison.planificationMinTournee(this.depot,this.mesClients);
    }
    
    
    public void planificationBestInsertion()
    {
        this.planningLivraison.planificationBestInsertion(this.depot,this.mesClients);
    }
    
    
    public void planificationParNaysson()
    {
        this.planningLivraison.clearHashCodeClientsTable(mesClients.size());
        this.planningLivraison.planificationNaysson(depot, mesClients);
    }

    
    public static void testLectureV2(String nomFichier)
    {
        Routage r = new Routage(0);
        r.lectureClient(nomFichier);
        r.initialiserRoutes();
        r.planificationMinTournee();
        
        
        System.out.println("Nombre de tournées : " + r.planningLivraison.getNombreTournees() );
        System.out.println("Distance totale : " + r.planningLivraison.getDistanceTotaleParcourue() );
        System.out.println("Nombre de caisses livrées : " + r.planningLivraison.getNbTotalCaissesLivrees() );
       
        
        r.ecriturePlanning("test5_sol.txt");
    }
    
    
    public static void testLectureV3(String nomFichier)
    {
        Routage r = new Routage(0);
        r.lectureClient(nomFichier);
        r.initialiserRoutes();
        r.planificationBestInsertion();
        
        
        System.out.println("Nombre de tournées : " + r.planningLivraison.getNombreTournees() );
        System.out.println("Distance totale : " + r.planningLivraison.getDistanceTotaleParcourue() );
        System.out.println("Nombre de caisses livrées : " + r.planningLivraison.getNbTotalCaissesLivrees() );
       
        
        r.ecriturePlanning("test5_sol.txt");
    }

    public static void testLectureV4(String nomFichier)
    {
        Routage r = new Routage(0);
        r.lectureClient(nomFichier);
        r.initialiserRoutes();
        r.planificationParNaysson();
        
        System.out.println("Nombre de tournées : " + r.planningLivraison.getNombreTournees() );
        System.out.println("Distance totale : " + r.planningLivraison.getDistanceTotaleParcourue() );
        System.out.println("Nombre de caisses livrées : " + r.planningLivraison.getNbTotalCaissesLivrees() );
        
        r.ecriturePlanning("test1_sol.txt");
    }
    /**
     * La méthode lit le fichier d’entrée, 
     * initialise toutes les routes du réseau routier (avec un appel à la méthodeinitialiserRoutes()), 
     * puis elle fait appel aux trois méthodes de planification que vous avez écrit. 
     * Enfin, le planning fourni par chaque algorithme d’optimisation est écrit dans un fichier différent. 
     * Le fichier sera nommé de manière automatique. 
     * Les trois fichiers de sortie pourront être mis dans des répertoires différents.
     */
    public static void comparerMethodes(String fichierEntree)
    {
        if( fichierEntree == null)
            return;
        
        Routage r = new Routage(0);
        r.lectureClient(fichierEntree);
        r.initialiserRoutes();
        String fichierSortie = fichierEntree.replace(".txt","_sol.txt");
        
        /*r.planningLivraison.getEnsTournees().clear();
        r.planificationBasique();
        r.ecriturePlanning("basique\\" + fichierSortie);
        System.out.println("Nombre de tournées : " + r.planningLivraison.getNombreTournees() );
        System.out.println("Distance totale : " + r.planningLivraison.getDistanceTotaleParcourue() );
        System.out.println("Nombre de caisses livrées : " + r.planningLivraison.getNbTotalCaissesLivrees() );
        
        r.planningLivraison.getEnsTournees().clear();
        r.planificationMinTournee();
        r.ecriturePlanning("minTournee\\" + fichierSortie);
        System.out.println("Nombre de tournées : " + r.planningLivraison.getNombreTournees() );
        System.out.println("Distance totale : " + r.planningLivraison.getDistanceTotaleParcourue() );
        System.out.println("Nombre de caisses livrées : " + r.planningLivraison.getNbTotalCaissesLivrees() );*/
        
        r.planningLivraison.getEnsTournees().clear();
        r.planificationBestInsertion();
        r.ecriturePlanning("bestInsertion\\" + fichierSortie);
        System.out.println("Nombre de tournées : " + r.planningLivraison.getNombreTournees() );
        System.out.println("Distance totale : " + r.planningLivraison.getDistanceTotaleParcourue() );
        System.out.println("Nombre de caisses livrées : " + r.planningLivraison.getNbTotalCaissesLivrees() );
        
        r.planningLivraison.getEnsTournees().clear();
        r.planificationParNaysson();
        r.ecriturePlanning("Naysson\\" + fichierSortie);
        System.out.println("Nombre de tournées : " + r.planningLivraison.getNombreTournees() );
        System.out.println("Distance totale : " + r.planningLivraison.getDistanceTotaleParcourue() );
        System.out.println("Nombre de caisses livrées : " + r.planningLivraison.getNbTotalCaissesLivrees() );       
    }
    
    public static void main(String[] args) {
        //Routage.test(); // TP5 Q30
        //Routage.lectureClient("testLecture0.txt");    // TP6 Q12
        //Routage.testLecture("test5.txt");   // TP6 Q15
        //Routage.testLectureV2("test5.txt");   // TP7 Q1
        //Routage.testLectureV3("test5.txt"); // TP7 Q5
        
        Routage.comparerMethodes("test5.txt");  // TP7 Q6
        //Routage.testLectureV4("test1.txt");
      
        
        
    }
}
    
    
    
    
    
    
    
