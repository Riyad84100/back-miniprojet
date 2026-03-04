package pharmacie.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pharmacie.service.ApprovisionnementService;

@RestController
@RequestMapping("/api/approvisionnement")
@RequiredArgsConstructor
public class ApprovisionnementController {

    private final ApprovisionnementService approvisionnementService;

    @PostMapping("/envoyer-devis")
    public ResponseEntity<String> envoyerDevis() {
        approvisionnementService.envoyerDemandesDevis();
        return ResponseEntity.ok("Demandes de devis envoyées avec succès !");
    }
}