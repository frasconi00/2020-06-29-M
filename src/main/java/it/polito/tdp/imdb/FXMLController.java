/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	Integer anno = this.boxAnno.getValue();
    	
    	if(anno==null) {
    		txtResult.setText("Devi scegliere un anno per creare il grafo!\n");
    		return;
    	}
    	
    	this.model.creaGrafo(anno);
    	
    	txtResult.setText("Grafo creato");
    	txtResult.appendText("\n# VERTICI: "+this.model.nVertici());
    	txtResult.appendText("\n# ARCHI: "+this.model.nArchi());
    	
    	this.boxRegista.getItems().clear();
    	this.boxRegista.getItems().addAll(this.model.getDirectorsGrafo());
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	
    	Director director = boxRegista.getValue();
    	
    	if(director==null) {
    		txtResult.setText("Devi scegliere un regista dopo aver creato il grafo!\n");
    		return;
    	}
    	
    	List<Vicino> vicini = this.model.getVicini(director);
    	
    	txtResult.setText("REGISTI ADIACENTI A:"+director.toString());
    	for(Vicino v : vicini) {
    		txtResult.appendText("\n"+v.getDirector().toString()+" - # attori condivisi: "+v.getPeso());
    	}

    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	
    	Director director = boxRegista.getValue();
    	
    	if(director==null) {
    		txtResult.setText("Devi scegliere un regista dopo aver creato il grafo!\n");
    		return;
    	}
    	
    	try {
    		int c = Integer.parseInt(txtAttoriCondivisi.getText());
    		this.model.preparaRicorsione(director, c);
        	List<Director> best = this.model.getBest();
        	
        	if(best.size()==0 || best.size()==1)
        		txtResult.setText("Nessun percorso trovato");
        	
        	txtResult.setText("Percorso trovato:");
        	for(Director d : best) {
        		txtResult.appendText("\n"+d.toString());
        	}
    	}catch(NumberFormatException e) {
    		txtResult.setText("Devi inserire un numero di attori condivisi!");
    		return;
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        this.boxAnno.getItems().clear();
        for(int anno=2004;anno<=2006;anno++) {
        	this.boxAnno.getItems().add(anno);
        }

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	
    }
    
}
