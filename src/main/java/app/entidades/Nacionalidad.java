package app.entidades;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "nacionalidad")
public class Nacionalidad {
    
    public Nacionalidad(){
        super();
    }

    public Nacionalidad(int id, String pais) {
        this.id = id;
        this.pais = pais;
    }

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pais", nullable = false, unique = true)
    private String pais;

    @OneToMany(mappedBy = "nacionalidad", cascade = CascadeType.ALL)
    private List<Cliente06> clientes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    
}
