package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

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
		
		List<Author> vertex = dao.getAllAuthors(authorIdMap);
		Graphs.addAllVertices(this.graph, vertex);
		
		for(Author a : this.graph.vertexSet()) {
			
			List<Author> coauthors = dao.getAllCoauthors(a);
			
			a.addAllCoauthor(coauthors); //ho aggiunto all'autore la lista di coautori
			
			//collego nel grafo autore e coautori
			
			for(Author c : coauthors) {
				this.graph.addEdge(a, c);
			}
			
		}
	}

	public List<Author> getCoAuthors(String author) throws NoSuchFieldException {
		
		Author start = this.authorIdMap.get(author);
			
		if(start != null && this.graph.containsVertex(start)) {
			
			List<Author> visitati = new ArrayList<>();
			
			DepthFirstIterator<Author, DefaultEdge> bfv = new DepthFirstIterator<>(this.graph, start);
			
			while(bfv.hasNext()) {
				Author c = bfv.next();
				if(!c.equals(start))
					visitati.add(c);
			}
			
			Collections.sort(visitati);

			return visitati ;
		}
		
		if(!this.graph.containsVertex(start))
			throw new NoSuchFieldException();
		
		return null ;
	}
	
	public List<Paper> getSequenza(String primo, String secondo) {
		
		Author start = this.authorIdMap.get(primo);
		Author stop = this.authorIdMap.get(secondo);
		
		List<Paper> papers = new ArrayList<>();
		
		if(start == null || stop == null)
			throw new RuntimeException("Gli areoporti selezionati non sono presenti in memoria\n");
		
		ShortestPathAlgorithm<Author, DefaultEdge> spa = new DijkstraShortestPath<>(graph);
		
		GraphPath<Author, DefaultEdge> gp = spa.getPath(start, stop);
		
		Graph<Author, DefaultEdge> grafo = gp.getGraph();
		
		for(Author a : grafo.vertexSet()) {
			List<Author> neighbor = Graphs.neighborListOf(this.graph, a);
			
			Paper p = dao.getPaperFromAuthors(a, neighbor.get(neighbor.size()-1)) ;
			
			papers.add(p);
		}
		
		return papers ;
	}
}
