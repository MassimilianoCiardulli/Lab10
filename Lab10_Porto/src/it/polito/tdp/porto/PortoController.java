package it.polito.tdp.porto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> boxPrimo;

    @FXML
    private ComboBox<String> boxSecondo;

    @FXML
    private TextArea txtResult;
    
    private Model model ;

    @FXML
    void handleCoautori(ActionEvent event) {
    	String author = this.boxPrimo.getValue();
    	model.createGraph();
    	try {
			List<Author> coauthors = model.getCoAuthors(author);
			
			txtResult.setText("La lista dei coautori è:\n");
			for(Author a : coauthors)
				txtResult.appendText(a.getLastname() + " " +a.getFirstname() +"\n");
			
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			this.txtResult.appendText("L'autore selezionato non esiste\n");
		}
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	String first = this.boxPrimo.getValue();
    	String second = this.boxSecondo.getValue();
    	
    	if(first!=null && second!=null) {
    		model.createGraph();
    		List<Paper> papers = model.getSequenza(first, second);
    		if(!papers.isEmpty()) {
    			for(Paper p : papers)
    				this.txtResult.appendText(p.toString());
    		}
    		else
    			this.txtResult.setText("Non esiste una sequenza che colleghi i due autori.");
    	}
    	else
    		this.txtResult.setText("Seleziona due autori!");
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model m) {
		this.model = m;
		
		for(Author a : m.getAllAuthors()) {
			this.boxPrimo.getItems().add(a.getLastname() + " " +a.getFirstname());
			this.boxSecondo.getItems().add(a.getLastname() + " " +a.getFirstname());
		}
	}
}
