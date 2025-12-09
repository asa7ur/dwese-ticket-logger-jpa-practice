package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "regions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.region.code.notEmpty}")
    @Size(max = 2, message = "{msg.region.code.size}")
    @Column(name = "code", nullable = false, length = 2)
    private String code;

    @NotEmpty(message = "{msg.region.name.notEmpty}")
    @Size(max = 100, message = "{msg.region.name.notEmpty}")
    @Column(name = "name",  nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Province> provinces;

    @Column(name = "image")
    private String image;

    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
