package it.polito.tdp.genes.model;

public class Statistica {

	private String localization;
	private double peso;
	public Statistica(String localization, double peso) {
		super();
		this.localization = localization;
		this.peso = peso;
	}
	@Override
	public String toString() {
		return localization + ": " + peso + "\n";
	}
	
}
