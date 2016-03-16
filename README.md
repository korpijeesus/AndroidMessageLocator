# AndroidMessageLocator
Simple app to send messages to a board with location

Mobiiliohjelmoinnin ja verkko-ohjelmoinnin projekti.

Android Studion filut ja valmis buildi löytyy MessageLocator\app\build\outputs\apk alta.

Serverin osoite on http://tunkinpalauttaja.tunk.org ja pyörii 
iänkaiken vanhalla Raspberry Pi Model B -pikkutietokoneella Seinäjoen Kasperin perukoilla.

Serveri on perus LAMP-stack ja toimii 90% ajasta.

Tietokantana käytetään MySQL -databasea nimeltä messageboard ja siellä käytetään vain yhtä taulua message.
Taulun sisällä on tiedot text, username, latitude ja longitude.

PHP-skripteistä selviää kuinka kaikki on tehty.

Projekti oli suhteellisen haastava, mutta onnistui lopulta.
Tulipahan opittua kuinka Android naitetaan MySQL-tietokantaan

TODO:
-WebViewn vaihtaminen listviewiksi
-JSON implementaatio
-Vaihtoehdot datasijainnin ja GPS sijainnin välillä (melko helppo https://github.com/obaro/SimpleLocationApp pohjalta)
