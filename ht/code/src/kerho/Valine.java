package kerho;

import static kanta.HetuTarkistus.rand;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Michal
 * @version 4 Mar 2025
 *
 */
public class Valine {
    private int tunnusNro;
    private int jasenNro;
    private String kovaline;
    private int aloitusvuosi;
    private String kokemustaso;
    

    private static int seuraavaNro    = 1;
    
    
    /**
     * Alustetaan valine. Valine
     */
    public Valine() {
        //
    }


    /**
     * Alustetaan tietyn jäsenen kokemus.  
     * @param jasenNro jäsenen viitenumero 
     */
    public Valine(int jasenNro) {
        this.jasenNro = jasenNro;
    }
    
    /**
     * @return jäsenen väline
     */
    public String getValine() {
        return kovaline;
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Kokemukselle.
     * Aloitusvuosi arvotaan, jotta kahdella kokemuksella ei olisi
     * samoja tietoja.
     * @param nro viite henkilöön, jonka kokemuksesta on kyse
     */
    public void vastaaValineSukset(int nro) {
        jasenNro = nro;
        kovaline = "Sukset";
        aloitusvuosi = rand(1900, 2000);
        kokemustaso = "Kokenut";
    }


    /**
     * Tulostetaan kokemuksen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(kovaline + " " + aloitusvuosi + " " + kokemustaso);
    }


    /**
     * Tulostetaan henkilön tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa välineelle seuraavan rekisterinumeron.
     * @return välineen uusi tunnus_nro
     * @example
     * <pre name="test">
     *   Kokemus pitsi1 = new Kokemus();
     *   pitsi1.getTunnusNro() === 0;
     *   pitsi1.rekisteroi();
     *   Kokemus pitsi2 = new Kokemus();
     *   pitsi2.rekisteroi();
     *   int n1 = pitsi1.getTunnusNro();
     *   int n2 = pitsi2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palautetaan kokemuksen oma id
     * @return kokemuksen id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }

     /**
      * Asettaa tunnusnumeron ja samalla varmistaa että
      * seuraava numero on aina suurempi kuin tähän mennessä suurin.
      * @param nr asetettava tunnusnumero
      */
     private void setTunnusNro(int nr) {
         tunnusNro = nr;
         if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
     }
    
     
        /**
      * Palauttaa valineen tiedot merkkijonona jonka voi tallentaa tiedostoon.
      * @return valine tolppaeroteltuna merkkijonona
      * @example
      * <pre name="test">
      *   Valine valine = new Valine();
      *   valine.parse("   2   |  sukset  | 1949 | kokenut ");
      *   valine.toString()    === "2|sukset|1949|kokenut";
      * </pre>
      */
     @Override
     public String toString() {
         return "" + getTunnusNro() + "|" + jasenNro + "|" + kovaline + "|" + aloitusvuosi + "|" + kokemustaso;
     }
    
    

    /**
     * Palautetaan mille jäsenelle Kokemuskuuluu
     * @return jäsenen id
     */
    public int getJasenNro() {
        return jasenNro;
    }

   
   /**
    * Selvitää välineen tiedot | erotellusta merkkijonosta.
    * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
    * @param rivi josta välineen tiedot otetaan
    */
    public void parse(String rivi) {
           StringBuffer sb = new StringBuffer(rivi);
           setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
           jasenNro = Mjonot.erota(sb, '|', jasenNro);
           kovaline = Mjonot.erota(sb, '|', kovaline);
           aloitusvuosi = Mjonot.erota(sb, '|', aloitusvuosi);
           kokemustaso = Mjonot.erota(sb, '|', kokemustaso);
       }
   
   
       @Override
       public boolean equals(Object obj) {
           if ( obj == null ) return false;
           return this.toString().equals(obj.toString());
       }
      
   
       @Override
       public int hashCode() {
           return tunnusNro;
       }
   

    /**
     * Testiohjelma Kokemukselle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Valine val = new Valine();
        val.vastaaValineSukset(2);
        val.tulosta(System.out);
    }

}
