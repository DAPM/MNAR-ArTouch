H1. Documentation

Folder Guercion contains the application for Guercion's painting.
This is portrait only application.
Full screen.

Folder Sginac contains the applicaiton for Signac's painting
This is landscape only application.
Full screen.

H1. General architecture

The application is a linear interactive image slideshow with sound.
The main flow is:
* Show splash screen
* Show language chooser screen
* Show application usage screen
* Show calibration screen
* Show painting screen with voice over explanations
* Show painting screen with music
* Only for Signac show "the movie - zoom-out - zoom-in"
* Go to Show language choose screen

Note: nice to have an about screen - to show the team members

The application is a single view, full screen, that displays a specific image for each screen and processes certain user gestures (touch, triple touch, multi-touch navigation).
The sound is played usign MediaPlayer since only one stream will be played - we may try to have maximum 2 streams, the second stream will be an 'almos quiet' background sound. The people from association stressed the fact that multiple (2 ore more) sounds in parallel may confuse the user.

A timer is required for language chooser screen.
The user input should be disabled while voice over is still playing. This is not the case for painting with music screen.
The image resolution should be under 2MB, if possible the exact resolution of the screen, eventually filled with black border.

The application will be in 3 languages: French, English, Romanian. We will use internationalization correctly for this.

The applicaiton must containt testing part.