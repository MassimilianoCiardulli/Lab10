package it.polito.tdp.porto.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private PortoDAO dao ;
	private AuthorIdMap authorIdMap ;
	
	private Graph<Author, DefaultEdge> graph ;
	
	public Model() {
		dao = new PortoDAO();
		authorIdMap = new AuthorIdMap();
		graph = new SimpleGraph<>(DefaultEdge.class);
	}
	
	public List<Author> getAllAuthors(){
		return dao.getAllAuthors(authorIdMap);
	}

	public void createGraph() {
		
		Graphs.addAllVertices(this.graph, dao.getAllAuthors(authorIdMap));
		
	}

	public List<Author> getCoAuthors(String author) {
		// TODO Auto-generated method stub
		return null;
	}
}
