package ht.fxLasketteluKerho;

import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import kerho.Jasen;
import kerho.Kerho;
import kerho.Kokemus;
import kerho.SailoException;
import kerho.Valine;

/**
 * @author Michal
 * @version 19.2.2025
 *
 */
public class LasketteluKerhoGUIController implements Initializable {
    
    @FXML private TextField hakuehto;
    @FXML private ListChooser<Jasen> chooserJasenet;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    
    @FXML private TextField editKenganKoko;
    @FXML private TextField editNimi;
    @FXML private TextField editValineenPituus;
    
    @FXML private StringGrid<Valine> tableLaskettelu;

    
    /**
     * @param arg0 url
     * @param arg1 bundle
     */
    @Override
    public void initialize(java.net.URL arg0, ResourceBundle arg1) {
        alusta();
    }
    
    @FXML private void handleHakuehto() {
        hae(0);
    }
    
    @FXML private void handleTallenna() {
        tallenna();
    }

    @FXML private void handleUusiJasen() {
        uusiJasen();
    }
    
    @FXML private void handleMuokkaaJasen() {
        muokkaa();
    }
    
    @FXML private void handlePoistaJasen() {
        poistaJasen();
    }
    
    @FXML private void handleUusiKokemus() {
        uusiKokemus();
    }
    
    @FXML private void handleUusiValine() {
        uusiValine();
    }
    
    @FXML private void handleAvaa() {
        avaa();
    }
    

    
    // ============================================================================
    
    private String kerhonnimi = "alppaajat";
    private Kerho kerho;
    private Jasen jasenKohdalla;
    private static Jasen apujasen = new Jasen();

    /**
     * alustetaan Interfacen komponentit
     */
    private void alusta() {
        
        cbKentat.clear(); 
        for (int k = apujasen.ekaKentta(); k < apujasen.getKenttia(); k++) 
            cbKentat.add(apujasen.getKysymys(k), null); 
        cbKentat.getSelectionModel().select(0);


        
        chooserJasenet.clear();
        chooserJasenet.addSelectionListener(e -> naytaJasen());
    }
    
    
    /**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    private String tallenna() {
        try {
            kerho.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
     
    /**
     * Uuden jäsenen luominen kerhoon
     */
    private void uusiJasen() {
        Jasen uusi = new Jasen();
        uusi.rekisteroi();
        uusi.vastaaAkuAnkka();
        try {
            kerho.lisaa(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa" + e.getMessage());
        }
        hae(uusi.getTunnusNro());
        naytaJasen();
    }
    
    private void muokkaa() {
        jasenKohdalla = chooserJasenet.getSelectedObject();
        if (jasenKohdalla == null) return;
        Jasen jasen;
        try {
            jasen = jasenKohdalla.clone();
            jasen = JasenDialogController.kysyJasen(null, jasen);
            if (jasen == null) return;
            kerho.korvaaTaiLisaa(jasen);
            hae(jasenKohdalla.getTunnusNro());
        } catch (CloneNotSupportedException e) { 
            // 
        } catch (SailoException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 

   }
    
    private void uusiKokemus() {
        jasenKohdalla = chooserJasenet.getSelectedObject();
        if (jasenKohdalla == null) return;
        Kokemus har = new Kokemus();
        har.rekisteroi();
        har.vastaaKokemusAloittelija(jasenKohdalla.getTunnusNro());
        kerho.lisaa(har);
        hae(jasenKohdalla.getTunnusNro());
    }
    
    
    private void uusiValine() {
        jasenKohdalla = chooserJasenet.getSelectedObject();
        if (jasenKohdalla == null) return;
        Valine val = new Valine();
        val.rekisteroi();
        val.vastaaValineSukset(jasenKohdalla.getTunnusNro());
        kerho.lisaa(val);
        hae(jasenKohdalla.getTunnusNro());
    }

    
    /**
     * Jo olemassaolevan jäsenen poistaminen
     */
    private void poistaJasen() {
        Jasen jasen = jasenKohdalla;
        if ( jasen == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko jäsen: " + jasen.getNimi(), "Kyllä", "Ei") )
            return;
        kerho.poista(jasen);
        int index = chooserJasenet.getSelectedIndex();
        hae(0);
        chooserJasenet.setSelectedIndex(index);
    }
    

    /**
     * @return false jos painetaan cancel
     */
    public boolean avaa() {
        String uusinimi = LasketteluKerhonNimiController.kysyNimi(null, kerhonnimi);
        if (uusinimi == null) return false;
        try {
            lueTiedosto(uusinimi);
        } catch (SailoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }
    
    
   private void setTitle(String title) {
           ModalController.getStage(hakuehto).setTitle(title);
       }
    
    
   /**
    * Alustaa kerhon lukemalla sen valitun nimisestä tiedostosta
    * @param nimi tiedosto josta kerhon tiedot luetaan
    * @return null jos onnistuu, muuten virhe tekstinä
    * @throws SailoException throw
    */
   protected String lueTiedosto(String nimi) throws SailoException {
       kerhonnimi = nimi;
       setTitle("Kerho - " + kerhonnimi);
       try {
           kerho.lueTiedostosta(nimi);
           hae(0);
           return null;
       } catch (SailoException e) {
           hae(0);
           String virhe = e.getMessage(); 
           if ( virhe != null ) Dialogs.showMessageDialog(virhe);
           return virhe;
       }
    }
    
   /**
    * näyttää jäsenen ja asettaa oikeat tekstit oikeisiin kohteen
    */
   private void naytaJasen() {
       Jasen jasenKohdalla1 = chooserJasenet.getSelectedObject();
       
       if (jasenKohdalla1 == null) return;
       
           editKenganKoko.setText(String.valueOf(mondoPoint()));
           editNimi.setText(jasenKohdalla1.getNimi());
           editValineenPituus.setText(String.valueOf(valineenPituus()));
           
           naytaLaskettelu(jasenKohdalla1);
       }
   
   /**
    * @return välineen pituus
    */
   public double valineenPituus() {
       Jasen jasenKohdalla1 = chooserJasenet.getSelectedObject();
       double vpituus;
       if (jasenKohdalla1.getKokemus() == "aloittelija") {
           vpituus = jasenKohdalla1.getPituus() - 15;
           return vpituus;
       }
       if (jasenKohdalla1.getKokemus() == "keskitasoinen") {
           vpituus = jasenKohdalla1.getPituus() - 5;
           return vpituus;
       }
       vpituus = jasenKohdalla1.getPituus() + 3;
       return vpituus;
   }
   
   
   /**
    * @return mondopoint
    */
   public double mondoPoint() {
       Jasen jasenKohdalla1 = chooserJasenet.getSelectedObject();
       if (jasenKohdalla1.getKenganKoko() == 35) {
           return 21.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 35.5) {
           return 22;
       }
       if (jasenKohdalla1.getKenganKoko() == 36) {
           return 22.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 36.5) {
           return 23;
       }
       if (jasenKohdalla1.getKenganKoko() == 37) {
           return 23.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 37.5) {
           return 24;
       }
       if (jasenKohdalla1.getKenganKoko() == 38) {
           return 24.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 38.5) {
           return 25;
       }
       if (jasenKohdalla1.getKenganKoko() == 39) {
           return 25.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 39.5) {
           return 26;
       }
       if (jasenKohdalla1.getKenganKoko() == 40) {
           return 26.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 40.5) {
           return 27;
       }
       if (jasenKohdalla1.getKenganKoko() == 41) {
           return 27.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 41.5) {
           return 28;
       }
       if (jasenKohdalla1.getKenganKoko() == 42) {
           return 28.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 42.5) {
           return 29;
       }
       if (jasenKohdalla1.getKenganKoko() == 43) {
           return 29.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 43.5) {
           return 30;
       }
       if (jasenKohdalla1.getKenganKoko() == 44) {
           return 30.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 44.5) {
           return 31;
       }
       if (jasenKohdalla1.getKenganKoko() == 45) {
           return 31.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 45.5) {
           return 32;
       }
       if (jasenKohdalla1.getKenganKoko() == 46) {
           return 32.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 46.5) {
           return 33;
       }
       if (jasenKohdalla1.getKenganKoko() == 47) {
           return 33.5;
       }
       if (jasenKohdalla1.getKenganKoko() == 47.5) {
           return 34;
       }
       return 34.5;
   }
   
   
   private void naytaLaskettelu(Jasen jasen) {
       tableLaskettelu.clear();
       if (jasen == null) return;
       List<Valine> valineet = kerho.annaValineet(jasen);
       if (valineet.size() == 0) return;
       
       for (Valine val: valineet) {
           naytaValine(val);
       }
   }
   
   
   private void naytaValine(Valine val) {
       String[] rivi = val.toString().split("\\|");
       tableLaskettelu.add(val, rivi[2], rivi[3], rivi[4]);
   }

    
    /**
     * Alustetaan käytettävä kerho
     * @param kerho jota käytetään
     */
    public void setKerho(Kerho kerho) {
        this.kerho = kerho;
    }

    
    /**
     * Haetaan jäsenen nimi ja katsotaan onko siinä samoja osia kuin hakuehdossa
     * @param jnr jäsennumero
     */
    protected void hae(int jnr) {
        int jnro1 = jnr;
        if (jnro1 <= 0) {
            Jasen jasen = chooserJasenet.getSelectedObject();
            if (jasen != null) jnro1 = jasen.getTunnusNro();
        }

        int k = cbKentat.getSelectedIndex() + apujasen.ekaKentta(); 
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 

        chooserJasenet.clear();

        int index = 0;
        try {
            Collection<Jasen> jasenet = kerho.etsi(ehto, k);
            int i = 0;
            for (Jasen jasen : jasenet) {
                if (jasen.getTunnusNro() == jnro1) index = i;
                chooserJasenet.add(jasen.getNimi(), jasen);
                i++;
            }
            chooserJasenet.setSelectedIndex(index);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Virhe haettaessa jäseniä: " + e.getMessage());
        }
    }
}
