package kerho;

import java.util.ArrayList;
import java.util.Collection;
import java.util.*;

/**
 * @author Michal
 * @version 28 Mar 2025
 *
 */
public class Kokemukset implements Iterable<Kokemus> {
    
    private String tiedostonNimi = "";

    /** Taulukko kokemuksesta */
    private final Collection<Kokemus> alkiot = new ArrayList<Kokemus>();


    /**
     * Kokemuksien alustaminen
     */
    public Kokemukset() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden kokemuksen tietorakenteeseen.  Ottaa kokemuksen omistukseensa.
     * @param har lisättävä kokemus.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Kokemus har) {
        alkiot.add(har);
    }


    /**
     * Lukee jäsenistön tiedostosta.  
     * TODO Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + ".har";
        throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
    }


    /**
     * Tallentaa jäsenistön tiedostoon.  
     * TODO Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
    }


    /**
     * Palauttaa kerhon kokemusten lukumäärän
     * @return kokemusten lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }


    /**
     * Iteraattori kaikkien kokemusten läpikäymiseen
     * @return kokemusiteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Kokemukset taitoTasot = new Kokemukset();
     *  Kokemus pitsi21 = new Kokemus(2); taitoTasot.lisaa(pitsi21);
     *  Kokemus pitsi11 = new Kokemus(1); taitoTasot.lisaa(pitsi11);
     *  Kokemus pitsi22 = new Kokemus(2); taitoTasot.lisaa(pitsi22);
     *  Kokemus pitsi12 = new Kokemus(1); taitoTasot.lisaa(pitsi12);
     *  Kokemus pitsi23 = new Kokemus(2); taitoTasot.lisaa(pitsi23);
     * 
     *  Iterator<Kokemus> i2=taitoTasot.iterator();
     *  i2.next() === pitsi21;
     *  i2.next() === pitsi11;
     *  i2.next() === pitsi22;
     *  i2.next() === pitsi12;
     *  i2.next() === pitsi23;
     *  i2.next() === pitsi12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int jnrot[] = {2,1,2,1,2};
     *  
     *  for ( Kokemus har:taitoTasot ) { 
     *    har.getJasenNro() === jnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public java.util.Iterator<Kokemus> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki jäsen kokemjukset
     * @param tunnusnro jäsenen tunnusnumero jolle kokemuksia haetaan
     * @return tietorakenne jossa viiteet löydetteyihin kokemuksiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Kokemukset taitoTasot = new Kokemukset();
     *  Kokemus pitsi21 = new Kokemus(2); taitoTasot.lisaa(pitsi21);
     *  Kokemus pitsi11 = new Kokemus(1); taitoTasot.lisaa(pitsi11);
     *  Kokemus pitsi22 = new Kokemus(2); taitoTasot.lisaa(pitsi22);
     *  Kokemus pitsi12 = new Kokemus(1); taitoTasot.lisaa(pitsi12);
     *  Kokemus pitsi23 = new Kokemus(2); taitoTasot.lisaa(pitsi23);
     *  Kokemus pitsi51 = new Kokemus(5); taitoTasot.lisaa(pitsi51);
     *  
     *  List<Kokemus> loytyneet;
     *  loytyneet = taitoTasot.annaKokemukset(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = taitoTasot.annaKokemukset(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == pitsi11 === true;
     *  loytyneet.get(1) == pitsi12 === true;
     *  loytyneet = taitoTasot.annaKokemukset(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == pitsi51 === true;
     * </pre> 
     */
    public List<Kokemus> annaKokemukset(int tunnusnro) {
        List<Kokemus> loydetyt = new ArrayList<Kokemus>();
        for (Kokemus har : alkiot)
            if (har.getJasenNro() == tunnusnro) loydetyt.add(har);
        return loydetyt;
    }


    /**
     * Testiohjelma kokemuksille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Kokemukset taitoTasot = new Kokemukset();
        Kokemus pitsi1 = new Kokemus();
        pitsi1.vastaaKokemusAloittelija(2);
        Kokemus pitsi2 = new Kokemus();
        pitsi2.vastaaKokemusAloittelija(1);
        Kokemus pitsi3 = new Kokemus();
        pitsi3.vastaaKokemusAloittelija(2);
        Kokemus pitsi4 = new Kokemus();
        pitsi4.vastaaKokemusAloittelija(2);

        taitoTasot.lisaa(pitsi1);
        taitoTasot.lisaa(pitsi2);
        taitoTasot.lisaa(pitsi3);
        taitoTasot.lisaa(pitsi2);
        taitoTasot.lisaa(pitsi4);

        System.out.println("============= Kokemukset testi =================");

        List<Kokemus> kokemukset2 = taitoTasot.annaKokemukset(2);

        for (Kokemus har : kokemukset2) {
            System.out.print(har.getJasenNro() + " ");
            har.tulosta(System.out);
        }

    }

}
