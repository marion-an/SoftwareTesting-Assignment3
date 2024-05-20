# 08 Bus Tracking

## Bus Tracker
the `BusTracker` class uses the `GPSDeviceService` to get access to the GPS locations, to then update the position in the `MapService` and finally notifying via the `NotificationService`. The class features a constructur for the dependenies, which allows for easy testing, by injecting mocks. (for the services)

## A. Accuracy of Location Updates
the method `updateBusLocation` takes a string busId as parameter. The busId can either be null, a non-existing busId or an existing busId. This gives us the following test cases
- `T1` busId is null => location will be null
- `T2` busId is not existent => location will be null
- `T3` busId is existent => location will be returned

## B. Notification of Key Events
the `NotificationService` has a single method `notifyPassengers` which takes two inputs: busId and message. If a bus returns a new location which is a waypoint the `NotificationService` will be triggered, or as seen before if the busId is non-existent or null.
- `T4` location with no waypoint => triggers no notification
- `T5` location with waypoint => triggers notification update

## C. Response to GPS Signal Loss
- `T6` location is null => trigger notification that gps is currently not working
- `T7` location is null => mapservice updateLocation never gets triggered

## D. Comparison
the event driven updates allow to abstract away and have more readable code, however it poses a challenge when it needs to be tested, since it is private it is hard to mock. Whereas direct method calls can easier be tested and controlled. Testability of code should be considered, but so should be readability. In this particular case we should use a method call for testability instead of the helper function.