Baza danych studentów.
 Program,   który   umożliwia   gromadzenie   i   przetwarzanie   danych   o   studentach studiów   inżynierskich   stacjonarnych   na   kierunku   informatyka.
    Program   maspełniać   funkcję   prostej   bazy   danych.
  Uwzględnij   kilka   grup   użytkownikówo różnych rolach: student, nauczyciel, pracownik dziekanatu
  
  W bazie przechowywane są dane o: 
  
  - studentach:imię i nazwisko, nralbumu, Tools.PESEL (testowanie na fikcyjnych danych spełniających warunek wymaganej liczby znaków), rok studiów;
  - przedmiotach i ocenach z przedmiotów;
  - prowadzących;
  
  Dane przechowywane w plikach (zapis i odczyt, plik/pliki studentów, plik prowadzących, plik przedmiotów), 
  
  - studenta dodaje do bazy dziekanat, oceny wpisuje prowadzący
  
  - wpisanie na kolejny semestr wykonuje dziekanat 
  
  - nauczyciel/ dziekanat:
    - przeglądanie zawartości bazy, generowanie listy ocen wskazanego studenta, listy ocen studentów z przedmiotu
    - obliczanie średniej dla studenta w podanym semestrze (dziekanat), obliczanie średniej z podanego przedmiotu (nauczyciel)
  
  - student: 
    - sprawdzenie swoich ocen
  - dodawanie rekordu na końcu bazy, 
  - modyfikacja wybranego rekordu (dziekanat/nauczyciel)
  - wyszukiwanie wg różnych kryteriów: nazwisko, nralbumu
  - usuwanie wybranego rekordu, wyszukiwanie wg różnych kryteriów: nazwisko, nralbumu (dziekanat),
  - przy modyfikacji i usuwaniu aktualizacja danych we wszystkich plikach
  - sortowanie rekordów według wybranego kryterium 