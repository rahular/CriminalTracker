##Criminal Tracker

An Android app which parses GPS data of criminals provided by a public server to calculate various behaviors such as their trajectories, frequently visited areas, potential victims, etc. It also uses a heuristic clustering algorithm to dynamically compute crime prone areas which can help people in making informed decisions while planning their routes. Also it can help law enforcement agencies to deploy their forces to the right places at the right time.

### Steps to be performed to run the application from Eclipse

1. Setup Google Play Services SDK and import the library project into Eclipse for accessing the Maps API (can be done by following the instructions at http://developer.android.com/google/play-services/setup.html)
2. Please make sure that the above mentioned library project is properly imported from `<android-sdk-dir>/extras/google/google-play-services/libproject`. CriminalTracker **WILL NOT** work until this is done properly.
3. Import the *CriminalTracker* project into Eclipse (`File -> Import`)
4. Add the **google-play-services_lib** (Google Play Services library which was imported initially) as a library to *CriminalTracker* (Right click on the root of `'CriminalTracker' -> Properties -> Android (in the left pane) -> Add (bottom-right part of the window)`) 
5. Run the project as an *Android Application* on a real device as Google Maps is not supported by ADT's emulator

Note : The minimum API level required for the application to run in `11 (Honeycomb)`

