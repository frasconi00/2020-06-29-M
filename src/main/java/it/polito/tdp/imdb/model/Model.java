package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	
	private Map<Integer,Director> idMapDirectors;
	
	private Graph<Director,DefaultWeightedEdge> grafo;
	
	private List<Director> best;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.idMapDirectors = new HashMap<Integer, Director>();
		this.dao.listAllDirectors(idMapDirectors);
	}
	
	public void creaGrafo(int anno) {
		
		//creo il grafo
		this.grafo = new SimpleWeightedGraph<Director, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(anno));
		
//		System.out.println("#vertici: "+this.grafo.vertexSet().size());
		
		//aggiungo gli archi
		for(Adiacenza a : this.dao.getAdiacenze(idMapDirectors, anno)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
		}
		
//		System.out.println("#archi: "+this.grafo.edgeSet().size());
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean grafoCreato() {
		if(this.grafo == null)
			return false;
		else
			return true;
	}
	
	public List<Director> getDirectorsGrafo() {
		
		List<Director> lista = new ArrayList<Director>(this.grafo.vertexSet());
		
		Collections.sort(lista, new Comparator<Director>() {

			@Override
			public int compare(Director o1, Director o2) {
				return o1.id-o2.id;
			}
		});
		
		return lista;
	}
	
	public List<Vicino> getVicini(Director director) {
		
		List<Vicino> vicini = new ArrayList<Vicino>();
		
		for(Director d : Graphs.neighborListOf(this.grafo, director)) {
			
			Vicino v = new Vicino(d,(int) this.grafo.getEdgeWeight(this.grafo.getEdge(d, director)));
			vicini.add(v);
		}
		
		Collections.sort(vicini, new Comparator<Vicino>() {

			@Override
			public int compare(Vicino o1, Vicino o2) {
				return -(o1.getPeso()-o2.getPeso());
			}
		});
		
		return vicini;
		
	}
	
	public void preparaRicorsione(Director partenza, int c) {
		
		this.best = new ArrayList<Director>();
		
		List<Director> parziale = new ArrayList<Director>();
		parziale.add(partenza);
		
		cerca(parziale,c);
		
	}

	private void cerca(List<Director> parziale, int c) {
		
		int peso = pesoLista(parziale);
		
		if(peso>c) { // non continuare a esplorare: non va bene
			return;
		}
		
		if(parziale.size()>this.best.size()) {
			//soluzione migliore
			this.best = new ArrayList<Director>(parziale);
		}
		
		for(Director d : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			
			if(!parziale.contains(d)) {
				parziale.add(d);
				cerca(parziale, c);
				parziale.remove(parziale.size()-1);
			}
			
		}
		
	}
	
	public int pesoLista(List<Director> lista) {
		
		if(lista.size()==0 || lista.size()==1) return 0;
		
		int peso=0;
		
		for(int i=0;i<lista.size()-1;i++) {
			peso+=this.grafo.getEdgeWeight(this.grafo.getEdge(lista.get(i), lista.get(i+1)));
		}
		
		return peso;
		
	}

	public List<Director> getBest() {
		return best;
	}
	
	public int pesoListaDebug(List<Director> lista) {
		
		if(lista.size()==0 || lista.size()==1) return 0;
		
		int peso=0;
		
		for(int i=0;i<lista.size()-1;i++) {
			peso+=this.grafo.getEdgeWeight(this.grafo.getEdge(lista.get(i), lista.get(i+1)));
			System.out.println("Peso arco "+(i+1)+": "+this.grafo.getEdgeWeight(this.grafo.getEdge(lista.get(i), lista.get(i+1))));
		}
		
		return peso;
		
	}

}
