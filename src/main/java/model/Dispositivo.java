package model;

import jakarta.persistence.*;

@Entity
@Table(name = "DISPOSITIVOS")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispositivo")
    private int idDispositivo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "n_serie", unique = true, nullable = false)
    private String numeroSerie;

    public Dispositivo() {}

    public Dispositivo(String nombre, String modelo, String numeroSerie) {
        this.nombre = nombre;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
    }

    // Getters y Setters
    public int getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(int idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }
    
    @Override
	public String toString() {
		return idDispositivo + " " + nombre + " " + modelo + " " + numeroSerie;
	}
}
