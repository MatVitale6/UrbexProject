var map = L.map('map').setView([51.505, -0.09], 13); // Imposta la posizione e lo zoom iniziale

// Aggiungi il layer della mappa (es. OpenStreetMap)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
    maxZoom: 18,
}).addTo(map);

// Aggiungi eventuali marker o layer aggiuntivi alla mappa
// Esempio:
L.marker([51.5, -0.09]).addTo(map)
    .bindPopup('Hello, world!').openPopup();