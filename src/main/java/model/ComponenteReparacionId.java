package model;

import java.io.Serializable;
import java.util.Objects;

public class ComponenteReparacionId implements Serializable {

	/**
	 * Clase embebida que representa la clave compuesta para la entidad ComponenteReparacion.
	 * Combina los IDs de reparación y componente.
	 */
	private static final long serialVersionUID = 1L;
	private int reparacion;
    private int componente;

    public ComponenteReparacionId() {}

    public ComponenteReparacionId(int reparacion, int componente) {
        this.reparacion = reparacion;
        this.componente = componente;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ComponenteReparacionId componenteReparacionId = (ComponenteReparacionId) obj;
        return Objects.equals(reparacion, componenteReparacionId.reparacion) &&
               Objects.equals(componente, componenteReparacionId.componente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reparacion, componente);
    }

    // Getters y setters
    public int getReparacion() {
        return reparacion;
    }

    public void setReparacion(int reparacion) {
        this.reparacion = reparacion;
    }

    public int getComponente() {
        return componente;
    }

    public void setComponente(int componente) {
        this.componente = componente;
    }
}
