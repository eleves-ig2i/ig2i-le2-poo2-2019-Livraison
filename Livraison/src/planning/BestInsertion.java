/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planning;

/**
 *
 * @author user
 */
public class BestInsertion 
{
    private Tournee t;
    
    /**
     * Correspond à la distance entre le point actuel et le client à ajouter + entre le client à ajouter et le point suivant moins la distance entre point actuel et le point suivant.
     */
    private double distanceAjoutee;
    
    /**
     * Position du client à ajouter, dans la liste de clients.
     * Si la meilleure insertion s'effectue entre le 1er et le 2e client, 
     * alors cet attribut vaudra 1.
     */
    private int positionTournee;
    
    /**
     * Initialisation de la tournée à null et de la distance à "infini"
     */
    public BestInsertion()
    {
        this.t = null;
        distanceAjoutee = Double.MAX_VALUE;
        this.positionTournee = 0;
    }

    public BestInsertion(Tournee t, double longueurDetour, int positionTournee) {
        this();
        
        if( t != null)
            this.t = t;
        
        this.distanceAjoutee = longueurDetour;
        
        if( positionTournee >= 0)
            this.positionTournee = positionTournee;
    }

    public Tournee getT() {
        return t;
    }   
    

    public double getDistanceAjoutee() {
        return distanceAjoutee;
    }

    public void setDistanceAjoutee(double longueurDetour) {
        this.distanceAjoutee = longueurDetour;
    }

    public int getPositionTournee() {
        return positionTournee;
    }

    public void setPositionTournee(int positionTournee) {
        if( positionTournee >= 0)
            this.positionTournee = positionTournee;
        else
        {
            System.err.println("Position tournée invalide.");
        }
    }
    
    
    
    
}
