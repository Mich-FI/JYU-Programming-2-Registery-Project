package ht.fxLasketteluKerho;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Michal
 * @version 25 Feb 2025
 *
 */
public class LasketteluKerhonNimiController implements ModalControllerInterface<String> {
    
    @FXML private TextField kerhonNimi;
    
    @FXML private void handleOK() {
        vastaus = kerhonNimi.getText();
        ModalController.closeStage(kerhonNimi);
    }
    
    @FXML private void handleCancel() {
        ModalController.closeStage(kerhonNimi);
    }
    
    // =================================================================================
    
    
    private String vastaus = null;

    @Override
    public String getResult() {
        return vastaus;
    }
    
    @Override
    public void setDefault(String oletus) {
        kerhonNimi.setText(oletus);
    }
    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        kerhonNimi.requestFocus();
    }
    
    
    /**
     * Luodaan nimenkysymysdialogi ja palautetaan siihen kirjoitettu nimi tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä nimeä näytetään oletuksena
     * @return null jos painetaan Cancel, muuten kirjoitettu nimi
     * 
     */
    public static String kysyNimi(Stage modalityStage, String oletus) {
        return ModalController.showModal(
                LasketteluKerhonNimiController.class.getResource("KerhonLuominenGUIView.fxml"),
                "Kerho",
                modalityStage, oletus);
    }
}