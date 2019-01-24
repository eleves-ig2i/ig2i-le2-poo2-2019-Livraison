/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reseau;

import mesExceptions.*;
import java.util.*;
/**
 *
 * @author user
 */
public class Client extends Point {
    
    private int nbCaissesBieresALivrer;
    
    /**
     * Constructeur par données de la classe Client. Si nbCaisses négatif alors l'attribut associé vaudra 0.
     * @param x
     * @param y
     * @param nbCaisses 
     */
    public Client(double x, double y,int nbCaisses) throws ErrQuantite
    {
        super(x,y);

        if( nbCaisses > 0 )
            this.nbCaissesBieresALivrer = nbCaisses;
        else
            throw new ErrQuantite(nbCaisses);        
    }

    public int getNbCaissesBieresALivrer() {
        return nbCaissesBieresALivrer;
    }
    
    
    @Override
    public String toString() {
        String sReturn = new String();
        sReturn = "   Client\n{     "; 
        sReturn += super.toString() + " - Nombre de caisses de bières demandées = " + this.nbCaissesBieresALivrer + "\n},";
        return sReturn;   
    }
    
    public static void main(String[] args) {
        // TP5
        /*
        Client c1 = new Client(5,5,10);
        Client c2 = new Client(-5,5,10);
        Client c3 = new Client(-5,-5,10);
        Client c4 = new Client(5,-5,10);
        Set<Point> mesClients = new HashSet<>();
        mesClients.add(c2);
        mesClients.add(c3);
        mesClients.add(c4);
        c1.ajouterRoutes(mesClients);
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
        System.out.println(c4);
        */
        
        Client c2 = null;
        try {
            Client c1 = new Client(5, 5, 10);
            System.out.println("Creation ok");
            c2 = new Client(5, -5,0);
            System.out.println("Creation ok");
        }
        catch (ErrQuantite ex) {
            System.err.println( ex.getMessage() );
            if( ex.getQuantite() < 0)
            {
                System.exit(-1);
            }
         }
        
        System.out.println( c2.toString() );
    }
}
