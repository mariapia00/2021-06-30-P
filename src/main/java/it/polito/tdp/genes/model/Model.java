package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {

	private GenesDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> best;
	private double bestLunghezza;
	private ConnectivityInspector<String, DefaultWeightedEdge> isp;
	public Model() {
		dao = new GenesDao();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici());
		
		//aggiungo gli archi
		/*Ogni localizzazione contiene un insieme di geni,
		 *  i quali possono essere collegati tra di loro 
		 *  attraverso la tabella interactions. I vertici 
		 *  corrispondenti a due localizzazioni saranno collegati 
		 *  da un arco se e solo se esiste almeno una interazione 
		 *  che coinvolge due geni, rispettivamente della prima 
		 *  della seconda localizzazione (o nell’ordine inverso).
		 */
		
		for(Edge e : this.dao.getArchi()) {
			Graphs.addEdgeWithVertices(this.grafo, e.getL1(), e.getL2(), e.getPeso());
		}
	}

	public boolean isGrafoCreato() {
		if(this.grafo==null) {
			return false;
		}
		return true;
	}

	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}

	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}

	public Set<String> getVertici() {
		return this.grafo.vertexSet();
	}

	public List<Statistica> doStatistiche(String localization) {
		ConnectivityInspector<String, DefaultWeightedEdge> ispettore = new ConnectivityInspector<>(this.grafo);
		List<Statistica> result = new ArrayList<>();
		for(String l : ispettore.connectedSetOf(localization)) {
			if(this.grafo.containsEdge(localization, l)){ //IMPORTANTE
				result.add(new Statistica(l, this.grafo.getEdgeWeight(this.grafo.getEdge(localization, l))));
		
			}
		}
		return result;
		
	}

	public List<String> calcolaCammino(String localization){
		bestLunghezza = 0.0;
		best = new ArrayList<>();
		isp = new ConnectivityInspector<>(this.grafo);
		List<String> parziale = new ArrayList<>();
		parziale.add(localization);
		ricorsione(parziale);
		return best;
	
		
		
	}

	private void ricorsione(List<String> parziale) {
		if(lunghezza(parziale)>bestLunghezza ) {
			bestLunghezza = lunghezza(parziale);
			best = new ArrayList<>(parziale);
		}
		
		for(String s : isp.connectedSetOf(parziale.get(parziale.size()-1))) {
			DefaultWeightedEdge e = this.grafo.getEdge(s,parziale.get(parziale.size()-1));
			if(!parziale.contains(s) && e!=null) {
				parziale.add(s);
				ricorsione(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
	}

	public double lunghezza(List<String> parziale) {
		double peso = 0.0;
		for(int i = 0; i<parziale.size()-1; i++) {
			peso+=this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i), parziale.get(i+1)));
		}
		return peso;
	}
	
	public List<Edge> getBestCammino(){
		
		List<Edge> cammino = new ArrayList<Edge>();
		for(int i=0; i<best.size()-1;i++) {
			cammino.add(new Edge(best.get(i),best.get(i+1),(int) this.grafo.getEdgeWeight(this.grafo.getEdge(best.get(i),best.get(i+1)))));
		}
		
		return cammino;
	}
}