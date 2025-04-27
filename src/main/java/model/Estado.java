package model;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTADOS")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private int idEstado;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    @Column(name = "descripcion", nullable = false, unique = false)
    private String descripcion;

    public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Estado() {}

    public Estado(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
