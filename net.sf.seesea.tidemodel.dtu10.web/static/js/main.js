var self = this;

var path = "ws://"+ window.location.hostname +":8080/ws/vehicles";
var connection = new WebSocket(path);

var rasterLayer = new ol.layer.Tile({
    source: new ol.source.OSM()
});

var map = new ol.Map({
    controls: ol.control.defaults().extend([
      new ol.control.OverviewMap()
    ]),
    target: 'map',
    layers: [rasterLayer],
    view: new ol.View({
      projection: ol.proj.get('EPSG:3857'),
      //34.05374 | -118.30814
      center: ol.proj.fromLonLat([8.05374,54.30814]),
      zoom: 14
    })
});

self.createOverlay = function() {
    return new ol.Overlay({
        element: document.getElementById('myOverlay'),
        positioning: 'top-right',
        stopEvent: false,
        insertFirst: false
    });
}
self.overlay = createOverlay();

console.log("created overlay: "+self.overlay);

self.map.addOverlay(self.overlay);

self.map.on('moveend', function (event) {
  console.log("map move end");

  if (self.socket != null) {
    self.socket.send("close")
    self.socket.close();
  }
  
  self.socket.onmessage = function (msg) {
      console.log("websocket")
      self.draw( jQuery.parseJSON(msg.data));
  }
  
 }

$(function() {
    $( "#slider" ).slider({
        range: "max",
        min: 0,
        max: 10,
        value: 0,
        slide: function( event, ui ) {
            $( "#amount" ).val( ui.value );

            var mapExtent = self.map.getView().calculateExtent(map.getSize());
            var bottomRight = ol.extent.getBottomRight(mapExtent);
            var topLeft = ol.extent.getTopLeft(mapExtent);
            var brLonLat = ol.proj.toLonLat(bottomRight);
            var tlLonLat = ol.proj.toLonLat(topLeft);

            if (ui.value > 0) {
                self.requestVehiclesOnBoundingBox(tlLonLat[1]+","+tlLonLat[0]+","+brLonLat[1]+","+brLonLat[0], ui.value)
            } else {
                //clean old data
                console.log("cleaning old data")
                self.vehicleSource.clear();
            }
        }
    });
    $( "#amount" ).val( $( "#slider" ).slider( "value" ) );
    $( "#hotspot" ).button().click(function() {
        if ($('#hotspot').is(':checked')) {
            console.log("draw hotspots")
            var mapExtent = self.map.getView().calculateExtent(map.getSize());
            var bottomRight = ol.extent.getBottomRight(mapExtent);
            var topLeft = ol.extent.getTopLeft(mapExtent);
            var brLonLat = ol.proj.toLonLat(bottomRight);
            var tlLonLat = ol.proj.toLonLat(topLeft);

            self.ajax(self.akkaHotSpotBasis+tlLonLat[1]+","+tlLonLat[0]+","+brLonLat[1]+","+brLonLat[0]).done(function(data){
              self.drawHotSpotsOnMap(data)
            });
        } else {
          if (self.hotspotSource.getFeatures().length > 0) {
             console.log("cleared hotspotSource");
             self.hotspotSource.clear();
          }
        }
    });
});