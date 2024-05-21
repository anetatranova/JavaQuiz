# Java Quiz

Toto je kvízová aplikace implementvaná v jazyce Java (a sestavená s Mavenem). Umožňuje uživatelům odpovídat na otázky z různých kategorií a sledovat své skóre a statistické informace na základě přezdívky zvolenou uživatelem.

## Požadavky
- Java JDK 11 nebo novější
- Maven 3.6.3 nebo novější

## Instalace a spuštění

1. Klonování repozitáře
   ```bash
      git clone https://github.com/anetatranova/JavaQuiz
      cd JavaQuiz
   ```
2. Sestavení projektu (build)
   ```ruby
      mvn clean install
   ```
3. Spuštění aplikace

   Při použití následujícího příkazu se aplikace spustí s výchozím kvízovým souborem `questions.txt`.
   ```ruby
      mvn exec:java
   ```
   Pokud chcete spustit aplikaci s jiným souborem, použijte následující příkaz
   ```ruby
      mvn exec:java -Dexec.args="cesta/k/souboru/s_otázkami.txt"
   ```
## Přidání nového souboru s otázkami
Nové soubory s otázkami by měly být přidány do složky `./src/main/resources`. Pokud uživatel během hry bude chtít změnit soubor otázek, lze tak učinit po dokončení kvízu při volbě "4. Podívat se na statistiky.", kde pak stačí zadat pouze název souboru.

### Formát souboru
Soubor s otázkami by měl mít následující formát
 ```
kategorie
otázka
správná odpověď
špatná odpověď 1
špatná odpověď 2
...
špatná odpověď x
prázdný řádek

 ```

### Příklad 

## Navigace v aplikaci

### Zadání přezdívky
Po spuštění aplikace je uživatel vyzván k zadání své přezdívky pod kterou bude odpovídat na kvízové otázky. Tato přezdívka bude použita pro sledování skóre a statistik. Přezdívku jde použít opakovaně i po skončení a znovuspuštění programu (program pozná, zda byla přezdívka použita a případně nahraje data z minulého kvízu).

### Výběr kategorií a počtu otázek
Po zadání přezdívky se zobrazí seznam kategorii s počty otázek. Uživatel si může vybrat jednu i více kategorií (oddělených mezerou), číslem 0 jsou vybrány všechny kategorie. Po zadání kategorií si uživatel vybere počet otázek, na které chce odpovědět. Následně se mu zobrazí požadovaný počet otázek z vybraných kategorií.

### Možnosti po kvízu
Po dokončení kvízu se zobrazí následující možnosti:

1. Pokračovat ve stejném souboru s kvízem.
   - uživateli bude zobrazena stejná nabídka kategorií jako předtím
2. Načíst nový soubor s otázkama.
   - následně bude uživatel vyzván k zadání názvu souboru, soubor by měl být uložen ve složce `./src/main/resources`
3. Změnit přezdívku.
   - po změně přezdívky se opět zobrazí tyto možnosti
4. Podívat se na statistiky.
   - zobrazí se počet dokončených kvízu, nejvyšší dosažené skóre a průměrné skóre
5. Uložit protokol o testu.
   - uživatel bude vyzván k zadání jména souboru, kam protokol uložit (najde jej v kořenu repozitáře)
5. Ukončit program.
   - program se ukončí

## Soubory s výsledky kvízu a statistiky

### Soubor s výsledky kvízu
Výsledky kvízu jsou uloženy v `./data/quiz_results.txt`. Každý záznam o kvízu obsahuje přezdívku, timestamp, název kvízového souboru, skóre, úspěšnost a podrobné výsledky kategorií.

### Soubor se statistikami
Průběžné statistiky jsou zaznamenávány v `./data/user_statistics.txt`. Tento soubor obsahuje údaje jako jsou celkový počet dokončených kvízů, nejvyšší dosažené skóre a průměrné skóre.

## Dokumentace
Pro vygenerování dokumentace použijte:
```ruby
mvn javadoc:javadoc
```
Dokumentace bude následně dostupná ve složce target/site/apidocs.
