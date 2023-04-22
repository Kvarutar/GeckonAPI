package ru.voronchikhin.geckon.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voronchikhin.geckon.dto.ThemeDTO;
import ru.voronchikhin.geckon.models.Theme;
import ru.voronchikhin.geckon.repositories.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeDTO findBySlug(String slug){
        return themeRepository.findBySlug(slug).map(this::convertThemeToThemeDTO).orElse(null);
    }

    public boolean isPresent(String slug){
        return themeRepository.findBySlug(slug).isPresent();
    }

    @Transactional
    public void save(Theme theme){
        findBySlug(theme.getSlug());
        themeRepository.save(theme);
    }

    @Transactional
    public void delete(String slug){
        themeRepository.deleteBySlug(slug);
    }

    public ThemeDTO convertThemeToThemeDTO(Theme theme){
        return new ThemeDTO(theme.getId(), theme.getName(), theme.getSlug(), theme.getDateOfCreation());
    }

    public Theme convertThemeDTOToTheme(ThemeDTO themeDTO){
        return new Theme(themeDTO.getName(), themeDTO.getSlug(), themeDTO.getDateOfCreation());
    }
}
