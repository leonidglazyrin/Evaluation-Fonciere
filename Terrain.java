
package evaluationfonciere;

import java.text.DecimalFormat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author Leonid Glazyrin GLAL77080105
 *         Goldlen Chhun CHHG20069604
 *         Steven Chieng CHIS01069604
 *         Eric Drapeau DRAE21079108
 * 
 */
public class Terrain {
    double[] prixMinMax = new double[2];

    double valeurFonciereTotale;
    double taxeScolaire;
    double taxeMunicipale;
    Lotissement[] lotissements;

    static final double PRIX_FIXE = 733.77;
    static final double TAUX_SCOLAIRE = 0.012;
    static final double TAUX_MUNICIPALE = 0.025;
   
    public Terrain(JSONObject JSONSource) throws FormatInvalide {
        setPrixMin(JSONSource, prixMinMax);
        setPrixMax(JSONSource, prixMinMax);
        
        JSONArray lesLots = setLots(JSONSource);
        int typeDeTerrain = setTypeTerrain(JSONSource);
        verifierValeursTerrain(typeDeTerrain, lesLots, prixMinMax);
        
        this.lotissements = formaterLot(lesLots, typeDeTerrain);
        verifierValeursLots();
    }

    private void verifierValeursLots() {
        for(Lotissement lot : lotissements) {
            lot.verifierNbDroitsPassages();
            lot.verifierNbServices();
            lot.verifierSuperficie();
        }
    }
    
    private double stringEnDouble(String prixEnString){
        //On le separe du signe $ et on remplace les , par . s'il y en a
        prixEnString = prixEnString.split(" ")[0].replaceAll(",",".");
        return Double.parseDouble(prixEnString);
    }
    
    private String formaterDecimal(double valeur) {
        String pattern = "#.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(valeur);
    }

    public double arrondiAu5sousSuperieur(double montant) {
        return Math.ceil(montant * 20) / 20;
    }

    private Lotissement[] formaterLot(JSONArray source, int typeDeTerrain) {
        CreerTypeLotissement createur = new CreerTypeLotissement();
        JSONObject unLot;
        lotissements = new Lotissement[source.size()];
        for (int i = 0; i < source.size(); i++) {
            unLot = source.getJSONObject(i);
            lotissements[i] = createur.creerLotissement(typeDeTerrain, unLot);
            lotissements[i].setPrixMinMax(prixMinMax);
        }
        return lotissements;
    }

    private boolean typeNonValide(int type) {
        return type != 0 && type != 1 && type != 2;
    }
}
