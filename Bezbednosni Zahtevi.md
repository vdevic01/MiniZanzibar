# Bezbednosni Zahtevi

## Aplikacija za Deljenje Dokumenata

### Autentifikacija i Autorizacija
- Koristiti JWT za autentifikaciju korisnika.
- Implementirati sistem za logovanje aktivnosti korisnika.

### Šifrovanje Komunikacije
- Koristiti HTTPS za sve komunikacije između web aplikacije, zanzibara i baze podataka.

### Sigurnost Baze Podataka
- Primijeniti jake šifre minimalne dužine deset sa specijalnim karakterima.
- Osigurati da baza podataka nije dostupna direktno preko interneta.

### Zaštita od SQLi Napada
- Proveriti sve korisnički unete podatke pre njihovog korišćenja u SQL upitima.

### Informaciona Zaštita
- Osigurati da poruke greške ne sadrže osetljive informacije.
- Voditi računa da log zapisi ne sadrže osetljive korisničke podatke.

## Mini Zanzibar

### API Sigurnost
- Koristiti API ključ za svaki HTTP zahtev ka Mini Zanzibar API-ju.
- Ograničiti pristup API-ju samo sa određenih IP adresa.

### Jedinstveni API Ključevi
- Svakoj aplikaciji dodeliti jedinstveni API ključ. U slučaju kompromitacije, menjati samo odgovarajući ključ.

### Logovanje Aktivnosti
- Implementirati sistem logovanja za sve izmene ACL-a i namespace konfiguracija.

### Ograničavanje Privilegija
- Dodeliti role API ključevima, npr. jedna rola za aplikacije koje koriste ACL, a druga za zaposlene koji upravljaju namespace konfiguracijama.

## Generalni Zahtevi

### Sigurnosne Prakse za Razvoj
- Analizirati kod softverom za statičku analizu koda (Codecy)

### Backup Podataka
- Napraviti kopije podataka
