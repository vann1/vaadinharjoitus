# Vaadinharjoitus
Tämä työ on osa Java-Web-ohjelmoinnin kurssia. 
>[!NOTE]
>Päänäkymä(Main view) näkyy kaikille käyttäjä ryhmille ja myös ei kirjautuneille
></br></br>
>User search näkymä näkyy userille sekä adminille.
</br></br>
>Loput näkymistä näkyy vain adminille.
</br></br>

## 1. Tein Data ja Entiteetit osiosta 3 pisteeseen asti

![data_and_entity](https://github.com/user-attachments/assets/1320c711-8653-41ec-9a87-566506a2c044)

Entiteettejä on yhteensä 3. User entiteetillä on OneToOne relaatio PatientOverview entiteetin kanssa. Kun taas Measurement entiteetillä on ManyToOne relaatio User entiteetin kanssa.

![image](https://github.com/user-attachments/assets/49143b2e-8521-4236-bf47-35468a348778)

### CRUD OPERATIONS
1. User entiteettejä pystyy lisämään rekisteröinti lomakkeen kautta. User entiteettejä pystyy hakemaan "User Search" sivulta, kun on kirjautunut user/user TAI admin/admin käyttäjillä.

2. PatientOverview entiteettien käsitteleminen edellyttää admin/admin käyttäjällä kirjautumista. PatientOverview entiteettejä pystyy lisämään, muokkaamaan, poistamaan ja suodattamaan "Patient's overview add/edit" sivulla. PatientOverview entiteettejä pystyy hakemaan "Patient's overview search" sivulla.

3. Measurement entiteettien käsitteleminen edellyttää admin/admin käyttäjällä kirjautumista. Measurement entiteettejä pystyy lisämään, muokkaamaan, poistamaan ja suodattamaan "Patient's measurements add/edit" sivulla. Measurement entiteettejä pystyy hakemaan "Measurements search" sivulla.





***

## 2. Suodattimet osiossa 5 pisteeseen asti

![image](https://github.com/user-attachments/assets/a5c870b5-c8b1-4d1e-8d43-183f42d3fa97)


Tein suodatuksen Measurement entiteettiin. 
Ensimmäinen suodatus on MeasurmentName kentän perusteella. Toinen suodatus ajankohdan mukaan. Kolmas suodatus on relaatiossa olevan User entiteetin name-kentän perusteella. Nämä kaikki suodatukset toimivat myös yhdessä.
![image](https://github.com/user-attachments/assets/ab0880b6-f829-4a75-92b3-c0c47433b2b5)

Tein myös suodatuksen PatientOverview entiteettiin, jossa on yhteensä 5 suodatus muotoa.
![image](https://github.com/user-attachments/assets/a91872bc-5e32-415c-a088-94d5a784f350)

***

## 3. Tyylit osiossa 5 pisteeseen asti
![image](https://github.com/user-attachments/assets/574b7284-5bbd-4957-a99c-ab694f043554)


Vahidoin kaikille vaadin-napeille taustakuvan globaalisti.
</br>
![image](https://github.com/user-attachments/assets/8dd1278d-c876-4e42-b77b-7307074afd26)
![image](https://github.com/user-attachments/assets/b057b8ce-961b-408f-a9dd-38a57a1d8f8d)

Lisäsin Filters komponentille oman css luokan. Siirsin komponentin haku ja tyhjennys nappeja alaspäin.
![image](https://github.com/user-attachments/assets/81ff8d32-d2f8-46ef-9d81-5f8e30f121af)

Lisäsin päänäkymään kuvan ja muokkasin H2 elementin tyyliä.
![image](https://github.com/user-attachments/assets/820a1ca4-1a18-46fc-bedb-4276e6d97844)

Lisäsin kustomoidun tyylitiedoston "filters-component.css".
<br>
![image](https://github.com/user-attachments/assets/59d9c5cf-e99f-4bd6-8f19-3790360a08c0)


***

## 4. Ulkoasu osiossa 5 pisteeseen asti
![image](https://github.com/user-attachments/assets/fd608bc4-1215-4615-aa54-c5a557823199)
Sivustolta löytyy päänäkymä, jonka kaikki näkevät. Header osio, josta löytyy Drawer toggle elementti. Navigation osio, josta löytyy linkit eri sivuille. Footer osio, josta löytyy sisään-/uloskirjautumis-valinta. Sivusolta löytyy 6 eri sisältö sivua.


***

## 5. Autentikointi ja tietoturva osiossa 4 pisteeseen asti
![image](https://github.com/user-attachments/assets/2e7b0db9-aa82-4b73-820b-afebdb86c78a)
</br>
Security-palikka on otettu käyttöön
</br>
![image](https://github.com/user-attachments/assets/a9376b4f-77fd-41e3-b2fe-92a3a9887fa4)
</br>
Sisäänkirjautuminen.
</br>
![image](https://github.com/user-attachments/assets/ca16f749-d3d6-4f6a-aabe-1d31c732b9a1)
</br>
Käyttäjäentiteetti luominen ja roolien määrittäminen(Admin ja user)
</br>
![image](https://github.com/user-attachments/assets/acfd530f-c1fa-4e98-9178-b6f93f4db6e2)
![image](https://github.com/user-attachments/assets/3791c6e7-6b98-408c-8d35-6d77e6786e20)
</br>
Osasta 4. lainaten. Sivusolta löytyy 3 eri sisältö sivua: Päänäkymä(kaiklle), useradmin näkymä(käyttäjälle ja adminille) ja Patient's measurements(adminille).

***
## 6. Lisätoiminnallisuudet osiossa 3 pisteeseen asti
Työ on julkaistu tämän readMe repositorioon.
![image](https://github.com/user-attachments/assets/8c6601ae-8e41-46c2-89d2-fb4cbabbc4e8)

Salasanojen salaus toteutettu BCrypt-salaus menetelmällä.

Lokalisointi toteutettu englanniksi ja suomeksi sisäänkirjautumis-näkymään.



















