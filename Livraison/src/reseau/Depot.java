/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reseau;

import java.util.*;
import mesExceptions.*;
/**
 *
 * @author user
 */
public class Depot extends Point {
    
    public Depot(double x, double y)
    {
        super(x,y);
    }

    @Override
    public String toString() {
        return "Depot {" + super.toString() + "}";   
    }
    
    // Utilisé dans la question 28
    public void ajouterRoutesV2(Set<Client> listeClients)
    {
        // on "transforme" la liste de clients en liste de points.
        Set<Point> mesClients = new HashSet<>();
        for(Client c : listeClients)
        {
            mesClients.add(c);
        }
        this.ajouterRoutes(mesClients);
        
    }
    
    
    public static void main(String[] args) {
        try {
            Depot d = new Depot(0,0);
            Client c1 = new Client(5,5,10);
            Client c2 = new Client(-5,5,10);
            Client c3 = new Client(-5,-5,10);
            Client c4 = new Client(5,-5,10);
            Set<Point> mesClients = new HashSet<>();
            mesClients.add(c1);
            mesClients.add(c2);
            mesClients.add(c3);
            mesClients.add(c4);
            d.ajouterRoutes(mesClients);
            System.out.println(d);
        }
        catch( ErrQuantite ex)
        {
            System.out.println("Erreur: quantité negative (" + ex.getQuantite() + ")");
            System.exit(-1);
        }
       
    }
}
