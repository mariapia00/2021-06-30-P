package it.polito.tdp.genes.model;

public class Edge {

	private String l1;
	private String l2;
	private double peso;
	
	public Edge(String l1, String l2, double peso) {
		super();
		this.l1 = l1;
		this.l2 = l2;
		this.peso = peso;
	
	}
	public String getL1() {
		return l1;
	}
	public String getL2() {
		return l2;
	}
	public double getPeso() {
		return peso;
	}
	@Override
	public String toString() {
		return "Edge [l1=" + l1 + ", l2=" + l2 + ", peso=" + peso + "]";
	}
	
	
	
}
