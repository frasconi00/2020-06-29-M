package it.polito.tdp.imdb.model;

public class Vicino {
	
	private Director director;
	private int peso;
	public Vicino(Director director, int peso) {
		super();
		this.director = director;
		this.peso = peso;
	}
	public Director getDirector() {
		return director;
	}
	public void setDirector(Director director) {
		this.director = director;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}

}
