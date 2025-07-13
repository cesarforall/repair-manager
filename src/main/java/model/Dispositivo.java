package model;

import jakarta.persistence.*;

@Entity
@Table(name = "DISPOSITIVOS")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispositivo")
    private int idDispositivo;

    @Column(nullable = false)
    private String tipo;    

	@Column(name = "fabricante", nullable = false)
    private String fabricante;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "n_serie", unique = true, nullable = false)
    private String numeroSerie;

    public Dispositivo() {}

    public Dispositivo(String tipo, String fabricante, String modelo, String numeroSerie) {
        this.tipo = tipo;
    	this.fabricante = fabricante;
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
    
    public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
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
		return idDispositivo + " " + tipo + " " + fabricante + " " + modelo + " " + numeroSerie;
	}
}
