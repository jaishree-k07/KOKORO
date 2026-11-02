package com.kokoro.config;

import com.kokoro.model.Counsellor;
import com.kokoro.repository.CounsellorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DEMO DATA ONLY: placeholder counsellors so the page works out of the box.
 * Names, numbers and ratings are fictional. Replace with real, verified listings
 * (insert rows into the `counsellors` table or build an admin form).
 */
@Component
public class CounsellorSeeder implements CommandLineRunner {
    private final CounsellorRepository repo;
    public CounsellorSeeder(CounsellorRepository repo) { this.repo = repo; }

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;
        add("Dr. Demo Counsellor A", "Anxiety & Stress", "Calm Minds Clinic", "Indiranagar", "Bengaluru", "Karnataka", 12.9784, 77.6408, 4.8, 12, 1200, "+91 00000 00001", true);
        add("Demo Counsellor B", "Depression", "Serene Space", "Koramangala", "Bengaluru", "Karnataka", 12.9352, 77.6245, 4.6, 8, 1000, "+91 00000 00002", true);
        add("Demo Counsellor C", "Relationships", "Hope Therapy Centre", "Whitefield", "Bengaluru", "Karnataka", 12.9698, 77.7500, 4.5, 6, 900, "+91 00000 00003", false);
        add("Dr. Demo Counsellor D", "Trauma", "Mindful Roots", "Jayanagar", "Bengaluru", "Karnataka", 12.9250, 77.5938, 4.9, 15, 1500, "+91 00000 00004", true);
        add("Demo Counsellor E", "Student Wellbeing", "Campus Care", "Vijayanagar", "Mysuru", "Karnataka", 12.2958, 76.6394, 4.4, 5, 700, "+91 00000 00005", true);
        add("Dr. Demo Counsellor F", "Anxiety & Stress", "Inner Peace Clinic", "T Nagar", "Chennai", "Tamil Nadu", 13.0418, 80.2341, 4.7, 10, 1100, "+91 00000 00006", true);
        add("Demo Counsellor G", "Depression", "Lotus Wellness", "Adyar", "Chennai", "Tamil Nadu", 13.0067, 80.2570, 4.5, 7, 950, "+91 00000 00007", false);
        add("Dr. Demo Counsellor H", "Relationships", "Harmony House", "Bandra", "Mumbai", "Maharashtra", 19.0596, 72.8295, 4.8, 14, 1800, "+91 00000 00008", true);
        add("Demo Counsellor I", "Trauma", "Safe Harbour", "Andheri", "Mumbai", "Maharashtra", 19.1136, 72.8697, 4.6, 9, 1400, "+91 00000 00009", true);
        add("Demo Counsellor J", "Student Wellbeing", "Bright Minds", "Hauz Khas", "Delhi", "Delhi", 28.5494, 77.2001, 4.7, 11, 1300, "+91 00000 00010", true);
    }

    private void add(String n, String sp, String cl, String ar, String ci, String st, double la, double lo,
                     double rt, int ex, int fee, String ph, boolean on) {
        Counsellor c = new Counsellor();
        c.setName(n); c.setSpecialization(sp); c.setClinic(cl); c.setArea(ar); c.setCity(ci); c.setState(st);
        c.setLatitude(la); c.setLongitude(lo); c.setRating(rt); c.setExperienceYears(ex); c.setFee(fee);
        c.setPhone(ph); c.setOnline(on);
        repo.save(c);
    }
}
