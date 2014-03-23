Documentation
=============

Folder Guercion contains the application for Guercion's painting.
This is portrait only application.
Full screen.

Folder Sginac contains the applicaiton for Signac's painting
This is landscape only application.
Full screen.

General architecture
====================

The application is a linear interactive image slideshow with sound.
The main flow is:
1. Show splash screen
2. Show language chooser screen
3. Show application usage screen
4. Show calibration screen
5. Show painting screen with voice over explanations
6. Show painting screen with music
7. Only for Signac show "the movie - zoom-out - zoom-in"
8. Go to (2) Show language choose screen

Note: nice to have an about screen - to show the team members

The application is a single view, full screen, that displays a specific image for each screen and processes certain user gestures (touch, triple touch, multi-touch navigation).
The sound is played usign MediaPlayer since only one stream will be played - we may try to have maximum 2 streams, the second stream will be an 'almos quiet' background sound. The people from association stressed the fact that multiple (2 ore more) sounds in parallel may confuse the user.

A timer is required for language chooser screen.
The user input should be disabled while voice over is still playing. This is not the case for painting with music screen.
The image resolution should be under 2MB, if possible the exact resolution of the screen, eventually filled with black border.

The application will be in 3 languages: French, English, Romanian. We will use internationalization correctly for this.

The applicaiton must containt testing part.

Icons
=====

http://android-ui-utils.googlecode.com/hg/asset-studio/dist/icons-launcher.html#foreground.space.trim=1&foreground.space.pad=0&foreColor=33b5e5%2C0&crop=0&backgroundShape=bevel&backColor=ffffff%2C100

YouTube soundtrack
==================

Guercino - 3 personaje: Sf. Benedict, Sf. Francisc si Ingerul muzicant. Compozitori: Cavalieri, Cavalli, Bertali.

Sf. Benedict - min 0.46- 1.10
https://www.youtube.com/watch?v=2lCAqDGaHLg

Sf. Francisc - min 37.24- 37.52
https://www.youtube.com/watch?v=ClhpvfW9KIc

Ingerul muzicant - min 0.42- 1.22
https://www.youtube.com/watch?v=t8p9rolham8

Signac - 3 planuri: femeia in rosu, barcile, muntii (le scriu prescurtat, stiu ca contin mult mai multe). Compozitor: Debussy

1. femeia min. 0.57-1.24 sau 1.25-1.52 sau 3.10 - 3.41
2. barcile min. 4.36-5.01 sau 5.02 - 5.30
3. muntii si cerul min 7.41-8.14 sau 8.00-8.30
https://www.youtube.com/watch?v=s-PtlUOnkBo    (e acelasi link pentru toate trei, aceeasi suita, piese diferite)

mai am un link pentru intreg, care e aceeasi suita de Debussy, doar ca nu e cantata de pian la patru maini, ci de orchestra, orchestra sugerand intreg tabloul min. 0.06-0.38
https://www.youtube.com/watch?v=JSO8Z6YmZtY

How to extract soundtrack
-------------------------

http://www.wikihow.com/Rip-Music-from-YouTube
http://www.digitaltrends.com/web/how-to-download-music-from-youtube/#!AZrUf
http://www.listentoyoutube.com/

Process soudtrack
-----------------

http://audacity.sourceforge.net/?lang=ro

PLEASE PRODUCE FILES ONLY IN MP3 FORMAT

Testing
=======

The AVD configuration is:
```
avd.ini.encoding=ISO-8859-1
target=android-17
path=/Users/dragos/.android/avd/AVD_for_Evolio_8x.avd
path.rel=avd/AVD_for_Evolio_8x.avd
```

The image configuration is:
```
avd.ini.encoding=ISO-8859-1
abi.type=x86
avd.ini.encoding=ISO-8859-1
disk.dataPartition.size=200M
hw.accelerometer=no
hw.audioInput=yes
hw.battery=yes
hw.cpu.arch=x86
hw.dPad=no
hw.device.hash2=MD5:1aeb8c86fb261677dad914b4644dc9b8
hw.device.manufacturer=User
hw.device.name=Evolio 8x
hw.gps=no
hw.gpu.enabled=yes
hw.keyboard=no
hw.lcd.density=160
hw.mainKeys=yes
hw.ramSize=1024
hw.sdCard=no
hw.sensors.orientation=yes
hw.sensors.proximity=no
hw.trackBall=no
image.sysdir.1=system-images/android-17/x86/
skin.dynamic=yes
skin.name=1024x768
skin.path=1024x768
tag.display=Default
tag.id=default
vm.heapSize=32
```

Voice over text
===============

SCREEN 0 - Splash screen

[ ] TODO - find a short 10-20 sec intro music (1 x MP3 file)

SCREEN 1 - Language chooser

[X] DONE - Ro, Fr, En with acknowledge (6 x MP3 files)

SCREEN 2 - Application usage

[ ] TODO - detailed description of application usage and screen content. Ro, Fr, En (3 x MP3 files)

E.g. Ro:
```
Aplicatia contine 5 ecrane. Ascultati cu atentie instructiunile.
Modul de interactiune este: apasati de 3 ori pe ecran pentru 
a naviga la ecranul urmator sau modul explorare in care puteti 
atinge ecranul cu 1 sau mai multe degete pentru a explora 
continutul ecranului.

Urmatoarele 3 ecrane sunt pentru orientare. Trebuie sa gasiti reprele staga,
sus si dreapta de pe marginea tabletei si sa apasati de 3 ori pe ecran 
dupa ce le-ati gasit.

Ecranul 4 va permite sa descoperiti continutul tabloului. Folositi 1 sau mai 
multe degete pentru a naviga pe suprafata tabletei. Veti primi mai multe instructiuni 
inainte de a incepe navigarea in ecranul 4.

Ecranul 5 va permite sa descoperiti muzicalitatea tabloului. Folositi 1 sau mai 
multe degete pentru a naviga pe suprafata tabletei. Veti primi mai multe instructiuni 
inainte de a incepe navigarea in ecranul 4.
```

SCREEN 3, 4, 5 - Orientation

[ ] TODO - identify orientation: left side, top, right side. Ro, Fr, En (3 x MP3 files)

```
Reformulat textul. Aici vor fi stanga 1 pct de silicon, 
sus 2 pcte de silicon, dreapta 3 pcte de silicon.
```

SCREEN 6 - Painting description

[ ] TODO - describe the characters in each zone, 3 zones. Ro, Fr, En (3 x MP3 files)

MNAR va fruniza textele finale.

E.g. Ro:
```
Zona 1 - In partea de sus a tabloului se afla un inger care canta la vioara.

Zona 2 - In partea drepata jos se gaseste Sf. Francisc.

Zona 3 - In partea stanga se gaseste Sf. Benedict.
```

SCREEN 7 - Painting with music

[ ] TODO - extract music from YouTube (3 x MP3 files)


