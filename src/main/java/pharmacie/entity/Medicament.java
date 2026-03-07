package pharmacie.entity;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Medicament {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Integer reference = null;

	@NonNull
	@Column(unique=true, length = 255)
	private String nom;

	private String quantiteParUnite = "Une boîte de 12";

	@PositiveOrZero
	private BigDecimal prixUnitaire = BigDecimal.TEN;

	@ToString.Exclude
	@PositiveOrZero
	private int unitesEnStock = 0;

	@ToString.Exclude
	@PositiveOrZero
	private int unitesCommandees = 0;

	@ToString.Exclude
	@PositiveOrZero
	private int niveauDeReappro = 0;

	@ToString.Exclude
	private boolean indisponible = false;

	@Column(length = 500)
	private String imageURL;

	@ToString.Exclude
	@JsonIgnoreProperties("medicaments")
	@NonNull
	@ManyToOne(optional = false)
	private Categorie categorie;

	@ToString.Exclude
	@JsonIgnore
	@OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ligne> lignes = new LinkedList<>();
}