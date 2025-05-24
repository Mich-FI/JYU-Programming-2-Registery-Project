package kerho;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * @author Michal
 * @version 24 Feb 2025
 *
 */
public class Jasenet implements Iterable<Jasen> {
    private static final int MAX_JASENIA   = 25;
    private boolean muutettu = false;
    private int              lkm           = 0;
    private String kokoNimi = "Alppaajat";
    private String tiedostonPerusNimi = "nimet";
    private Jasen            alkiot[]      = new Jasen[MAX_JASENIA];


    /**
     * Oletusmuodostaja
     */
    public Jasenet() {
        this.alkiot = new Jasen[MAX_JASENIA];
    }


    /**
     * Lisää uuden jäsenen tietorakenteeseen.  Ottaa jäsenen omistukseensa.
     * @param jasen lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Jasenet jasenet = new Jasenet();
     * Jasen aku1 = new Jasen(), aku2 = new Jasen();
     * jasenet.getLkm() === 0;
     * jasenet.lisaa(aku1); jasenet.getLkm() === 1;
     * jasenet.lisaa(aku2); jasenet.getLkm() === 2;
     * jasenet.lisaa(aku1); jasenet.getLkm() === 3;
     * jasenet.anna(0) === aku1;
     * jasenet.anna(1) === aku2;
     * jasenet.anna(2) === aku1;
     * jasenet.anna(1) == aku1 === false;
     * jasenet.anna(1) == aku2 === true;
     * jasenet.anna(3) === aku1; #THROWS IndexOutOfBoundsException 
     * jasenet.lisaa(aku1); jasenet.getLkm() === 4;
     * jasenet.lisaa(aku1); jasenet.getLkm() === 5;
     * </pre>
     */
    public void lisaa(Jasen jasen) throws SailoException {
        if (lkm >= this.alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+20); 
        this.alkiot[lkm] = jasen;
        lkm++;
        muutettu = true;
    }
    
    

    /**
     * Korvaa jäsenen tietorakenteessa.  Ottaa jäsenen omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva jäsen.  Jos ei löydy,
     * niin lisätään uutena jäsenenä.
     * @param jasen lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Jasenet jasenet = new Jasenet();
     * Jasen aku1 = new Jasen(), aku2 = new Jasen();
     * aku1.rekisteroi(); aku2.rekisteroi();
     * jasenet.getLkm() === 0;
     * jasenet.korvaaTaiLisaa(aku1); jasenet.getLkm() === 1;
     * jasenet.korvaaTaiLisaa(aku2); jasenet.getLkm() === 2;
     * Jasen aku3 = aku1.clone();
     * aku3.aseta(3,"kkk");
     * Iterator<Jasen> it = jasenet.iterator();
     * it.next() == aku1 === true;
     * jasenet.korvaaTaiLisaa(aku3); jasenet.getLkm() === 2;
     * it = jasenet.iterator();
     * Jasen j0 = it.next();
     * j0 === aku3;
     * j0 == aku3 === true;
     * j0 == aku1 === false;
     * </pre>
     */

    public void korvaaTaiLisaa(Jasen jasen) throws SailoException {
        int id = jasen.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = jasen;
                muutettu = true;
                return;
            }
        }
        lisaa(jasen);
    }



    /**
     * Palauttaa viitteen i:teen jäseneen.
     * @param i monennenko jäsenen viite halutaan
     * @return viite jäseneen, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public Jasen anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    /** 
     * Poistaa jäsenen jolla on valittu tunnusnumero  
     * @param id poistettavan jäsenen tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Jasenet jasenet = new Jasenet(); 
     * Jasen aku1 = new Jasen(), aku2 = new Jasen(), aku3 = new Jasen(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getTunnusNro(); 
     * jasenet.lisaa(aku1); jasenet.lisaa(aku2); jasenet.lisaa(aku3); 
     * jasenet.poista(id1+1) === 1; 
     * jasenet.annaId(id1+1) === null; jasenet.getLkm() === 2; 
     * jasenet.poista(id1) === 1; jasenet.getLkm() === 1; 
     * jasenet.poista(id1+3) === 0; jasenet.getLkm() === 1; 
     * </pre> 
     *  
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 



    /**
     * Lukee jäsenistön tiedostosta.  Kesken.
     * @param tied tied
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
            setTiedostonPerusNimi(tied);
            try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonPerusNimi())) ) {
                kokoNimi = fi.readLine();
                if ( kokoNimi == null ) throw new SailoException("Kerhon nimi puuttuu");
                String rivi = fi.readLine();
                if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
                // int maxKoko = Mjonot.erotaInt(rivi,10); // tehdään jotakin
    
                while ( (rivi = fi.readLine()) != null ) {
                    rivi = rivi.trim();
                    if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                    Jasen jasen = new Jasen();
                    jasen.parse(rivi); // voisi olla virhekäsittely
                    lisaa(jasen);
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
     * Tallentaa jäsenistön tiedostoon. 
     * Tiedoston muoto:
     * <pre>
     * Kelmien kerho
     * 20
     * ; kommenttirivi
     * 2|Ankka Aku|121103-706Y|Paratiisitie 13|12345|ANKKALINNA|12-1234|||1996|50.0|30.0|Velkaa Roopelle
     * 3|Ankka Tupu|121153-706Y|Paratiisitie 13|12345|ANKKALINNA|12-1234|||1996|50.0|30.0|Velkaa Roopelle
     * </pre>
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
        //fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        //ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");
   
        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(getKokoNimi());
            fo.println(alkiot.length);
            for (Jasen jasen : this) {
                fo.println(jasen.toString());
            }
            //} catch ( IOException e ) { // ei heitä poikkeusta
            //  throw new SailoException("Tallettamisessa ongelmia: " + e.getMessage());
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
   
        muutettu = false;
    }

    
    /**
     * Palauttaa Kerhon koko nimen
     * @return Kerhon koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
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
     * Palauttaa kerhon jäsenten lukumäärän
     * @return jäsenten lukumäärä
     */
    public int getLkm() {
        return lkm;
    }


     /**
      * Luokka jäsenten iteroimiseksi.
      * @example
      * <pre name="test">
      * #THROWS SailoException
      * #PACKAGEIMPORT
      * #import java.util.*;
      *
      * Jasenet jasenet = new Jasenet();
      * Jasen aku1 = new Jasen(), aku2 = new Jasen();
      * aku1.rekisteroi(); aku2.rekisteroi();
      *
      * jasenet.lisaa(aku1);
      * jasenet.lisaa(aku2);
      * jasenet.lisaa(aku1);
      *
      * StringBuffer ids = new StringBuffer(30);
      * for (Jasen jasen:jasenet)   // Kokeillaan for-silmukan toimintaa
      *   ids.append(" "+jasen.getTunnusNro());           
      *
      * String tulos = " " + aku1.getTunnusNro() + " " + aku2.getTunnusNro() + " " + aku1.getTunnusNro();
      *
      * ids.toString() === tulos;
      *
      * ids = new StringBuffer(30);
      * for (Iterator<Jasen>  i=jasenet.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
      *   Jasen jasen = i.next();
      *   ids.append(" "+jasen.getTunnusNro());           
      * }
      *
      * ids.toString() === tulos;
      *
      * Iterator<Jasen>  i=jasenet.iterator();
      * i.next() == aku1  === true;
      * i.next() == aku2  === true;
      * i.next() == aku1  === true;
      *
      * i.next();  #THROWS NoSuchElementException
      * 
      * </pre>
      */
     public class JasenetIterator implements Iterator<Jasen> {
         private int kohdalla = 0;
 
 
         /**
          * Onko olemassa vielä seuraavaa jäsentä
          * @see java.util.Iterator#hasNext()
          * @return true jos on vielä jäseniä
          */
         @Override
         public boolean hasNext() {
             return kohdalla < getLkm();
         }
 
 
         /**
          * Annetaan seuraava jäsen
          * @return seuraava jäsen
          * @throws NoSuchElementException jos seuraava alkiota ei enää ole
          * @see java.util.Iterator#next()
          */
         @Override
         public Jasen next() throws NoSuchElementException {
             if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
             return anna(kohdalla++);
         }
 
 
         /**
          * Tuhoamista ei ole toteutettu
          * @throws UnsupportedOperationException aina
          * @see java.util.Iterator#remove()
          */
         @Override
         public void remove() throws UnsupportedOperationException {
             throw new UnsupportedOperationException("Me ei poisteta");
         }
     }
 
 
     /**
      * Palautetaan iteraattori jäsenistään.
      * @return jäsen iteraattori
      */
     @Override
    public Iterator<Jasen> iterator() {
         return new JasenetIterator();
     }
 
 
     /**
      * Palauttaa "taulukossa" hakuehtoon vastaavien jäsenten viitteet
      * @param hakuehto hakuehto
      * @param k etsittävän kentän indeksi 
      * @return tietorakenteen löytyneistä jäsenistä
      * @example
      * <pre name="test">
      * #THROWS SailoException 
      *   Jasenet jasenet = new Jasenet();
      *   Jasen jasen1 = new Jasen(); jasen1.parse("1|Ankka Aku|030201-115H|Paratiisitie 13|");
      *   Jasen jasen2 = new Jasen(); jasen2.parse("2|Ankka Tupu||030552-123B|");
      *   Jasen jasen3 = new Jasen(); jasen3.parse("3|Susi Sepe|121237-121V||131313|Perämetsä");
      *   Jasen jasen4 = new Jasen(); jasen4.parse("4|Ankka Iines|030245-115V|Ankkakuja 9");
      *   Jasen jasen5 = new Jasen(); jasen5.parse("5|Ankka Roope|091007-408U|Ankkakuja 12");
      *   jasenet.lisaa(jasen1); jasenet.lisaa(jasen2); jasenet.lisaa(jasen3); jasenet.lisaa(jasen4); jasenet.lisaa(jasen5);
      *   // TODO: toistaiseksi palauttaa kaikki jäsenet
      * </pre>
      */ 
     public Collection<Jasen> etsi(String hakuehto, int k) { 
         Collection<Jasen> loytyneet = new ArrayList<Jasen>();
         int hk = k;
         if (hk < 0) hk = 1;
         for (Jasen jasen : this) { 
             String sisalto = jasen.anna(hk);
             if (sisalto == null) break;
             if (WildChars.onkoSamat(sisalto, hakuehto))
                 loytyneet.add(jasen);
         }
         return loytyneet; 
     }
     
     
     /** 
      * Etsii jäsenen id:n perusteella 
      * @param id tunnusnumero, jonka mukaan etsitään 
      * @return jäsen jolla etsittävä id tai null 
      * <pre name="test"> 
      * #THROWS SailoException  
      * Jasenet jasenet = new Jasenet(); 
      * Jasen aku1 = new Jasen(), aku2 = new Jasen(), aku3 = new Jasen(); 
      * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
      * int id1 = aku1.getTunnusNro(); 
      * jasenet.lisaa(aku1); jasenet.lisaa(aku2); jasenet.lisaa(aku3); 
      * jasenet.annaId(id1  ) == aku1 === true; 
      * jasenet.annaId(id1+1) == aku2 === true; 
      * jasenet.annaId(id1+2) == aku3 === true; 
      * </pre> 
      */ 
     public Jasen annaId(int id) { 
         for (Jasen jasen : this) { 
             if (id == jasen.getTunnusNro()) return jasen; 
         } 
         return null; 
     } 


     /** 
      * Etsii jäsenen id:n perusteella 
      * @param id tunnusnumero, jonka mukaan etsitään 
      * @return löytyneen jäsenen indeksi tai -1 jos ei löydy 
      * <pre name="test"> 
      * #THROWS SailoException  
      * Jasenet jasenet = new Jasenet(); 
      * Jasen aku1 = new Jasen(), aku2 = new Jasen(), aku3 = new Jasen(); 
      * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
      * int id1 = aku1.getTunnusNro(); 
      * jasenet.lisaa(aku1); jasenet.lisaa(aku2); jasenet.lisaa(aku3); 
      * jasenet.etsiId(id1+1) === 1; 
      * jasenet.etsiId(id1+2) === 2; 
      * </pre> 
      */ 
     public int etsiId(int id) { 
         for (int i = 0; i < lkm; i++) 
             if (id == alkiot[i].getTunnusNro()) return i; 
         return -1; 
     } 

    
    
    /**
     * Testiohjelma jäsenistölle
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Jasenet jasenet = new Jasenet();

        Jasen aku = new Jasen(), aku2 = new Jasen();
        aku.rekisteroi();
        aku.vastaaAkuAnkka();
        aku2.rekisteroi();
        aku2.vastaaAkuAnkka();

        try {
            jasenet.lisaa(aku);
            jasenet.lisaa(aku2);

            System.out.println("============= Jäsenet testi =================");

            for (int i = 0; i < jasenet.getLkm(); i++) {
                Jasen jasen = jasenet.anna(i);
                System.out.println("Jäsen nro: " + i);
                jasen.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
