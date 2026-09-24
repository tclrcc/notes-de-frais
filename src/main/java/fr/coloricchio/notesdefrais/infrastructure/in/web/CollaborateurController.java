package fr.coloricchio.notesdefrais.infrastructure.in.web;

import fr.coloricchio.notesdefrais.domain.port.in.ListerCollaborateurs;
import fr.coloricchio.notesdefrais.infrastructure.in.web.dto.CollaborateurResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/collaborateurs")
public class CollaborateurController {

    private final ListerCollaborateurs collaborateurs;

    public CollaborateurController(ListerCollaborateurs collaborateurs) {
        this.collaborateurs = collaborateurs;
    }

    @GetMapping
    public List<CollaborateurResponse> lister() {
        return collaborateurs.executer().stream()
                .map(CollaborateurResponse::depuis)
                .toList();
    }
}
