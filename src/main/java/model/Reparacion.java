package model;

import jakarta.persistence.*;

@Entity
@Table(name = "REPARACIONES")
public class Reparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reparacion")
    private int idReparacion;

    @ManyToOne
    @JoinColumn(name = "id_dispositivo", nullable = false, foreignKey = @ForeignKey(name = "fk_reparacion_dispositivo"))
    private Dispositivo dispositivo;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false, foreignKey = @ForeignKey(name = "fk_reparacion_cliente"))
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_estado", foreignKey = @ForeignKey(name = "fk_reparacion_estado"))
    private Estado estado;

    @Column(name = "detalle")
    private String detalle;

    @Column(name = "f_entrada", nullable = false)
    private String fechaEntrada;

    @Column(name = "f_salida")
    private String fechaSalida;

    @Column(name = "enlace_doc")
    private String enlaceDocumento;

    @Column(name = "precio", nullable = false)
    private double precio;

    public Reparacion() {}

    public Reparacion(Dispositivo dispositivo, Cliente cliente, Estado estado, String detalle, String fechaEntrada, String fechaSalida, String enlaceDocumento, double precio) {
        this.dispositivo = dispositivo;
        this.cliente = cliente;
        this.estado = estado;
        this.detalle = detalle;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.enlaceDocumento = enlaceDocumento;
        this.precio = precio;
    }
    
    public Reparacion(Reparacion otra) {
    	this.idReparacion = otra.idReparacion;
    	this.dispositivo = otra.dispositivo;
    	this.cliente = otra.cliente;
    	this.estado = otra.estado;
    	this.detalle = otra.detalle;
    	this.fechaEntrada = otra.fechaEntrada;
    	this.fechaSalida = otra.fechaSalida;
    	this.enlaceDocumento = otra.enlaceDocumento;
    	this.precio = otra.precio;
    }

    public int getIdReparacion() {
        return idReparacion;
    }

    public void setIdReparacion(int idReparacion) {
        this.idReparacion = idReparacion;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEnlaceDocumento() {
        return enlaceDocumento;
    }

    public void setEnlaceDocumento(String enlaceDocumento) {
        this.enlaceDocumento = enlaceDocumento;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
