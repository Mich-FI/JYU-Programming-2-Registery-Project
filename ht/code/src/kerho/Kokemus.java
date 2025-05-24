package kerho;

import java.io.OutputStream;
import java.io.PrintStream;

import static kanta.HetuTarkistus.rand;


/**
 * @author Michal
 * @version 28 Mar 2025
 *
 */
public class Kokemus {
    
    private int tunnusNro;
    private int jasenNro;
    private String kokemustaso;
    private int aloitusvuosi;
    

    private static int seuraavaNro    = 1;
    
    
    /**
     * Alustetaan kokemus
     */
    public Kokemus() {
        //
    }


    /**
     * Alustetaan tietyn jäsenen kokemus.  
     * @param jasenNro jäsenen viitenumero 
     */
    public Kokemus(int jasenNro) {
        this.jasenNro = jasenNro;
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Kokemukselle.
     * Aloitusvuosi arvotaan, jotta kahdella kokemuksella ei olisi
     * samoja tietoja.
     * @param nro viite henkilöön, jonka kokemuksesta on kyse
     */
    public void vastaaKokemusAloittelija(int nro) {
        jasenNro = nro;
        kokemustaso = "Aloittelija";
        aloitusvuosi = rand(1900, 2000);
    }


    /**
     * Tulostetaan kokemuksen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(kokemustaso + " " + aloitusvuosi);
    }


    /**
     * Tulostetaan henkilön tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa kokemukselle seuraavan rekisterinumeron.
     * @return kokemuksen uusi tunnus_nro
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
     * Palautetaan mille jäsenelle Kokemuskuuluu
     * @return jäsenen id
     */
    public int getJasenNro() {
        return jasenNro;
    }


    /**
     * Testiohjelma Kokemukselle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Kokemus har = new Kokemus();
        har.vastaaKokemusAloittelija(2);
        har.tulosta(System.out);
    }

}
