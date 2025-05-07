let routePoints = []; // Se sobrescribirá desde Thymeleaf inline

function initMap() {
    const formattedPoints = routePoints.map(p => ({ lat: p.latitude, lng: p.longitude }));

    const bounds = new google.maps.LatLngBounds();
    formattedPoints.forEach(p => bounds.extend(p));

    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 13,
        center: bounds.getCenter()
    });

    const polyline = new google.maps.Polyline({
        path: formattedPoints,
        geodesic: true,
        strokeColor: "#FF0000",
        strokeOpacity: 1.0,
        strokeWeight: 3,
    });

    polyline.setMap(map);
    map.fitBounds(bounds);
}
