package fr.coloricchio.notesdefrais.infrastructure.in.web;

import fr.coloricchio.notesdefrais.domain.port.out.CategorieDepenseRepository;
import fr.coloricchio.notesdefrais.infrastructure.in.web.dto.CategorieResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieDepenseController {

    private final CategorieDepenseRepository categories;

    public CategorieDepenseController(CategorieDepenseRepository categories) {
        this.categories = categories;
    }

    @GetMapping
    public List<CategorieResponse> lister() {
        return categories.toutes().stream()
                .map(CategorieResponse::depuis)
                .toList();
    }
}
