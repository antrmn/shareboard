package util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class InstantFormatter {
    public static String printRelative(Instant then){
        long n;
        if ((n = Math.abs(Instant.now().until(then, ChronoUnit.SECONDS))) < 60){
            return n == 1 ? n + " secondo fa" : n + " secondi fa";
        } else if ((n = Math.abs(Instant.now().until(then, ChronoUnit.MINUTES))) < 60){
            return n == 1 ? n + " minuto fa" : n + " minuti fa";
        } else if ((n = Math.abs(Instant.now().until(then, ChronoUnit.HOURS))) < 24){
            return n == 1 ? n + " ora fa" : n + " ore fa";
        } else if ((n = Math.abs(Instant.now().until(then, ChronoUnit.DAYS))) < 30) {
            return n == 1 ? n + " giorno fa" : n + " giorni fa";
        } else if ((n = Math.abs(Instant.now().until(then, ChronoUnit.DAYS))) < 365) {
            n /= 30;
            return n == 1 ? n + " mese fa" : n + " mesi fa";
        } else {
            n = Math.abs(Instant.now().until(then, ChronoUnit.YEARS));
            return n == 1 ? n + " anno fa" : n + " anni fa";
        }
    }
}
