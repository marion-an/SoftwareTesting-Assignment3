package zest;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BusTrackerTest {

    @Mock
    private GPSDeviceService gpsService;
    @Mock
    private MapService mapService;
    @Mock
    private NotificationService notificationService;

    private final String NOGPS = "GPS signal lost. Please check back later.";

    private final String ARRIVALMSG = "The bus has arrived at ";
    private String prepareMessage(String waypoint){
        return ARRIVALMSG + waypoint;
    }

    @Test
    void testBusIdNullLocationNull(){
        BusTracker busTracker = Mockito.spy(new BusTracker(gpsService,mapService,notificationService));
        Mockito.when(gpsService.getCurrentLocation(Mockito.any())).thenReturn(null);

        busTracker.updateBusLocation(null);

        Mockito.verify(busTracker,Mockito.times(1)).updateBusLocation(null);
    }

    @Test
    void testBusIdDoesNotExist(){
        BusTracker busTracker = Mockito.spy(new BusTracker(gpsService,mapService,notificationService));
        Mockito.when(gpsService.getCurrentLocation(Mockito.any())).thenReturn(null);

        busTracker.updateBusLocation("1");

        Mockito.verify(busTracker,Mockito.times(1)).updateBusLocation("1");
    }

    @Test
    void testBusIdDoesExist(){
        Location location = new Location(123,123,false,"waypoint");
        BusTracker busTracker = Mockito.spy(new BusTracker(gpsService,mapService,notificationService));
        Mockito.when(gpsService.getCurrentLocation(Mockito.any())).thenReturn(location);

        busTracker.updateBusLocation("12");

        Mockito.verify(gpsService,Mockito.times(1)).getCurrentLocation("12");
        Mockito.verify(mapService,Mockito.times(1)).updateMap("12",location);
    }

    @Test
    void testLocationNoWayPointNotificationUntriggered(){
        Location location = new Location(123,123,false,"wayPoint");
        BusTracker busTracker = new BusTracker(gpsService,mapService,notificationService);
        Mockito.when(gpsService.getCurrentLocation(Mockito.any())).thenReturn(location);

        busTracker.updateBusLocation("12");

        Mockito.verify(notificationService,Mockito.never()).notifyPassengers(Mockito.any(),Mockito.any());
    }

    @Test
    void testLocationWayPointNotificationTriggered(){
        Location location = new Location(123,123,true,"wayPoint");
        BusTracker busTracker = new BusTracker(gpsService,mapService,notificationService);
        Mockito.when(gpsService.getCurrentLocation(Mockito.any())).thenReturn(location);

        ArgumentCaptor<String> busIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageArgumentCaptor = ArgumentCaptor.forClass(String.class);

        busTracker.updateBusLocation("12");

        String expectedMessage = prepareMessage(location.getWaypointName());
        Mockito.verify(notificationService).notifyPassengers(busIdArgumentCaptor.capture(),messageArgumentCaptor.capture());
        assertEquals("12",busIdArgumentCaptor.getValue());
        assertEquals(expectedMessage,messageArgumentCaptor.getValue());
    }

    @Test
    void testLocationNullBusIdNullNotifyError(){
        BusTracker busTracker = new BusTracker(gpsService,mapService,notificationService);
        Mockito.when(gpsService.getCurrentLocation(Mockito.any())).thenReturn(null);

        ArgumentCaptor<String> busIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageArgumentCaptor = ArgumentCaptor.forClass(String.class);

        busTracker.updateBusLocation("12");

        Mockito.verify(notificationService).notifyPassengers(busIdArgumentCaptor.capture(),messageArgumentCaptor.capture());
        assertEquals("12",busIdArgumentCaptor.getValue());
        assertEquals(NOGPS,messageArgumentCaptor.getValue());
    }

    @Test
    void testLocationNullBusIdNullNoUpdateMap(){
        BusTracker busTracker = new BusTracker(gpsService,mapService,notificationService);
        Mockito.when(gpsService.getCurrentLocation(Mockito.any())).thenReturn(null);

        busTracker.updateBusLocation("12");

        Mockito.verify(mapService,Mockito.never()).updateMap(Mockito.any(),Mockito.any());
    }


}
