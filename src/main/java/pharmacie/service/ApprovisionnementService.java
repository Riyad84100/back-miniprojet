package pharmacie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pharmacie.dao.FournisseurRepository;
import pharmacie.dao.MedicamentRepository;
import pharmacie.entity.Categorie;
import pharmacie.entity.Fournisseur;
import pharmacie.entity.Medicament;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovisionnementService {

    private final MedicamentRepository medicamentRepository;
    private final FournisseurRepository fournisseurRepository;
    private final JavaMailSender mailSender;

    public void envoyerDemandesDevis() {
        // 1. Trouver les médicaments à réapprovisionner
        List<Medicament> aReapprovisionner = medicamentRepository.findAll().stream()
            .filter(m -> m.getUnitesEnStock() < m.getNiveauDeReappro())
            .toList();

        if (aReapprovisionner.isEmpty()) {
            log.info("Aucun médicament à réapprovisionner.");
            return;
        }

        // 2. Pour chaque fournisseur, envoyer un mail
        List<Fournisseur> fournisseurs = fournisseurRepository.findAll();

        for (Fournisseur fournisseur : fournisseurs) {
            envoyerMailFournisseur(fournisseur, aReapprovisionner);
        }
    }

    private void envoyerMailFournisseur(Fournisseur fournisseur, List<Medicament> aReapprovisionner) {
        // Filtrer les médicaments que ce fournisseur peut fournir
        List<Integer> codesCategoriesFournisseur = fournisseur.getCategories().stream()
            .map(Categorie::getCode)
            .toList();

        List<Medicament> medicamentsFournisseur = aReapprovisionner.stream()
            .filter(m -> codesCategoriesFournisseur.contains(m.getCategorie().getCode()))
            .toList();

        if (medicamentsFournisseur.isEmpty()) return;

        // Grouper par catégorie
        Map<String, List<Medicament>> parCategorie = medicamentsFournisseur.stream()
            .collect(Collectors.groupingBy(m -> m.getCategorie().getLibelle()));

        // Construire le message
        StringBuilder corps = new StringBuilder();
        corps.append("Bonjour ").append(fournisseur.getNom()).append(",\n\n");
        corps.append("Nous vous contactons afin d'obtenir un devis pour les médicaments suivants :\n\n");

        parCategorie.forEach((categorie, medicaments) -> {
            corps.append("-- ").append(categorie).append(" --\n");
            medicaments.forEach(m -> corps.append("  - ").append(m.getNom())
                .append(" (stock actuel : ").append(m.getUnitesEnStock())
                .append(", seuil : ").append(m.getNiveauDeReappro()).append(")\n"));
            corps.append("\n");
        });

        corps.append("Merci de nous transmettre votre devis dans les plus brefs délais.\n");
        corps.append("Cordialement,\nLa Pharmacie");

        // Envoyer le mail
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(fournisseur.getEmail());
        message.setSubject("Demande de devis - Réapprovisionnement médicaments");
        message.setText(corps.toString());

        mailSender.send(message);
        log.info("Mail envoyé à {}", fournisseur.getEmail());
    }
}