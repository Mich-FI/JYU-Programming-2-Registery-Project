package ht.fxLasketteluKerho;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kerho.Jasen;

/**
 * Luokka Jasen dialogin controllerille, joka hallitsee toimintaa jasenen toimintaa
 * @author Michal
 * @version 8 May 2025
 *
 */
public class JasenDialogController implements ModalControllerInterface<Jasen>, Initializable {

    @FXML private TextField editNimi;
    @FXML private TextField editSotu;
    @FXML private TextField editKatuosoite;
    @FXML private TextField editSahkoposti;
    @FXML private TextField editPostinumero;
    
    @FXML private GridPane gridJasen;
    @FXML private Label labelVirhe;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        
    }
    
    @FXML private void handleOK() {
        if ( jasenKohdalla != null && jasenKohdalla.getNimi().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }

    
    @FXML private void handleCancel() {
        jasenKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

    
 // ========================================================
    
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    
    
    
    @Override
    public Jasen getResult() {
        return jasenKohdalla;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(Jasen oletus) {
        jasenKohdalla = oletus;
        naytaJasen(edits, jasenKohdalla);
        
    }
    
    private Jasen jasenKohdalla;
    private TextField[] edits;
    private static Jasen apujasen = new Jasen();
    
    
    /**
     * Luodaan GridPaneen jäsenen tiedot
     * @param gridJasen mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridJasen) {
        gridJasen.getChildren().clear();
        TextField[] edits = new TextField[apujasen.getKenttia()];
        
        for (int i=0, k = apujasen.ekaKentta(); k < apujasen.getKenttia(); k++, i++) {
            Label label = new Label(apujasen.getKysymys(k));
            gridJasen.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridJasen.add(edit, 1, i);
        }
        return edits;
    }

    
    private void alusta() {
        //edits = new TextField[] {editNimi, editSotu, editKatuosoite, editPostinumero};
        edits = luoKentat(gridJasen);
        for (TextField edit : edits) {
            if (edit != null) {
                //edit.setOnKeyReleased( e -> kasitteleMuutosJaseneen((TextField)(e.getSource())));
                edit.setOnKeyReleased( e -> kasitteleMuutosJaseneen(edit));
            }
        }
    }
    
    
    /**
     * Palautetaan komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna 
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }

    
    /**
     * Käsitellään jäseneen tullut muutos
     * @param edit muuttunut kenttä
     */
    private void kasitteleMuutosJaseneen(TextField edit) {
        if (jasenKohdalla == null) return;
        int k = getFieldId(edit,apujasen.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = jasenKohdalla.aseta(k, s);
        if (virhe == null) {
            naytaVirhe(virhe);
        } else {
            naytaVirhe(virhe);
        }
    }
    
    /**
     * @param edits edits taulukko
     * @param jasen jasen
     */
    public static void naytaJasen(TextField[] edits, Jasen jasen) {
        if (jasen == null) return;
        for (int k = jasen.ekaKentta(); k < jasen.getKenttia(); k++) {
            edits[k].setText(jasen.anna(k));
        }
        
    }
    
    /**
     * @param modalityStage modalityStage
     * @param oletus oletus
     * @return Jasen
     */
    public static Jasen kysyJasen(Stage modalityStage, Jasen oletus) {
        return ModalController.showModal(LasketteluKerhoGUIController.class.getResource("JasentiedotGUIView.fxml"), "Jäsenen muokkaus", modalityStage, oletus);
    }
    

}
