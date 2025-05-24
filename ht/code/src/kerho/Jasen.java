package kerho;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.HetuTarkistus;

import static kanta.HetuTarkistus.arvoHetu;

/**
 * @author Michal
 * @version 24 Feb 2025
 *
 */
public class Jasen implements Cloneable {
    
    private  int       tunnusNro;
    private String     nimi           = "";
    private String     hetu           = "";
    private String     sahkoposti     = "";
    private String     katuosoite     = "";
    private String     postinumero    = "";
    private String     puhelin        = "";
    private double     pituus         = 0;
    private double     paino          = 0;
    private double     kengankoko     = 0;
    private String     kokemus        = "";
    private int        liittymisvuosi = 0;
    private String     lisatietoja    = "";

    private static int seuraavaNro    = 1;
    
    
    /**
     * @return montako kenttaa
     */
    public int getKenttia() {
        return 13;
    }
    
    /**
     * Eka kenttä joka on mielekäs kysyttäväksi
     * @return ekan kentän indeksi
     */
    public int ekaKentta() {
        return 1;
    }
    
    /**
     * @return jäsenen nimi
     * @example
     * <pre name="test">
     *   Jasen aku = new Jasen();
     *   aku.vastaaAkuAnkka();
     *   aku.getNimi() =R= "Ankka Aku .*";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }
    
    
    /**
     * @return pituus
     */
    public double getPituus() {
        return pituus;
    }
    
    /**
     * @return kengan koko
     */
    public double getKenganKoko() {
        return kengankoko;
    }
    
    
    /**
     * @return kokemustaso
     */
    public String getKokemus() {
        return kokemus;
    }
    
    /**
     * Asettaa nimen ja palauttaa virheen
     * @param s uusi nimi
     * @return null jos ok, muuten virhe
     */
    public String setNimi(String s) {
        this.nimi = s;
        return null;
    }
    
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi;
        case 2: return "" + hetu;
        case 3: return "" + sahkoposti;
        case 4: return "" + katuosoite;
        case 5: return "" + postinumero;
        case 6: return "" + puhelin;
        case 7: return "" + pituus;
        case 8: return "" + paino;
        case 9: return "" + kengankoko;     
        case 10: return "" + kokemus;
        case 11: return "" + liittymisvuosi;
        case 12: return "" + lisatietoja;
        default: return "Äääliö";
        }
    }
    
    /**
     * Palauttaa k:tta jäsenen kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttää vastaava kysymys
     */
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "tunnusNro";
        case 1: return "nimi";
        case 2: return "hetu";
        case 3: return "sahkoposti";
        case 4: return "katuosoite";
        case 5: return "postinumero";
        case 6: return "puhelin";
        case 7: return "pituus";
        case 8: return "paino";
        case 9: return "kengankoko";
        case 10: return "kokemus";
        case 11: return "liittymisvuosi";
        case 12: return "lisatietoja";
        default: return "Äääliö";
        }
    }
    
    
    /**
     * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kentän arvo asetetaan
     * @param jono jonoa joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     * @example
     * <pre name="test">
     *   Jasen jasen = new Jasen();
     *   jasen.aseta(1,"Ankka Aku") === null;
     *   jasen.aseta(2,"kissa") =R= "Hetu liian lyhyt"
     *   jasen.aseta(2,"030201-1111") === "Tarkistusmerkin kuuluisi olla C"; 
     *   jasen.aseta(2,"030201-111C") === null; 
     *   jasen.aseta(11,"kissa") === "Liittymisvuosi väärin jono = \"kissa\"";
     *   jasen.aseta(9,"1940") === null;
     * </pre>
     */

     public String aseta(int k, String jono) {
         String tjono = jono.trim();    
         StringBuffer sb = new StringBuffer(tjono);
         switch ( k ) {
         case 0:
             setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
             return null;
         case 1:
             nimi = tjono;
             return null;
         case 2:
             HetuTarkistus hetut = new HetuTarkistus();
             String virhe = hetut.tarkista(tjono);
             if ( virhe != null ) return virhe;
             hetu = tjono;
             return null;
         case 3:
             sahkoposti = tjono;
             return null;
         case 4:
             katuosoite = tjono;
             return null;
         case 5:
             postinumero = tjono;
             return null;
         case 6:
             puhelin = tjono;
             return null;
         case 7:
             pituus = Mjonot.erota(sb, '§', pituus);
             return null;
         case 8:
             paino = Mjonot.erota(sb, '§', paino);
             return null;
         case 9:
             kengankoko = Mjonot.erota(sb, '§', kengankoko);
             return null;
         case 10:
             kokemus = tjono;
             return null;
         case 11:
             try {
                 liittymisvuosi = Mjonot.erotaEx(sb, '§', liittymisvuosi);
             } catch ( NumberFormatException ex ) {
                 return "Liittymisvuosi väärin " + ex.getMessage();
             }
             return null;
         case 12:
             lisatietoja = Mjonot.erota(sb, '§', lisatietoja);
             return null;
         default:
             return "ÄÄliö";
         }
     }

    
    
    /**
     * 
     */
    public Jasen() {
        
    }
    
    
    /**
     * @param os os
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Tulostetaan henkilön tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro, 3) + "  " + nimi + "  "
                + hetu);
        out.println("  " + sahkoposti + " " + katuosoite + "  " + postinumero);
        out.println("  p: " + puhelin);
        out.print(pituus);
        out.print(paino);
        out.print(" Kengän koko on " + kengankoko + ".");
        out.println("  Kokemus: " + kokemus);
        out.print("  Liittynyt " + liittymisvuosi + ".");
        out.println("  " + lisatietoja);
    }
    
    
    /**
     * Palauttaa jäsenen tunnusnumeron.
     * @return jäsenen tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    /**
     * Antaa jäsenelle seuraavan rekisterinumeron.
     * @return jäsenen uusi tunnusNro
     * @example
     * <pre name="test">
     *   Jasen aku1 = new Jasen();
     *   aku1.getTunnusNro() === 0;
     *   aku1.rekisteroi();
     *   Jasen aku2 = new Jasen();
     *   aku2.rekisteroi();
     *   int n1 = aku1.getTunnusNro();
     *   int n2 = aku2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.tunnusNro = seuraavaNro;
        seuraavaNro++;
        return this.tunnusNro;
    }
    
    
     /**
      * Asettaa tunnusnumeron ja samalla varmistaa että
      * seuraava numero on aina suurempi kuin tähän mennessä suurin.
      * @param nr asetettava tunnusnumero
      */
     private void setTunnusNro(int nr) {
         tunnusNro = nr;
         if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
     }
    
  
     /**
      * Palauttaa jäsenen tiedot merkkijonona jonka voi tallentaa tiedostoon.
      * @return jäsen tolppaeroteltuna merkkijonona
      * @example
      * <pre name="test">
      *   Jasen jasen = new Jasen();
      *   jasen.parse("   3  |  Ankka Aku   | 030201-111C");
      *   jasen.toString().startsWith("3|Ankka Aku|030201-111C|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
      * </pre> 
      */
     @Override
     public String toString() {
         return "" +
                 getTunnusNro() + "|" +
                 nimi + "|" +
                 hetu + "|" +
                 sahkoposti + "|" +
                 katuosoite + "|" +
                 postinumero + "|" +
                 puhelin + "|" +
                 pituus + "|" +
                 paino + "|" +
                 kengankoko + "|" +
                 kokemus + "|" +
                 liittymisvuosi + "|" +
                 lisatietoja;
     }
    
    
    /**
     * Selvitää jäsenen tiedot | erotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta jäsenen tiedot otetaan
     *
     * @example
     * <pre name="test">
     *   Jasen jasen = new Jasen();
     *   jasen.parse("   3  |  Ankka Aku   | 030201-111C");
     *   jasen.getTunnusNro() === 3;
     *   jasen.toString().startsWith("3|Ankka Aku|030201-111C|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     *
     *   jasen.rekisteroi();
     *   int n = jasen.getTunnusNro();
     *   jasen.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   jasen.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   jasen.getTunnusNro() === n+20+1;
     *     
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        nimi = Mjonot.erota(sb, '|', nimi);
        hetu = Mjonot.erota(sb, '|', hetu);
        sahkoposti = Mjonot.erota(sb, '|', sahkoposti);
        katuosoite = Mjonot.erota(sb, '|', katuosoite);
        postinumero = Mjonot.erota(sb, '|', postinumero);
        puhelin = Mjonot.erota(sb, '|', puhelin);
        pituus = Mjonot.erota(sb, '|', pituus);
        paino = Mjonot.erota(sb, '|', paino);
        kengankoko = Mjonot.erota(sb, '|', kengankoko);
        kokemus = Mjonot.erota(sb, '|', kokemus);
        liittymisvuosi = Mjonot.erota(sb, '|', liittymisvuosi);
        lisatietoja = Mjonot.erota(sb, '|', lisatietoja);
    }
  
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot jäsenelle.
     * @param apuhetu hetu joka annetaan henkilölle 
     */
    public void vastaaAkuAnkka(String apuhetu) {
        nimi = "Ankka Aku " + HetuTarkistus.rand(1, 9999);
        hetu = apuhetu;
        sahkoposti = "ANKKALINNA@gmail.com";
        katuosoite = "Paratiisitie 13";
        postinumero = "12345";
        puhelin = "12-1234";
        pituus = 180;
        paino = 70.00;
        kengankoko = 42;
        kokemus = "Kokenut";
        liittymisvuosi = 1996;
        lisatietoja = "Velkaa Roopelle";
    }
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot jäsenelle.
     * Henkilötunnus arvotaan, jotta kahdella jäsenellä ei olisi
     * samoja tietoja.
     */
    public void vastaaAkuAnkka() {
        String apuhetu = arvoHetu();
        vastaaAkuAnkka(apuhetu);
    }
    
    
    @Override
    public boolean equals(Object jasen) {
        if ( jasen == null ) return false;
        return this.toString().equals(jasen.toString());
    }
  
  
    @Override
    public int hashCode() {
        return tunnusNro;
    }
    
    
    /**
     * Tehdään identtinen klooni jäsenestä
     * @return kloonattu jäsen
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Jasen jasen = new Jasen();
     *   jasen.parse("   3  |  Ankka Aku   | 123");
     *   Jasen kopio = jasen.clone();
     *   Object olio = jasen.clone();
     *   kopio.toString() === jasen.toString();
     *   jasen.parse("   4  |  Ankka Tupu   | 123");
     *   kopio.toString().equals(jasen.toString()) === false;
     *   olio instanceof Jasen === true;
     * </pre>
     */
    @Override
    public Jasen clone() throws CloneNotSupportedException { 
        Jasen uusi;
        uusi = (Jasen)super.clone();
        return uusi;
    }

    
    
    /**
     * @param args args
     */
    public static void main(String args[]) {
        
        Jasen aku = new Jasen(), aku2 = new Jasen();
        aku.rekisteroi();
        aku2.rekisteroi();
        
        aku.tulosta(System.out);     
        aku2.tulosta(System.out);

        aku.vastaaAkuAnkka();
        aku2.vastaaAkuAnkka();
        
        aku.tulosta(System.out);
        aku2.tulosta(System.out);
    }
    
}
