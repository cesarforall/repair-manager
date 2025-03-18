package model;

import jakarta.persistence.*;

@Entity
@Table(name = "TELEFONOS")
public class Telefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono")
    private int idTelefono;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false, foreignKey = @ForeignKey(name = "fk_telefono_cliente"))
    private Cliente cliente;

    @Column(name = "telefono", nullable = false, unique = true)
    private String telefono;

    public Telefono() {}

    public Telefono(Cliente cliente, String telefono) {
        this.cliente = cliente;
        this.telefono = telefono;
    }

    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
