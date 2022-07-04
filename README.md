<div id="top"></div>

<h3 align="center">FANTASY MAP UTILITY</h3>

  <p align="center">
    A tool for dynamically adding and tracking locations along with party movements on a 2D map for use in homebrewed tabletop role-playing adventures

## About The Project
This project was conceived as a learning exercise. My goal was to gain some facility in java, springboot and thymeleaf.

Early in development it was decided that the app should remain pure java in order to constrain the scope of the project and to focus my learning.

A secondary ambition was to use Test Driven Development throughout the project.  As a result a fairly robust suite of both unit and integration tests are included.
## Getting Started

The following steps will run this project locally.

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/Puzzlebottom/FantasyMap.git
   ```
2. Load Docker image
   ```sh
   docker compose up
   ```
3. Build and Run
   ```sh
   ./gradlew bootrun
   ```
4. Populate DB with origin location "Bastion"
   ```sh
   curl --location --request POST 'http://localhost:8080/locations' --header 'Authorization: Basic ZGV2OmRldg==' --header 'Content-Type: application/json' --data-raw '{"name": "Bastion","x": 0,"y": 0}'
   ```
5. Go to http://localhost:8080/

## Usage

Every adventure starts somewhere; the party will initially be located at the origin "Bastion".

All new locations are plotted relative to an existing one.  Assign a unique name, along with distance & direction from any of your locations to create new location.

Party movement can be controlled by selecting a destination from among your existing locations. Input the travel time in hours and hit GO! The party marker will be moved on a straight line path toward the destination.

The party marker can also be moved along a directional path.  Select a cardinal direction from the drop-down, input the travel time in hours and hit GO! You adventurers are now wandering aimlessly through the hostile wilderness!

All party movements are logged by the app and a simple UNDO feature will delete the last logged movement. The passage of time is also tracked with the current in-game date and time displayed.


## License

Distributed under the MIT License.

## Contact

Conor Meldrum - conor.j.meldrum@gmail.com

Project Link: [https://github.com/Puzzlebottom/FantasyMap](https://github.com/PuzzleBottom/FantasyMap)


