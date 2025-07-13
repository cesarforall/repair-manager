package model;

import jakarta.persistence.*;

@Entity
@Table(name = "COMPONENTES_REPARACIONES")
@IdClass(ComponenteReparacionId.class)
public class ComponenteReparacion {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_reparacion", nullable = false, foreignKey = @ForeignKey(name = "fk_componente_reparacion_reparacion"))
    private Reparacion reparacion;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_componente", nullable = false, foreignKey = @ForeignKey(name = "fk_componente_reparacion_componente"))
    private Componente componente;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    public ComponenteReparacion() {}

    public ComponenteReparacion(Reparacion reparacion, Componente componente, int cantidad) {
        this.reparacion = reparacion;
        this.componente = componente;
        this.cantidad = cantidad;
    }

    public Reparacion getReparacion() {
        return reparacion;
    }

    public void setReparacion(Reparacion reparacion) {
        this.reparacion = reparacion;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
