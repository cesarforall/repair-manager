package model;

import jakarta.persistence.*;

@Entity
@Table(name = "COMPONENTES")
public class Componente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_componente")
    private int idComponente;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private double precio;

    public Componente() {}

    public Componente(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getidComponente() {
        return idComponente;
    }

    public void setidComponente(int idComponente) {
        this.idComponente = idComponente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    @Override
   	public String toString() {
   		return "C" + idComponente + " " + nombre;
   	}
}
