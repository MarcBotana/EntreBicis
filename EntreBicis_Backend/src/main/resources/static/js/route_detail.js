let map, bounds;

function initMap() {
    const gpsPoints = routePoints.map(p => ({
        lat: p.latitude,
        lng: p.longitude,
        speed: p.speed
    }));

    const maxVel = systemParams.maxVelocity

    bounds = new google.maps.LatLngBounds();
    gpsPoints.forEach(p => bounds.extend(p));

    const start = gpsPoints[0];
    const end = gpsPoints[gpsPoints.length - 1];

    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 13,
        center: bounds.getCenter(),
        mapTypeControl: false,
        streetViewControl: false,
        fullscreenControl: false,
        zoomControl: true,
        gestureHandling: "greedy",
    });

    let segment = [gpsPoints[0]];
    for (let i = 1; i < gpsPoints.length; i++) {
        const current = gpsPoints[i];
        const prev = gpsPoints[i - 1];

        if (current.speed < maxVel !== prev.speed < maxVel || i === gpsPoints.length - 1) {
            segment.push(current);

            const polyline = new google.maps.Polyline({
                path: segment.map(p => ({ lat: p.lat, lng: p.lng })),
                geodesic: true,
                strokeColor: prev.speed < maxVel ? "#0000FF" : "#FFA600",
                strokeOpacity: 1.0,
                strokeWeight: 8,
            });

            polyline.setMap(map);
            segment = [current];
        } else {
            segment.push(current);
        }
    }


    const startMarker = new google.maps.Marker({
        position: start,
        map: map,
        label: "Inici",
        icon: {
            path: google.maps.SymbolPath.CIRCLE,
            scale: 12,
            fillColor: "green",
            fillOpacity: 1,
            strokeWeight: 2,
            strokeColor: "white"
        }
    });

    const finishMarker = new google.maps.Marker({
        position: end,
        map: map,
        label: "Fi",
        icon: {
            path: google.maps.SymbolPath.CIRCLE,
            scale: 12,
            fillColor: "red",
            fillOpacity: 1,
            strokeWeight: 2,
            strokeColor: "white"
        }
    });

    startMarker.setMap(map);
    finishMarker.setMap(map);
    map.fitBounds(bounds);

}

function centerRoute() {
    if (map && bounds) {
        map.fitBounds(bounds);
    }
}
