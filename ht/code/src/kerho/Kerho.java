package kerho;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Kerho-luokka, joka huolehtii jäsenistöstä.  Pääosin kaikki metodit
 * ovat vain "välittäjämetodeja" jäsenistöön.
 *
 * @author Michal
 * Testien alustus
 * @example
 * <pre name="testJAVA">
 * #import kerho.SailoException;
 *  private Kerho kerho;
 *  private Jasen aku1;
 *  private Jasen aku2;
 *  private int jid1;
 *  private int jid2;
 *  private Valine valine21;
 *  private Valine valine11;
 *  private Valine valine22; 
 *  private Valine valine12; 
 *  private Valine valine23;
 *  
 *  @Before
 *  @SuppressWarnings("javadoc")
 *  public void alustaKerho() {
 *    kerho = new Kerho();
 *    aku1 = new Jasen(); aku1.vastaaAkuAnkka(); aku1.rekisteroi();
 *    aku2 = new Jasen(); aku2.vastaaAkuAnkka(); aku2.rekisteroi();
 *    jid1 = aku1.getTunnusNro();
 *    jid2 = aku2.getTunnusNro();
 *    valine21 = new Valine(jid2); valine21.vastaaValineSukset(jid2);
 *    valine11 = new Valine(jid1); valine11.vastaaValineSukset(jid1);
 *    valine22 = new Valine(jid2); valine22.vastaaValineSukset(jid2); 
 *    valine12 = new Valine(jid1); valine12.vastaaValineSukset(jid1); 
 *    valine23 = new Valine(jid2); valine23.vastaaValineSukset(jid2);
 *    try {
 *    kerho.lisaa(aku1);
 *    kerho.lisaa(aku2);
 *    kerho.lisaa(valine21);
 *    kerho.lisaa(valine11);
 *    kerho.lisaa(valine22);
 *    kerho.lisaa(valine12);
 *    kerho.lisaa(valine23);
 *    } catch ( Exception e) {
 *       System.err.println(e.getMessage());
 *    }
 *  }
 * </pre>
*/
public class Kerho {

    private Jasenet jasenet = new Jasenet();
    private Kokemukset kokemukset = new Kokemukset();
    private Valineet valineet = new Valineet();
    
    /**
     * Palauttaa kerhon jäsenmäärän
     * @return jäsenmäärä
     */
    public int getJasenia() {
        return jasenet.getLkm();
    }
    
    
    /**
     * Poistaa jäsenistöstä ja valineita jäsenen tiedot 
     * @param jasen jäsen jokapoistetaan
     * @return montako jäsentä poistettiin
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaKerho();
     *   kerho.etsi("*",0).size() === 2;
     *   kerho.annaValineet(aku1).size() === 2;
     *   kerho.poista(aku1) === 1;
     *   kerho.etsi("*",0).size() === 1;
     *   kerho.annaValineet(aku1).size() === 0;
     *   kerho.annaValineet(aku2).size() === 3;
     * </pre>
     */
    public int poista(Jasen jasen) {
        if ( jasen == null ) return 0;
        int ret = jasenet.poista(jasen.getTunnusNro()); 
        valineet.poistaJasenenValineet(jasen.getTunnusNro()); 
        return ret; 
    }
    

    /** 
     * Poistaa tämän välinen 
     * @param valine poistettava väline 
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaKerho();
     *   kerho.annaValineet(aku1).size() === 2;
     *   kerho.poistaValine(valine11);
     *   kerho.annaValineet(aku1).size() === 1;
     */ 
    public void poistaValine(Valine valine) { 
        valineet.poista(valine); 
    } 


    
    
    /**
     * @param jasen uusi jäsen
     * @throws SailoException heittää roskiin jos lisää
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Kerho kerho = new Kerho();
     * Jasen aku1 = new Jasen(), aku2 = new Jasen();
     * aku1.rekisteroi(); aku2.rekisteroi();
     * kerho.getJasenia() === 0;
     * kerho.lisaa(aku1); kerho.getJasenia() === 1;
     * kerho.lisaa(aku2); kerho.getJasenia() === 2;
     * kerho.lisaa(aku1); kerho.getJasenia() === 3;
     * kerho.getJasenia() === 3;
     * kerho.annaJasen(0) === aku1;
     * kerho.annaJasen(1) === aku2;
     * kerho.annaJasen(2) === aku1;
     * kerho.annaJasen(3) === aku1; #THROWS IndexOutOfBoundsException
     * kerho.lisaa(aku1); kerho.getJasenia() === 4;
     * kerho.lisaa(aku1); kerho.getJasenia() === 5;
     * </pre>
     */
    public void lisaa(Jasen jasen) throws SailoException {
        jasenet.lisaa(jasen);
    }
    
    
    /** 
     * Korvaa jäsenen tietorakenteessa.  Ottaa jäsenen omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva jäsen.  Jos ei löydy, 
     * niin lisätään uutena jäsenenä. 
     * @param jasen lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException sailo
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaKerho();
     *  kerho.etsi("*",0).size() === 2;
     *  kerho.korvaaTaiLisaa(aku1);
     *  kerho.etsi("*",0).size() === 2;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Jasen jasen) throws SailoException { 
        jasenet.korvaaTaiLisaa(jasen); 
    } 

    
    
    /**
     * Lisätään uusi kokemus kerhoon
     * @param har lisättävä kokemus
     */
    public void lisaa(Kokemus har) {
        kokemukset.lisaa(har);
    }
    
    /**
     * Lisätään uusi valine kerhoon
     * @param val lisättävä valine
     */
    public void lisaa(Valine val) {
        valineet.lisaa(val);
    }
    
    
    /**
     * Palauttaa i:n jäsenen
     * @param i monesko jäsen palautetaan
     * @return viite i:teen jäseneen
     * @throws IndexOutOfBoundsException jos i Väärin
     */
    public Jasen annaJasen(int i) throws IndexOutOfBoundsException {
        return jasenet.anna(i);
    }
    

    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien jäsenten viitteet
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi 
     * @return tietorakenteen löytyneistä jäsenistä
     * @throws SailoException Jos jotakin menee väärin
     */ 
    public Collection<Jasen> etsi(String hakuehto, int k) throws SailoException { 
        return jasenet.etsi(hakuehto, k); 
    }
    
    
    /**
     * Haetaan kaikki jäsen kokemukset
     * @param jasen jäsen jolle kokemuksia haetaan
     * @return tietorakenne jossa viitteet löydetteyihin kokemuksiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Kerho kerho = new Kerho();
     *  Jasen aku1 = new Jasen(), aku2 = new Jasen(), aku3 = new Jasen();
     *  aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi();
     *  int id1 = aku1.getTunnusNro();
     *  int id2 = aku2.getTunnusNro();
     *  Kokemus pitsi11 = new Kokemus(id1); kerho.lisaa(pitsi11);
     *  Kokemus pitsi12 = new Kokemus(id1); kerho.lisaa(pitsi12);
     *  Kokemus pitsi21 = new Kokemus(id2); kerho.lisaa(pitsi21);
     *  Kokemus pitsi22 = new Kokemus(id2); kerho.lisaa(pitsi22);
     *  Kokemus pitsi23 = new Kokemus(id2); kerho.lisaa(pitsi23);
     *  
     *  List<Kokemus> loytyneet;
     *  loytyneet = kerho.annaKokemukset(aku3);
     *  loytyneet.size() === 0; 
     *  loytyneet = kerho.annaKokemukset(aku1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == pitsi11 === true;
     *  loytyneet.get(1) == pitsi12 === true;
     *  loytyneet = kerho.annaKokemukset(aku2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == pitsi21 === true;
     * </pre> 
     */
    public List<Kokemus> annaKokemukset(Jasen jasen) {
        return kokemukset.annaKokemukset(jasen.getTunnusNro());
    }

    
    /**
     * Haetaan kaikki jäsen välineet
     * @param jasen jäsen jolle välineitä haetaan
     * @return tietorakenne jossa viitteet löydetteyihin välineisiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Kerho kerho = new Kerho();
     *  Jasen aku1 = new Jasen(), aku2 = new Jasen(), aku3 = new Jasen();
     *  aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi();
     *  int id1 = aku1.getTunnusNro();
     *  int id2 = aku2.getTunnusNro();
     *  Valine valine11 = new Valine(id1); kerho.lisaa(valine11);
     *  Valine valine12 = new Valine(id1); kerho.lisaa(valine12);
     *  Valine valine21 = new Valine(id2); kerho.lisaa(valine21);
     *  Valine valine22 = new Valine(id2); kerho.lisaa(valine22);
     *  Valine valine23 = new Valine(id2); kerho.lisaa(valine23);
     *  
     *  List<Valine> loytyneet;
     *  loytyneet = kerho.annaValineet(aku3);
     *  loytyneet.size() === 0; 
     *  loytyneet = kerho.annaValineet(aku1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == valine11 === true;
     *  loytyneet.get(1) == valine12 === true;
     *  loytyneet = kerho.annaValineet(aku2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == valine21 === true;
     * </pre> 
     */
    public List<Valine> annaValineet(Jasen jasen) {
        return valineet.annaValineet(jasen.getTunnusNro());
    }
    
    
     /**
      * Asettaa tiedostojen perusnimet
      * @param nimi uusi nimi
      */
     public void setTiedosto(String nimi) {
         File dir = new File(nimi);
         dir.mkdirs();
         //String hakemistonNimi = "";
         //if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
         jasenet.setTiedostonPerusNimi("alppaajat/nimet");
         valineet.setTiedostonPerusNimi("alppaajat/valineet");
     }
    
    
    /**
     * Lukee kerhon tiedot tiedostosta
     * @param nimi jota käytetään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
            jasenet = new Jasenet(); // jos luetaan olemassa olevaan niin helpoin tyhjentää näin
            valineet = new Valineet();
    
            setTiedosto(nimi);
            jasenet.lueTiedostosta("alppaajat/nimet");
            valineet.lueTiedostosta("alppaajat/valineet");
        }
    
    
    /**
     * Tallettaa kerhon tiedot tiedostoon
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
         String virhe = "";
         try {
             jasenet.setTiedostonPerusNimi("alppaajat/nimet");
             jasenet.tallenna();
         } catch ( SailoException ex ) {
             virhe = ex.getMessage();
         }
     
         try {
             valineet.setTiedostonPerusNimi("alppaajat/valineet");
             valineet.tallenna();
         } catch ( SailoException ex ) {
             virhe += ex.getMessage();
         }
         if ( !"".equals(virhe) ) throw new SailoException(virhe);
     }
    
    
    /**
     * Testiohjelma kerhosta
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Kerho kerho = new Kerho();
        
        try {
            /**File dir = new File("alppaajat");
            if (!dir.exists()) {
                System.out.println("'Alppaajat' -tiedostoa ei löydy. Luodaan tiedosto...");
                dir.mkdirs();
            }*/
            
            kerho.lueTiedostosta("alppaajat");
            
            Jasen aku1 = new Jasen(), aku2 = new Jasen();
            aku1.rekisteroi();
            aku1.vastaaAkuAnkka();
            aku2.rekisteroi();
            aku2.vastaaAkuAnkka();
            
            kerho.lisaa(aku1);
            kerho.lisaa(aku2);
            
            Kokemus pitsi1 = new Kokemus();
            pitsi1.vastaaKokemusAloittelija(aku2.getTunnusNro());
            Kokemus pitsi2 = new Kokemus();
            pitsi2.vastaaKokemusAloittelija(aku1.getTunnusNro());
            Kokemus pitsi3 = new Kokemus();
            pitsi3.vastaaKokemusAloittelija(aku2.getTunnusNro());
            Kokemus pitsi4 = new Kokemus();
            pitsi4.vastaaKokemusAloittelija(aku2.getTunnusNro());

            kerho.lisaa(pitsi1);
            kerho.lisaa(pitsi2);
            kerho.lisaa(pitsi3);
            kerho.lisaa(pitsi4);

            
            System.out.println("============= Kerhon testi =============");
            
            for (int i = 0; i < kerho.getJasenia(); i++) {
                Jasen jasen = kerho.annaJasen(i);
                System.out.println("Jäsen paikassa: " + i);
                jasen.tulosta(System.out);
                List<Kokemus> kokemukset = kerho.annaKokemukset(jasen);
                
                for (Kokemus har : kokemukset) {
                    System.out.print(har.getJasenNro() + " ");
                    har.tulosta(System.out);
                }
                
                List<Valine> valineet = kerho.annaValineet(jasen);
                
                for (Valine val : valineet) {
                    System.out.print(val.getJasenNro() + " ");
                    val.tulosta(System.out);
                }
            }
            
        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
