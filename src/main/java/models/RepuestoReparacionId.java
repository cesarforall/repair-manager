package models;

import java.io.Serializable;
import java.util.Objects;

public class RepuestoReparacionId implements Serializable {

    private Long reparacion;
    private Long repuesto;

    public RepuestoReparacionId() {}

    public RepuestoReparacionId(Long reparacion, Long repuesto) {
        this.reparacion = reparacion;
        this.repuesto = repuesto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepuestoReparacionId that = (RepuestoReparacionId) o;
        return Objects.equals(reparacion, that.reparacion) &&
               Objects.equals(repuesto, that.repuesto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reparacion, repuesto);
    }

    // Getters y setters
    public Long getReparacion() {
        return reparacion;
    }

    public void setReparacion(Long reparacion) {
        this.reparacion = reparacion;
    }

    public Long getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Long repuesto) {
        this.repuesto = repuesto;
    }
}
