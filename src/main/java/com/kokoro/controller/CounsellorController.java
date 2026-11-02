package com.kokoro.controller;

import com.kokoro.model.Counsellor;
import com.kokoro.repository.CounsellorRepository;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Suggests counsellors hierarchically, closest level first:
 *   1 = your area / within 5 km     2 = your city / within 15 km
 *   3 = your state / within 60 km   4 = elsewhere (online sessions)
 * Inside each level, the best-rated counsellor comes first.
 */
@RestController
@RequestMapping("/api/counsellors")
public class CounsellorController {
    public record CounsellorDto(Long id, String name, String specialization, String clinic, String area,
                                String city, String state, double rating, int experienceYears, int fee,
                                String phone, boolean online, Double distanceKm, int level) {}

    private final CounsellorRepository repo;

    public CounsellorController(CounsellorRepository repo) { this.repo = repo; }

    @GetMapping("/cities")
    public List<String> cities() {
        return repo.findAll().stream().map(Counsellor::getCity).distinct().sorted().toList();
    }

    @GetMapping
    public List<CounsellorDto> suggest(@RequestParam(required = false) String city,
                                       @RequestParam(required = false) String area,
                                       @RequestParam(required = false) Double lat,
                                       @RequestParam(required = false) Double lng,
                                       @RequestParam(required = false) String specialization) {
        boolean hasGps = lat != null && lng != null;
        List<CounsellorDto> out = new ArrayList<>();
        for (Counsellor c : repo.findAll()) {
            if (specialization != null && !specialization.isBlank()
                    && !c.getSpecialization().toLowerCase().contains(specialization.toLowerCase())) continue;
            Double km = hasGps ? haversine(lat, lng, c.getLatitude(), c.getLongitude()) : null;
            int level = hasGps ? levelByDistance(km) : levelByPlace(c, city, area);
            out.add(new CounsellorDto(c.getId(), c.getName(), c.getSpecialization(), c.getClinic(), c.getArea(),
                    c.getCity(), c.getState(), c.getRating(), c.getExperienceYears(), c.getFee(), c.getPhone(),
                    c.isOnline(), km == null ? null : Math.round(km * 10) / 10.0, level));
        }
        out.sort(Comparator.comparingInt(CounsellorDto::level)
                .thenComparing(Comparator.comparingDouble(CounsellorDto::rating).reversed())
                .thenComparing(d -> d.distanceKm() == null ? 0 : d.distanceKm()));
        return out;
    }

    private static int levelByDistance(double km) {
        return km <= 5 ? 1 : km <= 15 ? 2 : km <= 60 ? 3 : 4;
    }

    private int levelByPlace(Counsellor c, String city, String area) {
        boolean sameCity = city != null && c.getCity().equalsIgnoreCase(city.trim());
        if (sameCity && area != null && !area.isBlank() && c.getArea().toLowerCase().contains(area.trim().toLowerCase())) return 1;
        if (sameCity) return 2;
        // same state as any counsellor in the chosen city
        if (city != null) {
            for (Counsellor o : repo.findAll())
                if (o.getCity().equalsIgnoreCase(city.trim()) && o.getState().equalsIgnoreCase(c.getState())) return 3;
        }
        return 4;
    }

    private static double haversine(double la1, double lo1, double la2, double lo2) {
        double r = 6371, dLa = Math.toRadians(la2 - la1), dLo = Math.toRadians(lo2 - lo1);
        double a = Math.sin(dLa / 2) * Math.sin(dLa / 2)
                + Math.cos(Math.toRadians(la1)) * Math.cos(Math.toRadians(la2)) * Math.sin(dLo / 2) * Math.sin(dLo / 2);
        return 2 * r * Math.asin(Math.sqrt(a));
    }
}
