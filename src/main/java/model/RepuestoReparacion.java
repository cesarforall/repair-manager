package model;

import jakarta.persistence.*;

@Entity
@Table(name = "REPUESTOS_REPARACIONES")
@IdClass(RepuestoReparacionId.class)
public class RepuestoReparacion {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_reparacion", nullable = false, foreignKey = @ForeignKey(name = "fk_repuesto_reparacion_reparacion"))
    private Reparacion reparacion;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_repuesto", nullable = false, foreignKey = @ForeignKey(name = "fk_repuesto_reparacion_repuesto"))
    private Repuesto repuesto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    public RepuestoReparacion() {}

    public RepuestoReparacion(Reparacion reparacion, Repuesto repuesto, int cantidad) {
        this.reparacion = reparacion;
        this.repuesto = repuesto;
        this.cantidad = cantidad;
    }

    public Reparacion getReparacion() {
        return reparacion;
    }

    public void setReparacion(Reparacion reparacion) {
        this.reparacion = reparacion;
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Repuesto repuesto) {
        this.repuesto = repuesto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
