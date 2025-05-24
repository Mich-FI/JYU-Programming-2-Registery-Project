package kerho;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Michal
 * @version 4 Mar 2025
 *
 */
public class Valineet implements Iterable<Valine> {
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "valineet";

    /** Taulukko välineistä */
    private final Collection<Valine> alkiot = new ArrayList<Valine>();


    /**
     * Välineen alustaminen
     */
    public Valineet() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden välineen tietorakenteeseen.  Ottaa välineen omistukseensa.
     * @param val lisättävä väline.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Valine val) {
        alkiot.add(val);
        muutettu = true;
    }
    
    
    /**
     * Poistaa valitun valineen
     * @param valine poistettava valine
     * @return tosi jos löytyi poistettava tietue 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Valineet valineet = new Valineet();
     *  Valine valine21 = new Valine(); valine21.vastaaValineSukset(2);
     *  Valine valine11 = new Valine(); valine11.vastaaValineSukset(1);
     *  Valine valine22 = new Valine(); valine22.vastaaValineSukset(2); 
     *  Valine valine12 = new Valine(); valine12.vastaaValineSukset(1); 
     *  Valine valine23 = new Valine(); valine23.vastaaValineSukset(2); 
     *  valineet.lisaa(valine21);
     *  valineet.lisaa(valine11);
     *  valineet.lisaa(valine22);
     *  valineet.lisaa(valine12);
     *  valineet.poista(valine23) === false ; valineet.getLkm() === 4;
     *  valineet.poista(valine11) === true;   valineet.getLkm() === 3;
     *  List<Valine> h = valineet.annaValineet(1);
     *  h.size() === 1; 
     *  h.get(0) === valine12;
     * </pre>
     */
    public boolean poista(Valine valine) {
        boolean ret = alkiot.remove(valine);
        if (ret) muutettu = true;
        return ret;
    }

    
    /**
     * Poistaa kaikki tietyn tietyn jäsenen välineet
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     * @example
     * <pre name="test">
     *  Valineet valineet = new Valineet();
     *  Valine valine21 = new Valine(); valine21.vastaaValineSukset(2);
     *  Valine valine11 = new Valine(); valine11.vastaaValineSukset(1);
     *  Valine valine22 = new Valine(); valine22.vastaaValineSukset(2); 
     *  Valine valine12 = new Valine(); valine12.vastaaValineSukset(1); 
     *  Valine valine23 = new Valine(); valine23.vastaaValineSukset(2); 
     *  valineet.lisaa(valine21);
     *  valineet.lisaa(valine11);
     *  valineet.lisaa(valine22);
     *  valineet.lisaa(valine12);
     *  valineet.lisaa(valine23);
     *  valineet.poistaJasenenValineet(2) === 3;  valineet.getLkm() === 2;
     *  valineet.poistaJasenenValineet(3) === 0;  valineet.getLkm() === 2;
     *  List<Valine> h = valineet.annaValineet(2);
     *  h.size() === 0; 
     *  h = valineet.annaValineet(1);
     *  h.get(0) === valine11;
     *  h.get(1) === valine12;
     * </pre>
     */
    public int poistaJasenenValineet(int tunnusNro) {
        int n = 0;
        for (Iterator<Valine> it = alkiot.iterator(); it.hasNext();) {
            Valine val = it.next();
            if ( val.getJasenNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }



    /**
     * Tallentaa jäsenistön tiedostoon.  
     * TODO Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
            if ( !muutettu ) return;
            
            File kansio = new File("alppaajat");
            if (!kansio.exists()) {
                if (!kansio.mkdirs()) {
                    throw new SailoException("Kansion luonti epäonnistui: " + kansio.getAbsolutePath());
                }
            }
       
            //File fbak = new File(getBakNimi());
            File ftied = new File(getTiedostonPerusNimi());
            //fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
            //ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");
       
            try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
                for (Valine val : this) {
                    fo.println(val.toString());
                }
            } catch ( FileNotFoundException ex ) {
                throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
            } catch ( IOException ex ) {
                throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
            }
       
            muutettu = false;
        }


    /**
     * Palauttaa kerhon välineiden lukumäärän
     * @return välineiden lukumäärä
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
     *  Valineet valineet = new Valineet();
     *  Valine valine21 = new Valine(2); valineet.lisaa(valine21);
     *  Valine valine11 = new Valine(1); valineet.lisaa(valine11);
     *  Valine valine22 = new Valine(2); valineet.lisaa(valine22);
     *  Valine valine12 = new Valine(1); valineet.lisaa(valine12);
     *  Valine valine23 = new Valine(2); valineet.lisaa(valine23);
     * 
     *  Iterator<Valine> i2=valineet.iterator();
     *  i2.next() === valine21;
     *  i2.next() === valine11;
     *  i2.next() === valine22;
     *  i2.next() === valine12;
     *  i2.next() === valine23;
     *  i2.next() === valine12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int jnrot[] = {2,1,2,1,2};
     *  
     *  for ( Valine val:valineet ) { 
     *    val.getJasenNro() === jnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Valine> iterator() {
        return alkiot.iterator();
    }

    /**
     * Lukee jäsenistön tiedostosta.  
     * TODO Kesken.
     * @param tied tied
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
            setTiedostonPerusNimi(tied);
            try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonPerusNimi())) ) {
    
                String rivi;
                while ( (rivi = fi.readLine()) != null ) {
                    rivi = rivi.trim();
                    if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                    Valine val = new Valine();
                    val.parse(rivi); // voisi olla virhekäsittely
                    lisaa(val);
                }
                muutettu = false;
    
            } catch ( FileNotFoundException e ) {
                throw new SailoException("Tiedosto " + getTiedostonPerusNimi() + " ei aukea");
            } catch ( IOException e ) {
                throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
            }
        }
    
    
     /**
      * Luetaan aikaisemmin annetun nimisestä tiedostosta
      * @throws SailoException jos tulee poikkeus
      */
     public void lueTiedostosta() throws SailoException {
         lueTiedostosta(getTiedostonPerusNimi());
     }
     
     
         /**
       * Asettaa tiedoston perusnimen ilan tarkenninta
       * @param nimi tallennustiedoston perusnimi
       */
      public void setTiedostonPerusNimi(String nimi) {
          tiedostonPerusNimi = nimi;
      }
     
     
      /**
       * Palauttaa tiedoston nimen, jota käytetään tallennukseen
       * @return tallennustiedoston nimi
       */
      public String getTiedostonPerusNimi() {
          return tiedostonPerusNimi;
      }
     
     
      /**
       * Palauttaa tiedoston nimen, jota käytetään tallennukseen
       * @return tallennustiedoston nimi
       */
      public String getTiedostonNimi() {
          return getTiedostonPerusNimi() + ".dat";
      }
     
     
      /**
       * Palauttaa varakopiotiedoston nimen
       * @return varakopiotiedoston nimi
       */
      public String getBakNimi() {
          return tiedostonPerusNimi + ".bak";
      }

    /**
     * Haetaan kaikki jäsen välineet
     * @param tunnusnro jäsenen tunnusnumero jolle välineitä haetaan
     * @return tietorakenne jossa viiteet löydetteyihin välineisiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Valineet valineet = new Valineet();
     *  Valine valine21 = new Valine(2); valineet.lisaa(valine21);
     *  Valine valine11 = new Valine(1); valineet.lisaa(valine11);
     *  Valine valine22 = new Valine(2); valineet.lisaa(valine22);
     *  Valine valine12 = new Valine(1); valineet.lisaa(valine12);
     *  Valine valine23 = new Valine(2); valineet.lisaa(valine23);
     *  Valine valine51 = new Valine(5); valineet.lisaa(valine51);
     *  
     *  List<Valine> loytyneet;
     *  loytyneet = valineet.annaValineet(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = valineet.annaValineet(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == valine11 === true;
     *  loytyneet.get(1) == valine12 === true;
     *  loytyneet = valineet.annaValineet(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == valine51 === true;
     * </pre> 
     */
    public List<Valine> annaValineet(int tunnusnro) {
        List<Valine> loydetyt = new ArrayList<Valine>();
        for (Valine val : alkiot)
            if (val.getJasenNro() == tunnusnro) loydetyt.add(val);
        return loydetyt;
    }


    /**
     * Testiohjelma välineille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Valineet valineet = new Valineet();
        Valine valine1 = new Valine();
        valine1.vastaaValineSukset(2);
        Valine valine2 = new Valine();
        valine2.vastaaValineSukset(1);
        Valine valine3 = new Valine();
        valine3.vastaaValineSukset(2);
        Valine valine4 = new Valine();
        valine4.vastaaValineSukset(2);

        valineet.lisaa(valine1);
        valineet.lisaa(valine2);
        valineet.lisaa(valine3);
        valineet.lisaa(valine2);
        valineet.lisaa(valine4);
        
        System.out.println("============= välineet testi =================");

        List<Valine> valineet2 = valineet.annaValineet(2);

        for (Valine val : valineet2) {
            System.out.print(val.getJasenNro() + " ");
            val.tulosta(System.out);
        }

    }

}
