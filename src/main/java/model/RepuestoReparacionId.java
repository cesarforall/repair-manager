package model;

import java.io.Serializable;
import java.util.Objects;

public class RepuestoReparacionId implements Serializable {

	/**
	 * Clase embebida que representa la clave compuesta para la entidad RepuestoReparacion.
	 * Combina los IDs de reparación y repuesto.
	 */
	private static final long serialVersionUID = 1L;
	private int reparacion;
    private int repuesto;

    public RepuestoReparacionId() {}

    public RepuestoReparacionId(int reparacion, int repuesto) {
        this.reparacion = reparacion;
        this.repuesto = repuesto;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RepuestoReparacionId repuestoReparacionId = (RepuestoReparacionId) obj;
        return Objects.equals(reparacion, repuestoReparacionId.reparacion) &&
               Objects.equals(repuesto, repuestoReparacionId.repuesto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reparacion, repuesto);
    }

    // Getters y setters
    public int getReparacion() {
        return reparacion;
    }

    public void setReparacion(int reparacion) {
        this.reparacion = reparacion;
    }

    public int getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(int repuesto) {
        this.repuesto = repuesto;
    }
}
