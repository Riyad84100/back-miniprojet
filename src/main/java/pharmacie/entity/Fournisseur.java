package pharmacie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NonNull
    @NotBlank
    @Column(unique = true, length = 255)
    private String nom;

    @NonNull
    @Email
    @NotBlank
    @Column(unique = true, length = 255)
    private String email;

    @ManyToMany
    @JoinTable(
        name = "fournisseur_categorie",
        joinColumns = @JoinColumn(name = "fournisseur_id"),
        inverseJoinColumns = @JoinColumn(name = "categorie_id")
    )
    @ToString.Exclude
    private List<Categorie> categories = new LinkedList<>();
}